plugins {
    id("dev.nevack.plugins.android-library-module")
    id("dev.nevack.plugins.android-hilt")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "dev.nevack.unitconverter.feature.converter"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:converter"))
    implementation(project(":core:design"))
    implementation(project(":nbrb-api"))

    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.activity.compose)
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
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.javax.inject)
}
