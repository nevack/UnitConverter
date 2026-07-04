package dev.nevack.unitconverter.model.converter

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nevack.unitconverter.design.UnitConverterColors
import dev.nevack.unitconverter.feature.converter.R
import dev.nevack.unitconverter.model.AppConverterCategory
import dev.nevack.unitconverter.model.AppConverterFactory
import dev.nevack.unitconverter.model.ConverterCategory
import dev.nevack.unitconverter.model.ConverterCategoryIds
import dev.nevack.unitconverter.model.CurrencyUnitsRepository

@Module
@InstallIn(SingletonComponent::class)
object ConvertersModule {
    @Provides
    @IntoSet
    fun provideMassConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.MASS, 0),
                    categoryName = R.string.mass,
                    icon = R.drawable.ic_weight,
                    color = UnitConverterColors.Category.Mass,
                ),
        ) { MassConverter(context.massUnits()) }

    @Provides
    @IntoSet
    fun provideVolumeConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.VOLUME, 1),
                    categoryName = R.string.volume,
                    icon = R.drawable.ic_volume,
                    color = UnitConverterColors.Category.Volume,
                ),
        ) { VolumeConverter(context.volumeUnits()) }

    @Provides
    @IntoSet
    fun provideTemperatureConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.TEMPERATURE, 2),
                    categoryName = R.string.temperature,
                    icon = R.drawable.ic_temperature,
                    color = UnitConverterColors.Category.Temperature,
                ),
        ) { TemperatureConverter(context.temperatureUnits()) }

    @Provides
    @IntoSet
    fun provideSpeedConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.SPEED, 3),
                    categoryName = R.string.speed,
                    icon = R.drawable.ic_speed,
                    color = UnitConverterColors.Category.Speed,
                ),
        ) { SpeedConverter(context.speedUnits()) }

    @Provides
    @IntoSet
    fun provideLengthConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.LENGTH, 4),
                    categoryName = R.string.length,
                    icon = R.drawable.ic_ruler,
                    color = UnitConverterColors.Category.Length,
                ),
        ) { LengthConverter(context.lengthUnits()) }

    @Provides
    @IntoSet
    fun provideAreaConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.AREA, 5),
                    categoryName = R.string.area,
                    icon = R.drawable.ic_area,
                    color = UnitConverterColors.Category.Area,
                ),
        ) { AreaConverter(context.areaUnits()) }

    @Provides
    @IntoSet
    fun provideMemoryConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.MEMORY, 6),
                    categoryName = R.string.memory,
                    icon = R.drawable.ic_memory,
                    color = UnitConverterColors.Category.Memory,
                ),
        ) { MemoryConverter(context.memoryUnits()) }

    @Provides
    @IntoSet
    fun provideTimeConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.TIME, 7),
                    categoryName = R.string.time,
                    icon = R.drawable.ic_timer,
                    color = UnitConverterColors.Category.Time,
                ),
        ) { TimeConverter(context.timeUnits()) }

    @Provides
    @IntoSet
    fun provideCurrencyConverterFactory(
        @ApplicationContext context: Context,
        repository: CurrencyUnitsRepository,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.CURRENCY, 8),
                    categoryName = R.string.currency,
                    icon = R.drawable.ic_currency_usd,
                    color = UnitConverterColors.Category.Currency,
                ),
        ) {
            CurrencyConverter(
                bynUnit = context.bynUnit(),
                loadUnits = repository::getUnits,
            )
        }

    @Provides
    @IntoSet
    fun provideOtherConverterFactory(
        @ApplicationContext context: Context,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.OTHER, 9),
                    categoryName = R.string.other,
                    icon = R.drawable.ic_other,
                    color = UnitConverterColors.Category.Other,
                ),
        ) { OtherConverter(context.otherUnits()) }
}

private class LambdaAppConverterFactory(
    override val appCategory: AppConverterCategory,
    private val createConverter: () -> Converter,
) : AppConverterFactory {
    override val category: ConverterCategory
        get() = appCategory.category

    override fun create(): Converter = createConverter()
}
