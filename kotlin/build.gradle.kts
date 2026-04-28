import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(alibs.plugins.android.library)
    alias(alibs.plugins.kotlin.android)
    alias(alibs.plugins.kotlin.serialization)
}

android {
    compileSdk = alibs.versions.android.compileSdk.toInt()
    namespace = "com.github.kotlin"

    defaultConfig {
        minSdk = alibs.versions.android.minSdk.toInt()
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
