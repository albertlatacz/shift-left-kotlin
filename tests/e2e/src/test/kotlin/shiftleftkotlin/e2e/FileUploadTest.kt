package shiftleftkotlin.e2e

import dev.forkhandles.result4k.onFailure
import org.http4k.chaos.ChaosBehaviours.ReturnStatus
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.core.ContentType.Companion.OCTET_STREAM
import org.http4k.core.Method.POST
import org.http4k.core.MultipartFormBody
import org.http4k.core.Request
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.MultipartFormFile
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import shiftleftkotlin.api.application.SlackNotificationError
import shiftleftkotlin.core.domain.ApplicationEvent
import shiftleftkotlin.e2e.environment.E2ETestEnvironment
import shiftleftkotlin.filestore.application.FileUploadCompleted
import shiftleftkotlin.slack.domain.FakeMessage
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class FileUploadTest {

    private val environment = E2ETestEnvironment()

    @Test
    fun `should upload file and notify slack`() {
        with(environment) {
            val fileName = "test.txt"
            val fileContent = "test file"

            uploadFile(fileName, fileContent)
            expectEvent(FileUploadCompleted(fileName))
            expectFileExistsInS3(fileName, fileContent)
            expectSlackMessage("Uploaded $fileName")
        }
    }

    @Test
    fun `should not fail to upload when slack is unavailable`() {
        with(environment) {
            val fileName = "test.txt"
            val fileContent = "test file"

            slack.misbehave(ReturnStatus(INTERNAL_SERVER_ERROR))
            uploadFile(fileName, fileContent)
            expectFileExistsInS3(fileName, fileContent)
            expectEvent(FileUploadCompleted(fileName))
            expectEvent(SlackNotificationError("Received response with status '500 Internal Server Error'"))
        }
    }
}

private fun E2ETestEnvironment.uploadFile(name: String, content: String) {
    val body = MultipartFormBody()
        .plus("file" to MultipartFormFile(name, OCTET_STREAM, content.byteInputStream()))

    val response = apiService(
        Request(POST, "/upload")
            .header("content-type", "multipart/form-data; boundary=${body.boundary}")
            .body(body)
    )
    expectThat(response.status).isEqualTo(OK)
}

private fun E2ETestEnvironment.expectFileExistsInS3(name: String, content: String) {
    val file = uploadBucket[BucketKey.of(name)].onFailure { fail(it.toString()) }
    expectThat(file).isNotNull().and {
        get { reader().readText() }.isEqualTo(content)
    }
}

private fun E2ETestEnvironment.expectSlackMessage(content: String) {
    expectThat(slack.messagesList()).contains(FakeMessage(slackChannel, content))
}

private fun E2ETestEnvironment.expectEvent(event: ApplicationEvent) {
    expectThat(events.all).contains(event)
}