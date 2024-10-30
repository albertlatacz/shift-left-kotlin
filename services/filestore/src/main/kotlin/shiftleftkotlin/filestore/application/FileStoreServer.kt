package shiftleftkotlin.filestore.application

import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import shiftleftkotlin.core.adapters.JsonEvents
import shiftleftkotlin.core.startAndDisplay

fun main() {
    fileStoreApi(S3Bucket.Http(BucketName.of("prod-bucket"), Region.EU_WEST_2), JsonEvents())
        .asServer(SunHttp(9001))
        .startAndDisplay()
}