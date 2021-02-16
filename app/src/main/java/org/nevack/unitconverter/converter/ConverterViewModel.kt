package org.nevack.unitconverter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.nevack.unitconverter.NBRBService
import org.nevack.unitconverter.history.db.HistoryDatabase
import org.nevack.unitconverter.model.ConverterCategory
import org.nevack.unitconverter.model.converter.Converter
import org.nevack.unitconverter.model.converter.CurrencyConverter
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

    fun setDrawerOpened(opened: Boolean): Boolean {
        if (_drawerOpened.value == opened)
            return false
        _drawerOpened.value = opened
        return true
    }

    fun load(converter: Converter, category: ConverterCategory) {
        _title.value = category.categoryName
        _color.value = category.color

        if (converter is CurrencyConverter) {
            converter.moshi = moshi
            converter.service = service
        }

        viewModelScope.launch {
            converter.load()
        }
    }
}