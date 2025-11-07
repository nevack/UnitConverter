@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.github.ben-manes.versions") version "0.53.0"
        id("com.diffplug.spotless") version "8.0.0"
        id("com.android.application") version "8.13.0"
        id("org.jetbrains.kotlin.android") version "2.2.21"
        id("com.google.devtools.ksp") version "2.3.2"
        id("com.google.dagger.hilt.android") version "2.57.2"
        id("org.gradle.android.cache-fix") version "3.0.2"
        id("com.autonomousapps.dependency-analysis") version "3.4.1"
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
