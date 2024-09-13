package shiftleftkotlin.domain

data class Descriptor(
    val owner: Team,
    val module: Module,
    val dependencies: Set<Module>,
)