plugins {
    id("com.autonomousapps.dependency-analysis")
    id("com.diffplug.spotless")
}

spotless {
    val ktlintVersion = "1.8.0"

    kotlin {
        ktlint(ktlintVersion)
        target("**/*.kt")
    }
}
