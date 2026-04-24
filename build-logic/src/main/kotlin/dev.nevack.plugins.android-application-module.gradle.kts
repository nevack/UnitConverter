plugins {
    id("com.android.application")
    id("dev.nevack.plugins.module-quality")
}

android {
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }
}

kotlin.jvmToolchain(21)
