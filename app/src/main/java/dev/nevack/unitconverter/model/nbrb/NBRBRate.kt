package dev.nevack.unitconverter.model.nbrb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.nevack.unitconverter.model.ConversionUnit
import java.util.Date

@JsonClass(generateAdapter = true)
data class NBRBRate(
    @Json(name = "Cur_Abbreviation")
    val curAbbreviation: String,
    @Json(name = "Cur_ID")
    val curID: Int,
    @Json(name = "Cur_Name")
    val curName: String,
    @Json(name = "Cur_OfficialRate")
    val curOfficialRate: Double,
    @Json(name = "Cur_Scale")
    val curScale: Int,
    @Json(name = "Date")
    val date: Date,
) {
    fun toUnitLocalized(name: String): ConversionUnit {
        return ConversionUnit(
            name,
            curOfficialRate / curScale,
            curAbbreviation
        )
    }
}
