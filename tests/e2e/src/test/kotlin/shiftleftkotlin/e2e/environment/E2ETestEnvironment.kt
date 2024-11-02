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
import shiftleftkotlin.slack.FakeSlack
import shiftleftkotlin.slack.Slack
import java.time.Clock

data class E2ETestEnvironment(
    val clock: Clock = Clock.systemUTC()
) {
    val events = TestEvents()
    private val credentials = { AwsCredentials("key", "secret") }
    private val region = EU_WEST_2
    private val bucketName = BucketName.of("test-bucket")

    private val slackToken = "test-slack-token"

    val slackChannel = "test-channel"
    val slack = FakeSlack(token = slackToken)

    private val fakeS3 = FakeS3()
    private val s3Client = S3.Http(credentials, fakeS3).apply {
        createBucket(bucketName, region).onFailure { fail(it.toString()) }
    }
    val uploadBucket = S3Bucket.Http(bucketName, region, credentials, fakeS3)

    val fileStoreService = fileStoreService(uploadBucket, events)
    val apiService = apiService(events, fileStoreService, Slack(slack, slackToken), slackChannel)
}