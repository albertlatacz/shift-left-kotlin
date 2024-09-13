import shiftleftkotlin.generators.GenerateBuildOverview

apply<GenerateBuildOverview>()

tasks {
    named("check").get().dependsOn(named("generateBuildOverview").get())
}
