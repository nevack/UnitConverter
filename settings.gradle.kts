@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.github.ben-manes.versions") version "0.50.0"
        id("com.diffplug.spotless") version "6.22.0"
        id("com.android.application") version "8.2.0-rc03"
        id("org.jetbrains.kotlin.android") version "1.9.20"
        id("com.google.devtools.ksp") version "1.9.21-1.0.15"
        id("com.google.dagger.hilt.android") version "2.48.1"
        id("org.gradle.android.cache-fix") version "3.0"
        id("com.google.gms.google-services") version "4.4.0"
        id("com.google.firebase.crashlytics") version "2.9.9"
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
