import shiftleftkotlin.generators.GenerateBuildOverview
import shiftleftkotlin.generators.GenerateGitHubAction

apply<GenerateGitHubAction>()
apply<GenerateBuildOverview>()

tasks {
    named("check").get().dependsOn(named("generateGitHubAction").get())
    named("check").get().dependsOn(named("generateBuildOverview").get())
}
