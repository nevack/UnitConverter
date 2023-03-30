@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.github.ben-manes.versions") version "0.46.0"
        id("com.diffplug.spotless") version "6.17.0"
        id("com.android.application") version "7.4.2"
        id("org.jetbrains.kotlin.android") version "1.8.20"
        id("org.jetbrains.kotlin.kapt") version "1.8.20"
        id("com.google.dagger.hilt.android") version "2.45"
        id("org.gradle.android.cache-fix") version "2.7.0"
        id("com.google.gms.google-services") version "4.3.15"
        id("com.google.firebase.crashlytics") version "2.9.4"
    }

    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(":app")
