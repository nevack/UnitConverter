plugins {
    id("com.android.application")
    id("dev.nevack.plugins.signing-config")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.gradle.android.cache-fix")
    id("com.autonomousapps.dependency-analysis")
    id("com.diffplug.spotless")
}

android {
    namespace = "dev.nevack.unitconverter"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.nevack.unitconverter"
        minSdk = 26
        targetSdk = 36
        versionCode = 7
        versionName = "1.1.7"
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro"),
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

//noinspection KtxExtensionAvailable
dependencies {
    implementation(project(":feature:converter"))
    implementation(project(":feature:history"))
    implementation(project(":nbrb-api"))
    // Coroutines
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    // AndroidX
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
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    // Material
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    // Okio
    implementation("com.squareup.okio:okio:3.17.0")
    // Insetter
    implementation("dev.chrisbanes.insetter:insetter:0.6.1")
    // Dagger + Hilt
    ksp("com.google.dagger:hilt-android-compiler:2.59.2")
    implementation("com.google.dagger:dagger:2.59.2")
    implementation("com.google.dagger:hilt-core:2.59.2")
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
