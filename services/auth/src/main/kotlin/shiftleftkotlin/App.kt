package shiftleftkotlin


import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import org.http4k.client.JavaHttpClient
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_2
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.connect.google.analytics.ga4.GoogleAnalytics
import org.http4k.connect.google.analytics.ga4.Http
import org.http4k.connect.google.analytics.model.ClientId as GAClientId
import org.http4k.connect.google.analytics.ga4.collect
import org.http4k.connect.google.analytics.ga4.model.ApiSecret
import org.http4k.connect.google.analytics.ga4.model.MeasurementId
import org.http4k.connect.google.analytics.model.Event
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson
import org.http4k.lens.Path
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.AccessToken
import org.http4k.security.InsecureCookieBasedOAuthPersistence
import org.http4k.security.OAuthProvider
import org.http4k.security.OAuthProviderConfig
import org.http4k.security.oauth.server.*
import org.http4k.security.oauth.server.accesstoken.AuthorizationCodeAccessTokenRequest
import org.http4k.server.Http4kServer
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


fun api(bucket: S3Bucket, analytics: GoogleAnalytics): HttpHandler {
    val key = Path.of("key")
    val analyticsClientId = GAClientId.of("system")

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

        "//{key:.*}" bind routes(
            GET to {
                val path = key(it)
                bucket[BucketKey.of(path)]
                    .map {
                        when (it) {
                            null -> Response(NOT_FOUND)
                            else -> Response(OK).body(it)
                        }
                    }.recover { Response(INTERNAL_SERVER_ERROR) }
                    .also { analytics.collect(Event("files", "retrieve", path, it.status.code, analyticsClientId)) }
            },

            POST to {
                val path = key(it)
                bucket.set(BucketKey.of(key(it)), it.body.stream)
                    .map { Response(OK) }
                    .recover { Response(INTERNAL_SERVER_ERROR) }
                    .also { analytics.collect(Event("files", "store", path, it.status.code, analyticsClientId)) }

            }
        )
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
    api(
        bucket = S3Bucket.Http(BucketName.of("prod-bucket"), EU_WEST_2),
        analytics = GoogleAnalytics.Http(MeasurementId.of("TGA-17638673"), ApiSecret.of("prod-secret"))
    ).asServer(SunHttp(9000)).startAndDisplay()

    oAuthClientApp(JavaHttpClient())
        .asServer(SunHttp(8000)).startAndDisplay()

    // Go to http://localhost:8000/a-protected-resource to start the authorization flow
}

private fun Http4kServer.startAndDisplay() =
    start().also { println("Server started on http://localhost:" + it.port()) }
