package dev.nevack.unitconverter.model

data class ConversionUnit(
    val name: String,
    val factor: Double,
    val unitSymbol: String,
) {
    companion object {
        val EMPTY = ConversionUnit("", 1.0, "")
    }
}
