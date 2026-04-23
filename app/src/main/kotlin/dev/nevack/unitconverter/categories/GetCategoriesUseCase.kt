package dev.nevack.unitconverter.categories

import dev.nevack.unitconverter.model.AppConverterCatalog
import dev.nevack.unitconverter.model.AppConverterCategory
import javax.inject.Inject

class GetCategoriesUseCase
    @Inject
    constructor(
        private val catalog: AppConverterCatalog,
    ) {
        operator fun invoke(): List<AppConverterCategory> = catalog.categories
    }
