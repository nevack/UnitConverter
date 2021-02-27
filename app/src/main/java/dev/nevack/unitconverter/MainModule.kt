package dev.nevack.unitconverter

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.nevack.unitconverter.model.nbrb.NBRBCurrency
import dev.nevack.unitconverter.model.nbrb.NBRBRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.Date

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, DateJsonAdapter())
        .build()

    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.nbrb.by/API/ExRates/")
        .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

    @Provides
    fun provideNBRBService(retrofit: Retrofit): NBRBService =
        retrofit.create(NBRBService::class.java)

    @Provides
    fun provideNBRBRepository(
        @ApplicationContext context: Context,
        service: NBRBService,
        moshi: Moshi
    ): NBRBRepository {
        val localeList = ConfigurationCompat.getLocales(context.resources.configuration)
        val locale = NBRBCurrency.getCompatibleLocale(localeList)
        return NBRBRepository(locale, { name -> File(context.filesDir, name) }, service, moshi)
    }
}
