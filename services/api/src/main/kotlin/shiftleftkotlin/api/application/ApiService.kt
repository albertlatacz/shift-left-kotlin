package shiftleftkotlin.api.application

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.events.Events
import org.http4k.lens.MultipartFormFile
import org.http4k.lens.Validator
import org.http4k.lens.multipartForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import shiftleftkotlin.core.domain.ApplicationEvent
import shiftleftkotlin.core.domain.Reminder
import shiftleftkotlin.slack.Slack
import shiftleftkotlin.slack.domain.SlackException

@Reminder(at = "2025-09-16", reason = "Add health endpoint")
fun apiService(events: Events, fileStore: HttpHandler, slack: Slack, channelId: String): HttpHandler {
    return routes(
        "/upload" bind GET to {
            Response(Status.OK).body(
                """
                |<html>
                |  <body>
                |    <form method="post" enctype="multipart/form-data">
                |      <div>                
                |        <label for="file">Choose file to upload</label><br/>              
                |        <input type="file" id="file" name="file" multiple />
                |      </div>                
                |      <div style="margin-top:20px">
                |        <button>Submit</button>              
                |      </div>                
                |    </form>
                |  </body>
                |</html>
            """.trimMargin()
            )
        },

        "/upload" bind POST to {
            val file = fileField(uploadForm(it))

            try {
                slack.postMessage(channelId, "Uploaded ${file.filename}")
            } catch (e: SlackException) {
                events(SlackNotificationError(e.localizedMessage))
            }

            fileStore(Request(POST, "/files/${file.filename}").body(file.content))
        }
    )
}

data class SlackNotificationError(val error: String) : ApplicationEvent()


private val fileField = MultipartFormFile.required("file")
private val uploadForm = Body.multipartForm(Validator.Strict, fileField).toLens()