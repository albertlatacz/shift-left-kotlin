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
import org.http4k.core.then
import org.http4k.events.Event
import org.http4k.events.EventCategory
import org.http4k.events.Events
import org.http4k.filter.ServerFilters.CatchAll
import org.http4k.lens.Path
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import shiftleftkotlin.core.adapters.JsonEvents
import shiftleftkotlin.core.domain.ApplicationEvent
import shiftleftkotlin.core.startAndDisplay

fun api(bucket: S3Bucket, events: Events): HttpHandler {
    val key = Path.of("key")
    return CatchAll {
        events(FileStoreError(it.toString()))
        Response(INTERNAL_SERVER_ERROR)
    }.then(routes(
        "/files/{key:.*}" bind routes(
            GET to {
                val path = key(it)
                bucket[BucketKey.of(path)]
                    .map {
                        when (it) {
                            null -> {
                                events(FileNotFound(path))
                                Response(NOT_FOUND)
                            }

                            else -> {
                                events(FileDownloadStarted(path))
                                Response(OK).body(it)
                            }
                        }
                    }.recover {
                        events(FileDownloadFailed(path, "${it.status.code} ${it.message ?: ""}".trim()))
                        Response(INTERNAL_SERVER_ERROR)
                    }
            },

            POST to {
                val path = key(it)
                bucket.set(BucketKey.of(path), it.body.stream)
                    .map {
                        events(FileUploadCompleted(path))
                        Response(OK)
                    }
                    .recover {
                        events(FileUploadFailed(path, "${it.status.code} ${it.message ?: ""}".trim()))
                        Response(INTERNAL_SERVER_ERROR)
                    }

            }
        )
    ))
}

data class FileStoreError(val error: String) : ApplicationEvent()
data class FileUploadCompleted(val path: String) : ApplicationEvent()
data class FileUploadFailed(val path: String, val error: String) : ApplicationEvent()
data class FileDownloadStarted(val path: String) : ApplicationEvent()
data class FileDownloadFailed(val path: String, val error: String) : ApplicationEvent()
data class FileNotFound(val path: String) : ApplicationEvent()

fun main() {
    api(S3Bucket.Http(BucketName.of("prod-bucket"), EU_WEST_2), JsonEvents())
        .asServer(SunHttp(9000))
        .startAndDisplay()
}
