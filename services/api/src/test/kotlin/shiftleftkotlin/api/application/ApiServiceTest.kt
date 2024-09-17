package shiftleftkotlin.api.application

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.strikt.bodyString
import org.http4k.strikt.status
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ApiServiceTest {
    private val app = apiService()

    @Test
    fun `should pong`() {
        val response = app(Request(GET, "/ping"))
        expectThat(response).and {
            status.isEqualTo(OK)
            bodyString.isEqualTo("pong")
        }
    }
}
