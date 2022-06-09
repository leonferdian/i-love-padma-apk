package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_company implements Serializable {
    private String iInternalId,iId, szId, szName,szDescription,szLedgerId,szCurrencyRateId,szTaxNo,szPKPNo,dtmNPWP,
            szNPWPNo,szSiup,szAddress,szCurrencyId,codeCompany,szUserCreatedId,dtmCreated,dtmLastUpdated,nama_user,divisi,
            flow_menu,flow_design,flow_output,table_korelasi,member,status_project,foto_user,tgl_lahir_user,nama_divisi,
            total_selesai,task_list,tipe_open,latitude,longitude,status_user,time_check_in,time_check_out,address_check_in,address_check_out,
            absen_today,app_version,latitude2,longitude2,id_absen,tiket_all,tiket_close,company_creator,user_status;

    public Data_company() {

    }

    public String getuser_status() {
        return user_status;
    }
    public void setuser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getcompany_creator() {
        return company_creator;
    }
    public void setcompany_creator(String company_creator) {
        this.company_creator = company_creator;
    }

    public String gettiket_close() {
        return tiket_close;
    }
    public void settiket_close(String tiket_close) {
        this.tiket_close = tiket_close;
    }

    public String gettiket_all() {
        return tiket_all;
    }
    public void settiket_all(String tiket_all) {
        this.tiket_all = tiket_all;
    }

    public String getid_absen() {
        return id_absen;
    }
    public void setid_absen(String id_absen) {
        this.id_absen = id_absen;
    }

    public String getlongitude2() {
        return longitude2;
    }
    public void setlongitude2(String longitude2) {
        this.longitude2 = longitude2;
    }

    public String getlatitude2() {
        return latitude2;
    }
    public void setlatitude2(String latitude2) {
        this.latitude2 = latitude2;
    }

    public String getapp_version() {
        return app_version;
    }
    public void setapp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getabsen_today() {
        return absen_today;
    }
    public void setabsen_today(String absen_today) {
        this.absen_today = absen_today;
    }

    public String getaddress_check_out() {
        return address_check_out;
    }
    public void setaddress_check_out(String address_check_out) {
        this.address_check_out = address_check_out;
    }

    public String getaddress_check_in() {
        return address_check_in;
    }
    public void setaddress_check_in(String address_check_in) {
        this.address_check_in = address_check_in;
    }

    public String gettime_check_out() {
        return time_check_out;
    }
    public void settime_check_out(String time_check_out) {
        this.time_check_out = time_check_out;
    }

    public String gettime_check_in() {
        return time_check_in;
    }
    public void settime_check_in(String time_check_in) {
        this.time_check_in = time_check_in;
    }

    public String getstatus_user() {
        return status_user;
    }
    public void setstatus_user(String status_user) {
        this.status_user = status_user;
    }

    public String getlongitude() {
        return longitude;
    }
    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getlatitude() {
        return latitude;
    }
    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }

    public String gettipe_open() {
        return tipe_open;
    }
    public void settipe_open(String tipe_open) {
        this.tipe_open = tipe_open;
    }

    public String gettask_list() {
        return task_list;
    }
    public void settask_list(String task_list) {
        this.task_list = task_list;
    }

    public String gettotal_selesai() {
        return total_selesai;
    }
    public void settotal_selesai(String total_selesai) {
        this.total_selesai = total_selesai;
    }

    public String getnama_divisi() {
        return nama_divisi;
    }
    public void setnama_divisi(String nama_divisi) {
        this.nama_divisi = nama_divisi;
    }

    public String gettgl_lahir_user() {
        return tgl_lahir_user;
    }
    public void settgl_lahir_user(String tgl_lahir_user) {
        this.tgl_lahir_user = tgl_lahir_user;
    }

    public String getfoto_user() {
        return foto_user;
    }
    public void setfoto_user(String foto_user) {
        this.foto_user = foto_user;
    }

    public String getstatus_project() {
        return status_project;
    }
    public void setstatus_project(String status_project) {
        this.status_project = status_project;
    }

    public String getmember() {
        return member;
    }
    public void setmember(String member) {
        this.member = member;
    }

    public String gettable_korelasi() {
        return table_korelasi;
    }
    public void settable_korelasi(String table_korelasi) {
        this.table_korelasi = table_korelasi;
    }

    public String getflow_output() {
        return flow_output;
    }
    public void setflow_output(String flow_output) {
        this.flow_output = flow_output;
    }

    public String getflow_design() {
        return flow_design;
    }
    public void setflow_design(String flow_design) {
        this.flow_design = flow_design;
    }

    public String getflow_menu() {
        return flow_menu;
    }
    public void setflow_menu(String flow_menu) {
        this.flow_menu = flow_menu;
    }

    public String getdivisi() {
        return divisi;
    }
    public void setdivisi(String divisi) {
        this.divisi = divisi;
    }

    public String getnama_user() {
        return nama_user;
    }
    public void setnama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getcodeCompany() {
        return codeCompany;
    }
    public void setcodeCompany(String codeCompany) {
        this.codeCompany = codeCompany;
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

    public String getszCurrencyId() {
        return szCurrencyId;
    }
    public void setszCurrencyId(String szCurrencyId) {
        this.szCurrencyId = szCurrencyId;
    }

    public String getszAddress() {
        return szAddress;
    }
    public void setszAddress(String szAddress) {
        this.szAddress = szAddress;
    }

    public String getszSiup() {
        return szSiup;
    }
    public void setszSiup(String szSiup) {
        this.szSiup = szSiup;
    }

    public String getszNPWPNo() {
        return szNPWPNo;
    }
    public void setszNPWPNo(String szNPWPNo) {
        this.szNPWPNo = szNPWPNo;
    }

    public String getdtmNPWP() {
        return dtmNPWP;
    }
    public void setdtmNPWP(String dtmNPWP) {
        this.dtmNPWP = dtmNPWP;
    }

    public String getszPKPNo() {
        return szPKPNo;
    }
    public void setszPKPNo(String szPKPNo) {
        this.szPKPNo = szPKPNo;
    }

    public String getszTaxNo() {
        return szTaxNo;
    }
    public void setszTaxNo(String szTaxNo) {
        this.szTaxNo = szTaxNo;
    }

    public String getszCurrencyRateId() {
        return szCurrencyRateId;
    }
    public void setszCurrencyRateId(String szCurrencyRateId) {
        this.szCurrencyRateId = szCurrencyRateId;
    }

    public String getszLedgerId() {
        return szLedgerId;
    }
    public void setszLedgerId(String szLedgerId) {
        this.szLedgerId = szLedgerId;
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
