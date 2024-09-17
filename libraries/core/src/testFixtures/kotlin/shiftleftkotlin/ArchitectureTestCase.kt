package shiftleftkotlin

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaMethod
import com.tngtech.archunit.core.domain.properties.HasName
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

interface ArchitectureTestCase {

    @Test
    fun `check package name is same as module name`() {
        val packageName = javaClass.`package`.name.removePrefix("shiftleftkotlin.").substringBefore('.')
        val dirName = File(".").absoluteFile.parentFile.name.filter { it.isLetter() }
        expectThat(packageName).isEqualTo(dirName)
    }

    @ArchTest
    fun `check architecture is respected`(classes: JavaClasses) {
        val application = "Application" to "..application.."
        val domain = "Domain" to "..domain.."

        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .withOptionalLayers(true)
            .layer(application)
            .layer(domain)
            .whereLayer(application).allowedToOnlyAccess(domain)
            .whereLayer(domain).allowedToOnlyAccess(domain)
            .whereLayer(domain).allowedToBeOnlyBeAccessedBy(application)
            .check(classes)
    }

    @ArchTest
    fun `check there are no package cycles`(classes: JavaClasses) {
        slices()
            .matching("(**)")
            .should()
            .beFreeOfCycles()
            .check(classes)
    }

    @ArchTest
    fun `check there are classes`(classes: JavaClasses) {
        classes()
            .should(object : ArchCondition<JavaClass>("classes are there") {
                override fun check(item: JavaClass, events: ConditionEvents) {}
            })
            .check(classes)
    }

    @ArchTest
    fun `check class code reminders has not expired`(classes: JavaClasses) {
        classes()
            .that()
            .areAnnotatedWith(Reminder::class.java)
            .should(object : ArchCondition<JavaClass>("not be expired") {
                override fun check(item: JavaClass, events: ConditionEvents) {
                    val bomb = item.getAnnotationOfType(Reminder::class.java)
                    checkCodeReminder(item, events, bomb)
                }
            })
            .allowEmptyShould(true)
            .check(classes)
    }

    @ArchTest
    fun `check method code reminders has not expired`(classes: JavaClasses) {
        methods()
            .that()
            .areAnnotatedWith(Reminder::class.java)
            .should(object : ArchCondition<JavaMethod>("not be expired") {
                override fun check(item: JavaMethod, events: ConditionEvents) {
                    val bomb = item.getAnnotationOfType(Reminder::class.java)
                    checkCodeReminder(item, events, bomb)
                }
            })
            .allowEmptyShould(true)
            .check(classes)
    }

    private fun checkCodeReminder(item: HasName, events: ConditionEvents, reminder: Reminder) {
        val remindAt = LocalDate.parse(reminder.at)
        if (remindAt.atStartOfDay().isBefore(LocalDateTime.now())) {
            val msg = "⏰ ${item.name} has expired: ${reminder.reason}"
            events.add(SimpleConditionEvent.violated(item, msg))
        }
    }

    private fun Architectures.LayeredArchitecture.layer(value: Pair<String, String>): Architectures.LayeredArchitecture =
        layer(value.first).definedBy(value.second)

    private fun Architectures.LayeredArchitecture.whereLayer(value: Pair<String, String>): Architectures.LayeredArchitecture.LayerDependencySpecification =
        whereLayer(value.first)

    private fun Architectures.LayeredArchitecture.LayerDependencySpecification.allowedToOnlyAccess(vararg value: Pair<String, String>): Architectures.LayeredArchitecture =
        mayOnlyAccessLayers(*value.map { it.first }.toTypedArray())

    private fun Architectures.LayeredArchitecture.LayerDependencySpecification.allowedToBeOnlyBeAccessedBy(vararg value: Pair<String, String>): Architectures.LayeredArchitecture =
        mayOnlyBeAccessedByLayers(*value.map { it.first }.toTypedArray())
}