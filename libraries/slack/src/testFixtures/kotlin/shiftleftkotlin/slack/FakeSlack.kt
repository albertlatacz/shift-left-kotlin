package shiftleftkotlin.slack

import org.http4k.chaos.ChaoticHttpHandler
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.routing.routes
import shiftleftkotlin.slack.domain.FakeSlackState
import shiftleftkotlin.slack.domain.InMemoryFakeSlackState
import shiftleftkotlin.slack.endpoints.SlackBearerAuth
import shiftleftkotlin.slack.endpoints.conversationsList

class FakeSlack(state: FakeSlackState = InMemoryFakeSlackState(), token: String) :
    HttpHandler, ChaoticHttpHandler(), FakeSlackState by state {

    override val app = SlackBearerAuth(token).then(
        routes(
            conversationsList(state)
        )
    )
}