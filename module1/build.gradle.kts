import shiftleftkotlin.domain.Team.TEAM1
import shiftleftkotlin.gradle.ShiftLeftModule

plugins {
    id("shiftleft-module")
}

configure<ShiftLeftModule> {
    team.set(TEAM1)
}

dependencies {
    implementation(project(":module2"))
}