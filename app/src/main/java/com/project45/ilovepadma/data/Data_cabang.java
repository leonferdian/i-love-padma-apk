package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_cabang implements Serializable {
    private String iInternalId,iId, szId, szName,szDescription,szCompanyId,szLangitude,szLongitude,szProvince,szTaxEntityId,
            szCity,szDistrict,szAddress,szSubDistrict,szUserCreatedId,dtmCreated,dtmLastUpdated;

    public Data_cabang() {

    }

    public String getdtmLastUpdated() {
        return dtmLastUpdated;
    }
    public void setdtmLastUpdated(String dtmLastUpdated) {
        this.dtmLastUpdated = dtmLastUpdated;
    }

    public String getdtmCreated() {
        return dtmCreated;
    }
    public void setdtmCreated(String dtmCreated) {
        this.dtmCreated = dtmCreated;
    }

    public String getszUserCreatedId() {
        return szUserCreatedId;
    }
    public void setszUserCreatedId(String szUserCreatedId) {
        this.szUserCreatedId = szUserCreatedId;
    }

    public String getszSubDistrict() {
        return szSubDistrict;
    }
    public void setszSubDistrict(String szSubDistrict) {
        this.szSubDistrict = szSubDistrict;
    }

    public String getszAddress() {
        return szAddress;
    }
    public void setszAddress(String szAddress) {
        this.szAddress = szAddress;
    }

    public String getszDistrict() {
        return szDistrict;
    }
    public void setszDistrict(String szDistrict) {
        this.szDistrict = szDistrict;
    }

    public String getszCity() {
        return szCity;
    }
    public void setszCity(String szCity) {
        this.szCity = szCity;
    }

    public String getszTaxEntityId() {
        return szTaxEntityId;
    }
    public void setszTaxEntityId(String szTaxEntityId) {
        this.szTaxEntityId = szTaxEntityId;
    }

    public String getszProvince() {
        return szProvince;
    }
    public void setszProvince(String szProvince) {
        this.szProvince = szProvince;
    }

    public String getszLongitude() {
        return szLongitude;
    }
    public void setszLongitude(String szLongitude) {
        this.szLongitude = szLongitude;
    }

    public String getszLangitude() {
        return szLangitude;
    }
    public void setszLangitude(String szLangitude) {
        this.szLangitude = szLangitude;
    }

    public String getszCompanyId() {
        return szCompanyId;
    }
    public void setszCompanyId(String szCompanyId) {
        this.szCompanyId = szCompanyId;
    }

    public String getszDescription() {
        return szDescription;
    }
    public void setszDescription(String szDescription) {
        this.szDescription = szDescription;
    }

    public String getszName() {
        return szName;
    }
    public void setszName(String szName) {
        this.szName = szName;
    }

    public String getszId() {
        return szId;
    }
    public void setszId(String szId) {
        this.szId = szId;
    }

    public String getiId() {
        return iId;
    }
    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getiInternalId() {
        return iInternalId;
    }
    public void setiInternalId(String iInternalId) {
        this.iInternalId = iInternalId;
    }
}
