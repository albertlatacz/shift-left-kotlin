package shiftleftkotlin.core.adapters

import org.http4k.events.Event
import org.http4k.events.Events
import org.http4k.format.Jackson.asFormatString

class JsonEvents : Events {
    override fun invoke(event: Event) {
        print(asFormatString(event))
    }
}