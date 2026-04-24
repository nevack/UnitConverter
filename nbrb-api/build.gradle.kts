plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.gradle.android.cache-fix")
    id("com.autonomousapps.dependency-analysis")
    id("com.diffplug.spotless")
}

android {
    namespace = "dev.nevack.unitconverter.nbrb"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    implementation(project(":core:converter"))

    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-android")

    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

    ksp("com.google.dagger:hilt-android-compiler:2.59.2")
    implementation("com.google.dagger:hilt-android:2.59.2")
    implementation("javax.inject:javax.inject:1")
}

kotlin.jvmToolchain(21)

spotless {
    val ktlintVersion = "1.8.0"

    kotlin {
        ktlint(ktlintVersion)
        target("**/*.kt")
    }
}
