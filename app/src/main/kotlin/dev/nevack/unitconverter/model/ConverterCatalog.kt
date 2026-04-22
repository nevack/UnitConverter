package dev.nevack.unitconverter.model

import dev.nevack.unitconverter.model.converter.Converter
import dev.nevack.unitconverter.model.converter.ConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConverterCatalog
    @Inject
    constructor(
        factories: Set<@JvmSuppressWildcards ConverterFactory>,
    ) {
        private val factoriesById =
            factories
                .sortedBy { it.category.order }
                .associateBy { it.category.id }
                .also { registeredFactories ->
                    require(registeredFactories.size == factories.size) {
                        "Converter category IDs must be unique"
                    }
                }

        val categories: List<ConverterCategory> = factoriesById.values.map { it.category }

        fun getCategory(id: String): ConverterCategory? = factoriesById[id]?.category

        fun createConverter(id: String): Converter = requireNotNull(factoriesById[id]) { "Unknown converter category: $id" }.create()
    }
