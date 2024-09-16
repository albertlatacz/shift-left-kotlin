rootProject.name = "shift-left-kotlin"

pluginManagement {
    includeBuild("gradle/scripts")
}

include("libraries:core")
include("services:api")
include("services:filestore")
