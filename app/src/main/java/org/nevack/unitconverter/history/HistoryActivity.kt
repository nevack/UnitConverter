package org.nevack.unitconverter.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        var fragment = supportFragmentManager.findFragmentById(R.id.container) as HistoryFragment?
        if (fragment == null) {
            fragment = HistoryFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.container, fragment)
            transaction.commit()
        }
        HistoryPresenter(this, fragment, db.dao())
    }
}