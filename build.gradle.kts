plugins {
    id("dev.nevack.plugins.dependency-updates")
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "8.2"
}
