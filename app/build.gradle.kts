plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "dev.nevack.unitconverter"
        minSdk = 23
        targetSdk = 30
        versionCode = 7
        versionName = "1.1.7"
    }
    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0-M2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
    // AndroidX
    implementation("androidx.core:core-ktx:1.6.0-alpha01")
    implementation("androidx.fragment:fragment-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.3.0-rc01")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.4.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha01")
    implementation("androidx.recyclerview:recyclerview:1.2.0-rc01")
    // Material
    implementation("com.google.android.material:material:1.4.0-alpha02")
    // Room
    implementation("androidx.room:room-runtime:2.3.0-rc01")
    kapt("androidx.room:room-compiler:2.3.0-rc01")
    implementation("androidx.room:room-ktx:2.3.0-rc01")
    // Okio
    implementation("com.squareup.okio:okio:3.0.0-alpha.3")
    // Moshi
    implementation("com.squareup.moshi:moshi:1.12.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    // Insetter
    implementation("dev.chrisbanes.insetter:insetter:0.5.0")
    // Dagger + Hilt
    implementation("com.google.dagger:hilt-android:2.34-beta")
    kapt("com.google.dagger:hilt-android-compiler:2.33-beta")
    // Test
    testImplementation("junit:junit:4.13.2")
}
