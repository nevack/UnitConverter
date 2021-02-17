package org.nevack.unitconverter.model

import com.squareup.moshi.Json

data class Currency(
    @Json(name = "Cur_ID") var curID: Int,
    @Json(name = "Cur_ParentID") var curParentID: Int,
    @Json(name = "Cur_Code") var curCode: String,
    @Json(name = "Cur_Abbreviation") var curAbbreviation: String,
    @Json(name = "Cur_Name") var curName: String,
    @Json(name = "Cur_Name_Bel") var curNameBel: String,
    @Json(name = "Cur_Name_Eng") var curNameEng: String,
    @Json(name = "Cur_QuotName") var curQuotName: String,
    @Json(name = "Cur_QuotName_Bel") var curQuotNameBel: String,
    @Json(name = "Cur_QuotName_Eng") var curQuotNameEng: String,
    @Json(name = "Cur_NameMulti") var curNameMulti: String,
    @Json(name = "Cur_Name_BelMulti") var curNameBelMulti: String,
    @Json(name = "Cur_Name_EngMulti") var curNameEngMulti: String,
    @Json(name = "Cur_Scale") var curScale: Int,
    @Json(name = "Cur_Periodicity") var curPeriodicity: Int,
    @Json(name = "Cur_DateStart") var curDateStart: String,
    @Json(name = "Cur_DateEnd") var curDateEnd: String,
)
