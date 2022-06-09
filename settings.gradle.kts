pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("com.github.ben-manes.versions") version "0.42.0"
        id("com.diffplug.spotless") version "6.7.0"
        id("com.android.application") version "7.2.1"
        id("org.jetbrains.kotlin.android") version "1.7.0"
        id("org.jetbrains.kotlin.kapt") version "1.6.21"
        id("com.google.dagger.hilt.android") version "2.42"
        id("org.gradle.android.cache-fix") version "2.5.3"
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
