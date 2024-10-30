package shiftleftkotlin.api.application

import org.http4k.core.ContentType.Companion.OCTET_STREAM
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.MultipartFormBody
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.TrafficFilters.RecordTo
import org.http4k.lens.MultipartFormFile
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.traffic.Sink.Companion.MemoryMap
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expectThat
import strikt.assertions.contains

@ExtendWith(ApprovalTest::class)
class ApiServiceTest {
    private val requests = mutableMapOf<Request, Response>()
    private val fileStorage = RecordTo(MemoryMap(requests)).then { Response(OK) }
    private val app = apiService(fileStorage)

    @Test
    fun `serves upload page`(approver: Approver) {
        approver.assertApproved(app(Request(GET, "/upload")))
    }

    @Test
    fun `uploads file`(approver: Approver) {

        val body = MultipartFormBody()
            .plus(
                "file" to MultipartFormFile(
                    "test.txt",
                    OCTET_STREAM,
                    "test file".byteInputStream()
                )
            )

        app(
            Request(POST, "/upload")
                .header("content-type", "multipart/form-data; boundary=${body.boundary}")
                .body(body)
        )

        expectThat(requests.keys).contains(Request(POST, "/files/test.txt").body("test file"))
    }

}
