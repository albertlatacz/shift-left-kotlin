package shiftleftkotlin.generators

import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftModulePlugin
import shiftleftkotlin.buildBadge
import java.io.File

class GenerateBuildOverview : ShiftLeftModulePlugin("generateBuildOverview") {
    override fun processModule(descriptor: Descriptor, root: File) {
        synchronized(taskName) {
            val file = File(root, "Builds.md")
            val existing = if (file.exists()) file.readLines() else emptyList()

            val prefix = "- [![" + descriptor.module.fullName + "]"
            val lines = existing
                .filter { it.startsWith("- ") }
                .filterNot { it.startsWith(prefix) }

            val newLines = (lines +
                    """- ${descriptor.buildBadge()}""".trimMargin()
                    ).sorted().joinToString("\n")

            val content = """# Build Overview
                |
                |$newLines
            """.trimMargin()
            file.writeText(content)
        }
    }
}
