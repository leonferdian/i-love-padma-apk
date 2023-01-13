package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_pertanyaan_post_everything implements Serializable {
    private String nomor,id_timeline_jawaban,id_timeline,kategori,jenis_post,id_timeline_pertanyaan,pertanyaan, jawaban,tipe_pertanyaan,keterangan_system, judul_pertanyaan;

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getId_timeline_jawaban() {
        return id_timeline_jawaban;
    }

    public void setId_timeline_jawaban(String id_timeline_jawaban) {
        this.id_timeline_jawaban = id_timeline_jawaban;
    }

    public String getId_timeline() {
        return id_timeline;
    }

    public void setId_timeline(String id_timeline) {
        this.id_timeline = id_timeline;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJenis_post() {
        return jenis_post;
    }

    public void setJenis_post(String jenis_post) {
        this.jenis_post = jenis_post;
    }

    public String getId_timeline_pertanyaan() {
        return id_timeline_pertanyaan;
    }

    public void setId_timeline_pertanyaan(String id_timeline_pertanyaan) {
        this.id_timeline_pertanyaan = id_timeline_pertanyaan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }

    public String getTipe_pertanyaan() {
        return tipe_pertanyaan;
    }

    public void setTipe_pertanyaan(String tipe_pertanyaan) {
        this.tipe_pertanyaan = tipe_pertanyaan;
    }

    public String getKeterangan_system() {
        return keterangan_system;
    }

    public void setKeterangan_system(String keterangan_system) {
        this.keterangan_system = keterangan_system;
    }

    public String getJudulPertanyaan() {
        return judul_pertanyaan;
    }

    public void setJudulPertanyaan(String judul_pertanyaan) {
        this.judul_pertanyaan = judul_pertanyaan;
    }
}
