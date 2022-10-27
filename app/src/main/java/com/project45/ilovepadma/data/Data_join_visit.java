package com.project45.ilovepadma.data;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.project45.ilovepadma.global.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Data_join_visit implements Serializable {
    private String  id,nmr_jv, divisi,nama_salesman,id_srt_tgs,date_create,create_by,
            nama_customer,id_customer,status_audit,keterangan_audit,hari,id_foto,jv_foto,kategori_foto,tgl_foto,url,hari_jv,depo,id_depo,asal2,
            nama_barang,jml_order,rp_order,qty_order,jml_kunjungan,durasi,jml_cust,co_all,co_gallon,co_aqua,co_vit,co_bev,aqua_other,jml_outlet,id_employee,dt_base;
    private String cp,cps,out_of_route,toko_tutup,out_arena;
    private String alamat_cust,lat_cust,long_cust,status_insert,phone,
            program_terakhir,harga,komplain,jam_awal,jam_akhir,date_update,update_by,nama_program,merk1,promo1,
            merk2,promo2,merk3,promo3,merk4,promo4,mulai_istirahat,akhir_istirahat,status_istirahat,jenis_item3,vol_aqua,
            vol_vit,vol_beverage,vol_tot_order,komitmen_aqua,komitmen_vit,komitmen_beverage,jml_foto_stok,jml_foto_display,
            sisa_target_aqua,sisa_target_vit,sisa_target_bev,target_vol,pre_route,on_route,post_route,market_visit,coaching,jml_brg_dijual,
            bln_hr_kerja,mtd_tgl,mtd,target_harian,not_komitmen,target_cs,mtd_ideal,foto_blm_diupload,foto_sdh_diupload,nilai_klik,target_ao,jml_rute,
            qty_so,qty_dist,entity,urutas_st,kunjugan_ke,jml_komen,status_like,jml_like,jenis_post;
    private int id_salesman;
    private Uri uri;
    private Bitmap bitmap;
    private transient JSONObject json;

    private String nama_depo;

    public String getnama_depo() {
        return nama_depo;
    }
    public void setnama_depo(String nama_depo) {
        this.nama_depo = nama_depo;
    }

    /*  for dms3*/
    private String szDocId,szCustId,id_reason,szFailReason,week_id,week_name,szName;
    /* --- */

    /*  for pre route*/
    private String id_soal,soal,id_jawaban,jawaban_soal;
    /* --- */

    /*  for jabatan*/
    private String id_hot,nama_hot,id_hos,nama_hos,id_sm,nama_sm;
    /* --- */

    /*  for hsl kunjungan*/
    private String wood,bronze,silver,platinum,gold,diamond,level_7,level_8,level_9;
    /* --- */

    /*  for hsl kunjungan*/
    private String vehicle,police_number,order_non_kunjungan;
    /* --- */

    /* for rate */
    private String rate1,rate2,rate3,rate4,rate5,rate6,rate7,rate8,rate9,avg_rate,jenis_file;
    /* ---*/

    private ArrayList<Data_pertanyaan_post_everything> pertanyaan_post_everything;

    public ArrayList<Data_pertanyaan_post_everything> getPertanyaan_post_everything(){
        return pertanyaan_post_everything;
    }

    public void setPertanyaan_post_everything(Data_pertanyaan_post_everything pertanyaan){
        if(this.pertanyaan_post_everything == null){
            declare_pertanyaan_post_everything();
        }
        this.pertanyaan_post_everything.add(pertanyaan);
    }

    private void declare_pertanyaan_post_everything(){
        this.pertanyaan_post_everything = new ArrayList<>();
    }

    public String getjenis_post() {
        return jenis_post;
    }
    public void setjenis_post(String jenis_post) {
        this.jenis_post = jenis_post;
    }

    public String getjenis_file() {
        return jenis_file;
    }
    public void setjenis_file(String jenis_file) {
        this.jenis_file = jenis_file;
    }

    public String getjml_like() {
        return jml_like;
    }
    public void setjml_like(String jml_like) {
        this.jml_like = jml_like;
    }

    public String getstatus_like() {
        return status_like;
    }
    public void setstatus_like(String status_like) {
        this.status_like = status_like;
    }

    public String getjml_komen() {
        return jml_komen;
    }
    public void setjml_komen(String jml_komen) {
        this.jml_komen = jml_komen;
    }

    public String getkunjugan_ke() {
        return kunjugan_ke;
    }
    public void setkunjugan_ke(String kunjugan_ke) {
        this.kunjugan_ke = kunjugan_ke;
    }

    public String geturutas_st() {
        return urutas_st;
    }
    public void seturutas_st(String urutas_st) {
        this.urutas_st = urutas_st;
    }

    public String getentity() {
        return entity;
    }
    public void setentity(String entity) {
        this.entity = entity;
    }

    public String getrate9() {
        return rate9;
    }
    public void setrate9(String rate9) {
        this.rate9 = rate9;
    }

    public String getrate8() {
        return rate8;
    }
    public void setrate8(String rate8) {
        this.rate8 = rate8;
    }

    public String getrate7() {
        return rate7;
    }
    public void setrate7(String rate7) {
        this.rate7 = rate7;
    }

    public String getrate6() {
        return rate6;
    }
    public void setrate6(String rate6) {
        this.rate6 = rate6;
    }

    public String getrate5() {
        return rate5;
    }
    public void setrate5(String rate5) {
        this.rate5 = rate5;
    }

    public String getrate4() {
        return rate4;
    }
    public void setrate4(String rate4) {
        this.rate4 = rate4;
    }

    public String getrate3() {
        return rate3;
    }
    public void setrate3(String rate3) {
        this.rate3 = rate3;
    }

    public String getrate2() {
        return rate2;
    }
    public void setrate2(String rate2) {
        this.rate2 = rate2;
    }

    public String getrate1() {
        return rate1;
    }
    public void setrate1(String rate1) {
        this.rate1 = rate1;
    }

    public String getavg_rate() {
        return avg_rate;
    }
    public void setavg_rate(String avg_rate) {
        this.avg_rate = avg_rate;
    }

    public String getaqua_other() {
        return aqua_other;
    }
    public void setaqua_other(String aqua_other) {
        this.aqua_other = aqua_other;
    }

    public String getco_gallon() {
        return co_gallon;
    }
    public void setco_gallon(String co_gallon) {
        this.co_gallon = co_gallon;
    }

    public String getorder_non_kunjungan() {
        return order_non_kunjungan;
    }
    public void setorder_non_kunjungan(String order_non_kunjungan) {
        this.order_non_kunjungan = order_non_kunjungan;
    }

    public String getszName() {
        return szName;
    }
    public void setszName(String szName) {
        this.szName = szName;
    }

    public String getlevel_7() {
        return level_7;
    }
    public void setlevel_7(String level_7) {
        this.level_7 = level_7;
    }

    public String getlevel_8() {
        return level_8;
    }
    public void setlevel_8(String level_8) {
        this.level_8 = level_8;
    }

    public String getlevel_9() {
        return level_9;
    }
    public void setlevel_9(String level_9) {
        this.level_9 = level_9;
    }

    public String getqty_so() {
        return qty_so;
    }
    public void setqty_so(String qty_so) {
        this.qty_so = qty_so;
    }

    public String getqty_dist() {
        return qty_dist;
    }
    public void setqty_dist(String qty_dist) {
        this.qty_dist = qty_dist;
    }

    public String getjml_rute() {
        return jml_rute;
    }
    public void setjml_rute(String jml_rute) {
        this.jml_rute = jml_rute;
    }

    public String gettarget_ao() {
        return target_ao;
    }
    public void settarget_ao(String target_ao) {
        this.target_ao = target_ao;
    }

    public String getpolice_number() {
        return police_number;
    }
    public void setpolice_number(String police_number) {
        this.police_number = police_number;
    }

    public String getvehicle() {
        return vehicle;
    }
    public void setvehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getdiamond() {
        return diamond;
    }
    public void setdiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getplatinum() {
        return platinum;
    }
    public void setplatinum(String platinum) {
        this.platinum = platinum;
    }

    public String getgold() {
        return gold;
    }
    public void setgold(String gold) {
        this.gold = gold;
    }

    public String getsilver() {
        return silver;
    }
    public void setsilver(String silver) {
        this.silver = silver;
    }

    public String getbronze() {
        return bronze;
    }
    public void setbronze(String bronze) {
        this.bronze = bronze;
    }

    public String getwood() {
        return wood;
    }
    public void setwood(String wood) {
        this.wood = wood;
    }

    public String getnilai_klik() {
        return nilai_klik;
    }
    public void setnilai_klik(String nilai_klik) {
        this.nilai_klik = nilai_klik;
    }

    public String getfoto_sdh_diupload() {
        return foto_sdh_diupload;
    }
    public void setfoto_sdh_diupload(String foto_sdh_diupload) {
        this.foto_sdh_diupload = foto_sdh_diupload;
    }

    public String getfoto_blm_diupload() {
        return foto_blm_diupload;
    }
    public void setfoto_blm_diupload(String foto_blm_diupload) {
        this.foto_blm_diupload = foto_blm_diupload;
    }

    public String getmtd_ideal() {
        return mtd_ideal;
    }
    public void setmtd_ideal(String mtd_ideal) {
        this.mtd_ideal = mtd_ideal;
    }

    public String gettarget_cs() {
        return target_cs;
    }
    public void settarget_cs(String target_cs) {
        this.target_cs = target_cs;
    }

    public String getnot_komitmen() {
        return not_komitmen;
    }
    public void setnot_komitmen(String not_komitmen) {
        this.not_komitmen = not_komitmen;
    }

    public String gettarget_harian() {
        return target_harian;
    }
    public void settarget_harian(String target_harian) {
        this.target_harian = target_harian;
    }

    public String getmtd() {
        return mtd;
    }
    public void setmtd(String mtd) {
        this.mtd = mtd;
    }

    public String getmtd_tgl() {
        return mtd_tgl;
    }
    public void setmtd_tgl(String mtd_tgl) {
        this.mtd_tgl = mtd_tgl;
    }

    public String getbln_hr_kerja() {
        return bln_hr_kerja;
    }
    public void setbln_hr_kerja(String bln_hr_kerja) {
        this.bln_hr_kerja = bln_hr_kerja;
    }

    public String getnama_sm() {
        return nama_sm;
    }
    public void setnama_sm(String nama_sm) {
        this.nama_sm = nama_sm;
    }

    public String getid_sm() {
        return id_sm;
    }
    public void setid_sm(String id_sm) {
        this.id_sm = id_sm;
    }

    public String getnama_hos() {
        return nama_hos;
    }
    public void setnama_hos(String nama_hos) {
        this.nama_hos = nama_hos;
    }

    public String getid_hos() {
        return id_hos;
    }
    public void setid_hos(String id_hos) {
        this.id_hos = id_hos;
    }

    public String getnama_hot() {
        return nama_hot;
    }
    public void setnama_hot(String nama_hot) {
        this.nama_hot = nama_hot;
    }

    public String getid_hot() {
        return id_hot;
    }
    public void setid_hot(String id_hot) {
        this.id_hot = id_hot;
    }


    public Data_join_visit() {
    }

    public Data_join_visit(String merk1, int id_salesman) {
        this.merk1 = merk1;
        this.id_salesman = id_salesman;

    }

    public Data_join_visit(String id, String nmr_jv, String divisi) {
        this.id = id;
        this.nmr_jv = nmr_jv;
        this.divisi = divisi;

    }

    public Data_join_visit(JSONObject jsonObject){
        this.setJson(jsonObject);
        //this.setPhotoUrlFromJson();
        this.setBitmap(null);
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public Data_join_visit(ImageView imageView){
        setBitmapFromImgView(imageView);
    }

    public void setBitmapFromImgView(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable){
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }
        else {
            return;
        }
        setBitmap(bitmap);
        setUri();
    }

    public String getjawaban_soal() {
        return jawaban_soal;
    }
    public void setjawaban_soal(String jawaban_soal) {
        this.jawaban_soal = jawaban_soal;
    }

    public String getid_jawaban() {
        return id_jawaban;
    }
    public void setid_jawaban(String id_jawaban) {
        this.id_jawaban = id_jawaban;
    }

    public String getsoal() {
        return soal;
    }
    public void setsoal(String soal) {
        this.soal = soal;
    }

    public String getid_soal() {
        return id_soal;
    }
    public void setid_soal(String id_soal) {
        this.id_soal = id_soal;
    }

    public String getjml_brg_dijual() {
        return jml_brg_dijual;
    }
    public void setjml_brg_dijual(String jml_brg_dijual) {
        this.jml_brg_dijual = jml_brg_dijual;
    }

    public String geton_route() {
        return on_route;
    }
    public void seton_route(String on_route) {
        this.on_route = on_route;
    }


    public String getpost_route() {
        return post_route;
    }
    public void setpost_route(String post_route) {
        this.post_route = post_route;
    }

    public String getmarket_visit() {
        return market_visit;
    }

    public void setmarket_visit(String market_visit) {
        this.market_visit = market_visit;
    }

    public String getcoaching() {
        return coaching;
    }

    public void setcoaching(String coaching) {
        this.coaching = coaching;
    }

    public String getpre_route() {
        return pre_route;
    }
    public void setpre_route(String pre_route) {
        this.pre_route = pre_route;
    }

    public String gettarget_vol() {
        return target_vol;
    }
    public void settarget_vol(String target_vol) {
        this.target_vol = target_vol;
    }

    public String getsisa_target_bev() {
        return sisa_target_bev;
    }
    public void setsisa_target_bev(String sisa_target_bev) {
        this.sisa_target_bev = sisa_target_bev;
    }

    public String getsisa_target_vit() {
        return sisa_target_vit;
    }
    public void setsisa_target_vit(String sisa_target_vit) {
        this.sisa_target_vit = sisa_target_vit;
    }

    public String getsisa_target_aqua() {
        return sisa_target_aqua;
    }
    public void setsisa_target_aqua(String sisa_target_aqua) {
        this.sisa_target_aqua = sisa_target_aqua;
    }

    public String getjml_foto_display() {
        return jml_foto_display;
    }
    public void setjml_foto_display(String jml_foto_display) {
        this.jml_foto_display = jml_foto_display;
    }

    public String getjml_foto_stok() {
        return jml_foto_stok;
    }
    public void setjml_foto_stok(String jml_foto_stok) {
        this.jml_foto_stok = jml_foto_stok;
    }

    public String getweek_name() {
        return week_name;
    }
    public void setweek_name(String week_name) {
        this.week_name = week_name;
    }

    public String getweek_id() {
        return week_id;
    }
    public void setweek_id(String week_id) {
        this.week_id = week_id;
    }

    public String getkomitmen_aqua() {
        return komitmen_aqua;
    }
    public void setkomitmen_aqua(String komitmen_aqua) {
        this.komitmen_aqua = komitmen_aqua;
    }

    public String getkomitmen_beverage() {
        return komitmen_beverage;
    }
    public void setkomitmen_beverage(String komitmen_beverage) {
        this.komitmen_beverage = komitmen_beverage;
    }

    public String getkomitmen_vit() {
        return komitmen_vit;
    }
    public void setkomitmen_vit(String komitmen_vit) {
        this.komitmen_vit = komitmen_vit;
    }

    public String getvol_aqua() {
        return vol_aqua;
    }
    public void setvol_aqua(String vol_aqua) {
        this.vol_aqua = vol_aqua;
    }

    public String getvol_tot_order() {
        return vol_tot_order;
    }
    public void setvol_tot_order(String vol_tot_order) {
        this.vol_tot_order = vol_tot_order;
    }

    public String getvol_beverage() {
        return vol_beverage;
    }
    public void setvol_beverage(String vol_beverage) {
        this.vol_beverage = vol_beverage;
    }

    public String getvol_vit() {
        return vol_vit;
    }
    public void setvol_vit(String vol_vit) {
        this.vol_vit = vol_vit;
    }

    public String getjenis_item3() {
        return jenis_item3;
    }
    public void setjenis_item3(String jenis_item3) {
        this.jenis_item3 = jenis_item3;
    }

    public String getstatus_istirahat() {
        return status_istirahat;
    }
    public void setstatus_istirahat(String status_istirahat) {
        this.status_istirahat = status_istirahat;
    }

    public String getakhir_istirahat() {
        return akhir_istirahat;
    }
    public void setakhir_istirahat(String akhir_istirahat) {
        this.akhir_istirahat = akhir_istirahat;
    }

    public String getmulai_istirahat() {
        return mulai_istirahat;
    }
    public void setmulai_istirahat(String mulai_istirahat) {
        this.mulai_istirahat = mulai_istirahat;
    }

    public String getszFailReason() {
        return szFailReason;
    }
    public void setszFailReason(String szFailReason) {
        this.szFailReason = szFailReason;
    }

    public String getid_reason() {
        return id_reason;
    }
    public void setid_reason(String id_reason) {
        this.id_reason = id_reason;
    }

    public String getmerk1() {
        return merk1;
    }
    public void setmerk1(String merk1) {
        this.merk1 = merk1;
    }

    public String getmerk2() {
        return merk2;
    }
    public void setmerk2(String merk2) {
        this.merk2 = merk2;
    }

    public String getmerk3() {
        return merk3;
    }
    public void setmerk3(String merk3) {
        this.merk3 = merk3;
    }

    public String getmerk4() {
        return merk4;
    }
    public void setmerk4(String merk4) {
        this.merk4 = merk4;
    }

    public String getpromo1() {
        return promo1;
    }
    public void setpromo1(String promo1) {
        this.promo1 = promo1;
    }

    public String getpromo2() {
        return promo2;
    }
    public void setpromo2(String promo2) {
        this.promo2 = promo2;
    }

    public String getpromo3() {
        return promo3;
    }
    public void setpromo3(String promo3) {
        this.promo3 = promo3;
    }

    public String getpromo4() {
        return promo4;
    }
    public void setpromo4(String promo4) {
        this.promo4 = promo4;
    }

    public String getnama_program() {
        return nama_program;
    }
    public void setnama_program(String nama_program) {
        this.nama_program = nama_program;
    }

    public String getupdate_by() {
        return update_by;
    }
    public void setupdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getdate_update() {
        return date_update;
    }
    public void setdate_update(String date_update) {
        this.date_update = date_update;
    }

    public String getjam_akhir() {
        return jam_akhir;
    }
    public void setjam_akhir(String jam_akhir) {
        this.jam_akhir = jam_akhir;
    }

    public String getjam_awal() {
        return jam_awal;
    }
    public void setjam_awal(String jam_awal) {
        this.jam_awal = jam_awal;
    }

    public String getkomplain() {
        return komplain;
    }
    public void setkomplain(String komplain) {
        this.komplain = komplain;
    }

    public String getharga() {
        return harga;
    }
    public void setharga(String harga) {
        this.harga = harga;
    }

    public String getprogram_terakhir() {
        return program_terakhir;
    }
    public void setprogram_terakhir(String program_terakhir) {
        this.program_terakhir = program_terakhir;
    }

    public String getszCustId() {
        return szCustId;
    }
    public void setszCustId(String szCustId) {
        this.szCustId = szCustId;
    }

    public String getszDocId() {
        return szDocId;
    }
    public void setszDocId(String szDocId) {
        this.szDocId = szDocId;
    }

    public String getphone() {
        return phone;
    }
    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getstatus_insert() {
        return status_insert;
    }
    public void setstatus_insert(String status_insert) {
        this.status_insert = status_insert;
    }

    public String getlong_cust() {
        return long_cust;
    }
    public void setlong_cust(String long_cust) {
        this.long_cust = long_cust;
    }

    public String getlat_cust() {
        return lat_cust;
    }
    public void setlat_cust(String lat_cust) {
        this.lat_cust = lat_cust;
    }

    public String getalamat_cust() {
        return alamat_cust;
    }
    public void setalamat_cust(String alamat_cust) {
        this.alamat_cust = alamat_cust;
    }

    public String getout_arena() {return out_arena;}
    public void setout_arena(String out_arena) {this.out_arena = out_arena; }

    public String gettoko_tutup() {return toko_tutup;}
    public void settoko_tutup(String toko_tutup) {this.toko_tutup = toko_tutup; }

    public String getout_of_route() {
        return out_of_route;
    }
    public void setout_of_route(String out_of_route) {
        this.out_of_route = out_of_route;
    }

    public String getcps() {return cps;}
    public void setcps(String cps) {this.cps = cps;}

    public String getcp() {return cp;}
    public void setcp(String cp) {this.cp = cp; }

    public String getqty_order() {
        return qty_order;
    }
    public void setqty_order(String qty_order) {
        this.qty_order = qty_order;
    }

    public String getdt_base() {
        return dt_base;
    }
    public void setdt_base(String dt_base) {
        this.dt_base = dt_base;
    }

    public String getid_employee() {
        return id_employee;
    }
    public void setid_employee(String id_employee) {
        this.id_employee = id_employee;
    }

    public String getjml_outlet() {
        return jml_outlet;
    }
    public void setjml_outlet(String jml_outlet) {
        this.jml_outlet = jml_outlet;
    }

    public String getco_bev() {
        return co_bev;
    }
    public void setco_bev(String co_bev) {
        this.co_bev = co_bev;
    }

    public String getco_vit() {
        return co_vit;
    }
    public void setco_vit(String co_vit) {
        this.co_vit = co_vit;
    }

    public String getco_aqua() {
        return co_aqua;
    }
    public void setco_aqua(String co_aqua) {
        this.co_aqua = co_aqua;
    }

    public String getco_all() {
        return co_all;
    }
    public void setco_all(String co_all) {
        this.co_all = co_all;
    }

    public String getjml_cust() {
        return jml_cust;
    }
    public void setjml_cust(String jml_cust) {
        this.jml_cust = jml_cust;
    }

    public String getdurasi() {
        return durasi;
    }
    public void setdurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getjml_kunjungan() {
        return jml_kunjungan;
    }
    public void setjml_kunjungan(String jml_kunjungan) {
        this.jml_kunjungan = jml_kunjungan;
    }

    public String getrp_order() {
        return rp_order;
    }
    public void setrp_order(String rp_order) {
        this.rp_order = rp_order;
    }

    public String getjml_order() {
        return jml_order;
    }
    public void setjml_order(String jml_order) {
        this.jml_order = jml_order;
    }

    public String getnama_barang() {
        return nama_barang;
    }
    public void setnama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getid_customer() {
        return id_customer;
    }
    public void setid_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getstatus_audit() {
        return status_audit;
    }
    public void setstatus_audit(String status_audit) {
        this.status_audit = status_audit;
    }

    public String getketerangan_audit() {
        return keterangan_audit;
    }
    public void setketerangan_audit(String keterangan_audit) {
        this.keterangan_audit = keterangan_audit;
    }

    public String getnmr_jv() {
        return nmr_jv;
    }
    public void setnmr_jv(String nmr_jv) {
        this.nmr_jv = nmr_jv;
    }

    public String getdivisi() {
        return divisi;
    }
    public void setdivisi(String divisi) {
        this.divisi = divisi;
    }

    public int getid_salesman() {
        return id_salesman;
    }
    public void setid_salesman(int id_salesman) {
        this.id_salesman = id_salesman;
    }

    public String getnama_salesman() {
        return nama_salesman;
    }
    public void setnama_salesman(String nama_salesman) {
        this.nama_salesman = nama_salesman;
    }

    public String getid_srt_tgs() {
        return id_srt_tgs;
    }
    public void setid_srt_tgs(String id_srt_tgs) {
        this.id_srt_tgs = id_srt_tgs;
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

    public String getnama_customer() {
        return nama_customer;
    }
    public void senama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }

    public String gethari() {
        return hari;
    }
    public void sethari(String hari) {
        this.hari = hari;
    }

    public String getid_foto() {
        return id_foto;
    }
    public void setid_foto(String id_foto) {
        this.id_foto = id_foto;
    }

    public String getjv_foto() {
        return jv_foto;
    }
    public void setjv_foto(String jv_foto) {
        this.jv_foto = jv_foto;
    }

    public String getkategori_foto() {
        return kategori_foto;
    }
    public void setkategori_foto(String kategori_foto) {
        this.kategori_foto = kategori_foto;
    }

    public String gettgl_foto() {
        return tgl_foto;
    }
    public void settgl_foto(String tgl_foto) {
        this.tgl_foto = tgl_foto;
    }

    public void setUri(Uri uri) {
        Log.d("PHOTO URI", uri.toString());
        this.uri = uri;
    }

    public void setUri(){
        Uri bitmapUri = null;
        try {
            File file = new File(this.getPhotoDir(), Api.APP_NAME + System.currentTimeMillis() + ".jpg");
            file.getParentFile().mkdirs();
            FileOutputStream outStream = new FileOutputStream(file);
            getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.close();
            bitmapUri = Uri.fromFile(file);
            setUri(bitmapUri);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static File getPhotoDir(){
        File photoDir = new File(Api.getFileImagePath());
        if(!photoDir.exists()){
            photoDir.mkdir();
        }
        return photoDir;
    }

    public Uri getUri() {
        return uri;
    }

    public void setBitmap(Bitmap bitmap) {
        if(this.bitmap != null && !this.bitmap.isRecycled()){
            this.bitmap.recycle();
            this.bitmap = null;
        }

        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String gethari_jv() {
        return hari_jv;
    }

    public void sethari_jv(String hari_jv) {
        this.hari_jv = hari_jv;
    }


    public String getdepo() {
        return depo;
    }
    public void setdepo(String depo) {
        this.depo = depo;
    }

    public String getid_depo() {
        return id_depo;
    }
    public void setid_depo(String id_depo) {
        this.id_depo = id_depo;
    }

    public String getasal2() {
        return asal2;
    }
    public void setasal2(String asal2) {
        this.asal2 = asal2;
    }


    /*Comparator for sorting the list by Avg Rate*/
    public static Comparator<Data_join_visit> AvgSortAcs = new Comparator<Data_join_visit>() {

        public int compare(Data_join_visit s1, Data_join_visit s2) {
            // sort by name (abjad) menggunakan tipe string
            //String AvgRate1 = s1.getavg_rate().toUpperCase();
            //String AvgRate2 = s2.getavg_rate().toUpperCase();
            int AvgRate1 = Integer.valueOf(s1.getavg_rate()) ;
            int AvgRate2 = Integer.valueOf(s2.getavg_rate());

            //ascending order
            //contoh sort by nama (abjad)return AvgRate1.compareTo(AvgRate2);
            return AvgRate1-AvgRate2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

    public static Comparator<Data_join_visit> AvgSortDecs = new Comparator<Data_join_visit>() {

        public int compare(Data_join_visit s1, Data_join_visit s2) {
            //String AvgRate1 = s1.getavg_rate().toUpperCase();
            //String AvgRate2 = s2.getavg_rate().toUpperCase();
            int AvgRate1 = Integer.valueOf(s1.getavg_rate()) ;
            int AvgRate2 = Integer.valueOf(s2.getavg_rate());

            //ascending order
            //return AvgRate1.compareTo(AvgRate2);

            //descending order
            //return AvgRate2.compareTo(AvgRate1);
            return AvgRate2-AvgRate1;
        }
    };
}
