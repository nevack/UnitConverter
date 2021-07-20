// Top-level build file where you can add configuration options common to all sub-projects/modules.

import com.diffplug.gradle.spotless.SpotlessPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha03")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38")
    }
}

plugins {
    id("com.diffplug.spotless") version "5.14.2"
    id("com.github.ben-manes.versions") version "0.39.0"
}

subprojects {
    apply<SpotlessPlugin>()
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint("0.41.0")
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true

            // Set JVM target to 1.8
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.0.2"
}
