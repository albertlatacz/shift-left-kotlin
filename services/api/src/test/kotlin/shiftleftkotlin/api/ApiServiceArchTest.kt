package shiftleftkotlin.api

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import shiftleftkotlin.ArchitectureTestCase

@AnalyzeClasses(packagesOf = [ApiServiceArchTest::class], importOptions = [DoNotIncludeTests::class])
object ApiServiceArchTest : ArchitectureTestCase
