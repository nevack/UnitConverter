package dev.nevack.unitconverter.model.converter

import android.content.Context
import androidx.annotation.StringRes
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

internal fun Context.massUnits(): List<ConversionUnit> =
    localize(
        MassConverter.DEFINITIONS,
        listOf(
            text(R.string.kilogram, R.string.kilogramsymbol),
            text(R.string.gram, R.string.gramsymbol),
            text(R.string.milligram, R.string.milligramsymbol),
            text(R.string.hundredweight, R.string.hundredweightsymbol),
            text(R.string.tonne, R.string.tonnesymbol),
        ),
    )

internal fun Context.volumeUnits(): List<ConversionUnit> =
    localize(
        VolumeConverter.DEFINITIONS,
        listOf(
            text(R.string.litre, R.string.litresymbol),
            text(R.string.cubicmetre) { getString(R.string.metresymbol) + Converter.CUBIC_POSTFIX },
            text(R.string.millilitre, R.string.millilitresymbol),
            text(R.string.gallon, R.string.gallonsymbol),
            text(R.string.barrel, R.string.barrelsymbol),
        ),
    )

internal fun Context.areaUnits(): List<ConversionUnit> =
    localize(
        AreaConverter.DEFINITIONS,
        listOf(
            text(R.string.squaremetre) { getString(R.string.metresymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.squarecentimetre) { getString(R.string.centimetresymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.squaremillimetre) { getString(R.string.millimetresymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.squarekilometre) { getString(R.string.kilometresymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.squarefoot) { getString(R.string.footsymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.squareyard) { getString(R.string.yardsymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.squaremile) { getString(R.string.milesymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.squareinch) { getString(R.string.inchsymbol) + Converter.SQUARE_POSTFIX },
            text(R.string.hectare, R.string.hectaresymbol),
            text(R.string.are, R.string.aresymbol),
            text(R.string.acre, R.string.acresymbol),
        ),
    )

internal fun Context.memoryUnits(): List<ConversionUnit> =
    localize(
        MemoryConverter.DEFINITIONS,
        listOf(
            text(R.string.bit, R.string.bit_symbol),
            text(R.string.memorybyte, R.string.memorybytesymbol),
            text(R.string.kilobyte, R.string.kilobytesymbol),
            text(R.string.megabyte, R.string.megabytesymbol),
            text(R.string.gigabyte, R.string.gigabytesymbol),
            text(R.string.terabyte, R.string.terabytesymbol),
            text(R.string.petabyte, R.string.petabytesymbol),
        ),
    )

internal fun Context.speedUnits(): List<ConversionUnit> =
    localize(
        SpeedConverter.DEFINITIONS,
        listOf(
            text(R.string.metrespersecond, R.string.mpssymbol),
            text(R.string.kilometresperhour, R.string.kmpssymbol),
            text(R.string.milesperhour, R.string.miphsymbol),
            text(R.string.footspersecond, R.string.ftpssymbol),
            text(R.string.knots, R.string.knotssymbol),
        ),
    )

internal fun Context.timeUnits(): List<ConversionUnit> =
    localize(
        TimeConverter.DEFINITIONS,
        listOf(
            text(R.string.microsecond, R.string.microsecondsymbol),
            text(R.string.millisecond, R.string.millisecondsymbol),
            text(R.string.second, R.string.secondsymbol),
            text(R.string.minute, R.string.minutesymbol),
            text(R.string.hour, R.string.hoursymbol),
            text(R.string.day, R.string.daysymbol),
            text(R.string.week, R.string.weeksymbol),
            text(R.string.year, R.string.yearsymbol),
        ),
    )

internal fun Context.lengthUnits(): List<ConversionUnit> =
    localize(
        LengthConverter.DEFINITIONS,
        listOf(
            text(R.string.metre, R.string.metresymbol),
            text(R.string.millimetre, R.string.millimetresymbol),
            text(R.string.centimetre, R.string.centimetresymbol),
            text(R.string.decimetre, R.string.decimetresymbol),
            text(R.string.kilometre, R.string.kilometresymbol),
            text(R.string.inch, R.string.inchsymbol),
            text(R.string.mile, R.string.milesymbol),
            text(R.string.foot, R.string.footsymbol),
            text(R.string.yard, R.string.yardsymbol),
            text(R.string.astronomicalunit, R.string.astronomicalunitsymbol),
            text(R.string.parsec, R.string.parsecsymbol),
            text(R.string.lightyear, R.string.lightyearsymbol),
        ),
    )

internal fun Context.temperatureUnits(): List<ConversionUnit> =
    localize(
        TemperatureConverter.DEFINITIONS,
        listOf(
            text(R.string.kelvin, R.string.kelvinsymbol),
            text(R.string.celsius, R.string.celsiussymbol),
            text(R.string.fahrenheit, R.string.fahrenheitsymbol),
        ),
    )

internal fun Context.otherUnits(): List<ConversionUnit> =
    localize(
        OtherConverter.DEFINITIONS,
        listOf(
            text(R.string.pico) { "-12".toExp() },
            text(R.string.nano) { "-9".toExp() },
            text(R.string.micro) { "-6".toExp() },
            text(R.string.milli) { "-3".toExp() },
            text({ "" }, { "" }),
            text(R.string.kilo) { "3".toExp() },
            text(R.string.mega) { "6".toExp() },
            text(R.string.giga) { "9".toExp() },
            text(R.string.tera) { "12".toExp() },
        ),
    )

internal fun Context.bynUnit(): ConversionUnit = ConversionUnit(getString(R.string.currency_byn_name), 1.0, "BYN")

private fun Context.localize(
    definitions: List<UnitDefinition>,
    texts: List<LocalizedUnitText>,
): List<ConversionUnit> {
    require(definitions.size == texts.size) {
        "Localized unit text count must match converter definitions"
    }
    return definitions.zip(texts) { definition, text ->
        ConversionUnit(
            name = text.name(this),
            factor = definition.factor,
            unitSymbol = text.symbol(this),
        )
    }
}

private data class LocalizedUnitText(
    val name: Context.() -> String,
    val symbol: Context.() -> String,
)

private fun text(
    @StringRes nameRes: Int,
    @StringRes symbolRes: Int,
): LocalizedUnitText = text({ getString(nameRes) }, { getString(symbolRes) })

private fun text(
    @StringRes nameRes: Int,
    symbol: Context.() -> String,
): LocalizedUnitText = text({ getString(nameRes) }, symbol)

private fun text(
    name: Context.() -> String,
    symbol: Context.() -> String,
): LocalizedUnitText = LocalizedUnitText(name, symbol)

private fun String.toExp(): String = "×10<sup><small>$this</small></sup>"
