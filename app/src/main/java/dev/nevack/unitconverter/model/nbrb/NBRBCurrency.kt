package dev.nevack.unitconverter.model.nbrb

import androidx.core.os.LocaleListCompat
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date
import java.util.Locale

@JsonClass(generateAdapter = true)
data class NBRBCurrency(
    @Json(name = "Cur_Abbreviation")
    val curAbbreviation: String,
    @Json(name = "Cur_Code")
    val curCode: String,
    @Json(name = "Cur_DateEnd")
    val curDateEnd: Date,
    @Json(name = "Cur_DateStart")
    val curDateStart: Date,
    @Json(name = "Cur_ID")
    val curID: Int,
    @Json(name = "Cur_Name")
    val curName: String,
    @Json(name = "Cur_Name_Bel")
    val curNameBel: String,
    @Json(name = "Cur_Name_BelMulti")
    val curNameBelMulti: String,
    @Json(name = "Cur_Name_Eng")
    val curNameEng: String,
    @Json(name = "Cur_Name_EngMulti")
    val curNameEngMulti: String,
    @Json(name = "Cur_NameMulti")
    val curNameMulti: String,
    @Json(name = "Cur_ParentID")
    val curParentID: Int,
    @Json(name = "Cur_Periodicity")
    val curPeriodicity: Int,
    @Json(name = "Cur_QuotName")
    val curQuotName: String,
    @Json(name = "Cur_QuotName_Bel")
    val curQuotNameBel: String,
    @Json(name = "Cur_QuotName_Eng")
    val curQuotNameEng: String,
    @Json(name = "Cur_Scale")
    val curScale: Int
) {
    companion object {
        private val EN = Locale("en")
        private val RU = Locale("ru")
        private val BE = Locale("be")
        private val LANGUAGES = listOf(EN, RU, BE).map { it.language }.toSet()

        fun getCompatibleLocale(list: LocaleListCompat): Locale {
            for (i in 0 until list.size()) {
                val locale = list[i] ?: continue
                if (locale.language in LANGUAGES) return locale
            }
            return list[0] ?: Locale.getDefault()
        }
    }

    fun getLocalizedName(locale: Locale): String = when (locale.language) {
        EN.language -> curNameEng
        RU.language -> curName
        BE.language -> curNameBel
        else -> curNameEng
    }
}
