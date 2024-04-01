plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization") version BuildVersions.kotlinVersion
}

android {
    compileSdk = BuildVersions.compileSdkVersion
    namespace = "com.github.kotlin"

    defaultConfig {
        minSdk = BuildVersions.minSdkVersion
        targetSdk = BuildVersions.targetSdkVersion
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            proguardFile("proguard-rules.pro")
            consumerProguardFile("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = BuildVersions.jvm
        targetCompatibility = BuildVersions.jvm
    }

    kotlinOptions {
        jvmTarget = BuildVersions.jvm.toString()
    }
}

dependencies {
    // JSON
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junitVersion}")
}
