plugins {
    id("dev.nevack.plugins.android-library-module")
    id("dev.nevack.plugins.android-hilt")
    id("dev.nevack.plugins.android-viewbinding")
}

android {
    namespace = "dev.nevack.unitconverter.feature.converter"
}

dependencies {
    implementation(project(":core:converter"))
    implementation(project(":feature:history-api"))
    implementation(project(":nbrb-api"))

    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)

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
    implementation(libs.androidx.drawerlayout)

    implementation(libs.material)
    implementation(libs.insetter)

    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.javax.inject)
}
