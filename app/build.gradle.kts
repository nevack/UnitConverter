plugins {
    id("dev.nevack.plugins.android-application-module")
    id("dev.nevack.plugins.signing-config")
    id("dev.nevack.plugins.android-hilt")
    id("dev.nevack.plugins.android-viewbinding")
}

android {
    namespace = "dev.nevack.unitconverter"

    defaultConfig {
        applicationId = "dev.nevack.unitconverter"
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
}

//noinspection KtxExtensionAvailable
dependencies {
    implementation(project(":feature:converter"))
    implementation(project(":feature:history"))
    implementation(project(":nbrb-api"))
    // Coroutines
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    runtimeOnly(libs.kotlinx.coroutines.android)
    // AndroidX
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.livedata.core)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.recyclerview)
    // Material
    implementation(libs.material)
    implementation(libs.androidx.drawerlayout)
    // Okio
    implementation(libs.okio)
    // Insetter
    implementation(libs.insetter)
    // Dagger + Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.dagger)
    implementation(libs.hilt.core)
    implementation(libs.hilt.android)
    implementation(libs.javax.inject)
}
