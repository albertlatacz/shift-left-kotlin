import shiftleftkotlin.ShiftLeftModule
import shiftleftkotlin.Team.TEAM1

apply(plugin = "shiftleft-module")

configure<ShiftLeftModule> {
    team.set(TEAM1)
}

dependencies {
    implementation(project(":libraries:core"))
    implementation(project(":libraries:slack"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-format-jackson")
    implementation("org.http4k:http4k-multipart")


    testImplementation("org.http4k:http4k-testing-approval")
    testImplementation(testFixtures(project(":libraries:core")))
    testImplementation(testFixtures(project(":libraries:slack")))
}