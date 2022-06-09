package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_note implements Serializable {
    private String id,nmr_note,tgl_note,subject_note,jenis_note,isi_note,date_create,create_by,date_update,update_by,nmr_tiket,participan,
            nama_user,status_note,file_note,email_user;

    public Data_note() {
    }

    public String getemail_user() {
        return email_user;
    }
    public void setemail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getfile_note() {
        return file_note;
    }
    public void setfile_note(String file_note) {
        this.file_note = file_note;
    }

    public String getstatus_note() {
        return status_note;
    }
    public void setstatus_note(String status_note) {
        this.status_note = status_note;
    }

    public String getparticipan() {
        return participan;
    }
    public void setparticipan(String participan) {
        this.participan = participan;
    }

    public String getnama_user() {
        return nama_user;
    }
    public void setnama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getnmr_note() {
        return nmr_note;
    }
    public void setnmr_note(String nmr_note) {
        this.nmr_note = nmr_note;
    }

    public String gettgl_note() {
        return tgl_note;
    }
    public void settgl_note(String tgl_note) {
        this.tgl_note = tgl_note;
    }

    public String getsubject_note() {
        return subject_note;
    }
    public void setsubject_note(String subject_note) {
        this.subject_note = subject_note;
    }

    public String getjenis_note() {
        return jenis_note;
    }
    public void setjenis_note(String jenis_note) {
        this.jenis_note = jenis_note;
    }

    public String getisi_note() {
        return isi_note;
    }
    public void setisi_note(String isi_note) {
        this.isi_note = isi_note;
    }

    public String getdate_create() {
        return date_create;
    }
    public void setdate_create(String date_create) {
        this.date_create = date_create;
    }

    public String getcreate_by() {
        return create_by;
    }
    public void setcreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getdate_update() {
        return date_update;
    }
    public void setdate_update(String date_update) {
        this.date_update = date_update;
    }

    public String getupdate_by() {
        return update_by;
    }
    public void setupdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getnmr_tiket() {
        return nmr_tiket;
    }
    public void setnmr_tiket(String nmr_tiket) {
        this.nmr_tiket = nmr_tiket;
    }

}
