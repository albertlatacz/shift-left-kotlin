package shiftleftkotlin.slack

import org.http4k.client.JavaHttpClient
import org.http4k.config.Environment.Companion.ENV
import org.http4k.config.EnvironmentKey
import org.http4k.lens.of
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import shiftleftkotlin.slack.domain.Conversation
import shiftleftkotlin.slack.domain.InMemoryFakeSlackState
import strikt.api.expectThat
import strikt.assertions.any
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThanOrEqualTo
import strikt.assertions.isNotBlank
import strikt.assertions.size

abstract class SlackContractTest {
    abstract val slack: Slack

    @Test
    fun `should list conversations`() {
        expectThat(slack.conversationsList()).and {
            size.isGreaterThanOrEqualTo(1)

            any {
                and {
                    get { name }.isEqualTo("shift-left-kotlin")
                    get { id }.isNotBlank()
                }
            }
        }
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
            add(Conversation("1", "shift-left-kotlin"))
        },
        token = token
    )

    override val slack = Slack(fakeSlack, token)
}