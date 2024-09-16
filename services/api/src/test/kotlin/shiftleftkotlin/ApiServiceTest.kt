package shiftleftkotlin

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApiServiceTest {
    private val app = apiService()

    @Test
    fun `should pong`() {
        val response = app(Request(GET, "/ping"))
        assertEquals(OK, response.status)
        assertEquals("pong", response.bodyString())
    }
}
