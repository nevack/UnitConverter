plugins {
    id("dev.nevack.plugins.android-library-module")
    id("dev.nevack.plugins.android-hilt")
    id("dev.nevack.plugins.android-viewbinding")
}

android {
    namespace = "dev.nevack.unitconverter.feature.history"
}

dependencies {
    implementation(project(":feature:history-api"))

    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.livedata.core)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.recyclerview)

    implementation(libs.material)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.sqlite)
    implementation(libs.insetter)

    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.javax.inject)
}
