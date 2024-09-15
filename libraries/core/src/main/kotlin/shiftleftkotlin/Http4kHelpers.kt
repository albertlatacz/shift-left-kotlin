package shiftleftkotlin

import org.http4k.server.Http4kServer

fun Http4kServer.startAndDisplay() =
    start().also { println("Server started on http://localhost:" + it.port()) }