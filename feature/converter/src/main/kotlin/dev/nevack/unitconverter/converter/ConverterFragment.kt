package dev.nevack.unitconverter.converter

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.nevack.unitconverter.feature.converter.R
import dev.nevack.unitconverter.model.ConversionUnit
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConverterFragment : Fragment() {
    @Inject
    lateinit var historyOpener: ConverterHistoryOpener

    private val viewModel: ConverterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    converterRoute(
                        viewModel = viewModel,
                        showNavigationButton = arguments?.getBoolean(SHOW_NAV_BUTTON_ARG) == true,
                        onOpenHistory = { historyOpener.open(requireContext()) },
                    )
                }
            }
        }

    companion object {
        const val SHOW_NAV_BUTTON_ARG = "showNavButton"
    }
}

@Composable
private fun converterRoute(
    viewModel: ConverterViewModel,
    showNavigationButton: Boolean,
    onOpenHistory: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val copiedMessage = stringResource(R.string.copy_result_toast)

    LaunchedEffect(state.loadError) {
        val loadError = state.loadError ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(loadError)
        viewModel.clearLoadError()
    }

    converterScreen(
        state = state,
        showNavigationButton = showNavigationButton,
        snackbarHostState = snackbarHostState,
        onOpenDrawer = { viewModel.setDrawerOpened(true) },
        onSaveResult = viewModel::saveResultToHistory,
        onOpenHistory = onOpenHistory,
        onConvert = viewModel::convert,
        onCopy = { withUnitSymbols ->
            context.getSystemService<ClipboardManager>()?.run {
                val units = state.converter?.units.orEmpty()
                val symbol = units.getOrNull(state.convertData.from)?.unitSymbol.orEmpty()
                val result =
                    state.convertData.result + if (withUnitSymbols) Html.fromHtml(symbol, Html.FROM_HTML_MODE_LEGACY).toString() else ""
                setPrimaryClip(ClipData.newPlainText("Conversion result", result))
            }
        },
        onCopied = { snackbarHostState.showSnackbar(copiedMessage) },
        onPaste = {
            context.getSystemService<ClipboardManager>()?.primaryClip?.run {
                if (description.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
                    viewModel.convert(state.convertData.copy(value = state.convertData.value + getItemAt(0).text))
                }
            }
        },
    )
}

@Composable
private fun converterScreen(
    state: ConverterUiState,
    showNavigationButton: Boolean,
    snackbarHostState: SnackbarHostState,
    onOpenDrawer: () -> Unit,
    onSaveResult: () -> Unit,
    onOpenHistory: () -> Unit,
    onConvert: (ConvertData) -> Unit,
    onCopy: (Boolean) -> Unit,
    onCopied: suspend () -> Unit,
    onPaste: () -> Unit,
) {
    val backgroundColor = state.backgroundColor?.let { colorResource(it) } ?: MaterialTheme.colorScheme.primary
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = backgroundColor,
    ) { padding ->
        if (isLandscape) {
            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
            ) {
                converterDisplayPane(
                    state = state,
                    showNavigationButton = showNavigationButton,
                    onOpenDrawer = onOpenDrawer,
                    onSaveResult = onSaveResult,
                    onOpenHistory = onOpenHistory,
                    onConvert = onConvert,
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .statusBarsPadding(),
                    isLandscape = true,
                )
                converterKeypad(
                    enabled = !state.isLoading,
                    onDigit = { onConvert(state.convertData.copy(value = state.convertData.value + it)) },
                    onBackspace = { clear ->
                        val value = if (clear) "" else state.convertData.value.dropLast(1)
                        onConvert(state.convertData.copy(value = value))
                    },
                    onCopy = onCopy,
                    onCopied = onCopied,
                    onPaste = onPaste,
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .systemBarsPadding(),
                )
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
            ) {
                converterDisplayPane(
                    state = state,
                    showNavigationButton = showNavigationButton,
                    onOpenDrawer = onOpenDrawer,
                    onSaveResult = onSaveResult,
                    onOpenHistory = onOpenHistory,
                    onConvert = onConvert,
                    modifier = Modifier.fillMaxWidth(),
                    isLandscape = false,
                )
                converterKeypad(
                    enabled = !state.isLoading,
                    onDigit = { onConvert(state.convertData.copy(value = state.convertData.value + it)) },
                    onBackspace = { clear ->
                        val value = if (clear) "" else state.convertData.value.dropLast(1)
                        onConvert(state.convertData.copy(value = value))
                    },
                    onCopy = onCopy,
                    onCopied = onCopied,
                    onPaste = onPaste,
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                )
            }
        }
    }
}

