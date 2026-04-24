package dev.nevack.unitconverter.converter

import dev.nevack.unitconverter.model.AppConverterCatalog
import dev.nevack.unitconverter.model.AppConverterCategory
import javax.inject.Inject

class GetConverterCategoryUseCase
    @Inject
    constructor(
        private val catalog: AppConverterCatalog,
    ) {
        operator fun invoke(categoryId: String): AppConverterCategory? = catalog.getCategory(categoryId)
    }
