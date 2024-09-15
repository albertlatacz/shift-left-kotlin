package shiftleftkotlin


import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.AccessToken
import org.http4k.security.InsecureCookieBasedOAuthPersistence
import org.http4k.security.OAuthProvider
import org.http4k.security.OAuthProviderConfig
import org.http4k.security.oauth.server.*
import org.http4k.security.oauth.server.accesstoken.AuthorizationCodeAccessTokenRequest
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.time.Clock
import java.time.temporal.ChronoUnit.DAYS
import java.util.*

fun oAuthClientApp(tokenClient: HttpHandler): RoutingHttpHandler {
    val persistence = InsecureCookieBasedOAuthPersistence("oauthTest")
    val authorizationServer = Uri.of("http://localhost:9000")

    val oauthProvider = OAuthProvider(
        OAuthProviderConfig(authorizationServer, "/login", "/oauth2/token", Credentials("my-app", "somepassword")),
        tokenClient,
        Uri.of("http://localhost:8000/auth/callback"),
        listOf("name", "age"),
        persistence
    )

    return routes(
        "/auth/callback" bind GET to oauthProvider.callback,
        "/a-protected-resource" bind GET to oauthProvider.authFilter.then {
            Response(OK).body(
                "user's protected resource"
            )
        }
    )
}

fun authServiceApi(): HttpHandler {
    val server = OAuthServer(
        tokenPath = "/oauth2/token",
        authRequestTracking = InsecureCookieBasedAuthRequestTracking(),
        clientValidator = InsecureClientValidator(),
        authorizationCodes = InsecureAuthorizationCodes(),
        accessTokens = InsecureAccessTokens(),
        json = Jackson,
        clock = Clock.systemUTC()
    )

    return routes(
        server.tokenRoute,
        "/login" bind GET to server.authenticationStart.then {
            Response(OK).body(
                """
                    <html>
                        <form method="POST">
                            <button type="submit">Let me in, please!</button>
                        </form>
                    </html>
                    """.trimIndent()
            )
        },
        "/login" bind POST to server.authenticationComplete,
    )
}

class InsecureClientValidator : ClientValidator {
    override fun validateClientId(request: Request, clientId: ClientId) = true
    override fun validateRedirection(request: Request, clientId: ClientId, redirectionUri: Uri) = true
    override fun validateScopes(request: Request, clientId: ClientId, scopes: List<String>) = true
    override fun validateCredentials(request: Request, clientId: ClientId, clientSecret: String) = true
}

class InsecureAuthorizationCodes : AuthorizationCodes {
    private val clock = Clock.systemUTC()
    private val codes = mutableMapOf<AuthorizationCode, AuthorizationCodeDetails>()

    override fun detailsFor(code: AuthorizationCode) =
        codes[code] ?: error("code not stored")

    override fun create(request: Request, authRequest: AuthRequest, response: Response) =
        Success(AuthorizationCode(UUID.randomUUID().toString()).also {
            codes[it] = AuthorizationCodeDetails(
                authRequest.client,
                authRequest.redirectUri!!,
                clock.instant().plus(1, DAYS),
                authRequest.state,
                authRequest.isOIDC()
            )
        })
}

class InsecureAccessTokens : AccessTokens {
    override fun create(clientId: ClientId, tokenRequest: TokenRequest) =
        Failure(UnsupportedGrantType("client_credentials"))

    override fun create(clientId: ClientId, tokenRequest: AuthorizationCodeAccessTokenRequest) =
        Success(AccessToken(UUID.randomUUID().toString()))
}

fun main() {
    authServiceApi().asServer(SunHttp(9000)).startAndDisplay()

    oAuthClientApp(JavaHttpClient())
        .asServer(SunHttp(8000)).startAndDisplay()
}
