package shiftleftkotlin.slack

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.filter.RequestFilters.SetHeader
import org.http4k.format.Jackson
import shiftleftkotlin.slack.domain.Conversation
import shiftleftkotlin.slack.domain.Message
import shiftleftkotlin.slack.domain.SlackException

class Slack(
    handler: HttpHandler,
    token: String,
    baseUrl: Uri = Uri.of("https://slack.com")
) {

    private val http = SetBaseUriFrom(baseUrl)
        .then(SetHeader("Authorization", "Bearer $token"))
        .then(handler)

    fun conversationsList(): List<Conversation> = conversationsListResponseLens(
        http(Request(GET, "/api/conversations.list")).expectSuccessfulResponse()
    ).channels

    fun postMessage(conversationId: String, text: String) {
        http(Request(POST, "/api/chat.postMessage").query("channel", conversationId).query("text", text))
            .expectSuccessfulResponse()
    }

    fun conversationsHistory(conversationId: String): List<Message> = conversationsHistoryResponseLens(
        http(
            Request(GET, "/api/conversations.history").query("channel", conversationId)
        ).expectSuccessfulResponse()
    ).messages

    private fun Response.expectSuccessfulResponse(): Response {
        if (status != OK)
            throw SlackException("Received response with status '$status'")

        val slackResponse = responseLens(this)

        if (!slackResponse.ok)
            throw SlackException("Received response with failure '${slackResponse.error}'")

        return this
    }

    companion object {
        private val responseLens = Jackson.autoBody<SlackResponse>().toLens()
        private val conversationsListResponseLens = Jackson.autoBody<ConversationsListResponse>().toLens()
        private val conversationsHistoryResponseLens = Jackson.autoBody<ConversationsHistoryResponse>().toLens()
    }
}

private data class SlackResponse(val ok: Boolean, val error: String? = null)
private data class ConversationsListResponse(val channels: List<Conversation>)
private data class ConversationsHistoryRequest(val channel: String)
private data class ConversationsHistoryResponse(val messages: List<Message>)