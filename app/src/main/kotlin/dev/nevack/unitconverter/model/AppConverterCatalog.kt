package dev.nevack.unitconverter.model

import dev.nevack.unitconverter.model.converter.Converter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConverterCatalog
    @Inject
    constructor(
        factories: Set<@JvmSuppressWildcards AppConverterFactory>,
    ) {
        private val catalog = ConverterCatalog(factories)
        private val categoriesById =
            factories
                .sortedBy { it.category.order }
                .associateBy(
                    keySelector = { it.category.id },
                    valueTransform = { it.appCategory },
                ).also { registeredCategories ->
                    require(registeredCategories.size == factories.size) {
                        "Converter category IDs must be unique"
                    }
                }

        val categories: List<AppConverterCategory> = catalog.categories.map { category -> requireNotNull(categoriesById[category.id]) }

        fun getCategory(id: String): AppConverterCategory? = categoriesById[id]

        fun createConverter(id: String): Converter = catalog.createConverter(id)
    }