@Composable
private fun converterDisplayPane(
    state: ConverterUiState,
    showNavigationButton: Boolean,
    onOpenDrawer: () -> Unit,
    onSaveResult: () -> Unit,
    onOpenHistory: () -> Unit,
    onConvert: (ConvertData) -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean,
) {
    Column(modifier = modifier) {
        converterTopBar(
            title = state.title,
            showNavigationButton = showNavigationButton,
            onOpenDrawer = onOpenDrawer,
            onSaveResult = onSaveResult,
            onOpenHistory = onOpenHistory,
        )
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        val units = state.converter?.units.orEmpty()
        converterDisplay(
            data = state.convertData,
            units = units,
            onConvert = onConvert,
            modifier = if (isLandscape) Modifier.weight(1f) else Modifier.fillMaxWidth(),
            isLandscape = isLandscape,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun converterTopBar(
    title: Int?,
    showNavigationButton: Boolean,
    onOpenDrawer: () -> Unit,
    onSaveResult: () -> Unit,
    onOpenHistory: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = { title?.let { Text(text = stringResource(it)) } },
        navigationIcon = {
            if (showNavigationButton) {
                IconButton(onClick = onOpenDrawer) {
                    Icon(
                        painter = painterResource(R.drawable.ic_menu),
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onSaveResult) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = stringResource(R.string.menu_save_label),
                )
            }
            IconButton(onClick = onOpenHistory) {
                Icon(
                    painter = painterResource(R.drawable.ic_history),
                    contentDescription = stringResource(R.string.menu_history_label),
                )
            }
        },
    )
}

@Composable
private fun converterDisplay(
    data: ConvertData,
    units: List<ConversionUnit>,
    onConvert: (ConvertData) -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean,
) {
    val sourceUnit = units.getOrNull(data.from) ?: ConversionUnit.EMPTY
    val resultUnit = units.getOrNull(data.to) ?: ConversionUnit.EMPTY

    Column(
        modifier = modifier.padding(bottom = if (isLandscape) 56.dp else 0.dp),
        verticalArrangement = if (isLandscape) Arrangement.Center else Arrangement.Top,
    ) {
        valueText(
            value = data.value,
            suffix = sourceUnit.unitSymbol,
            modifier = if (isLandscape) Modifier.weight(1f) else Modifier.fillMaxWidth(),
            alignBottom = isLandscape,
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FloatingActionButton(
                onClick = { onConvert(data.swap()) },
                modifier = Modifier.size(48.dp),
                containerColor = MaterialTheme.colorScheme.secondary,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_swap_vert),
                    contentDescription = stringResource(R.string.swap_source_and_result_units_and_values),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                unitDropdown(
                    units = units,
                    selectedIndex = data.from,
                    onSelected = { onConvert(data.copy(from = it)) },
                )
                unitDropdown(
                    units = units,
                    selectedIndex = data.to,
                    onSelected = { onConvert(data.copy(to = it)) },
                )
            }
        }
        valueText(
            value = data.result,
            suffix = resultUnit.unitSymbol,
            modifier = if (isLandscape) Modifier.weight(1f) else Modifier.fillMaxWidth(),
            alignBottom = false,
        )
    }
}

@Composable
private fun valueText(
    value: String,
    suffix: String,
    modifier: Modifier,
    alignBottom: Boolean,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = if (alignBottom) Alignment.BottomEnd else Alignment.TopEnd,
    ) {
        Text(
            text = buildValueText(value.ifEmpty { stringResource(R.string.zero_hint) }, suffix),
            color = MaterialTheme.colorScheme.surface,
            fontSize = 48.sp,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun unitDropdown(
    units: List<ConversionUnit>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = units.getOrNull(selectedIndex)?.name.orEmpty()

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = units.isNotEmpty(),
        ) {
            Text(
                text = selected,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.surface,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            units.forEachIndexed { index, unit ->
                DropdownMenuItem(
                    text = { Text(unit.name) },
                    onClick = {
                        expanded = false
                        onSelected(index)
                    },
                )
            }
        }
    }
}

@Composable
private fun converterKeypad(
    enabled: Boolean,
    onDigit: (String) -> Unit,
    onBackspace: (Boolean) -> Unit,
    onCopy: (Boolean) -> Unit,
    onCopied: suspend () -> Unit,
    onPaste: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier =
            modifier
                .background(colorResource(R.color.keypad_background_color))
                .padding(end = 2.dp),
    ) {
        keypadNumberColumn(listOf("1", "4", "7", "."), enabled, onDigit, Modifier.weight(3f))
        keypadNumberColumn(listOf("2", "5", "8", "0"), enabled, onDigit, Modifier.weight(3f))
        keypadNumberColumn(listOf("3", "6", "9", "-"), enabled, onDigit, Modifier.weight(3f))
        Column(
            modifier =
                Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(colorResource(R.color.keypad_background_color)),
        ) {
            Box(
                modifier =
                    Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 24.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(colorResource(R.color.colorSecondary))
                            .combinedClickable(
                                enabled = enabled,
                                onClick = { onBackspace(false) },
                                onLongClick = { onBackspace(true) },
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "C", fontSize = 48.sp)
                }
            }
            keypadIconButton(
                icon = R.drawable.ic_content_copy,
                contentDescription = stringResource(R.string.description_copy_button),
                enabled = enabled,
                onClick = {
                    onCopy(false)
                    coroutineScope.launch { onCopied() }
                },
                onLongClick = {
                    onCopy(true)
                    coroutineScope.launch { onCopied() }
                },
                modifier = Modifier.weight(1f),
            )
            keypadIconButton(
                icon = R.drawable.ic_content_paste,
                contentDescription = stringResource(R.string.description_paste_button),
                enabled = enabled,
                onClick = onPaste,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun keypadNumberColumn(
    values: List<String>,
    enabled: Boolean,
    onDigit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxHeight(),
    ) {
        values.forEach { value ->
            TextButton(
                onClick = { onDigit(value) },
                enabled = enabled,
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
            ) {
                Text(text = value, color = MaterialTheme.colorScheme.onPrimary, fontSize = 48.sp)
            }
        }
    }
}

@Composable
private fun keypadIconButton(
    icon: Int,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .combinedClickable(
                    enabled = enabled,
                    onClick = onClick,
                    onLongClick = onLongClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
        )
    }
}

private fun buildValueText(
    value: String,
    suffix: String,
): AnnotatedString =
    buildAnnotatedString {
        append(value)
        if (suffix.isNotEmpty()) {
            append(" ")
            appendHtmlUnitSymbol(suffix)
        }
    }

private fun AnnotatedString.Builder.appendHtmlUnitSymbol(symbol: String) {
    val match = Regex("(.*)<sup><small>(.*)</small></sup>").matchEntire(symbol)
    if (match == null) {
        append(Html.fromHtml(symbol, Html.FROM_HTML_MODE_LEGACY).toString())
        return
    }

    append(match.groupValues[1])
    withStyle(
        SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = 24.sp,
        ),
    ) {
        append(match.groupValues[2])
    }
}
