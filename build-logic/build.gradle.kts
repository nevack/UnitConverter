plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.hilt.gradle.plugin)
    implementation(libs.android.cache.fix.gradle.plugin)
    implementation(libs.gradle.versions.plugin)
    implementation(libs.spotless.gradle.plugin)
    implementation(libs.dependency.analysis.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("signingConfigs") {
            id = "dev.nevack.plugins.signing-config"
            implementationClass = "dev.nevack.plugins.SigningConfigPlugin"
        }
        register("dependencyUpdates") {
            id = "dev.nevack.plugins.dependency-updates"
            implementationClass = "dev.nevack.plugins.dependencies.DependencyUpdatesPlugin"
        }
    }
}
