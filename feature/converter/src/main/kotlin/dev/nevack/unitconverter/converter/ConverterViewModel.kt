package dev.nevack.unitconverter.converter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.categories.GetCategoriesUseCase
import dev.nevack.unitconverter.model.AppConverterCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel
    @Inject
    constructor(
        private val savedState: SavedStateHandle,
        getCategoriesUseCase: GetCategoriesUseCase,
        private val getConverterCategoryUseCase: GetConverterCategoryUseCase,
        private val loadConverterUseCase: LoadConverterUseCase,
        private val convertValueUseCase: ConvertValueUseCase,
        private val saveResultToHistoryUseCase: SaveResultToHistoryUseCase,
    ) : ViewModel() {
        val categories: List<AppConverterCategory> = getCategoriesUseCase()
        private val _uiState = MutableStateFlow(ConverterUiState())
        val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()
        private var loadJob: Job? = null

        init {
            savedState.get<String>(KEY_CATEGORY_ID)?.let { load(it) }
        }

        fun setDrawerOpened(opened: Boolean): Boolean {
            val changed = _uiState.value.drawerOpened != opened
            updateUiState { copy(drawerOpened = opened) }
            return changed
        }

        fun load(categoryId: String) {
            val category = getConverterCategoryUseCase(categoryId) ?: return
            savedState[KEY_CATEGORY_ID] = categoryId

            updateUiState {
                copy(
                    title = category.categoryName,
                    backgroundColor = category.color,
                    categoryId = category.id,
                    converter = null,
                    convertData = ConvertData("", "", 0, 1),
                    result = Result.Empty,
                )
            }

            loadJob?.cancel()
            loadJob =
                viewModelScope.launch {
                    val converter = loadConverterUseCase(categoryId) ?: return@launch
                    updateUiState {
                        if (this.categoryId != categoryId) {
                            this
                        } else {
                            copy(converter = converter)
                        }
                    }
                }
        }

        fun convert(data: ConvertData) {
            val resultString = convertValueUseCase(uiState.value.converter, data)
            updateUiState {
                copy(
                    convertData = data.copy(result = resultString ?: ""),
                    result = resultString?.let { Result.Converted(it) } ?: Result.Empty,
                )
            }
        }

        fun saveResultToHistory() {
            val uiState = _uiState.value
            val converter = uiState.converter ?: return
            val categoryId = uiState.categoryId ?: return
            viewModelScope.launch {
                saveResultToHistoryUseCase(converter, categoryId, uiState.convertData)
            }
        }

        private inline fun updateUiState(update: ConverterUiState.() -> ConverterUiState) {
            _uiState.update(update)
        }

        companion object {
            private const val KEY_CATEGORY_ID = "category_id"
        }
    }
