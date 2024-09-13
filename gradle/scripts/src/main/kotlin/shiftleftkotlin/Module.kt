package shiftleftkotlin

data class Module(val path: String) {
    val name get() = path.substringAfterLast(':')
    val glob get() = "${path.drop(1).replace(':', '/')}/**"
}
