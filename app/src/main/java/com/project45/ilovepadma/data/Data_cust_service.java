package com.project45.ilovepadma.data;

import android.net.Uri;

import java.io.Serializable;

public class Data_cust_service implements Serializable {

    private String id, id_pelanggan, nama_pelanggan,created_date,jenis_complain,for_divisi,sign_to,sales_id,isi_complain;
    private String foto_pelanggan,status_complain,due_date,periode_awal,periode_akhir,subject_complain,kat_complain,nmr_project,nmr_sub_project,
    l1,l2,l3,pic2,jml_respon,respon_new,tiket_creator,status_sent,nama_file,tipe_file,lokasi_file;
    Uri PathFile;
    public Data_cust_service() {
    }

    public Data_cust_service(String id, String id_pelanggan, String nama_pelanggan, String created_date, String jenis_complain) {
        this.id = id;
        this.id_pelanggan = id_pelanggan;
        this.nama_pelanggan = nama_pelanggan;
        this.jenis_complain = jenis_complain;
        this.created_date = created_date;
    }

    public Uri getPathFile() {
        return PathFile;
    }
    public void setPathFile(Uri PathFile) {
        this.PathFile = PathFile;
    }

    public String getlokasi_file() {
        return lokasi_file;
    }
    public void setlokasi_file(String lokasi_file) {
        this.lokasi_file = lokasi_file;
    }

    public String gettipe_file() {
        return tipe_file;
    }
    public void settipe_file(String tipe_file) {
        this.tipe_file = tipe_file;
    }

    public String getnama_file() {
        return nama_file;
    }
    public void setnama_file(String nama_file) {
        this.nama_file = nama_file;
    }

    public String getstatus_sent() {
        return status_sent;
    }
    public void setstatus_sent(String status_sent) {
        this.status_sent = status_sent;
    }

    public String gettiket_creator() {
        return tiket_creator;
    }
    public void settiket_creator(String tiket_creator) {
        this.tiket_creator = tiket_creator;
    }

    public String getrespon_new() {
        return respon_new;
    }
    public void setrespon_new(String respon_new) {
        this.respon_new = respon_new;
    }

    public String getjml_respon() {
        return jml_respon;
    }
    public void setjml_respon(String jml_respon) {
        this.jml_respon = jml_respon;
    }

    public String getpic2() {
        return pic2;
    }
    public void setpic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getsubject_complain() {
        return subject_complain;
    }
    public void setsubject_complain(String subject_complain) {
        this.subject_complain = subject_complain;
    }

    public String getl3() {
        return l3;
    }
    public void setl3(String l3) {
        this.l3 = l3;
    }

    public String getl2() {
        return l2;
    }
    public void setl2(String l2) {
        this.l2 = l2;
    }

    public String getl1() {
        return l1;
    }
    public void setl1(String l1) {
        this.l1 = l1;
    }

    public String getkat_complain() {
        return kat_complain;
    }
    public void setkat_complain(String kat_complain) {
        this.kat_complain = kat_complain;
    }

    public String getnmr_sub_project() {
        return nmr_sub_project;
    }
    public void setnmr_sub_project(String nmr_sub_project) {
        this.nmr_sub_project = nmr_sub_project;
    }

    public String getnmr_project() {
        return nmr_project;
    }
    public void setnmr_project(String nmr_project) {
        this.nmr_project = nmr_project;
    }

    public String getdue_date() {
        return due_date;
    }
    public void setdue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getperiode_awal() {
        return periode_awal;
    }
    public void setperiode_awal(String periode_awal) {
        this.periode_awal = periode_awal;
    }

    public String getperiode_akhir() {
        return periode_akhir;
    }
    public void setperiode_akhir(String periode_akhir) {
        this.periode_akhir = periode_akhir;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getid_pelanggan() {
        return id_pelanggan;
    }
    public void setid_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getnama_pelanggan() {
        return nama_pelanggan;
    }
    public void setnama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getcreated_date() {
        return created_date;
    }
    public void setcreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getjenis_complain() {
        return jenis_complain;
    }
    public void setjenis_complain(String jenis_complain) {
        this.jenis_complain = jenis_complain;
    }

    public String getfor_divisi() {
        return for_divisi;
    }
    public void setfor_divisi(String for_divisi) {
        this.for_divisi = for_divisi;
    }

    public String getsign_to() {
        return sign_to;
    }
    public void setsign_to(String sign_to) {
        this.sign_to = sign_to;
    }

    public String getsales_id() {
        return sales_id;
    }
    public void setsales_id(String sales_id) {
        this.sales_id = sales_id;
    }

    public String getisi_complain() {
        return isi_complain;
    }
    public void setisi_complain(String isi_complain) {
        this.isi_complain = isi_complain;
    }

    public String getfoto_pelanggan() {
        return foto_pelanggan;
    }
    public void setfoto_pelanggan(String foto_pelanggan) {
        this.foto_pelanggan = foto_pelanggan;
    }

    public String getstatus_complain() {
        return status_complain;
    }
    public void setstatus_complain(String status_complain) {
        this.status_complain = status_complain;
    }
}
