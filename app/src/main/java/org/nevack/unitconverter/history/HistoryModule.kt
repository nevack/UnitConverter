package org.nevack.unitconverter.history

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.room.Room

import org.nevack.unitconverter.history.db.HistoryDatabase

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return Room.databaseBuilder(context, HistoryDatabase::class.java, "history-db").build()
    }
}