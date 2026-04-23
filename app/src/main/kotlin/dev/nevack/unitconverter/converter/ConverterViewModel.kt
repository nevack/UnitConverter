package dev.nevack.unitconverter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.db.HistoryDatabase
import dev.nevack.unitconverter.history.db.toEntity
import dev.nevack.unitconverter.model.AppConverterCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel
    @Inject
    constructor(
        private val database: HistoryDatabase,
        private val catalog: AppConverterCatalog,
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
            val category = catalog.getCategory(categoryId) ?: return
            val converter = catalog.createConverter(categoryId)

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
                converter.load()
                updateUiState { copy(converter = converter) }
            }
        }

        fun convert(data: ConvertData) {
            val converter = uiState.value?.converter
            if (converter == null) {
                updateUiState { copy(result = Result.Empty) }
                return
            }
            try {
                val result =
                    Result.Converted(
                        converter.convert(
                            data.value,
                            data.from,
                            data.to,
                        ),
                    )
                updateUiState { copy(result = result) }
            } catch (ex: Exception) {
                updateUiState { copy(result = Result.Empty) }
            }
        }

        fun saveResultToHistory(result: ConvertData) {
            val uiState = _uiState.value ?: return
            val converter = uiState.converter ?: return
            val categoryId = uiState.categoryId ?: return
            val item =
                HistoryRecord(
                    unitFrom = converter[result.from].name,
                    unitTo = converter[result.to].name,
                    valueFrom = result.value,
                    valueTo = result.result,
                    categoryId = categoryId,
                )
            viewModelScope.launch(Dispatchers.IO) {
                database.dao().insertAll(item.toEntity())
            }
        }

        private inline fun updateUiState(update: ConverterUiState.() -> ConverterUiState) {
            _uiState.value = update(_uiState.value ?: ConverterUiState())
        }
    }
