package org.nevack.unitconverter.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.nevack.unitconverter.history.db.HistoryDatabase
import org.nevack.unitconverter.history.db.HistoryItem
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    db: HistoryDatabase
) : ViewModel() {
    private val dao = db.dao()

    private val _items = MutableLiveData<List<HistoryItem>>().also { fetch() }
    private var _filter = MutableLiveData<Int?>()
    private val _filteredItems by lazy {
        _items.map {
            val filter = _filter.value
            if (filter == null) {
                it
            } else {
                it.filter { item -> item.category == filter }
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
