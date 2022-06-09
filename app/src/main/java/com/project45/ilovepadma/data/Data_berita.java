package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_berita  implements Serializable {

    private String id,judul_berita,isi_berita,gambar_berita,tgl_berita,nama_user,foto_user;
    public Data_berita() {

    }

    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getjudul_berita() {
        return judul_berita;
    }
    public void setjudul_berita(String judul_berita) {
        this.judul_berita = judul_berita;
    }

    public String getisi_berita() {
        return isi_berita;
    }
    public void setisi_berita(String isi_berita) {
        this.isi_berita = isi_berita;
    }

    public String getgambar_berita() {
        return gambar_berita;
    }
    public void setgambar_berita(String gambar_berita) {
        this.gambar_berita = gambar_berita;
    }

    public String gettgl_berita() {
        return tgl_berita;
    }
    public void settgl_berita(String tgl_berita) {
        this.tgl_berita = tgl_berita;
    }

    public String getnama_user() {
        return nama_user;
    }
    public void setnama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getfoto_user() {
        return foto_user;
    }
    public void setfoto_user(String foto_user) {
        this.foto_user = foto_user;
    }

}
