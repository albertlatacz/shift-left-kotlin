package shiftleftkotlin

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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FileStoreServiceTest {
    private val credentials = { AwsCredentials("key", "secret") }
    private val region = EU_WEST_2
    private val bucketName = BucketName.of("test-bucket")

    private val s3Fake = FakeS3()
    private val s3Client = S3.Http(credentials, s3Fake)
    private val s3Bucket = S3Bucket.Http(bucketName, region, credentials, s3Fake)

    private val app = api(s3Bucket)

    @BeforeEach
    fun setup() {
        s3Fake.behave()
        s3Client.createBucket(bucketName, region).valueOrNull()
    }

    @Test
    fun `stores and retrieves a file`() {
        assertEquals(app(Request(POST, Uri.of("/files/some/file")).body("File content")).status, OK)
        assertEquals(app(Request(GET, Uri.of("/files/some/file"))).bodyString(), "File content")
    }

    @Test
    fun `returns not found for invalid file`() {
        assertEquals(app(Request(GET, Uri.of("/files/invalid"))).status, NOT_FOUND)
    }

    @Test
    fun `returns error when underlying storage fails`() {
        s3Fake.misbehave(ReturnStatus(BAD_GATEWAY))
        assertEquals(app(Request(GET, Uri.of("/files/invalid"))).status, INTERNAL_SERVER_ERROR)
    }

}
