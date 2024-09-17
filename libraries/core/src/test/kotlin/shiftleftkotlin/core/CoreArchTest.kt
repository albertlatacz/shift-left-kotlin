package shiftleftkotlin.core

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import shiftleftkotlin.core.ArchitectureTestCase

@AnalyzeClasses(packagesOf = [CoreArchTest::class], importOptions = [DoNotIncludeTests::class])
object CoreArchTest : ArchitectureTestCase
