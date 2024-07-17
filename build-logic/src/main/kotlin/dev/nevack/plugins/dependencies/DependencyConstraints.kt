package dev.nevack.plugins.dependencies

fun buildRejectStrategy(option: IgnoreConstraintsOption): RejectStrategy {
    if (option == IgnoreConstraintsOption.ALL) {
        return AllowAllRejectStrategy()
    }

    val strategies = mutableListOf<RejectStrategy>()

    if (option != IgnoreConstraintsOption.ANDROID) {
        strategies += AndroidRejectStrategy(allowance = 2)
        strategies += AndroidBuildRejectStrategy(allowance = 0)
    }
    if (option != IgnoreConstraintsOption.KOTLIN) {
        strategies += KotlinRejectStrategy(allowance = 1)
    }
    strategies += GuavaAndroidRejectStrategy()
    strategies += GroupExcludeRejectStrategy(emptyList()) // Nothing here for now.

    return CompositeRejectStrategy(strategies)
}

enum class IgnoreConstraintsOption {
    ALL,
    ANDROID,
    KOTLIN,
    NONE,
    ;

    companion object {
        fun from(name: String?): IgnoreConstraintsOption {
            return when ((name ?: return NONE).lowercase()) {
                "all" -> ALL
                "android" -> ANDROID
                "kotlin" -> KOTLIN
                else -> NONE
            }
        }
    }
}

interface RejectStrategy {
    operator fun invoke(
        group: String,
        module: String,
        fromVersion: String,
        toVersion: String,
    ): Boolean
}

class AllowAllRejectStrategy : RejectStrategy {
    override fun invoke(
        group: String,
        module: String,
        fromVersion: String,
        toVersion: String,
    ): Boolean = false
}

class GroupExcludeRejectStrategy(
    private val groups: List<String>,
) : RejectStrategy {
    override fun invoke(
        group: String,
        module: String,
        fromVersion: String,
        toVersion: String,
    ): Boolean {
        if (group in groups) {
            return rejectShortcut(fromVersion, toVersion)
        }
        return false
    }
}

abstract class StabilityRejectStrategy(
    private val allowance: Int = 0,
) : RejectStrategy {
    final override fun invoke(
        group: String,
        module: String,
        fromVersion: String,
        toVersion: String,
    ): Boolean =
        when {
            matches(group) -> {
                val fromStability = getStability(fromVersion)
                val toStability = getStability(toVersion)

                toStability + allowance < fromStability
            }

            else -> false
        }

    abstract fun getStability(version: String): Int

    abstract fun matches(group: String): Boolean
}

class KotlinRejectStrategy(
    allowance: Int = 0,
) : StabilityRejectStrategy(allowance) {
    override fun getStability(version: String): Int = getKotlinStability(version).ordinal

    override fun matches(group: String): Boolean = isKotlin(group) || isKSP(group)
}

class AndroidRejectStrategy(
    allowance: Int = 0,
) : StabilityRejectStrategy(allowance) {
    override fun getStability(version: String): Int = getAndroidStability(version).ordinal

    override fun matches(group: String): Boolean = isAndroidDep(group)
}

class AndroidBuildRejectStrategy(
    allowance: Int = 0,
) : StabilityRejectStrategy(allowance) {
    override fun getStability(version: String): Int = getAndroidStability(version).ordinal

    override fun matches(group: String): Boolean = isAndroidBuildDep(group)
}

class GuavaAndroidRejectStrategy : StabilityRejectStrategy() {
    override fun getStability(version: String): Int = if (version.endsWith("-android")) Int.MAX_VALUE else Int.MIN_VALUE

    override fun matches(group: String): Boolean = group == "com.google.guava"
}

class CompositeRejectStrategy(
    private val strategies: List<RejectStrategy>,
) : RejectStrategy {
    override fun invoke(
        group: String,
        module: String,
        fromVersion: String,
        toVersion: String,
    ): Boolean {
        for (strategy in strategies) {
            if (strategy(group, module, fromVersion, toVersion)) {
                return true
            }
        }
        return false
    }
}

private fun rejectShortcut(
    fromVersion: String,
    toVersion: String,
): Boolean = fromVersion != toVersion
