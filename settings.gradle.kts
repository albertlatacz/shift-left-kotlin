rootProject.name = "shift-left-kotlin"

pluginManagement {
    includeBuild("gradle/scripts")
}

include("module1")
include("module2")
include("libraries:core")
include("services:auth")
