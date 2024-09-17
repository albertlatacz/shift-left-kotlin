package shiftleftkotlin.core

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses

@AnalyzeClasses(packagesOf = [CoreArchTest::class], importOptions = [DoNotIncludeTests::class])
object CoreArchTest : ArchitectureTestCase
