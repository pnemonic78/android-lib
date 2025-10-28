plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = libs.versions.compileSdk.toInt()
    namespace = "com.github.lib"

    defaultConfig {
        minSdk = libs.versions.minSdk.toInt()
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
    api(project(":kotlin"))
    //api(project(":android-lib:kotlin"))

    // Jetpack
    api(libs.annotation)
    api(libs.appcompat)
    api(libs.bundles.ktx)

    // Views
    api(libs.constraintLayout)
    api(libs.recyclerview)

    api(libs.log.timber)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
}
