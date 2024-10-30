package shiftleftkotlin.api.application

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.POST
import org.http4k.core.MultipartFormBody
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes
import shiftleftkotlin.core.domain.Reminder

@Reminder(at = "2025-09-16", reason = "Should return json for all calls")
fun apiService(): HttpHandler {
    return routes(

        "/upload" bind Method.GET to {
            Response(Status.OK).body("""
                |<html>
                |<body>
                |<form method="post" enctype="multipart/form-data">                
                |<div>                
                |<label for="file">Choose file to upload</label><br/>              
                |<input type="file" id="file" name="file" multiple />
                |</div>                
                |<div style="margin-top:20px">
                |<button>Submit</button>              
                |</div>                
                |</form>
                |</body>
                |</html>
            """.trimMargin())
        },

        "/upload" bind POST to {
            val form = MultipartFormBody.from(it)
            println(form.files("file"))
            Response(Status.OK)
        }
    )
}