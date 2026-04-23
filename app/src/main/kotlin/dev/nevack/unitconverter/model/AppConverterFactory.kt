package dev.nevack.unitconverter.model

import dev.nevack.unitconverter.model.converter.ConverterFactory

interface AppConverterFactory : ConverterFactory {
    val appCategory: AppConverterCategory
}
