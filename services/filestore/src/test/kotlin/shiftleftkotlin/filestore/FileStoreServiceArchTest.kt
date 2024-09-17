package shiftleftkotlin.filestore

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import shiftleftkotlin.ArchitectureTestCase

@AnalyzeClasses(packagesOf = [FileStoreServiceArchTest::class], importOptions = [DoNotIncludeTests::class])
object FileStoreServiceArchTest : ArchitectureTestCase
