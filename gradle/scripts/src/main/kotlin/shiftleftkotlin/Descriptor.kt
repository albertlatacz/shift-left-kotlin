package shiftleftkotlin

data class Descriptor(
    val team: Team,
    val module: Module,
    val dependencies: Set<Module>,
)