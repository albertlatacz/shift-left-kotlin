package shiftleftkotlin

data class Module(val fullName: String) {
    val name get() = fullName.substringAfterLast(':')
    val homePath get() = fullName.drop(1).replace(':', '/')
    val glob get() = "${homePath}/**"
}
