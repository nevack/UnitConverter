package dev.nevack.unitconverter.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
        private val itemsRaw = MutableLiveData<List<HistoryRecord>>().also { fetch() }
        private var filter = MutableLiveData<String?>(null)
        private val itemsFiltered by lazy {
            MediatorLiveData<List<HistoryRecord>>().apply {
                addSource(filter) { categoryId ->
                    value = itemsRaw.value?.filter { item ->
                        categoryId == null || item.categoryId == categoryId
                    } ?: emptyList()
                }
                addSource(itemsRaw) {
                    val categoryId = filter.value
                    value = it.filter { item -> categoryId == null || item.categoryId == categoryId }
                }
            }
        }
        val items: LiveData<List<HistoryRecord>>
            get() = itemsFiltered

        fun removeItem(item: HistoryRecord) {
            viewModelScope.launch {
                removeHistoryItemUseCase(item)
                refreshItems()
            }
        }

        fun removeAll() {
            viewModelScope.launch {
                clearHistoryUseCase()
                refreshItems()
            }
        }

        fun filter(categoryId: String?) {
            filter.value = categoryId
        }

        private fun fetch() {
            viewModelScope.launch {
                refreshItems()
            }
        }

        private suspend fun refreshItems() {
            itemsRaw.postValue(getHistoryUseCase())
        }
    }
