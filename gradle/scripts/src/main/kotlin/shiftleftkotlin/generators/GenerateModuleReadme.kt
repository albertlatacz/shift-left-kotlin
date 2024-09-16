package shiftleftkotlin.generators

import shiftleftkotlin.*
import java.io.File

class GenerateModuleReadme : ShiftLeftModulePlugin("generateModuleReadme") {
    override fun processModule(descriptor: Descriptor, root: File) {
        val moduleDir = File(root, descriptor.module.homePath)
        val outputFile = File(moduleDir, "README.md")
        val content = listOf(
            module(descriptor),
            ownership(descriptor),
            builds("Dependencies", descriptor.dependencies),
            dependencyDiagram(listOf(descriptor)+descriptor)
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
