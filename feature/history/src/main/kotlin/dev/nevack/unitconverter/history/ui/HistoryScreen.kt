@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package dev.nevack.unitconverter.history.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.nevack.unitconverter.feature.history.R
import dev.nevack.unitconverter.history.HistoryCategory
import dev.nevack.unitconverter.history.HistoryRecord

@Composable
internal fun historyRoute(
    viewModel: HistoryViewModel,
    categories: List<HistoryCategory>,
    onShareItem: (HistoryRecord) -> Unit,
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.filter.collectAsStateWithLifecycle()

    historyScreen(
        items = items,
        categories = categories,
        selectedCategoryId = selectedCategoryId,
        onFilterSelected = viewModel::filter,
        onRemoveItem = viewModel::removeItem,
        onRemoveAll = viewModel::removeAll,
        onShareItem = onShareItem,
    )
}

@Composable
private fun historyScreen(
    items: List<HistoryRecord>,
    categories: List<HistoryCategory>,
    selectedCategoryId: String?,
    onFilterSelected: (String?) -> Unit,
    onRemoveItem: (HistoryRecord) -> Unit,
    onRemoveAll: () -> Unit,
    onShareItem: (HistoryRecord) -> Unit,
) {
    var isFilterDialogVisible by rememberSaveable { mutableStateOf(false) }
    val categoriesById = remember(categories) { categories.associateBy { it.id } }

    Scaffold(
        topBar = {
            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                title = { Text(text = stringResource(R.string.title_activity_history)) },
                actions = {
                    IconButton(onClick = { isFilterDialogVisible = true }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = stringResource(R.string.menu_filter_label),
                        )
                    }
                    IconButton(onClick = onRemoveAll) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.menu_delete_label),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
            if (items.isEmpty()) {
                emptyHistory()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(items = items, key = HistoryRecord::id) { item ->
                        historyItem(
                            category = categoriesById[item.categoryId],
                            item = item,
                            onRemove = { onRemoveItem(item) },
                            onShare = { onShareItem(item) },
                        )
                    }
                }
            }
        }
    }

    if (isFilterDialogVisible) {
        historyFilterDialog(
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            onDismiss = { isFilterDialogVisible = false },
            onConfirm = { categoryId ->
                onFilterSelected(categoryId)
                isFilterDialogVisible = false
            },
        )
    }
}

@Composable
private fun emptyHistory() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().padding(24.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.ic_history),
                contentDescription = stringResource(R.string.description_no_history_icon),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_history_items),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun historyItem(
    category: HistoryCategory?,
    item: HistoryRecord,
    onRemove: () -> Unit,
    onShare: () -> Unit,
) {
    val backgroundColor = category?.let { colorResource(it.colorRes) } ?: Color.Transparent
    val contentColor = if (category == null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
    val shadeColor = colorResource(R.color.history_shade_color)

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(shadeColor)
                        .height(48.dp)
                        .padding(start = 16.dp),
            ) {
                Text(
                    text = category?.let { stringResource(it.nameRes) } ?: item.categoryId,
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onShare) {
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = stringResource(R.string.share_button),
                        tint = contentColor,
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.remove_item_button),
                        tint = contentColor,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .size(48.dp)
                            .background(color = shadeColor, shape = CircleShape),
                ) {
                    if (category != null) {
                        Image(
                            painter = painterResource(category.iconRes),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                    historyValueRow(
                        unit = item.unitFrom,
                        value = item.valueFrom,
                        color = contentColor,
                    )
                    historyValueRow(
                        unit = item.unitTo,
                        value = item.valueTo,
                        color = contentColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun historyValueRow(
    unit: String,
    value: String,
    color: Color,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = unit,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = value,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun historyFilterDialog(
    categories: List<HistoryCategory>,
    selectedCategoryId: String?,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit,
) {
    var pendingSelection by rememberSaveable(selectedCategoryId) { mutableStateOf(selectedCategoryId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.menu_filter_label)) },
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                filterOption(
                    label = stringResource(R.string.history_filter_all),
                    selected = pendingSelection == null,
                    onClick = { pendingSelection = null },
                )
                categories.forEach { category ->
                    filterOption(
                        label = stringResource(category.nameRes),
                        selected = pendingSelection == category.id,
                        onClick = { pendingSelection = category.id },
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(pendingSelection) }) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(android.R.string.cancel))
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}

@Composable
private fun filterOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected,
                        onClick = onClick,
                        role = Role.RadioButton,
                    ).padding(vertical = 12.dp),
        ) {
            RadioButton(selected = selected, onClick = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
