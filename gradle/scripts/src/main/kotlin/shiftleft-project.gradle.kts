import shiftleftkotlin.generators.GenerateDependencyDiagram
import shiftleftkotlin.generators.GenerateProjectReadme

apply<GenerateDependencyDiagram>()
apply<GenerateProjectReadme>()

tasks {
    named("check").get().finalizedBy(named("generateDependencyDiagram").get())
    named("check").get().finalizedBy(named("generateProjectReadme").get())
}