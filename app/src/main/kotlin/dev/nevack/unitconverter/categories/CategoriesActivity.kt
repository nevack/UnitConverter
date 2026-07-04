package dev.nevack.unitconverter.categories

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.converter.ConverterActivity
import dev.nevack.unitconverter.converter.ConverterHistoryOpener
import dev.nevack.unitconverter.converter.ConverterViewModel
import dev.nevack.unitconverter.converter.converterRoute
import dev.nevack.unitconverter.design.UnitConverterColors
import dev.nevack.unitconverter.design.unitConverterTheme
import dev.nevack.unitconverter.model.AppConverterCategory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesActivity : AppCompatActivity() {
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val converterViewModel: ConverterViewModel by viewModels()

    @Inject
    lateinit var historyOpener: ConverterHistoryOpener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val isTablet = resources.getDimensionPixelSize(R.dimen.categories_column_width) > 0
        setContentView(
            ComposeView(this).apply {
                setContent {
                    unitConverterTheme {
                        categoriesMainScreen(
                            categories = categoriesViewModel.categories,
                            isTablet = isTablet,
                            converterViewModel = converterViewModel,
                            onCategoryClick = categoriesViewModel::openConverter,
                            onOpenHistory = { historyOpener.open(this@CategoriesActivity) },
                        )
                    }
                }
            },
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoriesViewModel.converterOpened.collect {
                    if (isTablet) {
                        converterViewModel.load(it)
                    } else {
                        startActivity(ConverterActivity.getIntent(this@CategoriesActivity, it))
                    }
                }
            }
        }

        if (isTablet) {
            categoriesViewModel.categories.firstOrNull()?.let { category ->
                converterViewModel.load(category.id)
            }
        }
    }
}

@Composable
private fun categoriesMainScreen(
    categories: List<AppConverterCategory>,
    isTablet: Boolean,
    converterViewModel: ConverterViewModel,
    onCategoryClick: (String) -> Unit,
    onOpenHistory: () -> Unit,
) {
    if (isTablet) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier
                        .width(dimensionResource(R.dimen.categories_column_width))
                        .fillMaxHeight(),
            ) {
                categoriesTopBar()
                categoriesGrid(
                    categories = categories,
                    onCategoryClick = onCategoryClick,
                    modifier = Modifier.weight(1f),
                )
            }
            converterRoute(
                viewModel = converterViewModel,
                showNavigationButton = false,
                onOpenHistory = onOpenHistory,
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
            )
        }
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { categoriesTopBar() },
        ) { padding ->
            categoriesGrid(
                categories = categories,
                onCategoryClick = onCategoryClick,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun categoriesTopBar() {
    TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
}

@Composable
private fun categoriesGrid(
    categories: List<AppConverterCategory>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(integerResource(R.integer.column_count)),
        modifier = modifier.navigationBarsPadding(),
        contentPadding = PaddingValues(top = 2.dp),
    ) {
        items(categories, key = { it.id }) { category ->
            categoryItem(
                category = category,
                onClick = { onCategoryClick(category.id) },
            )
        }
    }
}

@Composable
private fun categoryItem(
    category: AppConverterCategory,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .padding(2.dp)
                .aspectRatio(1f)
                .background(Color(category.color))
                .clickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(category.icon),
            contentDescription = stringResource(R.string.description_icon),
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .offset(y = (-12).dp)
                    .size(80.dp),
        )

        Text(
            text = stringResource(category.categoryName),
            color = UnitConverterColors.OnCategory,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(UnitConverterColors.ShadeOverlay)
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
        )
    }
}
