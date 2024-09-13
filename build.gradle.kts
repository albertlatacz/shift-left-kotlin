import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.20"
    id("shiftleft-root")
}

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
}

