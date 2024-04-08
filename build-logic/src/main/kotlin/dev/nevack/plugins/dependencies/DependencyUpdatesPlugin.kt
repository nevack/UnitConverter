package dev.nevack.plugins.dependencies

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.provideDelegate

class DependencyUpdatesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.github.ben-manes.versions")

        val ignoreConstraints: String? by target
        val ignoreConstraintsOption = IgnoreConstraintsOption.from(ignoreConstraints)

        val rejectStrategy = buildRejectStrategy(ignoreConstraintsOption)

        target.tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
            checkForGradleUpdate = true
            gradleReleaseChannel = "current"
            rejectVersionIf {
                rejectStrategy(candidate.group, candidate.module, currentVersion, candidate.version)
            }
        }
    }
}
