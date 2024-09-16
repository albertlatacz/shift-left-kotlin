package shiftleftkotlin

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import shiftleftkotlin.ModuleType.*
import kotlin.text.Typography.dollar

fun Project.allRuntimeDependencies(): Set<Module> =
    listOf("api", "implementation")
        .map(configurations::getByName)
        .flatMap { it.allDependencies }
        .filterIsInstance<ProjectDependency>()
        .flatMap { it.dependencyProject.allRuntimeDependencies() + it.dependencyProject.asModule()  }
        .sortedBy { it.fullName }
        .toSet()

fun Project.asModule() = Module(fullName = path)

fun Module.buildBadge(label: String = fullName) =
    "[![$label](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/${name}-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/${name}-build.yml)"

fun Module.vcsRoot() =
    "[$name](https://github.com/albertlatacz/shift-left-kotlin/tree/main/${homePath})"

fun dependencyDiagram(descriptors: List<Descriptor>): String {
    fun ModuleType.asC4() = when (this) {
        Library -> "Component"
        Service -> "System"
        Undefined -> "Container"
    }

    val actors = (descriptors.flatMap { listOf(it.module) + it.dependencies }).distinct()
        .joinToString("\n") { """   ${it.type.asC4()}(${it.name}, "${it.name}")""" }

    val relationships = descriptors
        .map { descriptor ->
            descriptor.dependencies.map {
                """   
                    |   Rel(${descriptor.module.name}, ${it.name}, " ") 
                    |   UpdateRelStyle(${descriptor.module.name}, ${it.name}, ${dollar}lineColor="blue")
                """.trimMargin()
            }
        }.sortedBy { it.toString() }
        .distinct()
        .flatten()
        .joinToString("\n")

    return """
        |## Dependency Diagram
        |
        |```mermaid
        |C4Context        
        |   UpdateLayoutConfig(${dollar}c4ShapeInRow="2")                           
        |$actors
        |$relationships                
    |```
    """.trimMargin()
}

fun builds(title: String, descriptors: Collection<Module>): String {
    val builds = when {
        descriptors.isNotEmpty() -> descriptors.groupBy { it.type }.map {
            val typeLines = it.value.sortedBy { it.fullName }.joinToString("\n") { "- ${it.buildBadge()}" }
            """### ${it.key.name}
              |
              |$typeLines""".trimMargin()

        }.joinToString("\n\n")

        else -> "none"
    }

    return """## $title
              |
              |${builds}
              |""".trimMargin()
}