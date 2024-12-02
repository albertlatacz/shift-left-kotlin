import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.21"
    `java-library`
    `java-test-fixtures`
    id("shiftleft-root")
}

apply(plugin = "shiftleft-project")

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "kotlin")
    apply(plugin = "java-test-fixtures")

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks {
        withType<KotlinJvmCompile> {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_21
                allWarningsAsErrors = true
            }
        }

        named<Test>("test").configure {
            useJUnitPlatform {
                reports {
                    html.required = true
                }
            }
        }
    }
    dependencies {
        implementation(platform("org.http4k:http4k-bom:5.37.1.1"))
        implementation(platform("org.http4k:http4k-connect-bom:5.35.1.0"))
        testImplementation(platform("org.junit:junit-bom:5.11.3"))
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
        testImplementation("org.http4k:http4k-testing-strikt")

        testFixturesImplementation(platform("org.http4k:http4k-bom:5.37.1.1"))
        testFixturesImplementation(platform("org.junit:junit-bom:5.11.3"))
        testFixturesImplementation("org.junit.jupiter:junit-jupiter-api")
        testFixturesImplementation("org.junit.jupiter:junit-jupiter-engine")
        testFixturesImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
        testFixturesImplementation("org.http4k:http4k-testing-strikt")
    }
}

