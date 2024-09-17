package shiftleftkotlin.filestore.application

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_2
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.model.BucketName
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

fun api(bucket: S3Bucket): HttpHandler {
    val key = Path.of("key")
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
            },

            POST to {
                bucket.set(BucketKey.of(key(it)), it.body.stream)
                    .map { Response(OK) }
                    .recover { Response(INTERNAL_SERVER_ERROR) }

            }
        )
    )
}

fun main() {
    api(S3Bucket.Http(BucketName.of("prod-bucket"), EU_WEST_2)).asServer(SunHttp(9000)).start()
        .also { println("Server started on http://localhost:" + it.port()) }
}
