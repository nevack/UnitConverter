@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        val kotlinVersion = "1.9.22"
        val kspSubVersion = "1.0.17"
        id("com.github.ben-manes.versions") version "0.51.0"
        id("com.diffplug.spotless") version "6.25.0"
        id("com.android.application") version "8.2.2"
        id("org.jetbrains.kotlin.android") version kotlinVersion
        id("com.google.devtools.ksp") version "$kotlinVersion-$kspSubVersion"
        id("com.google.dagger.hilt.android") version "2.51"
        id("org.gradle.android.cache-fix") version "3.0.1"
        id("com.google.gms.google-services") version "4.4.1"
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
