import shiftleftkotlin.ShiftLeftModule
import shiftleftkotlin.Team.TEAM1

apply(plugin = "shiftleft-module")

configure<ShiftLeftModule> {
    team.set(TEAM1)
}

dependencies {
    implementation(platform("org.http4k:http4k-bom:5.33.0.1"))
    implementation("org.http4k:http4k-core")
    implementation(project(":services:api"))
    implementation(project(":services:filestore"))
}