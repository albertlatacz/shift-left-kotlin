package shiftleftkotlin.api.application

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.strikt.bodyString
import org.http4k.strikt.status
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExtendWith(ApprovalTest::class)
class ApiServiceTest {
    private val app = apiService()

    @Test
    fun `serves upload page`(approver: Approver) {
        approver.assertApproved(app(Request(GET, "/upload")))
    }

}
