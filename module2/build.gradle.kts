import shiftleftkotlin.Team.TEAM2
import shiftleftkotlin.ShiftLeftModule

plugins {
    id("shiftleft-module")
}

configure<ShiftLeftModule> {
    team.set(TEAM2)
}

dependencies {
    implementation(project(":module1"))
}