package shiftleftkotlin.api.application

import org.http4k.server.SunHttp
import org.http4k.server.asServer
import shiftleftkotlin.core.startAndDisplay

fun main() {
    apiService()
        .asServer(SunHttp(9000))
        .startAndDisplay()
}
