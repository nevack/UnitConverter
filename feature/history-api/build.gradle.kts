plugins {
    id("com.android.library")
    id("com.diffplug.spotless")
    id("com.autonomousapps.dependency-analysis")
}

android {
    namespace = "dev.nevack.unitconverter.feature.history.api"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.10.0")
}

kotlin.jvmToolchain(21)

spotless {
    val ktlintVersion = "1.8.0"

    kotlin {
        ktlint(ktlintVersion)
        target("**/*.kt")
    }
}
