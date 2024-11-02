package shiftleftkotlin.slack

import org.http4k.client.JavaHttpClient
import org.http4k.config.Environment.Companion.ENV
import org.http4k.config.EnvironmentKey
import org.http4k.lens.of
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import shiftleftkotlin.slack.domain.FakeConversation
import shiftleftkotlin.slack.domain.InMemoryFakeSlackState
import strikt.api.expectThat
import strikt.assertions.any
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThanOrEqualTo
import strikt.assertions.isNotBlank
import strikt.assertions.none
import strikt.assertions.size
import java.util.*

abstract class SlackContractTest {

    @Test
    fun `should get conversations history`() {
        val messageText = "get-conversation-${UUID.randomUUID()}"

        val conversation = (slack.conversationsList().find { it.name == testConversationName }
            ?: fail("Could not find conversation"))

        expectThat(slack.conversationsHistory(conversation.id))
            .none { get { text }.isEqualTo(messageText) }

        slack.postMessage(conversation.id, messageText)

        expectThat(slack.conversationsHistory(conversation.id))
            .any { get { text }.isEqualTo(messageText) }
    }

    @Test
    fun `should list conversations`() {
        expectThat(slack.conversationsList()).and {
            size.isGreaterThanOrEqualTo(1)

            any {
                and {
                    get { name }.isEqualTo("shift-left-kotlin-tests")
                    get { id }.isNotBlank()
                }
            }
        }
    }

    @Test
    fun `should post message`() {
        val messageText = "post-message-${UUID.randomUUID()}"

        val conversation = (slack.conversationsList().find { it.name == testConversationName }
            ?: fail("Could not find conversation"))

        slack.postMessage(conversation.id, messageText)

        expectThat(slack.conversationsHistory(conversation.id))
            .any { get { text }.isEqualTo(messageText) }
    }

    abstract val slack: Slack

    companion object {
        const val testConversationName = "shift-left-kotlin-tests"
    }
}

class RealSlackTest : SlackContractTest() {
    init {
        assumeTrue(System.getenv("SLACK_BOT_TOKEN") != null)
    }

    override val slack = Slack(JavaHttpClient(), SLACK_BOT_TOKEN(ENV))

    companion object {
        val SLACK_BOT_TOKEN by EnvironmentKey.of().required()
    }
}

class FakeSlackTest : SlackContractTest() {
    private var token = "token"
    private val fakeSlack = FakeSlack(
        state = InMemoryFakeSlackState().apply {
            add(FakeConversation("1", testConversationName))
        },
        token = token
    )

    override val slack = Slack(fakeSlack, token)
}