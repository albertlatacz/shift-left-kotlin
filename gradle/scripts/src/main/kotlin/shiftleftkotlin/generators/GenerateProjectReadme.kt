package shiftleftkotlin.generators

import org.gradle.api.Project
import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftProjectPlugin
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

    return """### Build Overview
              |
              |${lines}""".trimMargin()
}

private fun projectDescription(project: Project): String {
    return """# ${project.name}
        |Example of shift-left engineering practices in Kotlin       
    """.trimMargin()

}

private fun dependencyDiagram(descriptors: List<Descriptor>): String {
    println(" descriptors = ${ descriptors}")
    return """
        |### Dependency Diagram
        |
        |```mermaid
        |C4Context        
        |   UpdateLayoutConfig(${dollar}c4ShapeInRow="3")           
        |   System(app, "app")
        |   System(auth, "auth")        
        |   Rel(app, auth, " ")
        |   UpdateRelStyle(app, auth, ${dollar}lineColor="blue")        
    |```
    """.trimMargin()

}


