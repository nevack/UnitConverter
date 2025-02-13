plugins {
    id("com.github.ben-manes.versions") apply false
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("com.google.devtools.ksp") apply false
    id("com.google.dagger.hilt.android") apply false
    id("org.gradle.android.cache-fix") apply false
    id("dev.nevack.plugins.dependency-updates")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint("1.5.0")
        target("**/*.kt")
    }

    kotlinGradle {
        ktlint("1.5.0")
        target("**/*.gradle.kts")
    }
}

tasks.wrapper {
    gradleVersion = "8.13-rc-1"
}
