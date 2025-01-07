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

        val locales = listOf(
            "af",
            "ar",
            "bg",
            "cs",
            "da",
            "de",
            "el",
            "en",
            "en_AU",
            "en_CA",
            "en_GB",
            "en_IE",
            "en_IN",
            "en_NZ",
            "en_US",
            "en_ZA",
            "es",
            "es_US",
            "et",
            "fa",
            "fi",
            "fr",
            "hi",
            "hu",
            "in",
            "it",
            "iw",
            "ja",
            "ko",
            "lt",
            "lv",
            "ms",
            "nb",
            "nl",
            "pl",
            "pt",
            "pt_PT",
            "ro",
            "ru",
            "sv",
            "th",
            "tr",
            "uk",
            "vi",
            "zh_CN",
            "zu"
        )
        resourceConfigurations += locales
        buildConfigField("String[]", "LOCALES", locales.toJavaString())
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
        buildConfig = true
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
                srcDir(file("../platform/packages/apps/Settings/res"))
            }
        }
    }
}

dependencies {
    // Kotlin
    api(project(":android-lib:kotlin"))

    // Jetpack
    api("androidx.activity:activity-ktx:1.9.3")
    api("androidx.annotation:annotation:1.9.1")
    api("androidx.appcompat:appcompat:1.7.0")
    api("androidx.core:core-ktx:1.16.0-alpha01")
    api("androidx.fragment:fragment-ktx:1.8.5")

    // Views
    api("androidx.recyclerview:recyclerview:1.3.2")
    api("androidx.constraintlayout:constraintlayout:2.2.0")
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
