package shiftleftkotlin.e2e.environment

import dev.forkhandles.result4k.onFailure
import org.http4k.aws.AwsCredentials
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_2
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketName
import org.junit.jupiter.api.fail
import shiftleftkotlin.api.application.apiService
import shiftleftkotlin.core.adapters.TestEvents
import shiftleftkotlin.filestore.application.fileStoreService
import java.time.Clock

data class E2ETestEnvironment(
    val clock: Clock = Clock.systemUTC()
) {
    private val events = TestEvents()
    private val credentials = { AwsCredentials("key", "secret") }
    private val region = EU_WEST_2
    private val bucketName = BucketName.of("test-bucket")

    private val s3Fake = FakeS3()
    private val s3Client = S3.Http(credentials, s3Fake).apply {
        createBucket(bucketName, region).onFailure { fail(it.toString()) }
    }
    val uploadBucket = S3Bucket.Http(bucketName, region, credentials, s3Fake)

    val fileStoreService = fileStoreService(uploadBucket, events)
    val apiService = apiService(fileStoreService)
}