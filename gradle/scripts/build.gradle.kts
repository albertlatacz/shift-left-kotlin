
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

    named<Test>("test").configure {
        useJUnitPlatform {
            includeTags("none()")
        }
    }
}

dependencies {
    api(gradleApi())
    api(platform("org.http4k:http4k-bom:5.27.0.0"))
    api(platform("dev.forkhandles:forkhandles-bom:2.20.0.0"))
    api("org.http4k:http4k-format-jackson-yaml")
    api("org.http4k:http4k-template-handlebars")
    api("org.http4k:http4k-cloudnative")
    api("org.http4k:http4k-config")
    api("dev.forkhandles:values4k")
    api("com.bertramlabs.plugins:hcl4j:0.9.2")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
    api("org.flywaydb:flyway-gradle-plugin:10.17.1")
    api("org.flywaydb:flyway-database-postgresql:10.17.1")

    api("com.avast.gradle:gradle-docker-compose-plugin:0.17.7")
    api("nu.studer.jooq:nu.studer.jooq.gradle.plugin:9.0")
    api("com.datadoghq:datadog-api-client:2.27.0")

    testApi(platform("org.junit:junit-bom:5.11.0"))
    testApi("org.junit.jupiter:junit-jupiter-api")
    testApi("org.http4k:http4k-testing-approval")
    testApi("org.http4k:http4k-testing-strikt")
    testApi("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
