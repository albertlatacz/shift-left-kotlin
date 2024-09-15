package shiftleftkotlin.generators

import org.gradle.api.Project
import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.type
import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftProjectPlugin
import shiftleftkotlin.ModuleType
import shiftleftkotlin.ModuleType.*
import shiftleftkotlin.buildBadge
import java.io.File
import kotlin.text.Typography.dollar

class GenerateProjectReadme : ShiftLeftProjectPlugin("generateProjectReadme") {
    override fun processProject(project: Project, descriptors: List<Descriptor>) {
        val outputFile = File(project.rootDir, "README.md")
        val content = listOf(
            projectDescription(project),
            buildOverview(descriptors),
            dependencyDiagram(descriptors)
        ).joinToString("\n\n")

        outputFile.writeText(content)
    }
}

private fun buildOverview(descriptors: List<Descriptor>): String {
    val lines = descriptors
        .sortedBy { it.module.fullName }
        .joinToString("\n") { "- ${it.module.buildBadge()}" }

    return """## Build Overview
              |
              |${lines}""".trimMargin()
}

private fun projectDescription(project: Project): String {
    return """# ${project.name}
        |Example of shift-left engineering practices in Kotlin       
    """.trimMargin()
}

fun dependencyDiagram(descriptors: List<Descriptor>): String {
    fun ModuleType.asC4() = when (this) {
        Library -> "Component"
        Service -> "System"
        Undefined -> "Container"
    }

    val actors = (descriptors.flatMap { listOf(it.module)+ it.dependencies }).distinct()
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


