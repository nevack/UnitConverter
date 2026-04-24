plugins {
    alias(libs.plugins.gradle.versions) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.cache.fix) apply false
    id("dev.nevack.plugins.dependency-updates")
    alias(libs.plugins.spotless)
    alias(libs.plugins.dependency.analysis)
}

spotless {
    val ktlintVersion = "1.8.0"

    kotlinGradle {
        ktlint(ktlintVersion)
        target("**/*.gradle.kts")
        targetExclude("**/build/**/*.gradle.kts")
    }
}

dependencyAnalysis {
    structure {
        ignoreKtx(true)
    }
}

tasks.wrapper {
    gradleVersion = "9.4.1"
}
