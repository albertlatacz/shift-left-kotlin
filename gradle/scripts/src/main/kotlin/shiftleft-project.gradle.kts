import shiftleftkotlin.generators.GenerateProjectReadme

apply<GenerateProjectReadme>()

tasks {
    named("check").get().finalizedBy(named("generateProjectReadme").get())
}