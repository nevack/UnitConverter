package dev.nevack.unitconverter.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.nevack.unitconverter.model.ConverterCategory

class CategoriesViewModel : ViewModel() {
    private val _converterOpened = MutableLiveData<Int>()
    val converterOpened: LiveData<Int>
        get() = _converterOpened

    fun openConverter(category: ConverterCategory) {
        this._converterOpened.value = category.index
    }
}
