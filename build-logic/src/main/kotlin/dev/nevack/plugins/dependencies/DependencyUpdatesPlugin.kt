package dev.nevack.plugins.dependencies

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.named

class DependencyUpdatesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.github.ben-manes.versions")

        val ignoreConstraints = target.findProperty("ignoreConstraints") as? String?
        val ignoreConstraintsOption = IgnoreConstraintsOption.from(ignoreConstraints)

        val rejectStrategy = buildRejectStrategy(ignoreConstraintsOption)

        target.tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
            checkForGradleUpdate = true
            gradleReleaseChannel = "current"
            rejectVersionIf {
                rejectStrategy(candidate.group, candidate.module, currentVersion, candidate.version)
            }
            filterConfigurations =
                Spec { configuration ->
                    !configuration.name.endsWith("DependencySources", ignoreCase = true)
                }
            notCompatibleWithConfigurationCache("DependencyUpdates")
        }
    }
}
