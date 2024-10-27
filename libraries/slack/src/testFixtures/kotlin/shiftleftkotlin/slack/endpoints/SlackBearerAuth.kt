package shiftleftkotlin.slack.endpoints

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Header
import shiftleftkotlin.slack.domain.SlackFailure
import shiftleftkotlin.slack.domain.SlackFailure.Companion.failureLens

class SlackBearerAuth(private val token: String) : Filter {

    override fun invoke(handler: HttpHandler): HttpHandler = {
        try {
            when (token) {
                AUTH_HEADER(it) -> handler(it)
                else -> Response(OK).with(failureLens of SlackFailure("invalid_auth"))
            }
        } catch (e: Exception) {
            Response(OK).with(failureLens of SlackFailure("error"))
        }
    }

    companion object {
        private val AUTH_HEADER = Header.map { it.replace("Bearer ", "") }.required("Authorization")
    }

}

