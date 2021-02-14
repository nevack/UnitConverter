package org.nevack.unitconverter.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rate(
    @Json(name = "Cur_ID") var curID: Int,
    @Json(name = "Date") var date: String,
    @Json(name = "Cur_Abbreviation") var curAbbreviation: String,
    @Json(name = "Cur_Scale") var curScale: Int,
    @Json(name = "Cur_Name") var curName: String,
    @Json(name = "Cur_OfficialRate") var curOfficialRate: Double,
) {
    fun toUnit(): Unit {
        return Unit(
            curName,
            curOfficialRate / curScale,
            curAbbreviation
        )
    }
}