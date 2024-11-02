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

/*
{
  "ok": true,
  "messages": [
    {
      "user": "U07TPA8UEAE",
      "type": "message",
      "ts": "1730054468.656099",
      "client_msg_id": "bcd05f07-7dbb-40a5-9c89-e02e375383c6",
      "text": "test",
      "team": "T07TLEF1QP7",
      "blocks": [
        {
          "type": "rich_text",
          "block_id": "gB9fq",
          "elements": [
            {
              "type": "rich_text_section",
              "elements": [
                {
                  "type": "text",
                  "text": "test"
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "subtype": "channel_join",
      "user": "U07TRR8KNTW",
      "text": "<@U07TRR8KNTW> has joined the channel",
      "inviter": "U07TPA8UEAE",
      "type": "message",
      "ts": "1730054396.338819"
    },
    {
      "subtype": "bot_add",
      "text": "added an integration to this channel: <https:\/\/shift-leftkotlin.slack.com\/services\/B07UCBZ43KJ|Shift-Left Bot>",
      "user": "U07TPA8UEAE",
      "bot_link": "<https:\/\/shift-leftkotlin.slack.com\/services\/B07UCBZ43KJ|Shift-Left Bot>",
      "bot_id": "B07UCBZ43KJ",
      "type": "message",
      "ts": "1730043913.593009"
    },
    {
      "subtype": "bot_add",
      "text": "added an integration to this channel: <https:\/\/shift-leftkotlin.slack.com\/services\/B07TPAFPW9G|Shift-Left Bot>",
      "user": "U07TPA8UEAE",
      "bot_link": "<https:\/\/shift-leftkotlin.slack.com\/services\/B07TPAFPW9G|Shift-Left Bot>",
      "bot_id": "B07TPAFPW9G",
      "type": "message",
      "ts": "1730030784.860079"
    }
  ],
  "has_more": false,
  "pin_count": 0,
  "channel_actions_ts": null,
  "channel_actions_count": 0
}

* */