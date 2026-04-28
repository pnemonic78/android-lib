pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("alibs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Android Library"
include(":lib")
include(":kotlin")
include(":kvm")
