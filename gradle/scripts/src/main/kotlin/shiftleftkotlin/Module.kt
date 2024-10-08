package shiftleftkotlin

import shiftleftkotlin.ModuleType.*

data class Module(val fullName: String) {
    val type get() = when {
        fullName.contains(":libraries:") -> Library
        fullName.contains(":services:") -> Service
        else -> Undefined
    }
    val name get() = fullName.substringAfterLast(':')
    val homePath get() = fullName.drop(1).replace(':', '/')
    val glob get() = "${homePath}/**"
}
