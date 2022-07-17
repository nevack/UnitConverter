package dev.nevack.plugins

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.provideDelegate

class DependencyUpdatesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.github.ben-manes.versions")

        val ignoreConstraints: String? by target
        val ignoreConstraintsBoolean = ignoreConstraints?.toBoolean() ?: false

        target.tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
            checkForGradleUpdate = true
            gradleReleaseChannel = "current"
            rejectVersionIf {
                when {
                    ignoreConstraintsBoolean -> false
                    candidate.module == "org.jacoco.ant" -> candidate.version != currentVersion
                    isAndroidDep(candidate.group) -> {
                        getAndroidStability(candidate.version) < getAndroidStability(currentVersion)
                    }
                    isKotlin(candidate.group) -> {
                        isKotlinStable(currentVersion) && !isKotlinStable(candidate.version)
                    }
                    else -> false
                }
            }
        }
    }
}

private enum class AndroidStability {
    ALPHA, BETA, RC, STABLE
}

private fun getAndroidStability(version: String): AndroidStability = when {
    version.contains("alpha") -> AndroidStability.ALPHA
    version.contains("beta") -> AndroidStability.BETA
    version.contains("rc") -> AndroidStability.RC
    else -> AndroidStability.STABLE
}

private fun isAndroidDep(group: String): Boolean = when {
    group.startsWith("androidx.") -> true
    group.startsWith("com.google.android.") -> true
    group.startsWith("com.android.") -> true
    else -> false
}

private fun isKotlin(group: String): Boolean = group.startsWith("org.jetbrains.kotlin")

private fun isKotlinStable(version: String): Boolean = when {
    version.contains("Beta") -> false
    version.contains("RC") -> false
    else -> true
}
