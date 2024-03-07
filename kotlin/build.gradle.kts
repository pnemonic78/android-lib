plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version BuildVersions.kotlinVersion
}

dependencies {
    // Kotlin
    api(kotlin("stdlib"))

    // JSON
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junitVersion}")
}
