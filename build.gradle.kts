plugins {
    id("com.github.ben-manes.versions") apply false
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("com.google.devtools.ksp") apply false
    id("com.google.dagger.hilt.android") apply false
    id("org.gradle.android.cache-fix") apply false
    id("com.google.gms.google-services") apply false
    id("com.google.firebase.crashlytics") apply false
    id("dev.nevack.plugins.dependency-updates")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint("1.3.1")
        target("**/*.kt")
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("1.3.1")
    }
}

tasks.wrapper {
    gradleVersion = "8.9"
}
