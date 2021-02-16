package org.nevack.unitconverter.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.loader.app.LoaderManager
import com.google.android.material.navigation.NavigationView
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import org.nevack.unitconverter.NBRBService
import org.nevack.unitconverter.R
import org.nevack.unitconverter.converter.ConverterActivity
import org.nevack.unitconverter.databinding.ActivityConverterBinding
import org.nevack.unitconverter.history.db.HistoryDatabase
import org.nevack.unitconverter.model.EUnitCategory
import javax.inject.Inject

@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConverterBinding
    private var converterId = 0
    private var presenter: ConverterContract.Presenter? = null

    @Inject
    lateinit var database: HistoryDatabase

    @Inject
    lateinit var moshi: Moshi

    @Inject
    lateinit var service: NBRBService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        converterId = intent.getIntExtra(CONVERTER_ID_EXTRA, 0)
        val window = window
        //        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.statusBarColor = 0x3F000000
        window.navigationBarColor = 0x3F000000
        //        window.setStatusBarColor(Color.TRANSPARENT);
        WindowCompat.setDecorFitsSystemWindows(window, false)

        supportFragmentManager.commit {
            val fragment = ConverterFragment()
            add(R.id.container, fragment)
            setReorderingAllowed(true)

            presenter = ConverterPresenter(
                this@ConverterActivity,
                LoaderManager.getInstance(this@ConverterActivity),
                fragment,
                database.dao(),
                moshi,
                service
            )
        }
    }

    private fun setupDrawerContent() = with(binding.navigationView) {
        val units = EUnitCategory.values()
        menu.setGroupCheckable(Menu.NONE, true, true)
        for ((i, unit) in units.withIndex()) {
            menu.add(Menu.NONE, Menu.NONE, i, getString(unit.categoryName))
            menu.getItem(i).setIcon(unit.icon)
        }
        menu.getItem(converterId).isChecked = true
        binding.navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            converterId = menuItem.order
            presenter!!.setConverter(units[converterId])
            menuItem.isChecked = true
            binding.navigationDrawer.closeDrawers()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        presenter!!.setConverter(EUnitCategory.values()[converterId])
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Open drawer when hamburger menu button (☰) pressed in actionbar
            android.R.id.home -> {
                binding.navigationDrawer.openDrawer(GravityCompat.START)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (binding.navigationDrawer.isDrawerVisible(GravityCompat.START)) {
            binding.navigationDrawer.closeDrawers()
        } else {
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