package shiftleftkotlin.generators

import org.gradle.api.Project
import shiftleftkotlin.Descriptor
import shiftleftkotlin.ShiftLeftModulePlugin
import shiftleftkotlin.ShiftLeftProjectPlugin
import java.io.File

class GenerateDependencyDiagram : ShiftLeftProjectPlugin("generateDependencyDiagram") {
    override fun processProject(project: Project, descriptors: List<Descriptor>) {

        descriptors.forEach{
            project.logger.error(it.module.fullName)
        }

    }
}