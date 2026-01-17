plugins {
    id("com.github.ben-manes.versions") apply false
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("com.google.devtools.ksp") apply false
    id("com.google.dagger.hilt.android") apply false
    id("org.gradle.android.cache-fix") apply false
    id("dev.nevack.plugins.dependency-updates")
    id("com.diffplug.spotless")
    id("com.autonomousapps.dependency-analysis")
}

spotless {
    val ktlintVersion = "1.7.1"

    kotlin {
        ktlint(ktlintVersion)
        target("**/*.kt")
    }

    kotlinGradle {
        ktlint(ktlintVersion)
        target("**/*.gradle.kts")
    }
}

dependencyAnalysis {
    structure {
        ignoreKtx(true)
    }
}

tasks.wrapper {
    gradleVersion = "9.3.0"
}
