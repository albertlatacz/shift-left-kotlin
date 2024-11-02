package shiftleftkotlin.slack.domain

interface FakeSlackState {
    fun add(conversation: FakeConversation)
    fun add(message: FakeMessage)
    fun messagesList(): List<FakeMessage>
    fun conversationsList(): List<FakeConversation>
    fun conversationsHistory(channelId: String): List<FakeMessage>
}

data class FakeConversation(val channelId: String, val name: String)
data class FakeMessage(val channelId: String, val text: String)

class InMemoryFakeSlackState : FakeSlackState {
    private val conversations: MutableList<FakeConversation> = mutableListOf()
    private val messages: MutableList<FakeMessage> = mutableListOf()

    override fun add(conversation: FakeConversation) {
        conversations.add(conversation)
    }

    override fun add(message: FakeMessage) {
        messages.add(message)
    }

    override fun conversationsList() =
        conversations

    override fun messagesList() =
        messages

    override fun conversationsHistory(channelId: String) =
        messages.filter { it.channelId == channelId }

}