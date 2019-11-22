package org.nevack.unitconverter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Currency {
    @SerializedName("Cur_ID")
    @Expose
    public Integer curID;
    @SerializedName("Cur_ParentID")
    @Expose
    public Integer curParentID;
    @SerializedName("Cur_Code")
    @Expose
    public String curCode;
    @SerializedName("Cur_Abbreviation")
    @Expose
    public String curAbbreviation;
    @SerializedName("Cur_Name")
    @Expose
    public String curName;
    @SerializedName("Cur_Name_Bel")
    @Expose
    public String curNameBel;
    @SerializedName("Cur_Name_Eng")
    @Expose
    public String curNameEng;
    @SerializedName("Cur_QuotName")
    @Expose
    public String curQuotName;
    @SerializedName("Cur_QuotName_Bel")
    @Expose
    public String curQuotNameBel;
    @SerializedName("Cur_QuotName_Eng")
    @Expose
    public String curQuotNameEng;
    @SerializedName("Cur_NameMulti")
    @Expose
    public String curNameMulti;
    @SerializedName("Cur_Name_BelMulti")
    @Expose
    public String curNameBelMulti;
    @SerializedName("Cur_Name_EngMulti")
    @Expose
    public String curNameEngMulti;
    @SerializedName("Cur_Scale")
    @Expose
    public Integer curScale;
    @SerializedName("Cur_Periodicity")
    @Expose
    public Integer curPeriodicity;
    @SerializedName("Cur_DateStart")
    @Expose
    public String curDateStart;
    @SerializedName("Cur_DateEnd")
    @Expose
    public String curDateEnd;
}
