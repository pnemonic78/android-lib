import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
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
    api(kotlin("stdlib"))
    api(libs.kotlinx.serialization.json)
    testImplementation(libs.bundles.test)
}
