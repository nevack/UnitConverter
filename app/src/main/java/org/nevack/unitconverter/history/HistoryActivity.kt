package org.nevack.unitconverter.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import org.nevack.unitconverter.R
import org.nevack.unitconverter.databinding.ActivityHistoryBinding
import org.nevack.unitconverter.history.db.HistoryDatabase
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
            add<HistoryFragment>(R.id.container)
            setReorderingAllowed(true)
        }
    }
}
