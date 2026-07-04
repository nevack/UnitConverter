plugins {
    id("dev.nevack.plugins.android-library-module")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "dev.nevack.unitconverter.core.design"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
}
