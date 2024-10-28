package shiftleftkotlin.core.adapters

import org.http4k.events.Event
import org.http4k.events.Events

class TestEvents : Events {
    val all = mutableListOf<Event>()

    override fun invoke(event: Event) {
        all.add(event)
    }

}