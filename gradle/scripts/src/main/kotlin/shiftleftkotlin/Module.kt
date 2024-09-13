package shiftleftkotlin

data class Module (val path: String) {
    val name get() = path.substringAfterLast(':')
}
