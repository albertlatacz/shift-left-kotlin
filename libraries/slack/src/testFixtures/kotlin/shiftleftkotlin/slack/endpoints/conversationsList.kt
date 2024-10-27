package shiftleftkotlin.slack.endpoints

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson
import org.http4k.routing.bind
import shiftleftkotlin.slack.domain.FakeSlackState
import shiftleftkotlin.slack.domain.Conversation
import shiftleftkotlin.slack.endpoints.ConversationsListResponse.Companion.successBody
import shiftleftkotlin.slack.endpoints.ConversationsListResponse.Success

fun conversationsList(state: FakeSlackState) =
    "/api/conversations.list" bind GET to {
        Response(OK).with(
            successBody of Success(state.conversationsList().map { Conversation(it.channelId, it.name) })
        )
    }

private sealed class ConversationsListResponse {
    abstract val ok: Boolean

    data class Success(val channels: List<Conversation>) : ConversationsListResponse() {
        override val ok = true
    }

    companion object {
        val successBody = Jackson.autoBody<Success>().toLens()
    }
}

