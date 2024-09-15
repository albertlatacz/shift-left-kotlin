import shiftleftkotlin.generators.GenerateBuildOverview
import shiftleftkotlin.generators.GenerateDependencyDiagram

apply<GenerateDependencyDiagram>()
apply<GenerateBuildOverview>()

tasks {
    named("check").get().finalizedBy(named("generateBuildOverview").get())
    named("check").get().finalizedBy(named("generateDependencyDiagram").get())
}