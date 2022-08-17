package dev.nevack.unitconverter.converter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.history.db.HistoryDatabase
import dev.nevack.unitconverter.history.db.HistoryItem
import dev.nevack.unitconverter.model.ConverterCategory
import dev.nevack.unitconverter.model.converter.Converter
import dev.nevack.unitconverter.model.converter.CurrencyConverter
import dev.nevack.unitconverter.model.nbrb.NBRBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val database: HistoryDatabase,
    private val repository: NBRBRepository,
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

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result>
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
            converter.repository = repository
        }

        viewModelScope.launch {
            converter.load()
            _converter.value = converter
        }
    }

    fun convert(data: ConvertData) {
        val converter = converter.value
        if (converter == null) {
            _result.value = Result.Empty
            return
        }
        try {
            val result =
                Result.Converted(
                    converter.convert(
                        data.value,
                        data.from,
                        data.to
                    )
                )
            _result.value = result
        } catch (ex: Exception) {
            _result.value = Result.Empty
        }
    }

    fun saveResultToHistory(result: ConvertData) {
        val converter = converter.value ?: return
        val item = HistoryItem(
            0,
            converter[result.from].name,
            converter[result.to].name,
            result.value,
            result.result,
            _index.value ?: 0
        )
        viewModelScope.launch(Dispatchers.IO) {
            database.dao().insertAll(item)
        }
    }
}
