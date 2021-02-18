package org.nevack.unitconverter.converter

data class ConvertData internal constructor(
    val value: String,
    val result: String,
    val from: Int,
    val to: Int
)

sealed class Result(val result: String) {
    object Empty : Result("")
    class Converted(result: String) : Result(result)
}
