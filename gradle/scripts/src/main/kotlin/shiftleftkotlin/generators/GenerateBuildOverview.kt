package shiftleftkotlin.generators

import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftModulePlugin
import java.io.File

class GenerateBuildOverview : ShiftLeftModulePlugin("generateBuildOverview") {
    override fun processModule(descriptor: Descriptor, root: File) {
        synchronized(taskName) {
            val file = File(root, "Builds.md")
            val existing = if (file.exists()) file.readLines() else emptyList()

            val prefix = "- [![" + descriptor.module.path + "]"
            val lines = existing
                .filter { it.startsWith("- ") }
                .filterNot { it.startsWith(prefix) }

            val newLines = (lines +
                    """$prefix(https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/${descriptor.module.name}-build.yml/badge.svg)](https://github.com/albertlatacz/shift-left-kotlin/actions/workflows/${descriptor.module.name}-build.yml)""".trimMargin()
                    ).sorted().joinToString("\n")

            val content = """# Build Overview
                |
                |$newLines
            """.trimMargin()
            file.writeText(content)
        }
    }
}
