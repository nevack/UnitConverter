plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
    id("com.diffplug.spotless")
    id("org.gradle.android.cache-fix")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "dev.nevack.unitconverter"
        minSdk = 23
        targetSdk = 31
        versionCode = 7
        versionName = "1.1.7"
    }
    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        viewBinding = true
        renderScript = false
        aidl = false
        shaders = false
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    // AndroidX
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.fragment:fragment-ktx:1.4.1")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    // Material
    implementation("com.google.android.material:material:1.6.0-alpha02")
    // Room
    implementation("androidx.room:room-runtime:2.4.1")
    kapt("androidx.room:room-compiler:2.4.1")
    implementation("androidx.room:room-ktx:2.4.1")
    // Okio
    implementation("com.squareup.okio:okio:3.0.0")
    // Moshi
    implementation("com.squareup.moshi:moshi:1.13.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    // Insetter
    implementation("dev.chrisbanes.insetter:insetter:0.6.1")
    // Dagger + Hilt
    implementation("com.google.dagger:hilt-android:2.41")
    kapt("com.google.dagger:hilt-android-compiler:2.40.5")
    // Test
    testImplementation("junit:junit:4.13.2")
}

spotless {
    kotlin {
        ktlint("0.43.2")
    }
}
