package shiftleftkotlin.api.application

import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.format.Jackson
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import shiftleftkotlin.core.startAndDisplay
import shiftleftkotlin.slack.Slack

fun main() {
    val fileStore = SetBaseUriFrom(Uri.of("http://localhost:9001"))
        .then(JavaHttpClient())

    val slack = Slack(JavaHttpClient(), "<token goes here>")

    apiService(fileStore, slack, "shift-left-kotlin-tests")
        .asServer(SunHttp(9002))
        .startAndDisplay()
}
