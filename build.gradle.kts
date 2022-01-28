plugins {
    id("com.github.ben-manes.versions") version "0.41.0"
    id("com.diffplug.spotless") version "6.2.0" apply false
    id("com.android.application") version "7.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.kotlin.kapt") version "1.6.10" apply false
    id("dagger.hilt.android.plugin") version "2.40.5" apply false
    id("org.gradle.android.cache-fix") version "2.4.6" apply false
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.3.3"
}
