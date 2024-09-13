package shiftleftkotlin.gradle

import org.gradle.api.provider.Property
import shiftleftkotlin.domain.Team

interface ShiftLeftModule {
    val team: Property<Team>
}
