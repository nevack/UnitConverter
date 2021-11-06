plugins {
    id("com.diffplug.spotless") version "5.17.1"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.github.spotbugs") version "4.7.9"
    id("com.android.application") version "7.2.0-alpha03" apply false
    id("org.jetbrains.kotlin.android") version "1.5.31" apply false
    id("dagger.hilt.android.plugin") version "2.40" apply false
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("$buildDir/**/*.kt")
        targetExclude("bin/**/*.kt")

        ktlint("0.42.1")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.3-rc-5"
}
