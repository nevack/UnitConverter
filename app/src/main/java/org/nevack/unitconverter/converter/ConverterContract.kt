package org.nevack.unitconverter.converter

interface ConverterContract {
    interface View {
        fun getConvertData(): ConvertData
        fun setConvertData(data: ConvertData)
        fun appendText(digit: String)
        fun clear()
        fun showError()
    }

    data class ConvertData internal constructor(
        val value: String,
        val result: String,
        val from: Int,
        val to: Int
    )
}
