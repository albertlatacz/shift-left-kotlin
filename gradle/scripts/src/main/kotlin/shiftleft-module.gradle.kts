import shiftleftkotlin.generators.GenerateGitHubAction
import shiftleftkotlin.generators.GenerateModuleReadme

apply<GenerateGitHubAction>()
apply<GenerateModuleReadme>()

tasks {
    named("check").get().dependsOn(named("generateGitHubAction").get())
    named("check").get().finalizedBy(named("generateModuleReadme").get())
}
