package org.nevack.unitconverter.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import org.nevack.unitconverter.R
import org.nevack.unitconverter.databinding.ActivityCategoriesBinding

class CategoriesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportFragmentManager.commit {
            add<CategoriesFragment>(R.id.container)
            setReorderingAllowed(true)
        }
    }
}
