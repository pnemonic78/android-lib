import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(alibs.plugins.android.library)
}

android {
    compileSdk {
        version = release(alibs.versions.android.compileSdk.get().toInt())
    }
    namespace = "com.github.lib"

    defaultConfig {
        minSdk = alibs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
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

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(BuildVersions.jvm.toString())
        }
    }

    sourceSets {
        getByName("main") {
            res {
                directories.add("../platform/packages/apps/Calendar/res")
                directories.add("../platform/packages/apps/Contacts/res")
                directories.add("../platform/packages/apps/DeskClock/res")
                directories.add("../platform/packages/apps/Gallery2/res")
                directories.add("../platform/packages/apps/Settings/res")
            }
        }
    }
}

dependencies {
//    api(project(":kotlin"))
    api(project(":android-lib:kotlin"))

    // Jetpack
    api(alibs.annotation)
    api(alibs.appcompat)
    api(alibs.bundles.ktx)

    // Views
    api(alibs.constraintLayout)
    api(alibs.recyclerview)

    api(alibs.log.timber)

    testImplementation(alibs.bundles.test)
    androidTestImplementation(alibs.bundles.test.android)
}
