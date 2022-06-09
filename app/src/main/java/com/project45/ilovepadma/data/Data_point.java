package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_point implements Serializable {
    private String id,kategori, date_create,nilai_point,isi_komen,komen_from;
    private int jml_point;

    public Data_point() {
    }

    public Data_point(String id, String kategori, String date_create) {
        this.id = id;
        this.kategori = kategori;
        this.date_create = date_create;

    }

    public String getkomen_from() {
        return komen_from;
    }
    public void setkomen_from(String komen_from) {
        this.komen_from = komen_from;
    }

    public String getisi_komen() {
        return isi_komen;
    }
    public void setisi_komen(String isi_komen) {
        this.isi_komen = isi_komen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getkategori() {
        return kategori;
    }

    public void setkategori(String kategori) {
        this.kategori = kategori;
    }

    public String getdate_create() {
        return date_create;
    }

    public void setdate_create(String date_create) {
        this.date_create = date_create;
    }

    public int getjml_point() {
        return jml_point;
    }

    public void setjml_point(int jml_point) {
        this.jml_point = jml_point;
    }



    public String getnilai_point() {
        return nilai_point;
    }

    public void setnilai_point(String nilai_point) {
        this.nilai_point = nilai_point;
    }



}
