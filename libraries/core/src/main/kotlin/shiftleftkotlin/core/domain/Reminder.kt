package shiftleftkotlin.core.domain

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class Reminder(val at: String, val reason: String)
