plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle-api:7.3.1")
    implementation("com.github.ben-manes:gradle-versions-plugin:0.42.0")
}

gradlePlugin {
    plugins {
        register("signingConfigs") {
            id = "dev.nevack.plugins.signing-config"
            implementationClass = "dev.nevack.plugins.SigningConfigPlugin"
        }
        register("dependencyUpdates") {
            id = "dev.nevack.plugins.dependency-updates"
            implementationClass = "dev.nevack.plugins.DependencyUpdatesPlugin"
        }
    }
}