pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.github.ben-manes.versions") version "0.42.0"
        id("com.diffplug.spotless") version "6.11.0"
        id("com.android.application") version "7.3.0"
        id("org.jetbrains.kotlin.android") version "1.7.20-RC"
        id("org.jetbrains.kotlin.kapt") version "1.7.20-RC"
        id("com.google.dagger.hilt.android") version "2.43.2"
        id("org.gradle.android.cache-fix") version "2.5.7"
        id("com.google.gms.google-services") version "4.3.14"
        id("com.google.firebase.crashlytics") version "2.9.2"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
