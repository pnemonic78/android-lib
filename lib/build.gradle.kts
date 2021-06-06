plugins {
    id("com.android.library")
    id("kotlin-android")
}

val versionMajor = (project.properties["LIB_VERSION_MAJOR"] as String).toInt()
val versionMinor = (project.properties["LIB_VERSION_MINOR"] as String).toInt()

android {
    compileSdkVersion(BuildVersions.compileSdkVersion)

    defaultConfig {
        minSdkVersion(BuildVersions.minSdkVersion)
        targetSdkVersion(BuildVersions.targetSdkVersion)
        versionCode = versionMajor * 100 + versionMinor
        versionName = "${versionMajor}." + versionMinor.toString().padStart(2, '0')
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles("proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lintOptions {
        disable("LocaleFolder")
        disable("RtlHardcoded")
        disable("UnusedResources")
    }

    sourceSets {
        getByName("main") {
            res { srcDir(file("../platform/packages/apps/Gallery2/res")) }
        }
    }
}

dependencies {
    // Kotlin
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildVersions.kotlin_version}")
    api("org.jetbrains.kotlin:kotlin-stdlib-common:${BuildVersions.kotlin_version}")

    // Support
    api("androidx.annotation:annotation:1.2.0")
    api("androidx.core:core-ktx:1.3.2")

    // Events
    api("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")

    // Views
    api("androidx.recyclerview:recyclerview:1.2.0")
    api("androidx.constraintlayout:constraintlayout:2.0.4")
    api("androidx.preference:preference-ktx:1.1.1")

    // JSON
    api("com.google.code.gson:gson:2.8.6")

    // Logging
    api("com.jakewharton.timber:timber:${BuildVersions.timberVersion}")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junitVersion}")
    androidTestImplementation("androidx.test:core:${BuildVersions.androidTestVersion}")
    androidTestImplementation("androidx.test:rules:${BuildVersions.androidTestVersion}")
    androidTestImplementation("androidx.test:runner:${BuildVersions.androidTestVersion}")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
}
