package org.nevack.unitconverter.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import org.nevack.unitconverter.R
import org.nevack.unitconverter.databinding.ActivityConverterBinding
import org.nevack.unitconverter.model.ConverterCategory

@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConverterBinding
    private var converterId = 0
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        converterId = intent.getIntExtra(CONVERTER_ID_EXTRA, 0)
        window.statusBarColor = 0x3F000000
        window.navigationBarColor = 0x3F000000
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setupDrawerContent()
        viewModel.drawerOpened.observe(this) { opened ->
            if (opened) {
                binding.navigationDrawer.openDrawer(Gravity.START)
            } else {
                binding.navigationDrawer.closeDrawer(Gravity.START)
            }
        }

        supportFragmentManager.commit {
            val fragment = ConverterFragment()
            add(R.id.container, fragment)
            setReorderingAllowed(true)
        }
    }

    private fun setupDrawerContent() = with(binding.navigationView) {
        val units = ConverterCategory.values()
        menu.setGroupCheckable(Menu.NONE, true, true)
        for ((i, unit) in units.withIndex()) {
            menu.add(Menu.NONE, Menu.NONE, i, getString(unit.categoryName)).setIcon(unit.icon)
        }
        menu[converterId].isChecked = true
        binding.navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            converterId = menuItem.order
//            presenter!!.setConverter(units[converterId])
            menuItem.isChecked = true
            binding.navigationDrawer.closeDrawers()
            true
        }
    }

    override fun onBackPressed() {
        if (!viewModel.setDrawerOpened(false)) {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CONVERTER_ID_EXTRA, converterId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        converterId = savedInstanceState.getInt(CONVERTER_ID_EXTRA)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim)
    }

    companion object {
        private const val CONVERTER_ID_EXTRA = "converter_id"

        @JvmStatic
        fun getIntent(context: Context?, converterId: Int): Intent {
            val intent = Intent(context, ConverterActivity::class.java)
            intent.putExtra(CONVERTER_ID_EXTRA, converterId)
            return intent
        }
    }
}