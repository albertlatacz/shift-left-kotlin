import shiftleftkotlin.ShiftLeftModule
import shiftleftkotlin.Team.TEAM1

apply(plugin = "shiftleft-module")

configure<ShiftLeftModule> {
    team.set(TEAM1)
}

dependencies {
    implementation(project(":libraries:core"))
    implementation(platform("org.http4k:http4k-bom:5.29.0.0"))
    implementation(platform("org.http4k:http4k-connect-bom:5.23.0.0"))
    implementation("org.http4k:http4k-connect-amazon-s3")
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-connect-google-analytics-ga4")
    testImplementation("org.http4k:http4k-connect-amazon-s3-fake")
    testImplementation("org.http4k:http4k-connect-google-analytics-ga4-fake")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}