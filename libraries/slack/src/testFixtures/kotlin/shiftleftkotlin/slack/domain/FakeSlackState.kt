package shiftleftkotlin.slack.domain

interface FakeSlackState {
    fun add(conversation: Conversation)
    fun conversationsList(): List<Conversation>
}

class InMemoryFakeSlackState : FakeSlackState {
    private val conversations: MutableList<Conversation> = mutableListOf()

    override fun add(conversation: Conversation) {
        conversations.add(conversation)
    }

    override fun conversationsList() =
        conversations
}