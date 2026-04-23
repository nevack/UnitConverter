package dev.nevack.unitconverter.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
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
        private val _items = MutableLiveData<List<HistoryRecord>>(emptyList())
        val items: LiveData<List<HistoryRecord>>
            get() = _items

        private val _messageResId = MutableLiveData<Int?>(null)
        val messageResId: LiveData<Int?>
            get() = _messageResId

        init {
            viewModelScope.launch {
                getHistoryUseCase()
                    .combine(filter) { items, categoryId ->
                        items.filter { item -> categoryId == null || item.categoryId == categoryId }
                    }.catch {
                        _messageResId.value = R.string.failed_to_load_history
                        emit(emptyList())
                    }.collect { _items.value = it }
            }
        }

        fun removeItem(item: HistoryRecord) {
            viewModelScope.launch {
                try {
                    removeHistoryItemUseCase(item)
                } catch (ignored: CancellationException) {
                    throw ignored
                } catch (ignored: Exception) {
                    _messageResId.value = R.string.failed_to_update_history
                }
            }
        }

        fun removeAll() {
            viewModelScope.launch {
                try {
                    clearHistoryUseCase()
                } catch (ignored: CancellationException) {
                    throw ignored
                } catch (ignored: Exception) {
                    _messageResId.value = R.string.failed_to_update_history
                }
            }
        }

        fun filter(categoryId: String?) {
            filter.value = categoryId
        }

        fun clearMessage() {
            _messageResId.value = null
        }
    }
