import shiftleftkotlin.ShiftLeftModule
import shiftleftkotlin.Team.TEAM1

plugins {
    id("shiftleft-module")
}

configure<ShiftLeftModule> {
    team.set(TEAM1)
}

dependencies {
    implementation(project(":module2"))
}