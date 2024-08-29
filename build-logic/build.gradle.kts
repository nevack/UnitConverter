plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle-api:8.6.0")
    implementation("com.github.ben-manes:gradle-versions-plugin:0.51.0")
}

gradlePlugin {
    plugins {
        register("signingConfigs") {
            id = "dev.nevack.plugins.signing-config"
            implementationClass = "dev.nevack.plugins.SigningConfigPlugin"
        }
        register("dependencyUpdates") {
            id = "dev.nevack.plugins.dependency-updates"
            implementationClass = "dev.nevack.plugins.dependencies.DependencyUpdatesPlugin"
        }
    }
}

tasks.checkKotlinGradlePluginConfigurationErrors { enabled = false }
