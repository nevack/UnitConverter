plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:9.2.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.20")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.3.7")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.59.2")
    implementation("org.gradle.android.cache-fix:org.gradle.android.cache-fix.gradle.plugin:3.0.3")
    implementation("com.github.ben-manes:gradle-versions-plugin:0.54.0")
    implementation("com.diffplug.spotless:com.diffplug.spotless.gradle.plugin:8.4.0")
    implementation("com.autonomousapps.dependency-analysis:com.autonomousapps.dependency-analysis.gradle.plugin:3.9.0")
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
