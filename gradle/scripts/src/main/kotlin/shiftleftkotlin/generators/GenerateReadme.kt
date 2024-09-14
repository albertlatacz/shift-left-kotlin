package shiftleftkotlin.generators

import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftModulePlugin
import java.io.File

class GenerateReadme : ShiftLeftModulePlugin("generateReadme") {
    override fun processModule(descriptor: Descriptor, root: File) {
        val moduleDir = File(root, descriptor.module.homePath)
        val outputFile = File(moduleDir, "README.md")
        val content = listOf(
            module(descriptor),
            team(descriptor)
        ).joinToString("\n\n")

        outputFile.writeText(content)
    }
}

private fun module(descriptor: Descriptor) = """
    # ${descriptor.module.name}    
""".trimIndent()

private fun team(descriptor: Descriptor) = """
    ## Ownership
    This module is maintained by *${descriptor.team}*
""".trimIndent()