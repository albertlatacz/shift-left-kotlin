package shiftleftkotlin.slack

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters.BearerAuth
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.format.Jackson
import org.http4k.lens.Query
import shiftleftkotlin.slack.domain.Conversation
import shiftleftkotlin.slack.domain.Message
import shiftleftkotlin.slack.domain.SlackException

class Slack(handler: HttpHandler, token: String, baseUrl: Uri = Uri.of("https://slack.com")) {

    fun conversationsHistory(conversationId: String): List<Message> =
        http(Request(GET, "/api/conversations.history").with(channelLens of conversationId))
            .assertSuccessfulResponse()
            .let(conversationsHistoryResponseLens)
            .messages


    fun conversationsList(): List<Conversation> =
        http(Request(GET, "/api/conversations.list"))
            .assertSuccessfulResponse()
            .let(conversationsListResponseLens)
            .channels

    fun postMessage(conversationId: String, text: String): Unit =
        http(Request(POST, "/api/chat.postMessage").with(channelLens of conversationId, textLens of text))
            .assertSuccessfulResponse()
            .let { }

    private val http = SetBaseUriFrom(baseUrl)
        .then(BearerAuth { token })
        .then(handler)

    private fun Response.assertSuccessfulResponse(): Response {
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
        private val channelLens = Query.required("channel")
        private val textLens = Query.required("text")

        private data class SlackResponse(val ok: Boolean, val error: String? = null)
        private data class ConversationsListResponse(val channels: List<Conversation>)
        private data class ConversationsHistoryResponse(val messages: List<Message>)
    }
}
