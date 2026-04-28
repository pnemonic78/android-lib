import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(alibs.plugins.kotlin.jvm)
    alias(alibs.plugins.kotlin.serialization)
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
    api(alibs.kotlinx.serialization.json)
    testImplementation(alibs.bundles.test)
}
