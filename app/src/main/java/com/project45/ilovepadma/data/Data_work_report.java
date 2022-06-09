package com.project45.ilovepadma.data;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Data_work_report implements Serializable {

    private String id,job_list, id_job_list, keterangan;
    private String waktu_mulai,waktu_selesai, date_create,jml_report,durasi,nmr_wr,tahun_wr,bulan_wr,minggu_wr,
                    divisi,department,pilar,tugas_project,create_by,complain_foto,nama_user,sub_project,id_user,user_login,status_agenda;
    Uri uri;
    public Data_work_report() {
    }

    public String getstatus_agenda() {
        return status_agenda;
    }
    public void setstatus_agenda(String status_agenda) {
        this.status_agenda = status_agenda;
    }

    public String getuser_login() {
        return user_login;
    }
    public void setuser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getid_user() {
        return id_user;
    }
    public void setid_user(String id_user) {
        this.id_user = id_user;
    }

    public String getsub_project() {
        return sub_project;
    }
    public void setsub_project(String sub_project) {
        this.sub_project = sub_project;
    }

    public String getnama_user() {
        return nama_user;
    }
    public void setnama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getcomplain_foto() {
        return complain_foto;
    }
    public void setcomplain_foto(String complain_foto) {
        this.complain_foto = complain_foto;
    }

    public String getcreate_by() {
        return create_by;
    }
    public void setcreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String gettugas_project() {
        return tugas_project;
    }
    public void settugas_project(String tugas_project) {
        this.tugas_project = tugas_project;
    }

    public String getpilar() {
        return pilar;
    }
    public void setpilar(String pilar) {
        this.pilar = pilar;
    }

    public String getdepartment() {
        return department;
    }
    public void setdepartment(String department) {
        this.department = department;
    }

    public String getdivisi() {
        return divisi;
    }
    public void setdivisi(String divisi) {
        this.divisi = divisi;
    }

    public String getminggu_wr() {
        return minggu_wr;
    }
    public void setminggu_wr(String minggu_wr) {
        this.minggu_wr = minggu_wr;
    }

    public String getbulan_wr() {
        return bulan_wr;
    }
    public void setbulan_wr(String bulan_wr) {
        this.bulan_wr = bulan_wr;
    }

    public String gettahun_wr() {
        return tahun_wr;
    }
    public void settahun_wr(String tahun_wr) {
        this.tahun_wr = tahun_wr;
    }

    public String getnmr_wr() {
        return nmr_wr;
    }
    public void setnmr_wr(String nmr_wr) {
        this.nmr_wr = nmr_wr;
    }

    public String getdurasi() {
        return durasi;
    }
    public void setdurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getjml_report() {
        return jml_report;
    }
    public void setjml_report(String jml_report) {
        this.jml_report = jml_report;
    }

    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getjob_list() {
        return job_list;
    }
    public void setjob_list(String job_list) {
        this.job_list = job_list;
    }

    public String getid_job_list() {
        return id_job_list;
    }
    public void setid_job_list(String id_job_list) {
        this.id_job_list = id_job_list;
    }

    public String getketerangan() {
        return keterangan;
    }
    public void setketerangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getwaktu_mulai() {
        return waktu_mulai;
    }
    public void setwaktu_mulai(String waktu_mulai) {
        this.waktu_mulai = waktu_mulai;
    }

    public String getwaktu_selesai() {
        return waktu_selesai;
    }
    public void setwaktu_selesai(String waktu_selesai) {
        this.waktu_selesai = waktu_selesai;
    }

    public String getdate_create() {
        return date_create;
    }
    public void setdate_create(String date_create) {
        this.date_create = date_create;
    }

    public Data_work_report(JSONObject jsonObject) {
        try {
                this.setjml_report(jsonObject.getString("jml_report"));
                if (!jsonObject.isNull("tahun")) {
                    this.settahun_wr(jsonObject.getString("tahun"));
                }

                if (!jsonObject.isNull("bulan")) {
                    this.setbulan_wr(jsonObject.getString("bulan"));
                }

                if (!jsonObject.isNull("minggu")) {
                    this.setminggu_wr(jsonObject.getString("minggu"));
                }

            if (!jsonObject.isNull("durasi")) {
                this.setdurasi(jsonObject.getString("durasi"));
            }

        }
        catch (JSONException je) {
            je.printStackTrace();
        }
    }

}
