package shiftleftkotlin.core.domain

import org.http4k.events.Event

abstract class ApplicationEvent() : Event {
    val type: String = javaClass.simpleName
}