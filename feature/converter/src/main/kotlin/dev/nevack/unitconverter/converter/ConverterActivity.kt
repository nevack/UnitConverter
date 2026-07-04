package dev.nevack.unitconverter.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.nevack.unitconverter.design.unitConverterTheme
import dev.nevack.unitconverter.model.AppConverterCategory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {
    private val viewModel: ConverterViewModel by viewModels()

    @Inject
    lateinit var historyOpener: ConverterHistoryOpener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val categories = viewModel.categories

        val callback =
            onBackPressedDispatcher.addCallback(this) {
                viewModel.setDrawerOpened(false)
            }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    callback.isEnabled = state.drawerOpened
                }
            }
        }

        setContent {
            unitConverterTheme {
                converterWithDrawer(
                    viewModel = viewModel,
                    categories = categories,
                    onOpenHistory = { historyOpener.open(this@ConverterActivity) },
                )
            }
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

@Composable
private fun converterWithDrawer(
    viewModel: ConverterViewModel,
    categories: List<AppConverterCategory>,
    onOpenHistory: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.drawerOpened) {
        if (state.drawerOpened) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            viewModel.setDrawerOpened(false)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars).windowInsetsPadding(WindowInsets.navigationBars),
            ) {
                categories.forEach { category ->
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(category.categoryName)) },
                        selected = category.id == state.categoryId,
                        icon = {
                            Icon(
                                painter = painterResource(category.icon),
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            viewModel.load(category.id)
                            viewModel.setDrawerOpened(false)
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
        },
    ) {
        converterRoute(
            viewModel = viewModel,
            showNavigationButton = true,
            onOpenHistory = onOpenHistory,
        )
    }
}
