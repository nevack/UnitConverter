plugins {
    id("dev.nevack.plugins.android-library-module")
}

android {
    namespace = "dev.nevack.unitconverter.feature.history.api"
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
}
