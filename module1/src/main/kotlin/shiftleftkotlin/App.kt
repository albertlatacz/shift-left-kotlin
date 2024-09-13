package shiftleftkotlin

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_2
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.connect.google.analytics.ga4.GoogleAnalytics
import org.http4k.connect.google.analytics.ga4.Http
import org.http4k.connect.google.analytics.ga4.collect
import org.http4k.connect.google.analytics.ga4.model.ApiSecret
import org.http4k.connect.google.analytics.ga4.model.MeasurementId
import org.http4k.connect.google.analytics.model.ClientId
import org.http4k.connect.google.analytics.model.Event
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Path
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun api(bucket: S3Bucket, analytics: GoogleAnalytics): HttpHandler {
    val key = Path.of("key")
    val analyticsClientId = ClientId.of("system")
    return routes(
        "/files/{key:.*}" bind routes(
            GET to {
                val path = key(it)
                bucket[BucketKey.of(path)]
                    .map {
                        when (it) {
                            null -> Response(NOT_FOUND)
                            else -> Response(OK).body(it)
                        }
                    }.recover { Response(INTERNAL_SERVER_ERROR) }
                    .also { analytics.collect(Event("files", "retrieve", path, it.status.code, analyticsClientId)) }
            },

            POST to {
                val path = key(it)
                bucket.set(BucketKey.of(key(it)), it.body.stream)
                    .map { Response(OK) }
                    .recover { Response(INTERNAL_SERVER_ERROR) }
                    .also { analytics.collect(Event("files", "store", path, it.status.code, analyticsClientId)) }

            }
        )
    )
}

fun main() {
    api(
        bucket = S3Bucket.Http(BucketName.of("prod-bucket"), EU_WEST_2),
        analytics = GoogleAnalytics.Http(MeasurementId.of("TGA-17638673"), ApiSecret.of("prod-secret"))

    ).asServer(SunHttp(9000)).start()
        .also { println("Server started on http://localhost:" + it.port()) }
}