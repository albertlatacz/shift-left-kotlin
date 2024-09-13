package shiftleftkotlin

import dev.forkhandles.result4k.valueOrNull
import org.http4k.aws.AwsCredentials
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_2
import org.http4k.connect.amazon.s3.*
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.connect.google.analytics.ga4.FakeGoogleAnalytics
import org.http4k.connect.google.analytics.ga4.GoogleAnalytics
import org.http4k.connect.google.analytics.ga4.Http
import org.http4k.connect.google.analytics.ga4.model.ApiSecret
import org.http4k.connect.google.analytics.ga4.model.MeasurementId
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class AppTest {
    private val credentials = { AwsCredentials("key", "secret") }
    private val region = EU_WEST_2
    private val bucketName = BucketName.of("test-bucket")

    private val s3Fake = FakeS3()
    private val ga4Fake = FakeGoogleAnalytics()
    private val s3Client = S3.Http(credentials, s3Fake)
    private val s3Bucket = S3Bucket.Http(bucketName, region, credentials, s3Fake)
    private val googleAnalytics = GoogleAnalytics.Http(MeasurementId.of("TEST-ID"), ApiSecret.of("secret"), ga4Fake)

    private val app = api(s3Bucket, googleAnalytics)

    @BeforeEach
    fun setup() {
        s3Client.createBucket(bucketName, region).valueOrNull()
    }

    @Test
    fun `App test`() {
        assertEquals(app(Request(GET, Uri.of("/files/some/file"))).status, NOT_FOUND)
        assertEquals(app(Request(POST, Uri.of("/files/some/file")).body("File content")).status, OK)
        assertEquals(app(Request(GET, Uri.of("/files/some/file"))).bodyString(), "File content")

        for (s in ga4Fake.calls.keySet()) {
            println("s = ${ga4Fake.calls[s]}")
        }
    }

}
