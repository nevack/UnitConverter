package dev.nevack.unitconverter.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.usecase.ClearHistoryUseCase
import dev.nevack.unitconverter.history.usecase.GetHistoryUseCase
import dev.nevack.unitconverter.history.usecase.RemoveHistoryItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
    @Inject
    constructor(
        private val getHistoryUseCase: GetHistoryUseCase,
        private val removeHistoryItemUseCase: RemoveHistoryItemUseCase,
        private val clearHistoryUseCase: ClearHistoryUseCase,
    ) : ViewModel() {
        private val filter = MutableStateFlow<String?>(null)
        val items: StateFlow<List<HistoryRecord>> =
            combine(getHistoryUseCase(), filter) { items, categoryId ->
                items.filter { item -> categoryId == null || item.categoryId == categoryId }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

        fun removeItem(item: HistoryRecord) {
            viewModelScope.launch {
                removeHistoryItemUseCase(item)
            }
        }

        fun removeAll() {
            viewModelScope.launch {
                clearHistoryUseCase()
            }
        }

        fun filter(categoryId: String?) {
            filter.value = categoryId
        }
    }
