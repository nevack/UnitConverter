package dev.nevack.unitconverter.converter

data class ConvertData(
    val value: String,
    val result: String,
    val from: Int,
    val to: Int,
) {
    fun swap() = ConvertData(result, value, to, from)
}

sealed class Result(
    val result: String,
) {
    data object Empty : Result("")

    class Converted(
        result: String,
    ) : Result(result)
}
