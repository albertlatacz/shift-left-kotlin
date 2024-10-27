package shiftleftkotlin.slack.endpoints

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson
import org.http4k.routing.bind
import shiftleftkotlin.slack.domain.FakeSlackState
import shiftleftkotlin.slack.domain.Message
import shiftleftkotlin.slack.domain.SlackException
import shiftleftkotlin.slack.domain.SlackFailure
import shiftleftkotlin.slack.domain.SlackFailure.Companion.failureLens
import shiftleftkotlin.slack.endpoints.ConversationsHistoryResponse.Companion.successBody
import shiftleftkotlin.slack.endpoints.ConversationsHistoryResponse.Success

fun conversationsHistory(state: FakeSlackState) =
    "/api/conversations.history" bind GET to {
        try {
            val channelId = it.query("channel") ?: throw SlackException("invalid_argument")

            Response(OK).with(
                successBody of Success(state.conversationsHistory(channelId))
            )
        } catch (e: SlackException) {
            Response(OK).with(failureLens of SlackFailure(e.localizedMessage))
        } catch (e: Exception) {
            Response(OK).with(failureLens of SlackFailure("error"))
        }
    }

private sealed class ConversationsHistoryResponse {
    abstract val ok: Boolean

    data class Success(val messages: List<Message>) : ConversationsHistoryResponse() {
        override val ok = true
    }

    companion object {
        val successBody = Jackson.autoBody<Success>().toLens()
    }
}

