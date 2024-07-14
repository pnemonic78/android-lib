import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version BuildVersions.kotlin
}

java {
    sourceCompatibility = BuildVersions.jvm
    targetCompatibility = BuildVersions.jvm
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(BuildVersions.jvm.toString()))
    }

    sourceSets {
        getByName("main") {
            kotlin {
                srcDir("../kotlin/src/main/kotlin")
            }
        }
    }
}

dependencies {
    // Kotlin
    api(kotlin("stdlib"))

    // JSON
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junit}")
}
