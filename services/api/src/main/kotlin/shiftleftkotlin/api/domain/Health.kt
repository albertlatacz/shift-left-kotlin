package shiftleftkotlin.api.domain

import org.http4k.format.Jackson

data class Health(val healthy: Boolean) {
    companion object {
        val healthLens = Jackson.autoBody<Health>().toLens()
    }
}
