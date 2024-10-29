package shiftleftkotlin.api.application

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.routing.bind
import org.http4k.routing.routes
import shiftleftkotlin.api.domain.Health
import shiftleftkotlin.core.domain.Reminder

@Reminder(at = "2025-09-16", reason = "Should return json for all calls")
fun apiService(): HttpHandler {
    return routes(
        "/health" bind Method.GET to { Response(Status.OK).with(Health.healthLens of Health(healthy = true)) },
        "/ping" bind Method.GET to { Response(Status.OK).body("pong") }
    )
}