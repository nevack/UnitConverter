package dev.nevack.unitconverter.history.di

import android.content.Context
import android.content.Intent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nevack.unitconverter.history.HistoryLauncher
import dev.nevack.unitconverter.history.ui.HistoryActivity

@Module
@InstallIn(SingletonComponent::class)
object HistoryLauncherModule {
    @Provides
    fun provideHistoryLauncher(): HistoryLauncher =
        HistoryLauncher { context: Context ->
            context.startActivity(Intent(context, HistoryActivity::class.java))
        }
}
