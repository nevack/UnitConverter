plugins {
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.diffplug.spotless") version "6.0.1" apply false
    id("com.android.application") version "7.2.0-alpha05" apply false
    id("org.jetbrains.kotlin.android") version "1.6.0" apply false
    id("org.jetbrains.kotlin.kapt") version "1.6.0" apply false
    id("dagger.hilt.android.plugin") version "2.40.2" apply false
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.3"
}
