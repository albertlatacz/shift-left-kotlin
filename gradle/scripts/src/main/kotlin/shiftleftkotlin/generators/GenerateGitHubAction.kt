package shiftleftkotlin.generators

import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftModulePlugin
import java.io.File

class GenerateGitHubAction : ShiftLeftModulePlugin("generateGitHubAction") {
    override fun processModule(descriptor: Descriptor, root: File) {
        val actionsDir = root.resolve(".github").resolve("workflows")
        val target = actionsDir.resolve("${descriptor.module.name}-build.yml")
        target.writeText(
            listOf(
                triggers(descriptor),
                build(descriptor),
            ).joinToString("")
        )
    }
}

private fun triggers(project: Descriptor): String {
    val paths = listOf("*.kts", project.module.glob) +
            project.dependencies.map { it.glob } +
            ".github/workflows/${project.module.name}-build.yml"
    return """name: ${project.module.name}-build
on:
  workflow_dispatch: 
  push:
    branches:
      - main
    paths:
      - '${paths.distinct().joinToString("'\n      - '")}'
jobs:"""
}

private fun build(project: Descriptor): String {
    return """
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4.0.0
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
          cache: 'gradle'
      - run: ./gradlew ${project.module.fullName}:check
"""
}