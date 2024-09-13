package shiftleftkotlin.gradle

import shiftleftkotlin.domain.Team
import shiftleftkotlin.domain.Descriptor
import shiftleftkotlin.domain.Module
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import java.io.File

abstract class ShiftLeftModulePlugin(private val taskName: String) : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.run {
            findByType<ShiftLeftModule>() ?: create<ShiftLeftModule>("ShiftLeftModule")
        }

        val repoRootDir = project.rootDir

        project.tasks.register(taskName) {
            doLast {
                val module = Module(project.path)
                processModule(
                    descriptor = Descriptor(
                        owner = extension.team.getOrElse(Team.TEAM1),
                        module = module,
                        dependencies = project.allRuntimeDependencies()
                    ),
                    root = repoRootDir
                )
            }
        }
    }

    abstract fun processModule(descriptor: Descriptor, root: File)
}
