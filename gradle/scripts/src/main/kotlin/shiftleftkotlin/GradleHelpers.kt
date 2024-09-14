package shiftleftkotlin

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

fun Project.allRuntimeDependencies(): Set<Module> =
    listOf("api", "implementation")
        .map(configurations::getByName)
        .flatMap { it.allDependencies }
        .filterIsInstance<ProjectDependency>()
        .flatMap { listOf(Module(it.dependencyProject.path)) + it.dependencyProject.allRuntimeDependencies() }
        .sortedBy { it.fullName }
        .toSet()
