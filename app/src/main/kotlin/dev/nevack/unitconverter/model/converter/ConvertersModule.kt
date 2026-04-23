package dev.nevack.unitconverter.model.converter

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.AppConverterCategory
import dev.nevack.unitconverter.model.AppConverterFactory
import dev.nevack.unitconverter.model.ConverterCategory
import dev.nevack.unitconverter.model.ConverterCategoryIds
import dev.nevack.unitconverter.nbrb.NBRBRepository

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
                    color = R.color.material_red_500,
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
                    color = R.color.material_green_accent_700,
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
                    color = R.color.material_purple_500,
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
                    color = R.color.material_indigo_500,
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
                    color = R.color.material_bluegrey_500,
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
                    color = R.color.material_teal_500,
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
                    color = R.color.material_blue_500,
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
                    color = R.color.material_orange_500,
                ),
        ) { TimeConverter(context.timeUnits()) }

    @Provides
    @IntoSet
    fun provideCurrencyConverterFactory(
        @ApplicationContext context: Context,
        repository: NBRBRepository,
    ): AppConverterFactory =
        LambdaAppConverterFactory(
            appCategory =
                AppConverterCategory(
                    category = ConverterCategory(ConverterCategoryIds.CURRENCY, 8),
                    categoryName = R.string.currency,
                    icon = R.drawable.ic_currency_usd,
                    color = R.color.material_green_800,
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
                    color = R.color.material_deep_purple_500,
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
