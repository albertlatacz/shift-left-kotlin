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
    implementation("org.http4k:http4k-connect-amazon-s3")

    testImplementation(testFixtures(project(":libraries:core")))
    testImplementation(testFixtures(project(":libraries:slack")))
    testImplementation("org.http4k:http4k-connect-amazon-s3-fake")
}