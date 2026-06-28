package dev.nevack.unitconverter.history.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.nevack.unitconverter.history.HistoryCategoriesProvider
import dev.nevack.unitconverter.history.HistoryRecord
import javax.inject.Inject

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {
    @Inject
    lateinit var historyCategoriesProvider: HistoryCategoriesProvider

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val categories = historyCategoriesProvider()
        setContent {
            historyTheme {
                historyRoute(
                    viewModel = viewModel,
                    categories = categories,
                    onShareItem = ::shareItem,
                )
            }
        }
    }

    private fun shareItem(item: HistoryRecord) {
        val message = "${item.valueFrom} ${item.unitFrom} = ${item.valueTo} ${item.unitTo}"
        val sendIntent =
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
        startActivity(Intent.createChooser(sendIntent, message))
    }
}
