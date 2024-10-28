package shiftleftkotlin.filestore.application

import dev.forkhandles.result4k.valueOrNull
import org.http4k.aws.AwsCredentials
import org.http4k.chaos.ChaosBehaviours.ReturnStatus
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_2
import org.http4k.connect.amazon.s3.*
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_GATEWAY
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.strikt.bodyString
import org.http4k.strikt.status
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import shiftleftkotlin.core.adapters.TestEvents
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isEqualTo

class FileStoreServiceTest {
    private val credentials = { AwsCredentials("key", "secret") }
    private val region = EU_WEST_2
    private val bucketName = BucketName.of("test-bucket")

    private val s3Fake = FakeS3()
    private val s3Client = S3.Http(credentials, s3Fake)
    private val s3Bucket = S3Bucket.Http(bucketName, region, credentials, s3Fake)

    private val events = TestEvents()
    private val app = api(s3Bucket, events)

    @BeforeEach
    fun setup() {
        s3Fake.behave()
        s3Client.createBucket(bucketName, region).valueOrNull()
    }

    @Test
    fun `stores and retrieves a file`() {
        expectThat(app(Request(POST, Uri.of("/files/some/file")).body("File content"))).status.isEqualTo(OK)
        expectThat(events.all).contains(FileUploadCompleted("some/file"))

        expectThat(app(Request(GET, Uri.of("/files/some/file")))).bodyString.isEqualTo("File content")
        expectThat(events.all).contains(FileDownloadStarted("some/file"))
    }

    @Test
    fun `returns not found for invalid file`() {
        expectThat(app(Request(GET, Uri.of("/files/invalid")))).status.isEqualTo(NOT_FOUND)
        expectThat(events.all).contains(FileNotFound("invalid"))
    }

    @Test
    fun `returns error when underlying storage fails`() {
        s3Fake.misbehave(ReturnStatus(BAD_GATEWAY))
        expectThat(app(Request(GET, Uri.of("/files/invalid")))).status.isEqualTo(INTERNAL_SERVER_ERROR)
        expectThat(events.all).contains(FileDownloadFailed("invalid", "502"))
    }

}
