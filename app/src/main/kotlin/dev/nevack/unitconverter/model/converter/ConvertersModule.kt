package dev.nevack.unitconverter.model.converter

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConverterCategory
import dev.nevack.unitconverter.nbrb.NBRBRepository

@Module
@InstallIn(SingletonComponent::class)
object ConvertersModule {
    @Provides
    @IntoSet
    fun provideMassConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category = ConverterCategory("mass", R.string.mass, R.drawable.ic_weight, R.color.material_red_500, 0),
        ) { MassConverter(context) }

    @Provides
    @IntoSet
    fun provideVolumeConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category =
                ConverterCategory(
                    "volume",
                    R.string.volume,
                    R.drawable.ic_volume,
                    R.color.material_green_accent_700,
                    1,
                ),
        ) { VolumeConverter(context) }

    @Provides
    @IntoSet
    fun provideTemperatureConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category =
                ConverterCategory(
                    "temperature",
                    R.string.temperature,
                    R.drawable.ic_temperature,
                    R.color.material_purple_500,
                    2,
                ),
        ) { TemperatureConverter(context) }

    @Provides
    @IntoSet
    fun provideSpeedConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category =
                ConverterCategory(
                    "speed",
                    R.string.speed,
                    R.drawable.ic_speed,
                    R.color.material_indigo_500,
                    3,
                ),
        ) { SpeedConverter(context) }

    @Provides
    @IntoSet
    fun provideLengthConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category =
                ConverterCategory(
                    "length",
                    R.string.length,
                    R.drawable.ic_ruler,
                    R.color.material_bluegrey_500,
                    4,
                ),
        ) { LengthConverter(context) }

    @Provides
    @IntoSet
    fun provideAreaConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category = ConverterCategory("area", R.string.area, R.drawable.ic_area, R.color.material_teal_500, 5),
        ) { AreaConverter(context) }

    @Provides
    @IntoSet
    fun provideMemoryConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category =
                ConverterCategory(
                    "memory",
                    R.string.memory,
                    R.drawable.ic_memory,
                    R.color.material_blue_500,
                    6,
                ),
        ) { MemoryConverter(context) }

    @Provides
    @IntoSet
    fun provideTimeConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category = ConverterCategory("time", R.string.time, R.drawable.ic_timer, R.color.material_orange_500, 7),
        ) { TimeConverter(context) }

    @Provides
    @IntoSet
    fun provideCurrencyConverterFactory(
        @ApplicationContext context: Context,
        repository: NBRBRepository,
    ): ConverterFactory =
        LambdaConverterFactory(
            category =
                ConverterCategory(
                    "currency",
                    R.string.currency,
                    R.drawable.ic_currency_usd,
                    R.color.material_green_800,
                    8,
                ),
        ) { CurrencyConverter(context, repository) }

    @Provides
    @IntoSet
    fun provideOtherConverterFactory(
        @ApplicationContext context: Context,
    ): ConverterFactory =
        LambdaConverterFactory(
            category =
                ConverterCategory(
                    "other",
                    R.string.other,
                    R.drawable.ic_other,
                    R.color.material_deep_purple_500,
                    9,
                ),
        ) { OtherConverter(context) }
}

private class LambdaConverterFactory(
    override val category: ConverterCategory,
    private val createConverter: () -> Converter,
) : ConverterFactory {
    override fun create(): Converter = createConverter()
}
