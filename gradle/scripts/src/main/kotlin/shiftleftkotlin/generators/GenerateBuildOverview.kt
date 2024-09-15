package shiftleftkotlin.generators

import org.gradle.api.Project
import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftProjectPlugin
import shiftleftkotlin.buildBadge
import java.io.File

class GenerateBuildOverview : ShiftLeftProjectPlugin("generateBuildOverview") {
    override fun processProject(project: Project, descriptors: List<Descriptor>) {
        synchronized(taskName) {
            project.logger.error(project.rootDir.toString())
            val file = File(project.rootDir, "Builds.md")

            project.logger.error("ASAAAA = ${descriptors}")
            val lines = descriptors
                .sortedBy { it.module.fullName }
                .joinToString("\n") { "- ${it.module.buildBadge()}" }

            file.writeText(
                """# Build Overview
                   |
                   |${lines}
                """.trimMargin()
            )
        }
    }
}
