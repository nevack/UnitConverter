package dev.nevack.unitconverter.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.history.db.HistoryDatabase
import dev.nevack.unitconverter.history.db.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    db: HistoryDatabase
) : ViewModel() {
    private val dao = db.dao()

    private val _items = MutableLiveData<List<HistoryItem>>().also { fetch() }
    private var _filter = MutableLiveData(0)
    private val _filteredItems by lazy {
        MediatorLiveData<List<HistoryItem>>().apply {
            addSource(_filter) {
                value = _items.value?.filter {
                    item ->
                    it == 0 || ((1 shl item.category) and it) != 0
                } ?: emptyList()
            }
            addSource(_items) {
                val filter = _filter.value ?: 0
                value = it.filter { item -> filter == 0 || ((1 shl item.category) and filter) != 0 }
            }
        }
    }
    val items: LiveData<List<HistoryItem>>
        get() = _filteredItems

    fun removeItem(item: HistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(item)
        }
        fetch()
    }

    fun removeAll() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAll()
        }
        fetch()
    }

    fun filter(mask: Int) {
        _filter.value = mask
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = dao.getAll()
            _items.postValue(items)
        }
    }
}
