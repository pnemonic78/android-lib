import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(alibs.plugins.android.library)
    alias(alibs.plugins.kotlin.serialization)
}

android {
    compileSdk {
        version = release(alibs.versions.android.compileSdk.get().toInt())
    }
    namespace = "com.github.kotlin"

    defaultConfig {
        minSdk = alibs.versions.android.minSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile("proguard-rules.pro")
            consumerProguardFile("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = BuildVersions.jvm
        targetCompatibility = BuildVersions.jvm
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(BuildVersions.jvm.toString())
        }
    }
}

dependencies {
    api(alibs.kotlinx.serialization.json)
    testImplementation(alibs.bundles.test)
}
