package dev.nevack.unitconverter.categories

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CategoriesViewModel : ViewModel() {
    private val _converterOpened =
        MutableSharedFlow<String>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    val converterOpened: SharedFlow<String> = _converterOpened.asSharedFlow()

    fun openConverter(categoryId: String) {
        _converterOpened.tryEmit(categoryId)
    }
}
