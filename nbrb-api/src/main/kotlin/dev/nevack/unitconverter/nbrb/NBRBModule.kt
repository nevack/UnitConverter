package dev.nevack.unitconverter.nbrb

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.nevack.unitconverter.nbrb.model.NBRBCurrency
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object NBRBModule {
    @Provides
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    @Provides
    fun provideRetrofit(json: Json): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://api.nbrb.by/exrates/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    fun provideNBRBService(retrofit: Retrofit): NBRBService = retrofit.create(NBRBService::class.java)

    @Provides
    fun provideNBRBRepository(
        @ApplicationContext context: Context,
        service: NBRBService,
        json: Json,
    ): NBRBRepository {
        val locale = NBRBCurrency.getCompatibleLocale(context.resources.configuration.locales)
        return NBRBRepository(locale, { name -> File(context.filesDir, name) }, service, json)
    }
}
