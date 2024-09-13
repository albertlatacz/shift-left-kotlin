package shiftleftkotlin.generators

import shiftleftkotlin.Descriptor
import shiftleftkotlin.Module
import shiftleftkotlin.ShiftLeftModulePlugin
import java.io.File

class GenerateGitHubAction : ShiftLeftModulePlugin("generateGitHubAction") {
    override fun processModule(descriptor: Descriptor, root: File) {
        descriptor.writeGitHubActionsFileTo(root)
    }
}

fun Descriptor.writeGitHubActionsFileTo(root: File) {
    val actionsDir = root.resolve(".github").resolve("workflows")
    val target = actionsDir.resolve("${module.name}-build.yml")
    target.writeText(githubActionsText(this))
}

fun githubActionsText(project: Descriptor): String =
    listOf(
        preamble(project.module, project.dependencies),
        buildWithGradle(project),
    ).joinToString("")

private fun preamble(module: Module, dependencies: Set<Module>): String {
    val paths = listOf("*.kts", module.glob) +
            dependencies.map { it.glob } +
            ".github/workflows/${module.name}-build.yml"
    return """name: ${module.name}-build
on:
  workflow_dispatch: 
  push:
    branches:
      - main
    paths:
      - '${paths.distinct().joinToString("'\n      - '")}'
jobs:"""
}

private fun buildWithGradle(project: Descriptor): String {
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
      - run: ./gradlew ${project.module.path}:check
"""
}