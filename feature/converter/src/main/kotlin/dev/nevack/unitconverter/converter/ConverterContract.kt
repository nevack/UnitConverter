package dev.nevack.unitconverter.converter

import dev.nevack.unitconverter.model.converter.Converter

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

data class ConverterUiState(
    val drawerOpened: Boolean = false,
    val backgroundColor: Int? = null,
    val title: Int? = null,
    val categoryId: String? = null,
    val converter: Converter? = null,
    val convertData: ConvertData = ConvertData("", "", 0, 1),
    val result: Result = Result.Empty,
    val loadError: String? = null,
    val isLoading: Boolean = false,
)
