package com.yunmi.device;

public enum MobileOperator {
    CHINA_MOBILE("中国移动"),
    CHINA_UNICOM("中国联通"),
    CHINA_TELCOM("中国电信"),
    CHINA_TIETONG("中国铁通"),
    T_MOBILE("T-Mobile"),
    AT_T("AT&T"),
    Irancell("Irancell"),
    TLS_TT("TLS-TT"),
    CT_Macao("CT Macao"),
    Ooredoo("Ooredoo"),
    SKTelecom("SKTelecom"),
    XL("XL"),
    NCL_MOBILIS("NCL MOBILIS"),
    DiGi("DiGi"),
    SYRIATEL("SYRIATEL"),
    Digitel("Digitel"),
    MTN_ZM("MTN ZM"),
    Mobinil("Mobinil"),
    MY_MAXIS("MY MAXIS"),
    TW_Mobile("TW Mobile"),
    Viettel_Mobile("Viettel Mobile"),
    IR_MCI("IR-MCI"),
    MegaFon("MegaFon"),
    Telenor("Telenor"),
    MTN_SA("MTN_SA"),
    MOVILNET("MOVILNET"),
    ITE("ITE"),
    Rogers_Wireless("Rogers_Wireless"),
    dtac_TriNet("dtac_TriNet"),
    MTS_RUS("MTS_RUS"),
    ASIACELL("ASIACELL"),
    RighTel("RighTel"),
    CMHK("CMHK"),
    TELKOMSEL("TELKOMSEL"),
    Etisalat("Etisalat"),
    MTN_CG("MTN_CG"),
    TELCEL("TELCEL"),
    Mobilis("Mobilis"),
    StarHub("StarHub"),
    Three_Macau("Three_Macau"),
    Far_EasTone("Far_EasTone"),
    NTT_DOCOMO("NTT_DOCOMO"),
    SmarTone("SmarTone"),
    MOBITEL_KHM("MOBITEL_KHM"),
    Mobilink("Mobilink"),
    UNKNOWN("未知");

    private final String name;

    MobileOperator(String arg3) {
        this.name = arg3;
    }

    public static MobileOperator parseOperator(long arg4) {
        String str = arg4 + "";
        if(str.length() != 15) {
            return MobileOperator.UNKNOWN;
        }

        switch(Integer.parseInt(str.substring(0, 5))) {
            case 25001: {
                return MobileOperator.MTS_RUS;
            }
            case 25002: {
                return MobileOperator.MegaFon;
            }
            case 30237: {
                return MobileOperator.Rogers_Wireless;
            }
            case 31011: {
                return MobileOperator.ITE;
            }
            case 31021:
            case 31026: {
                return MobileOperator.T_MOBILE;
            }
            case 31009:
            case 31038: {
                return MobileOperator.AT_T;
            }
            case 33402: {
                return MobileOperator.TELCEL;
            }
            case 41001: {
                return MobileOperator.Mobilink;
            }
            case 41303: {
                return MobileOperator.Etisalat;
            }
            case 41406: {
                return MobileOperator.Telenor;
            }
            case 41701: {
                return MobileOperator.SYRIATEL;
            }
            case 41805: {
                return MobileOperator.ASIACELL;
            }
            case 43211: {
                return MobileOperator.IR_MCI;
            }
            case 43220: {
                return MobileOperator.RighTel;
            }
            case 43235: {
                return MobileOperator.Irancell;
            }
            case 44010: {
                return MobileOperator.NTT_DOCOMO;
            }
            case 45005: {
                return MobileOperator.SKTelecom;
            }
            case 45204: {
                return MobileOperator.Viettel_Mobile;
            }
            case 45412: {
                return MobileOperator.CMHK;
            }
            case 45500: {
                return MobileOperator.SmarTone;
            }
            case 45502: {
                return MobileOperator.CT_Macao;
            }
            case 45503: {
                return MobileOperator.Three_Macau;
            }
            case 45601: {
                return MobileOperator.MOBITEL_KHM;
            }
            case 46001:
            case 46006: {
                return MobileOperator.CHINA_UNICOM;
            }
            case 45400:
            case 46000:
            case 46002:
            case 46007: {
                return MobileOperator.CHINA_MOBILE;
            }
            case 46003:
            case 46005:
            case 46011: {
                return MobileOperator.CHINA_TELCOM;
            }
            case 46020: {
                return MobileOperator.CHINA_TIETONG;
            }
            case 46601: {
                return MobileOperator.Far_EasTone;
            }
            case 46697: {
                return MobileOperator.TW_Mobile;
            }
            case 50212: {
                return MobileOperator.MY_MAXIS;
            }
            case 50216: {
                return MobileOperator.DiGi;
            }
            case 51010: {
                return MobileOperator.TELKOMSEL;
            }
            case 51011: {
                return MobileOperator.XL;
            }
            case 51402: {
                return MobileOperator.TLS_TT;
            }
            case 52005: {
                return MobileOperator.dtac_TriNet;
            }
            case 52505: {
                return MobileOperator.StarHub;
            }
            case 54601: {
                return MobileOperator.NCL_MOBILIS;
            }
            case 60201: {
                return MobileOperator.Mobinil;
            }
            case 60301: {
                return MobileOperator.Mobilis;
            }
            case 41405:
            case 60303: {
                return MobileOperator.Ooredoo;
            }
            case 62910: {
                return MobileOperator.MTN_CG;
            }
            case 64502: {
                return MobileOperator.MTN_ZM;
            }
            case 65510: {
                return MobileOperator.MTN_SA;
            }
            case 73402: {
                return MobileOperator.Digitel;
            }
            case 73406: {
                return MobileOperator.MOVILNET;
            }
            default: {
                return MobileOperator.UNKNOWN;
            }
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.name);
    }
}