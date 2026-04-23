package dev.nevack.unitconverter.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.db.HistoryDatabase
import dev.nevack.unitconverter.history.db.toEntity
import dev.nevack.unitconverter.history.db.toRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
    @Inject
    constructor(
        db: HistoryDatabase,
    ) : ViewModel() {
        private val dao = db.dao()

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
            viewModelScope.launch(Dispatchers.IO) {
                dao.delete(item.toEntity())
                itemsRaw.postValue(dao.getAll().map { it.toRecord() })
            }
        }

        fun removeAll() {
            viewModelScope.launch(Dispatchers.IO) {
                dao.deleteAll()
                itemsRaw.postValue(dao.getAll().map { it.toRecord() })
            }
        }

        fun filter(categoryId: String?) {
            filter.value = categoryId
        }

        private fun fetch() {
            viewModelScope.launch(Dispatchers.IO) {
                val items = dao.getAll().map { it.toRecord() }
                itemsRaw.postValue(items)
            }
        }
    }
