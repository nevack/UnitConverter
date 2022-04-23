plugins {
    id("com.github.ben-manes.versions") version "0.42.0"
    id("com.diffplug.spotless") version "6.5.0" apply false
    id("com.android.application") version "7.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("org.jetbrains.kotlin.kapt") version "1.6.21" apply false
    id("com.google.dagger.hilt.android") version "2.41" apply false
    id("org.gradle.android.cache-fix") version "2.5.1" apply false
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.4.2"
}

enum class AndroidStability {
    ALPHA, BETA, RC, STABLE
}

fun getAndroidStability(version: String): AndroidStability = when {
    version.contains("alpha") -> AndroidStability.ALPHA
    version.contains("beta") -> AndroidStability.BETA
    version.contains("rc") -> AndroidStability.RC
    else -> AndroidStability.STABLE
}

fun isAndroidDep(group: String): Boolean = when {
    group.startsWith("androidx.") -> true
    group.startsWith("com.google.android.") -> true
    group.startsWith("com.android.") -> true
    else -> false
}

tasks.dependencyUpdates {
    checkForGradleUpdate = true
    gradleReleaseChannel = "current"
    rejectVersionIf {
        when {
            candidate.module == "org.jacoco.ant" -> true
            isAndroidDep(candidate.group) -> {
                getAndroidStability(candidate.version) < getAndroidStability(currentVersion)
            }
            else -> false
        }
    }
}
