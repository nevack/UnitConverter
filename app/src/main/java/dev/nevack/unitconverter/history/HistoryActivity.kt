package dev.nevack.unitconverter.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.ActivityHistoryBinding
import dev.nevack.unitconverter.history.db.HistoryDatabase
import javax.inject.Inject

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {
    @Inject
    lateinit var db: HistoryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<HistoryFragment>(R.id.container)
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.toolbarLayout.applyInsetter {
            type(statusBars = true) { padding(top = true) }
            type(navigationBars = true) { margin(horizontal = true) }
        }
    }
}
