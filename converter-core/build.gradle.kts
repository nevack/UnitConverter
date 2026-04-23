plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.diffplug.spotless")
    id("com.autonomousapps.dependency-analysis")
}

kotlin.jvmToolchain(21)

dependencies {
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
}

spotless {
    val ktlintVersion = "1.8.0"

    kotlin {
        ktlint(ktlintVersion)
        target("**/*.kt")
    }
}
