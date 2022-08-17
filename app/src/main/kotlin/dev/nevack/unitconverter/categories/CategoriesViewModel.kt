package dev.nevack.unitconverter.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.nevack.unitconverter.model.ConverterCategory

class CategoriesViewModel : ViewModel() {
    private val _opened = MutableLiveData<Int>()
    val converterOpened: LiveData<Int>
        get() = _opened

    fun openConverter(category: ConverterCategory) {
        _opened.value = category.index
    }
}
