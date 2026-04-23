package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConverterCategory

interface ConverterFactory {
    val category: ConverterCategory

    fun create(): Converter
}
