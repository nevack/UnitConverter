plugins {
    id("dev.nevack.plugins.android-library-module")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dev.nevack.plugins.android-hilt")
}

android {
    namespace = "dev.nevack.unitconverter.nbrb"
}

dependencies {
    implementation(project(":core:converter"))

    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    runtimeOnly(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)

    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.javax.inject)
}
