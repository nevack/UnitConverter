package dev.nevack.unitconverter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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
        private var loadJob: Job? = null
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

            loadJob?.cancel()

            updateUiState {
                copy(
                    title = category.categoryName,
                    backgroundColor = category.color,
                    categoryId = category.id,
                    converter = null,
                    isLoading = true,
                    currentInput = null,
                    messageResId = null,
                    result = Result.Empty,
                )
            }

            loadJob =
                viewModelScope.launch {
                    try {
                        val converter = loadConverterUseCase(categoryId) ?: return@launch
                        val currentInput = uiState.value?.currentInput
                        val result =
                            currentInput
                                ?.let { convertValueUseCase(converter, it) }
                                ?.let { Result.Converted(it) }
                                ?: Result.Empty
                        updateUiState {
                            if (this.categoryId != categoryId) {
                                this
                            } else {
                                copy(
                                    converter = converter,
                                    isLoading = false,
                                    result = result,
                                )
                            }
                        }
                    } catch (ignored: CancellationException) {
                        throw ignored
                    } catch (ignored: Exception) {
                        updateUiState {
                            if (this.categoryId != categoryId) {
                                this
                            } else {
                                copy(
                                    isLoading = false,
                                    messageResId = R.string.failed_to_load_converter,
                                )
                            }
                        }
                    }
                }
        }

        fun clearMessage() {
            updateUiState { copy(messageResId = null) }
        }

        override fun onCleared() {
            loadJob?.cancel()
            super.onCleared()
        }

        fun convert(data: ConvertData) {
            val currentState = uiState.value ?: ConverterUiState()
            val result = convertValueUseCase(currentState.converter, data)
            updateUiState {
                copy(
                    currentInput = data,
                    result = result?.let { Result.Converted(it) } ?: Result.Empty,
                )
            }
        }

        fun saveResultToHistory(result: ConvertData) {
            val uiState = _uiState.value ?: return
            val converter = uiState.converter ?: return
            val categoryId = uiState.categoryId ?: return
            viewModelScope.launch {
                try {
                    saveResultToHistoryUseCase(converter, categoryId, result)
                } catch (ignored: CancellationException) {
                    throw ignored
                } catch (ignored: Exception) {
                    updateUiState { copy(messageResId = R.string.failed_to_save_history) }
                }
            }
        }

        private inline fun updateUiState(update: ConverterUiState.() -> ConverterUiState) {
            _uiState.value = update(_uiState.value ?: ConverterUiState())
        }
    }
