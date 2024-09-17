package shiftleftkotlin.api

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import shiftleftkotlin.Reminder
import shiftleftkotlin.startAndDisplay

@Reminder(at = "2025-09-16", reason = "Should return json for all calls")
fun apiService(): HttpHandler {
    return routes(
        "/ping" bind GET to { Response(OK).body("pong") }
    )
}

fun main() {
    apiService().asServer(SunHttp(9000)).startAndDisplay()
}
