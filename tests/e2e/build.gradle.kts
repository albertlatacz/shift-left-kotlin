import shiftleftkotlin.ShiftLeftModule
import shiftleftkotlin.Team.TEAM1

apply(plugin = "shiftleft-module")

configure<ShiftLeftModule> {
    team.set(TEAM1)
}

dependencies {
    testImplementation(testFixtures(project(":libraries:core")))
    testImplementation(project(":services:api"))
    testImplementation(project(":services:filestore"))
    testImplementation("org.http4k:http4k-core")
    testImplementation("org.http4k:http4k-multipart")
    testImplementation("org.http4k:http4k-testing-tracerbullet")
    testImplementation("org.http4k:http4k-connect-amazon-s3-fake")
}