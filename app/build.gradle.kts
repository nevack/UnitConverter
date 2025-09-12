plugins {
    id("com.android.application")
    id("dev.nevack.plugins.signing-config")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.gradle.android.cache-fix")
    id("com.autonomousapps.dependency-analysis")
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
    // Coroutines
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    // AndroidX
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.activity:activity-ktx:1.11.0")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.appcompat:appcompat-resources:1.7.1")
    implementation("androidx.lifecycle:lifecycle-common:2.9.3")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.9.3")
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.9.3")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    // Material
    implementation("com.google.android.material:material:1.13.0-rc01")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    // Room
    implementation("androidx.room:room-common:2.8.0-rc01")
    implementation("androidx.room:room-runtime:2.8.0-rc01")
    ksp("androidx.room:room-compiler:2.8.0-rc01")
    implementation("androidx.sqlite:sqlite:2.6.0-rc01")
    // Okio
    implementation("com.squareup.okio:okio:3.16.0")
    // Moshi
    implementation("com.squareup.moshi:moshi:1.15.2")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-moshi:3.0.0")
    // Insetter
    implementation("dev.chrisbanes.insetter:insetter:0.6.1")
    // Dagger + Hilt
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")
    implementation("com.google.dagger:dagger:2.57.1")
    implementation("com.google.dagger:hilt-core:2.57.1")
    implementation("com.google.dagger:hilt-android:2.57.1")
    implementation("javax.inject:javax.inject:1")
}

kotlin.jvmToolchain(21)
