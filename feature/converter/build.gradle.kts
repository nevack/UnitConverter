plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.gradle.android.cache-fix")
    id("com.autonomousapps.dependency-analysis")
    id("com.diffplug.spotless")
}

android {
    namespace = "dev.nevack.unitconverter.feature.converter"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:converter"))
    implementation(project(":feature:history-api"))
    implementation(project(":nbrb-api"))

    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("androidx.annotation:annotation:1.10.0")
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.activity:activity-ktx:1.13.0")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.appcompat:appcompat-resources:1.7.1")
    implementation("androidx.lifecycle:lifecycle-common:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.10.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")

    implementation("com.google.android.material:material:1.13.0")
    implementation("dev.chrisbanes.insetter:insetter:0.6.1")

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
