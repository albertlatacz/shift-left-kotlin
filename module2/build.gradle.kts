import shiftleftkotlin.domain.Team.TEAM2
import shiftleftkotlin.gradle.ShiftLeftModule

apply(plugin = "shiftleft-module")

configure<ShiftLeftModule> {
    team.set(TEAM2)
}