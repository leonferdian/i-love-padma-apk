package com.project45.ilovepadma.absensi;

public class HeaderItemAbsen implements list_absen.ListItem{
    private String id,id_user,hari_check_in,time_check_in,time_check_out,address_check_in,address_check_out,status_absensi,foto_user,image_absen;

    public void setid(String id) {
        this.id = id;
    }

    public void settime_check_in(String time_check_in) {
        this.time_check_in = time_check_in;
    }
    public void set_hari_time_check_in(String hari_check_in) {
        this.hari_check_in = hari_check_in;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

    @Override
    public String getid() {
        return id;
    }

    @Override
    public String getid_user() {
        return id_user;
    }

    @Override
    public String gethari_check_in() {
        return hari_check_in;
    }

    @Override
    public String gettime_check_in() {
        return time_check_in;
    }

    @Override
    public String gettime_check_out() {
        return time_check_out;
    }

    @Override
    public String getaddress_check_in() {
        return address_check_in;
    }

    @Override
    public String getaddress_check_out() {
        return address_check_out;
    }

    @Override
    public String getstatus_absensi() {
        return status_absensi;
    }

    @Override
    public String getfoto_user() {
        return foto_user;
    }

    @Override
    public String getfoto_absen() {
        return image_absen;
    }
}
