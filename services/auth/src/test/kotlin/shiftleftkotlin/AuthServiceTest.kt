package shiftleftkotlin

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Uri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthServiceTest {
    private val app = authServiceApi()

    @Test
    fun `Test route not found by default`() {
        assertEquals(app(Request(GET, Uri.of("/files/some/file"))).status, NOT_FOUND)
    }
}
