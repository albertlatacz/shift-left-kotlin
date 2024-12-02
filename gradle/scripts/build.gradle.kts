
import org.gradle.api.JavaVersion.VERSION_21
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

java {
    sourceCompatibility = VERSION_21
    targetCompatibility = VERSION_21
}

tasks {
    withType<KotlinJvmCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
            allWarningsAsErrors = true
        }
    }
}

dependencies {
    api(gradleApi())
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")

    testApi(platform("org.junit:junit-bom:5.11.3"))
    testApi("org.junit.jupiter:junit-jupiter-api")
}
