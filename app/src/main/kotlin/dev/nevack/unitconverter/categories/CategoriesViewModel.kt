package dev.nevack.unitconverter.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoriesViewModel : ViewModel() {
    private val _converterOpened = MutableLiveData<String>()
    val converterOpened: LiveData<String>
        get() = _converterOpened

    fun openConverter(categoryId: String) {
        _converterOpened.value = categoryId
    }
}
