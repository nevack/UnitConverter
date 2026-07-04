plugins {
    id("dev.nevack.plugins.android-application-module")
    id("dev.nevack.plugins.signing-config")
    id("dev.nevack.plugins.android-hilt")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "dev.nevack.unitconverter"

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "dev.nevack.unitconverter"
        targetSdk = 37
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
}

//noinspection KtxExtensionAvailable
dependencies {
    implementation(project(":feature:converter"))
    implementation(project(":feature:history"))
    implementation(project(":feature:history-api"))
    implementation(project(":core:design"))
    implementation(project(":nbrb-api"))
    // Coroutines
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    runtimeOnly(libs.kotlinx.coroutines.android)
    // AndroidX
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat.resources)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    // Material
    implementation(libs.material)
    // Okio
    implementation(libs.okio)
    // Dagger + Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.dagger)
    implementation(libs.hilt.core)
    implementation(libs.hilt.android)
    implementation(libs.javax.inject)
}
