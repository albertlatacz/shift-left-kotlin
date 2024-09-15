import shiftleftkotlin.generators.GenerateBuildOverview
import shiftleftkotlin.generators.GenerateGitHubAction
import shiftleftkotlin.generators.GenerateReadme

apply<GenerateGitHubAction>()
apply<GenerateBuildOverview>()
apply<GenerateReadme>()

tasks {
    named("check").get().dependsOn(named("generateGitHubAction").get())
    named("check").get().dependsOn(named("generateBuildOverview").get())
    named("check").get().finalizedBy(named("generateReadme").get())
}
