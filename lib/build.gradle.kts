plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = BuildVersions.compileSdkVersion
    namespace = "com.github.lib"

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
    api("org.jetbrains.kotlin:kotlin-stdlib:${BuildVersions.kotlin_version}")
    api("org.jetbrains.kotlin:kotlin-stdlib-common:${BuildVersions.kotlin_version}")

    // Jetpack
    api("androidx.activity:activity-ktx:1.7.2")
    api("androidx.annotation:annotation:1.7.0")
    api("androidx.core:core-ktx:1.12.0")
    api("androidx.fragment:fragment-ktx:1.6.1")

    // Events
    api("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")

    // Views
    api("androidx.recyclerview:recyclerview:1.3.1")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.preference:preference-ktx:1.2.1")

    // JSON
    api("com.google.code.gson:gson:2.9.0")

    // Logging
    api("com.jakewharton.timber:timber:${BuildVersions.timberVersion}")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junitVersion}")
    androidTestImplementation("androidx.test:core:${BuildVersions.androidTestVersion}")
    androidTestImplementation("androidx.test:rules:${BuildVersions.androidTestVersion}")
    androidTestImplementation("androidx.test:runner:${BuildVersions.androidTestVersion}")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
}
