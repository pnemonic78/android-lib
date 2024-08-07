plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = BuildVersions.compileSdk
    namespace = "com.github.lib"

    defaultConfig {
        minSdk = BuildVersions.minSdk
        targetSdk = BuildVersions.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            proguardFile("proguard-rules.pro")
            consumerProguardFile("proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = BuildVersions.jvm
        targetCompatibility = BuildVersions.jvm
    }

    kotlinOptions {
        jvmTarget = BuildVersions.jvm.toString()
    }

    sourceSets {
        getByName("main") {
            res {
                srcDir(file("../platform/packages/apps/Calendar/res"))
                srcDir(file("../platform/packages/apps/Contacts/res"))
                srcDir(file("../platform/packages/apps/DeskClock/res"))
                srcDir(file("../platform/packages/apps/Gallery2/res"))
            }
        }
    }
}

dependencies {
    // Kotlin
    api(project(":android-lib:kotlin"))

    // Jetpack
    api("androidx.activity:activity-ktx:1.9.0")
    api("androidx.annotation:annotation:1.8.0")
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.fragment:fragment-ktx:1.8.1")

    // Views
    api("androidx.recyclerview:recyclerview:1.3.2")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.preference:preference-ktx:1.2.1")

    // Logging
    api("com.jakewharton.timber:timber:${BuildVersions.timber}")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junit}")
    androidTestImplementation("androidx.test:core:${BuildVersions.androidTest}")
    androidTestImplementation("androidx.test:rules:${BuildVersions.androidTest}")
    androidTestImplementation("androidx.test:runner:${BuildVersions.androidTest}")
    androidTestImplementation("androidx.test.ext:junit:${BuildVersions.junitExt}")
}
