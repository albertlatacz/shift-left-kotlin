package shiftleftkotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType

abstract class ShiftLeftProjectPlugin(protected val taskName: String) : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.run {
            findByType<ShiftLeftProject>() ?: create<ShiftLeftProject>("ShiftLeftProject")
        }

        project.tasks.register(taskName) {
            doLast {
                processProject(
                    project = project,
                    descriptors = project.subprojects.mapNotNull { subproject ->
                        subproject.project.extensions.findByType<ShiftLeftModule>()
                            ?.let { descriptorFor(it, subproject) }
                    }
                )
            }
        }
    }

    abstract fun processProject(project: Project, descriptors: List<Descriptor>)
}
