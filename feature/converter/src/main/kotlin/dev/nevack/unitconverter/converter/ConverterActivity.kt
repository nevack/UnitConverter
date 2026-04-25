package dev.nevack.unitconverter.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.converter.ConverterFragment.Companion.SHOW_NAV_BUTTON_ARG
import dev.nevack.unitconverter.feature.converter.R
import dev.nevack.unitconverter.feature.converter.databinding.ActivityConverterBinding
import dev.nevack.unitconverter.model.AppConverterCategory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val categories = viewModel.categories

        setupNavigation(binding, categories)

        binding.navigationDrawer.addDrawerListener(
            object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(
                    drawerView: View,
                    slideOffset: Float,
                ) = Unit

                override fun onDrawerOpened(drawerView: View) = Unit

                override fun onDrawerStateChanged(newState: Int) = Unit

                override fun onDrawerClosed(drawerView: View) {
                    viewModel.setDrawerOpened(false)
                }
            },
        )

        val callback =
            onBackPressedDispatcher.addCallback(this) {
                viewModel.setDrawerOpened(false)
            }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    callback.isEnabled = state.drawerOpened
                    if (state.drawerOpened) {
                        binding.navigationDrawer.open()
                    } else {
                        binding.navigationDrawer.close()
                    }
                    val categoryId = state.categoryId ?: return@collect
                    val categoryIndex = categories.indexOfFirst { it.id == categoryId }
                    if (categoryIndex != -1) {
                        // findItem uses itemId which we set to the list index, so this is stable.
                        binding.navigationView.menu
                            .findItem(categoryIndex)
                            ?.isChecked = true
                    }
                }
            }
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ConverterFragment>(
                R.id.container,
                args = Bundle().apply { putBoolean(SHOW_NAV_BUTTON_ARG, true) },
            )
        }

        // Only load on a fresh launch; SavedStateHandle restores categoryId across
        // configuration changes and process death automatically.
        if (savedInstanceState == null) {
            val categoryId =
                intent.getStringExtra(CONVERTER_ID_EXTRA)
                    ?: categories.firstOrNull()?.id
                    ?: return
            viewModel.load(categoryId)
        }
    }

    private fun setupNavigation(
        binding: ActivityConverterBinding,
        categories: List<AppConverterCategory>,
    ) = with(binding.navigationView) {
        for ((i, unit) in categories.withIndex()) {
            // Use the list index as a stable itemId so the listener can look up the category
            // directly by ID rather than relying on the display order of menu items.
            menu
                .add(Menu.NONE, i, i, unit.categoryName)
                .setCheckable(true)
                .setIcon(unit.icon)
        }
        setNavigationItemSelectedListener { menuItem ->
            val category = categories.getOrNull(menuItem.itemId) ?: return@setNavigationItemSelectedListener false
            viewModel.load(category.id)
            viewModel.setDrawerOpened(false)
            true
        }
        applyInsetter {
            type(statusBars = true) { margin(top = true) }
            type(navigationBars = true) { margin(bottom = true) }
        }
    }

    companion object {
        private const val CONVERTER_ID_EXTRA = "converter_id"

        fun getIntent(
            context: Context,
            categoryId: String,
        ): Intent =
            Intent(context, ConverterActivity::class.java).apply {
                putExtra(CONVERTER_ID_EXTRA, categoryId)
            }
    }
}
