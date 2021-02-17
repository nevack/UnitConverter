package org.nevack.unitconverter.converter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.nevack.unitconverter.NBRBService
import org.nevack.unitconverter.history.db.HistoryDatabase
import org.nevack.unitconverter.history.db.HistoryItem
import org.nevack.unitconverter.model.ConverterCategory
import org.nevack.unitconverter.model.converter.Converter
import org.nevack.unitconverter.model.converter.CurrencyConverter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private var database: HistoryDatabase,
    private var moshi: Moshi,
    private var service: NBRBService,
) : ViewModel() {

    private val _drawerOpened = MutableLiveData(false)
    val drawerOpened: LiveData<Boolean>
        get() = _drawerOpened

    private val _color = MutableLiveData<Int>()
    val backgroundColor: LiveData<Int>
        get() = _color

    private val _title = MutableLiveData<Int>()
    val title: LiveData<Int>
        get() = _title

    private val _index = MutableLiveData<Int>()
    val category: LiveData<Int>
        get() = _index

    private val _converter = MutableLiveData<Converter>()
    val converter: LiveData<Converter>
        get() = _converter

    private val _result= MutableLiveData<String>()
    val result: LiveData<String>
        get() = _result

    fun setDrawerOpened(opened: Boolean): Boolean {
        val changed = _drawerOpened.value != opened
        _drawerOpened.value = opened
        return changed
    }

    fun load(category: ConverterCategory, context: Context) {
        _title.value = category.categoryName
        _color.value = category.color

        _index.value = category.index

        val converter = category.getConverter(context)

        if (converter is CurrencyConverter) {
            converter.moshi = moshi
            converter.service = service
        }

        viewModelScope.launch {
            converter.load()
            _converter.value = converter
        }
    }

    fun convert(data: ConverterContract.ConvertData) {
        try {
            _result.value = converter.value?.convert(data.value, data.from, data.to)
        } catch (ex: ArithmeticException) {
//            view.showError()
        }
    }

    fun saveResultToHistory() {
        converter.value ?: return
        val item = HistoryItem(
            0,
            "from",
            "to",
            "source",
            _result.value ?: "result",
            0
        )
        viewModelScope.launch(Dispatchers.IO) {
            database.dao().insertAll(item)
        }
    }
}