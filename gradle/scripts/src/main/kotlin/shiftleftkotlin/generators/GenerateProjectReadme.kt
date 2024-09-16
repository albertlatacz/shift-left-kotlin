package shiftleftkotlin.generators

import org.gradle.api.Project
import shiftleftkotlin.*
import java.io.File

class GenerateProjectReadme : ShiftLeftProjectPlugin("generateProjectReadme") {
    override fun processProject(project: Project, descriptors: List<Descriptor>) {
        val outputFile = File(project.rootDir, "README.md")
        val content = listOf(
            projectDescription(project),
            builds("Build Overview", descriptors.map { it.module }),
            dependencyDiagram(descriptors)
        ).joinToString("\n\n")

        outputFile.writeText(content)
    }
}


private fun projectDescription(project: Project): String {
    return """# ${project.name}
        |Example of shift-left engineering practices in Kotlin       
    """.trimMargin()
}




