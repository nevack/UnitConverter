package dev.nevack.unitconverter.model.nbrb

import dev.nevack.unitconverter.DateJsonAdapter
import dev.nevack.unitconverter.model.ConversionUnit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NBRBRate(
    @SerialName("Cur_Abbreviation")
    val curAbbreviation: String,
    @SerialName("Cur_ID")
    val curID: Int,
    @SerialName("Cur_Name")
    val curName: String,
    @SerialName("Cur_OfficialRate")
    val curOfficialRate: Double,
    @SerialName("Cur_Scale")
    val curScale: Int,
    @SerialName("Date")
    @Serializable(with = DateJsonAdapter::class)
    val date: Date,
) {
    fun toUnitLocalized(name: String): ConversionUnit =
        ConversionUnit(
            name,
            curOfficialRate / curScale,
            curAbbreviation,
        )
}
