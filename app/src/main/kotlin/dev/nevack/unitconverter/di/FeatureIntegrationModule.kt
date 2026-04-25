package dev.nevack.unitconverter.di

import android.content.Context
import android.content.Intent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nevack.unitconverter.converter.ConverterHistoryOpener
import dev.nevack.unitconverter.converter.ConverterHistoryRecord
import dev.nevack.unitconverter.converter.ConverterResultSaver
import dev.nevack.unitconverter.history.HistoryCategoriesProvider
import dev.nevack.unitconverter.history.HistoryCategory
import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.HistoryRepository
import dev.nevack.unitconverter.history.ui.HistoryActivity
import dev.nevack.unitconverter.model.AppConverterCatalog

@Module
@InstallIn(SingletonComponent::class)
object FeatureIntegrationModule {
    @Provides
    fun provideConverterHistoryOpener(): ConverterHistoryOpener =
        ConverterHistoryOpener { context: Context ->
            context.startActivity(Intent(context, HistoryActivity::class.java))
        }

    @Provides
    fun provideConverterResultSaver(historyRepository: HistoryRepository): ConverterResultSaver =
        ConverterResultSaver { record: ConverterHistoryRecord ->
            historyRepository.save(
                HistoryRecord(
                    unitFrom = record.unitFrom,
                    unitTo = record.unitTo,
                    valueFrom = record.valueFrom,
                    valueTo = record.valueTo,
                    categoryId = record.categoryId,
                ),
            )
        }

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
