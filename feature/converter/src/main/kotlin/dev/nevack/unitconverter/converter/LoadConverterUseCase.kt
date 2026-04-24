package dev.nevack.unitconverter.converter

import dev.nevack.unitconverter.model.AppConverterCatalog
import dev.nevack.unitconverter.model.converter.Converter
import javax.inject.Inject

class LoadConverterUseCase
    @Inject
    constructor(
        private val catalog: AppConverterCatalog,
    ) {
        suspend operator fun invoke(categoryId: String): Converter? {
            catalog.getCategory(categoryId) ?: return null
            return catalog.createConverter(categoryId).also { it.load() }
        }
    }
