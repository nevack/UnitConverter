package dev.nevack.unitconverter.nbrb.model

import dev.nevack.unitconverter.model.ConversionUnit
import dev.nevack.unitconverter.nbrb.DateJsonAdapter
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
