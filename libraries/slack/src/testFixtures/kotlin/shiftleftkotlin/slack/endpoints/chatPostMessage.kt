package shiftleftkotlin.slack.endpoints

import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson
import org.http4k.routing.bind
import shiftleftkotlin.slack.domain.FakeMessage
import shiftleftkotlin.slack.domain.FakeSlackState
import shiftleftkotlin.slack.domain.SlackException
import shiftleftkotlin.slack.domain.SlackFailure
import shiftleftkotlin.slack.domain.SlackFailure.Companion.failureLens
import shiftleftkotlin.slack.endpoints.ChatPostMessageResponse.Companion.successBody
import shiftleftkotlin.slack.endpoints.ChatPostMessageResponse.Success

fun chatPostMessage(state: FakeSlackState) =
    "/api/chat.postMessage" bind POST to {
        try {
            val channelId = it.query("channel") ?: throw SlackException("invalid_argument")
            val text = it.query("text") ?: throw SlackException("invalid_argument")

            state.add(FakeMessage(channelId, text))

            Response(OK).with(successBody of Success(channelId))
        } catch (e: SlackException) {
            Response(OK).with(failureLens of SlackFailure(e.localizedMessage))
        } catch (e: Exception) {
            Response(OK).with(failureLens of SlackFailure("error"))
        }
    }

private sealed class ChatPostMessageResponse {
    abstract val ok: Boolean

    data class Success(val channel: String) : ChatPostMessageResponse() {
        override val ok = true
    }

    companion object {
        val successBody = Jackson.autoBody<Success>().toLens()
    }
}

