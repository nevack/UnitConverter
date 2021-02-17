package org.nevack.unitconverter.history

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.nevack.unitconverter.history.db.HistoryDatabase

@Module
@InstallIn(SingletonComponent::class)
public object HistoryModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return Room.databaseBuilder(context, HistoryDatabase::class.java, "history-db").build()
    }
}
