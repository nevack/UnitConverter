package dev.nevack.plugins

import com.android.build.api.dsl.ApplicationExtension

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties

class SigningConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val signingPropertiesFile = rootProject.file("signing.properties")
        if (!signingPropertiesFile.exists()) return

        val signingProperties = Properties()
        signingPropertiesFile.inputStream().use { inputStream ->
            signingProperties.load(inputStream)
        }

        pluginManager.withPlugin("com.android.application") {
            extensions.configure<ApplicationExtension>("android") {
                val mainSigningConfig = signingConfigs.create("main") {
                    storeFile = rootProject.file(signingProperties.getProperty("storeFile"))
                    storePassword = signingProperties.getProperty("storePassword")
                    keyAlias = signingProperties.getProperty("keyAlias")
                    keyPassword = signingProperties.getProperty("keyPassword")
                }

                buildTypes {
                    release {
                        signingConfig = mainSigningConfig
                    }
                }
            }
        }
    }
}
