import shiftleftkotlin.domain.Team.TEAM1
import shiftleftkotlin.gradle.ShiftLeftModule

apply(plugin = "shiftleft-module")

configure<ShiftLeftModule> {
    team.set(TEAM1)
}

dependencies {
    implementation(project(":module2"))
}