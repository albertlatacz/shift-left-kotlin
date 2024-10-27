package shiftleftkotlin.slack.domain

import org.http4k.format.Jackson

data class SlackFailure(val error: String) {
    val ok = false
    companion object {
        val failureLens = Jackson.autoBody<SlackFailure>().toLens()
    }
}