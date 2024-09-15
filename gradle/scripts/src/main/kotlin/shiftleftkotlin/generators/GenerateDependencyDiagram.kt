package shiftleftkotlin.generators

import org.gradle.api.Project
import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftModulePlugin
import shiftleftkotlin.ShiftLeftProjectPlugin
import java.io.File

class GenerateDependencyDiagram : ShiftLeftProjectPlugin("generateDependencyDiagram") {
    override fun processProject(project: Project, descriptors: List<Descriptor>) {

        project.logger.error("descriptors = ${descriptors}")
    }
}