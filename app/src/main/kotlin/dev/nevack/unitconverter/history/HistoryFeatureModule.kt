package dev.nevack.unitconverter.history

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nevack.unitconverter.model.AppConverterCatalog

@Module
@InstallIn(SingletonComponent::class)
object HistoryFeatureModule {
    @Provides
    fun provideHistoryCategoriesProvider(catalog: AppConverterCatalog): HistoryCategoriesProvider =
        HistoryCategoriesProvider {
            catalog.categories.map { category ->
                HistoryCategory(
                    id = category.id,
                    nameRes = category.categoryName,
                    iconRes = category.icon,
                    colorRes = category.color,
                )
            }
        }
}
