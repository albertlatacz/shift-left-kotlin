package shiftleftkotlin.slack

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import shiftleftkotlin.core.ArchitectureTestCase

@AnalyzeClasses(packagesOf = [SlackArchTest::class], importOptions = [DoNotIncludeTests::class])
object SlackArchTest : ArchitectureTestCase
