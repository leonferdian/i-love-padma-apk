package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_customer implements Serializable {
    private int id, index;
    private String id_external, contact_person, land_area, building_area, floor, status, website, building, room_number,
            kelurahan, kecamatan, zip_code, province, country, register_date, contract_date;
    private String name, address, phone, city, email,image_customer;
    private String owner_name, owner_birth_place, owner_birth_date, owner_gender, owner_address, owner_keluarahan,
            owner_kecamatan, owner_zipcode, owner_city, owner_province, owner_country, owner_religion, owner_status,
            owner_work, owner_nationality, owner_nik, owner_phone, owner_email;
    private double credit_limit, payment_tempo, omzet;
    private int total_employee, total_carrier;
    private String invoice_processing, product_selling, sell_area, type_carrier;
    private String saluran_distribusi, segment_level_1, segment_level_2, outlet_type, chain_store, division,code_customer;
    private String LastAudit = "";
    private boolean can_credit;
    private double lat, lng;
    //for survey
    private String jenis_supplier,nama_supplier,brand,date_create,create_by,id_survey;
    private String sku,price,qty_per_week,qty_per_month,amount_per_month;
    private String sku2,price2,qty_per_week2,qty_per_month2,amount_per_month2;
    private String sku3,price3,qty_per_week3,qty_per_month3,amount_per_month3;
    private String sku4,price4,qty_per_week4,qty_per_month4,amount_per_month4;
    private String sku5,price5,qty_per_week5,qty_per_month5,amount_per_month5;
    private String ahs_l1,ahs_l2,ahs_jawaban;
    //for dms3
    private String szPaymetTermId,bAllowToCredit,branch_id,dt_base;
    private String iId,intItemNumber,allow_credit,szDocSO,koreksi_alamat;



    //for outlet semesta
    private String status_general,status_detail;

    public Data_customer() {

    }

    public Data_customer(int id, String name){
        this.setId(id);
        this.setName(name);

    }

    public Data_customer(int id, String name, String address){
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
    }

    public Data_customer(String name){
        this.setName(name);
    }

    public String getkoreksi_alamat() {
        return koreksi_alamat;
    }
    public void setkoreksi_alamat(String koreksi_alamat) {
        this.koreksi_alamat = koreksi_alamat;
    }

    public String getszDocSO() {
        return szDocSO;
    }
    public void setszDocSO(String szDocSO) {
        this.szDocSO = szDocSO;
    }

    public String getintItemNumber() {
        return intItemNumber;
    }
    public void setintItemNumber(String intItemNumber) {
        this.intItemNumber = intItemNumber;
    }

    public String getallow_credit() {
        return allow_credit;
    }
    public void setallow_credit(String allow_credit) {
        this.allow_credit = allow_credit;
    }

    public String getiId() {
        return iId;
    }
    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getdt_base() {
        return dt_base;
    }
    public void setdt_base(String dt_base) {
        this.dt_base = dt_base;
    }

    public String getbranch_id() {
        return branch_id;
    }
    public void setbranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getbAllowToCredit() {
        return bAllowToCredit;
    }
    public void setbAllowToCredit(String bAllowToCredit) {
        this.bAllowToCredit = bAllowToCredit;
    }

    public String getszPaymetTermId() {
        return szPaymetTermId;
    }
    public void setszPaymetTermId(String szPaymetTermId) {
        this.szPaymetTermId = szPaymetTermId;
    }

    public String getahs_jawaban() {
        return ahs_jawaban;
    }
    public void setahs_jawaban(String ahs_jawaban) {
        this.ahs_jawaban = ahs_jawaban;
    }

    public String getahs_l2() {
        return ahs_l2;
    }
    public void setahs_l2(String ahs_l2) {
        this.ahs_l2 = ahs_l2;
    }

    public String getahs_l1() {
        return ahs_l1;
    }
    public void setahs_l1(String ahs_l1) {
        this.ahs_l1 = ahs_l1;
    }

    public String getcode_customer() {
        return code_customer;
    }
    public void setcode_customer(String code_customer) {
        this.code_customer = code_customer;
    }

    public String getimage_customer() {
        return image_customer;
    }
    public void setimage_customer(String image_customer) {
        this.image_customer = image_customer;
    }

    public String getLastAudit() {
        return LastAudit;
    }
    public void setLastAudit(String lastAudit) {
        LastAudit = lastAudit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }



    public String getLabelSearch(){
        String label = "";
        if (!this.getName().isEmpty()){
            label += this.getName();
        }

        if (!this.getAddress().isEmpty()){
            label += " ";
            label += this.getAddress();
        }
        return label;
    }

    public String getId_external() {
        return id_external;
    }

    public void setId_external(String id_external) {
        this.id_external = id_external;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getLand_area() {
        return land_area;
    }

    public void setLand_area(String land_area) {
        this.land_area = land_area;
    }

    public String getBuilding_area() {
        return building_area;
    }

    public void setBuilding_area(String building_area) {
        this.building_area = building_area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getContract_date() {
        return contract_date;
    }

    public void setContract_date(String contract_date) {
        this.contract_date = contract_date;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_birth_place() {
        return owner_birth_place;
    }

    public void setOwner_birth_place(String owner_birth_place) {
        this.owner_birth_place = owner_birth_place;
    }

    public String getOwner_birth_date() {
        return owner_birth_date;
    }

    public void setOwner_birth_date(String owner_birth_date) {
        this.owner_birth_date = owner_birth_date;
    }

    public String getOwner_gender() {
        return owner_gender;
    }

    public void setOwner_gender(String owner_gender) {
        this.owner_gender = owner_gender;
    }

    public String getOwner_address() {
        return owner_address;
    }

    public void setOwner_address(String owner_address) {
        this.owner_address = owner_address;
    }

    public String getOwner_keluarahan() {
        return owner_keluarahan;
    }

    public void setOwner_keluarahan(String owner_keluarahan) {
        this.owner_keluarahan = owner_keluarahan;
    }

    public String getOwner_kecamatan() {
        return owner_kecamatan;
    }

    public void setOwner_kecamatan(String owner_kecamatan) {
        this.owner_kecamatan = owner_kecamatan;
    }

    public String getOwner_zipcode() {
        return owner_zipcode;
    }

    public void setOwner_zipcode(String owner_zipcode) {
        this.owner_zipcode = owner_zipcode;
    }

    public String getOwner_city() {
        return owner_city;
    }

    public void setOwner_city(String owner_city) {
        this.owner_city = owner_city;
    }

    public String getOwner_province() {
        return owner_province;
    }

    public void setOwner_province(String owner_province) {
        this.owner_province = owner_province;
    }

    public String getOwner_country() {
        return owner_country;
    }

    public void setOwner_country(String owner_country) {
        this.owner_country = owner_country;
    }

    public String getOwner_religion() {
        return owner_religion;
    }

    public void setOwner_religion(String owner_religion) {
        this.owner_religion = owner_religion;
    }

    public String getOwner_status() {
        return owner_status;
    }

    public void setOwner_status(String owner_status) {
        this.owner_status = owner_status;
    }

    public String getOwner_work() {
        return owner_work;
    }

    public void setOwner_work(String owner_work) {
        this.owner_work = owner_work;
    }

    public String getOwner_nationality() {
        return owner_nationality;
    }

    public void setOwner_nationality(String owner_nationality) {
        this.owner_nationality = owner_nationality;
    }

    public String getOwner_nik() {
        return owner_nik;
    }

    public void setOwner_nik(String owner_nik) {
        this.owner_nik = owner_nik;
    }

    public String getOwner_phone() {
        return owner_phone;
    }

    public void setOwner_phone(String owner_phone) {
        this.owner_phone = owner_phone;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public double getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(double credit_limit) {
        this.credit_limit = credit_limit;
    }

    public double getPayment_tempo() {
        return payment_tempo;
    }

    public void setPayment_tempo(double payment_tempo) {
        this.payment_tempo = payment_tempo;
    }

    public double getOmzet() {
        return omzet;
    }

    public void setOmzet(double omzet) {
        this.omzet = omzet;
    }

    public int getTotal_employee() {
        return total_employee;
    }

    public void setTotal_employee(int total_employee) {
        this.total_employee = total_employee;
    }

    public String getInvoice_processing() {
        return invoice_processing;
    }

    public void setInvoice_processing(String invoice_processing) {
        this.invoice_processing = invoice_processing;
    }

    public String getProduct_selling() {
        return product_selling;
    }

    public void setProduct_selling(String product_selling) {
        this.product_selling = product_selling;
    }

    public int getTotal_carrier() {
        return total_carrier;
    }

    public void setTotal_carrier(int total_carrier) {
        this.total_carrier = total_carrier;
    }

    public String getSell_area() {
        return sell_area;
    }

    public void setSell_area(String sell_area) {
        this.sell_area = sell_area;
    }

    public boolean isCan_credit() {
        return can_credit;
    }

    public void setCan_credit(boolean can_credit) {
        this.can_credit = can_credit;
    }

    public String getType_carrier() {
        return type_carrier;
    }

    public void setType_carrier(String type_carrier) {
        this.type_carrier = type_carrier;
    }

    public String getSaluran_distribusi() {
        return saluran_distribusi;
    }

    public void setSaluran_distribusi(String saluran_distribusi) {
        this.saluran_distribusi = saluran_distribusi;
    }

    public String getSegment_level_1() {
        return segment_level_1;
    }

    public void setSegment_level_1(String segment_level_1) {
        this.segment_level_1 = segment_level_1;
    }

    public String getSegment_level_2() {
        return segment_level_2;
    }

    public void setSegment_level_2(String segment_level_2) {
        this.segment_level_2 = segment_level_2;
    }

    public String getOutlet_type() {
        return outlet_type;
    }

    public void setOutlet_type(String outlet_type) {
        this.outlet_type = outlet_type;
    }

    public String getChain_store() {
        return chain_store;
    }

    public void setChain_store(String chain_store) {
        this.chain_store = chain_store;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getjenis_supplier() {
        return jenis_supplier;
    }
    public void setjenis_supplier(String jenis_supplier) {
        this.jenis_supplier = jenis_supplier;
    }

    public String getnama_supplierr() {
        return nama_supplier;
    }
    public void setnama_supplier(String nama_supplier) {
        this.nama_supplier = nama_supplier;
    }

    public String getbrand() {
        return brand;
    }
    public void setbrand(String brand) {
        this.brand = brand;
    }

    public String getsku() {
        return sku;
    }
    public void setsku(String sku) {
        this.sku = sku;
    }

    public String getsku2() {
        return sku2;
    }
    public void setsku2(String sku2) {
        this.sku2 = sku2;
    }

    public String getsku3() {
        return sku3;
    }
    public void setsku3(String sku3) {
        this.sku3 = sku3;
    }

    public String getsku4() {
        return sku4;
    }
    public void setsku4(String sku4) {
        this.sku4 = sku4;
    }

    public String getsku5() {
        return sku5;
    }
    public void setsku5(String sku5) {
        this.sku5 = sku5;
    }

    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }

    public String getprice2() {
        return price2;
    }
    public void setprice2(String price2) {
        this.price2 = price2;
    }

    public String getprice3() {
        return price3;
    }
    public void setprice3(String price3) {
        this.price3 = price3;
    }

    public String getprice4() {
        return price4;
    }
    public void setprice4(String price4) {
        this.price4 = price4;
    }

    public String getprice5() {
        return price5;
    }
    public void setprice5(String price5) {
        this.price5 = price5;
    }

    public String getqty_per_week() {
        return qty_per_week;
    }
    public void setqty_per_week(String qty_per_week) {
        this.qty_per_week = qty_per_week;
    }

    public String getqty_per_week2() {
        return qty_per_week2;
    }
    public void setqty_per_week2(String qty_per_week2) {
        this.qty_per_week2 = qty_per_week2;
    }

    public String getqty_per_week3() {
        return qty_per_week3;
    }
    public void setqty_per_week3(String qty_per_week3) {
        this.qty_per_week3 = qty_per_week3;
    }

    public String getqty_per_week4() {
        return qty_per_week4;
    }
    public void setqty_per_week4(String qty_per_week4) {
        this.qty_per_week4 = qty_per_week4;
    }

    public String getqty_per_week5() {
        return qty_per_week5;
    }
    public void setqty_per_week5(String qty_per_week5) {
        this.qty_per_week5 = qty_per_week5;
    }

    public String getqty_per_month() {
        return qty_per_month;
    }
    public void setqty_per_month(String qty_per_month) {
        this.qty_per_month = qty_per_month;
    }

    public String getqty_per_month2() {
        return qty_per_month2;
    }
    public void setqty_per_month2(String qty_per_month2) {
        this.qty_per_month2 = qty_per_month2;
    }

    public String getqty_per_month3() {
        return qty_per_month3;
    }
    public void setqty_per_month3(String qty_per_month3) {
        this.qty_per_month3 = qty_per_month3;
    }

    public String getqty_per_month4() {
        return qty_per_month4;
    }
    public void setqty_per_month4(String qty_per_month4) {
        this.qty_per_month4 = qty_per_month4;
    }

    public String getqty_per_month5() {
        return qty_per_month5;
    }
    public void setqty_per_month5(String qty_per_month5) {
        this.qty_per_month5 = qty_per_month5;
    }

    public String getamount_per_month() {
        return amount_per_month;
    }
    public void setamount_per_month(String amount_per_month) {
        this.amount_per_month = amount_per_month;
    }

    public String getamount_per_month2() {
        return amount_per_month2;
    }
    public void setamount_per_month2(String amount_per_month2) {
        this.amount_per_month2 = amount_per_month2;
    }

    public String getamount_per_month3() {
        return amount_per_month3;
    }
    public void setamount_per_month3(String amount_per_month3) {
        this.amount_per_month3 = amount_per_month3;
    }

    public String getamount_per_month4() {
        return amount_per_month4;
    }
    public void setamount_per_month4(String amount_per_month4) {
        this.amount_per_month4 = amount_per_month4;
    }

    public String getamount_per_month5() {
        return amount_per_month5;
    }
    public void setamount_per_month5(String amount_per_month5) {
        this.amount_per_month5 = amount_per_month5;
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

    public String getid_survey() {
        return id_survey;
    }
    public void setid_survey(String id_survey) {
        this.id_survey = id_survey;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }

    public String getStatus_general() {
        return status_general;
    }

    public void setStatus_general(String status_general) {
        this.status_general = status_general;
    }
}
