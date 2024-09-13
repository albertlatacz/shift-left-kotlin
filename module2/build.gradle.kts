import shiftleftkotlin.domain.Team.TEAM2
import shiftleftkotlin.gradle.ShiftLeftModule

plugins {
    id("shiftleft-module")
}

configure<ShiftLeftModule> {
    team.set(TEAM2)
}