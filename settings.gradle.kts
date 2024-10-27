rootProject.name = "shift-left-kotlin"

pluginManagement {
    includeBuild("gradle/scripts")
}

include("libraries:core")
include("libraries:slack")
include("services:api")
include("services:filestore")
include("tests:e2e")
