plugins {
    id("com.github.ben-manes.versions") version "0.42.0"
    id("com.diffplug.spotless") version "6.4.2" apply false
    id("com.android.application") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.20" apply false
    id("org.jetbrains.kotlin.kapt") version "1.6.20" apply false
    id("com.google.dagger.hilt.android") version "2.41" apply false
    id("org.gradle.android.cache-fix") version "2.5.0" apply false
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.4.2"
}

fun isNotStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.dependencyUpdates {
    checkForGradleUpdate = true
    gradleReleaseChannel = "current"
    rejectVersionIf {
        if (candidate.module == "org.jacoco.ant") true
        else isNotStable(candidate.version) && !isNotStable(currentVersion)
    }
}
