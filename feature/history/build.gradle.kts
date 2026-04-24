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

    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("androidx.annotation:annotation:1.10.0")
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.room:room-common:2.8.4")
    implementation("androidx.room:room-runtime:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")
    implementation("androidx.sqlite:sqlite:2.6.2")
    implementation("dev.chrisbanes.insetter:insetter:0.6.1")

    ksp("com.google.dagger:hilt-android-compiler:2.59.2")
    implementation("com.google.dagger:hilt-android:2.59.2")
    implementation("javax.inject:javax.inject:1")
}
