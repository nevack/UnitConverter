package dev.nevack.unitconverter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nevack.unitconverter.history.db.HistoryDatabase
import dev.nevack.unitconverter.history.db.HistoryItem
import dev.nevack.unitconverter.model.AppConverterCatalog
import dev.nevack.unitconverter.model.converter.Converter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel
    @Inject
    constructor(
        private val database: HistoryDatabase,
        private val catalog: AppConverterCatalog,
    ) : ViewModel() {
        private val _drawerOpened = MutableLiveData(false)
        val drawerOpened: LiveData<Boolean>
            get() = _drawerOpened

        private val _backgroundColor = MutableLiveData<Int>()
        val backgroundColor: LiveData<Int>
            get() = _backgroundColor

        private val _title = MutableLiveData<Int>()
        val title: LiveData<Int>
            get() = _title

        private val _categoryId = MutableLiveData<String>()
        val categoryId: LiveData<String>
            get() = _categoryId

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

        fun load(categoryId: String) {
            val category = catalog.getCategory(categoryId) ?: return
            _title.value = category.categoryName
            _backgroundColor.value = category.color

            _categoryId.value = category.id

            val converter = catalog.createConverter(categoryId)

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
                            data.to,
                        ),
                    )
                _result.value = result
            } catch (ex: Exception) {
                _result.value = Result.Empty
            }
        }

        fun saveResultToHistory(result: ConvertData) {
            val converter = converter.value ?: return
            val categoryId = _categoryId.value ?: return
            val item =
                HistoryItem(
                    0,
                    converter[result.from].name,
                    converter[result.to].name,
                    result.value,
                    result.result,
                    categoryId,
                )
            viewModelScope.launch(Dispatchers.IO) {
                database.dao().insertAll(item)
            }
        }
    }
