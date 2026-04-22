package dev.nevack.unitconverter.model.nbrb

import android.os.LocaleList
import dev.nevack.unitconverter.DateJsonAdapter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.Locale

@Serializable
data class NBRBCurrency(
    @SerialName("Cur_Abbreviation")
    val curAbbreviation: String,
    @SerialName("Cur_Code")
    val curCode: String,
    @SerialName("Cur_DateEnd")
    @Serializable(with = DateJsonAdapter::class)
    val curDateEnd: Date,
    @SerialName("Cur_DateStart")
    @Serializable(with = DateJsonAdapter::class)
    val curDateStart: Date,
    @SerialName("Cur_ID")
    val curID: Int,
    @SerialName("Cur_Name")
    val curName: String,
    @SerialName("Cur_Name_Bel")
    val curNameBel: String,
    @SerialName("Cur_Name_BelMulti")
    val curNameBelMulti: String,
    @SerialName("Cur_Name_Eng")
    val curNameEng: String,
    @SerialName("Cur_Name_EngMulti")
    val curNameEngMulti: String,
    @SerialName("Cur_NameMulti")
    val curNameMulti: String,
    @SerialName("Cur_ParentID")
    val curParentID: Int,
    @SerialName("Cur_Periodicity")
    val curPeriodicity: Int,
    @SerialName("Cur_QuotName")
    val curQuotName: String,
    @SerialName("Cur_QuotName_Bel")
    val curQuotNameBel: String,
    @SerialName("Cur_QuotName_Eng")
    val curQuotNameEng: String,
    @SerialName("Cur_Scale")
    val curScale: Int,
) {
    companion object {
        private val EN = Locale("en")
        private val RU = Locale("ru")
        private val BE = Locale("be")
        private val LANGUAGES = listOf(EN, RU, BE).map { it.language }.toSet()

        fun getCompatibleLocale(list: LocaleList): Locale {
            for (i in 0 until list.size()) {
                val locale = list[i] ?: continue
                if (locale.language in LANGUAGES) return locale
            }
            return list[0] ?: Locale.getDefault()
        }
    }

    fun getLocalizedName(locale: Locale): String =
        when (locale.language) {
            EN.language -> curNameEng
            RU.language -> curName
            BE.language -> curNameBel
            else -> curNameEng
        }
}
