package dev.nevack.unitconverter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel
    @Inject
    constructor(
        private val getConverterCategoryUseCase: GetConverterCategoryUseCase,
        private val loadConverterUseCase: LoadConverterUseCase,
        private val convertValueUseCase: ConvertValueUseCase,
        private val saveResultToHistoryUseCase: SaveResultToHistoryUseCase,
    ) : ViewModel() {
        private val _uiState = MutableLiveData(ConverterUiState())
        val uiState: LiveData<ConverterUiState>
            get() = _uiState

        fun setDrawerOpened(opened: Boolean): Boolean {
            val changed = _uiState.value?.drawerOpened != opened
            updateUiState { copy(drawerOpened = opened) }
            return changed
        }

        fun load(categoryId: String) {
            val category = getConverterCategoryUseCase(categoryId) ?: return

            updateUiState {
                copy(
                    title = category.categoryName,
                    backgroundColor = category.color,
                    categoryId = category.id,
                    converter = null,
                    result = Result.Empty,
                )
            }

            viewModelScope.launch {
                val converter = loadConverterUseCase(categoryId) ?: return@launch
                updateUiState { copy(converter = converter) }
            }
        }

        fun convert(data: ConvertData) {
            val result = convertValueUseCase(uiState.value?.converter, data)
            updateUiState {
                copy(
                    result = result?.let { Result.Converted(it) } ?: Result.Empty,
                )
            }
        }

        fun saveResultToHistory(result: ConvertData) {
            val uiState = _uiState.value ?: return
            val converter = uiState.converter ?: return
            val categoryId = uiState.categoryId ?: return
            viewModelScope.launch {
                saveResultToHistoryUseCase(converter, categoryId, result)
            }
        }

        private inline fun updateUiState(update: ConverterUiState.() -> ConverterUiState) {
            _uiState.value = update(_uiState.value ?: ConverterUiState())
        }
    }
