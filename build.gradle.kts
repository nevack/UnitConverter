plugins {
    id("com.github.ben-manes.versions")
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

fun isKotlin(group: String): Boolean = group.startsWith("org.jetbrains.kotlin")

fun isKotlinBeta(version: String): Boolean = version.contains("Beta")

tasks.dependencyUpdates {
    checkForGradleUpdate = true
    gradleReleaseChannel = "current"
    rejectVersionIf {
        when {
            candidate.module == "org.jacoco.ant" -> true
            isAndroidDep(candidate.group) -> {
                getAndroidStability(candidate.version) < getAndroidStability(currentVersion)
            }
            isKotlin(candidate.group) -> {
                !isKotlinBeta(currentVersion) && isKotlinBeta(candidate.version)
            }
            else -> false
        }
    }
}
