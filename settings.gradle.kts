@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.github.ben-manes.versions") version "0.44.0"
        id("com.diffplug.spotless") version "6.13.0"
        id("com.android.application") version "7.4.0"
        id("org.jetbrains.kotlin.android") version "1.8.0"
        id("org.jetbrains.kotlin.kapt") version "1.8.0"
        id("com.google.dagger.hilt.android") version "2.44.2"
        id("org.gradle.android.cache-fix") version "2.6.1"
        id("com.google.gms.google-services") version "4.3.14"
        id("com.google.firebase.crashlytics") version "2.9.2"
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
