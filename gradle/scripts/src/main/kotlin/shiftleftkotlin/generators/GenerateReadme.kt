package shiftleftkotlin.generators

import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftModulePlugin
import shiftleftkotlin.buildBadge
import shiftleftkotlin.vcsRoot
import java.io.File

class GenerateReadme : ShiftLeftModulePlugin("generateReadme") {
    override fun processModule(descriptor: Descriptor, root: File) {
        val moduleDir = File(root, descriptor.module.homePath)
        val outputFile = File(moduleDir, "README.md")
        val content = listOf(
            module(descriptor),
            ownership(descriptor),
            dependencies(descriptor)
        ).joinToString("\n")

        outputFile.writeText(content)
    }
}

private fun module(descriptor: Descriptor) = """
# ${descriptor.module.name}
        
${descriptor.module.buildBadge()}
"""

private fun ownership(descriptor: Descriptor) = """
## Ownership
This module is maintained by *${descriptor.team}*
"""

private fun dependencies(descriptor: Descriptor) = """
## Dependencies
${
    when {
        descriptor.dependencies.isEmpty() -> "none"
        else -> descriptor.dependencies.joinToString("\n") { "- ${it.vcsRoot()}"}
    }
}
"""