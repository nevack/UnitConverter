@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.github.ben-manes.versions") version "0.54.0"
        id("com.diffplug.spotless") version "8.4.0"
        id("com.android.application") version "9.2.0"
        id("com.android.library") version "9.2.0"
        id("org.jetbrains.kotlin.jvm") version "2.3.20"
        id("org.jetbrains.kotlin.android") version "2.3.20"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.3.20"
        id("com.google.devtools.ksp") version "2.3.7"
        id("com.google.dagger.hilt.android") version "2.59.2"
        id("org.gradle.android.cache-fix") version "3.0.3"
        id("com.autonomousapps.dependency-analysis") version "3.9.0"
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
include(":converter-core")
include(":nbrb-api")
