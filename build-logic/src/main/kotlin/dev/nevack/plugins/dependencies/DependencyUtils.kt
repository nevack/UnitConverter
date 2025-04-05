package dev.nevack.plugins.dependencies

interface DependencyStability {
    val stability: Int
}

enum class AndroidStability(
    override val stability: Int,
) : DependencyStability {
    ALPHA(-1),
    BETA(1),
    RC(2),
    STABLE(3),
}

fun getAndroidStability(version: String): AndroidStability =
    when {
        version.contains("alpha") -> AndroidStability.ALPHA
        version.contains("beta") -> AndroidStability.BETA
        version.contains("rc") -> AndroidStability.RC
        else -> AndroidStability.STABLE
    }

fun isAndroidDep(group: String): Boolean =
    when {
        isAndroidBuildDep(group) -> false
        group.startsWith("androidx.") -> true
        group.startsWith("com.google.android.") -> true
        else -> false
    }

fun isAndroidBuildDep(group: String): Boolean =
    when {
        group == "androidx.databinding" -> true
        group.startsWith("com.android.") -> true
        else -> false
    }

enum class KotlinStability(
    override val stability: Int,
) : DependencyStability {
    DEV(-1),
    BETA(1),
    RC(2),
    STABLE(3),
}

fun getKotlinStability(version: String): KotlinStability =
    when {
        version.contains("dev", ignoreCase = true) -> KotlinStability.DEV
        version.contains("beta", ignoreCase = true) -> KotlinStability.BETA
        version.contains("rc", ignoreCase = true) -> KotlinStability.RC
        else -> KotlinStability.STABLE
    }

fun isKotlin(group: String): Boolean = group.startsWith("org.jetbrains.kotlin")

fun isKSP(group: String): Boolean = group.startsWith("com.google.devtools.ksp")
