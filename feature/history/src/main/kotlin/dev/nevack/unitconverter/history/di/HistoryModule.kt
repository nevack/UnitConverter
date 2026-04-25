package dev.nevack.unitconverter.history.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dev.nevack.unitconverter.history.HistoryRepository
import dev.nevack.unitconverter.history.data.RoomHistoryRepository
import dev.nevack.unitconverter.history.data.db.HistoryDao
import dev.nevack.unitconverter.history.data.db.HistoryDatabase

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): HistoryDatabase =
        Room
            .databaseBuilder(context, HistoryDatabase::class.java, "history-db")
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideHistoryDao(database: HistoryDatabase): HistoryDao = database.dao()

    @Provides
    @Singleton
    fun provideHistoryRepository(dao: HistoryDao): HistoryRepository = RoomHistoryRepository(dao)

    private val MIGRATION_1_2 =
        object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS history_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        unit_from TEXT NOT NULL,
                        unit_to TEXT NOT NULL,
                        value_from TEXT NOT NULL,
                        value_to TEXT NOT NULL,
                        category_id TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    INSERT INTO history_new (id, unit_from, unit_to, value_from, value_to, category_id)
                    SELECT id, unit_from, unit_to, value_from, value_to,
                        CASE category
                            WHEN 0 THEN 'mass'
                            WHEN 1 THEN 'volume'
                            WHEN 2 THEN 'temperature'
                            WHEN 3 THEN 'speed'
                            WHEN 4 THEN 'length'
                            WHEN 5 THEN 'area'
                            WHEN 6 THEN 'memory'
                            WHEN 7 THEN 'time'
                            WHEN 8 THEN 'currency'
                            ELSE 'other'
                        END
                    FROM history
                    """.trimIndent(),
                )
                db.execSQL("DROP TABLE history")
                db.execSQL("ALTER TABLE history_new RENAME TO history")
            }
        }
}
