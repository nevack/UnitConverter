package dev.nevack.unitconverter.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.AppConverterCategory

@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private val viewModel: CategoriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    categoriesScreen(
                        categories = viewModel.categories,
                        onCategoryClick = viewModel::openConverter,
                    )
                }
            }
        }
}

@Composable
private fun categoriesScreen(
    categories: List<AppConverterCategory>,
    onCategoryClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(integerResource(R.integer.column_count)),
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
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
                .background(colorResource(category.color))
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
            color = MaterialTheme.colorScheme.surface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(colorResource(R.color.shade_color))
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
        )
    }
}
