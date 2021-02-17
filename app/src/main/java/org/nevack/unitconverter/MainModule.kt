package org.nevack.unitconverter

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.nbrb.by/API/ExRates/")
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
    }

    @Provides
    fun provideNBRBService(retrofit: Retrofit): NBRBService {
        return retrofit.create(NBRBService::class.java)
    }
}
