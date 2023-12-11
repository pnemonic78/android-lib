// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${BuildVersions.androidGradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildVersions.kotlinVersion}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
