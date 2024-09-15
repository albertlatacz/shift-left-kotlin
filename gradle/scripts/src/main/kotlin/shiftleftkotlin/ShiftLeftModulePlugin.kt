package shiftleftkotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import shiftleftkotlin.Team.UNASSIGNED
import java.io.File

abstract class ShiftLeftModulePlugin(protected val taskName: String) : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.run {
            findByType<ShiftLeftModule>() ?: create<ShiftLeftModule>("ShiftLeftModule")
        }

        val repoRootDir = project.rootDir

        project.tasks.register(taskName) {
            doLast {
                processModule(
                    descriptor = descriptorFor(extension, project),
                    root = repoRootDir
                )
            }
        }
    }

    abstract fun processModule(descriptor: Descriptor, root: File)
}

fun descriptorFor(module: ShiftLeftModule, project: Project): Descriptor {
    return Descriptor(
        team = module.team.getOrElse(UNASSIGNED),
        module = project.asModule(),
        dependencies = project.allRuntimeDependencies()
    )
}

