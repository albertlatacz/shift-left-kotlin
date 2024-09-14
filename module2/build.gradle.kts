import shiftleftkotlin.Team.TEAM2
import shiftleftkotlin.ShiftLeftModule

apply(plugin = "shiftleft-module")

configure<ShiftLeftModule> {
    team.set(TEAM2)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":module1"))
}