package com.project45.ilovepadma.timeline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_join_visit;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class detail_timeline extends AppCompatActivity {

    private static String url_get_brg_dijual     = Server.URL + "sfa_leader/get_brg_dijual/";
    private static String url_get_so     = Server.URL + "sfa_leader/get_list_so/";
    private static String url_get_phone     = Server.URL + "sfa_leader/get_outlet_phone/";
    private static String url_get_foto_display     = Server.URL + "sfa_leader/get_foto_display/";
    private static String url_get_cek_program     = Server.URL + "sfa_leader/get_cek_program/";
    private static String url_get_akt_kompetitor     = Server.URL + "sfa_leader/get_akt_kompetitor/";
    private static String url_get_toko_tutup     = Server.URL + "sfa/join_visit/get_toko_tutup/";

    private static final String TAG = detail_timeline.class.getSimpleName();

    String id_user, username,bm,email,employee_id_dms3,db_user,szName,id_customer,cust_name,szDocId,so_order,alamat_outlet="",display_image="",
            status_toko_tutup,status_out_arena,lat_cust,long_cust;
    EditText txt_idcust,txt_nama_cust;
    TableLayout tabel_1,tabel_2,tabel_3,tabel_4,tabel_5;

    List<Data_join_visit> itemList = new ArrayList<Data_join_visit>();
    TextView txt_sales_name,txt_idoutlet,txt_alamat_outlet,txt_hpoutlet,txt_alamat_seharusnya;
    ImageView image_cartlist;
    LinearLayout lay_btn1,lay_btn2,lay_btn3;
    LinearLayout layout_toko_tutup;
    ImageView image_toko_tutup;

    int success;
    String szLongitude="",szLatitude="",szPhone2="",szAddress="",tgl_buat="",test_date2 = "2020-02-11";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_timeline);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_det_timeline);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);

        tabel_1 =  findViewById(R.id.tabel_1);
        tabel_2 =  findViewById(R.id.tabel_2);
        tabel_3 =  findViewById(R.id.tabel_3);
        tabel_4 =  findViewById(R.id.tabel_4);
        tabel_5 =  findViewById(R.id.tabel_5);
        txt_idcust =  findViewById(R.id.txt_idcust);
        txt_nama_cust =  findViewById(R.id.txt_nama_cust);
        txt_sales_name =  findViewById(R.id.txt_sales_name);
        txt_idoutlet =  findViewById(R.id.txt_idoutlet);
        txt_alamat_outlet =  findViewById(R.id.txt_alamat_outlet);
        txt_hpoutlet =  findViewById(R.id.txt_hpoutlet);
        image_cartlist = findViewById(R.id.image_cartlist);
        lay_btn1 = findViewById(R.id.lay_btn1);
        lay_btn2 = findViewById(R.id.lay_btn2);
        lay_btn3 = findViewById(R.id.lay_btn3);
        layout_toko_tutup = findViewById(R.id.layout_toko_tutup);
        image_toko_tutup = findViewById(R.id.image_toko_tutup);
        txt_alamat_seharusnya = findViewById(R.id.txt_alamat_seharusnya);

        Intent intentku = getIntent(); // gets the previously created intent
        id_customer = intentku.getStringExtra("id_customer");
        cust_name = intentku.getStringExtra("cust_name");
        szDocId = intentku.getStringExtra("szDocId");
        so_order = intentku.getStringExtra("so_order");
        alamat_outlet = intentku.getStringExtra("alamat_outlet");
        display_image = intentku.getStringExtra("display_image");
        status_toko_tutup = intentku.getStringExtra("status_toko_tutup");
        status_out_arena = intentku.getStringExtra("status_out_arena");
        lat_cust = intentku.getStringExtra("lat_cust");
        long_cust = intentku.getStringExtra("long_cust");
        tgl_buat = intentku.getStringExtra("tgl_buat");

        if(status_toko_tutup.equals("yes")){
            layout_toko_tutup.setVisibility(View.VISIBLE);
            tabel_1.setVisibility(View.GONE);
            tabel_2.setVisibility(View.GONE);
            tabel_3.setVisibility(View.GONE);
            tabel_4.setVisibility(View.GONE);
            tabel_5.setVisibility(View.GONE);
        }

        if(status_out_arena.equals("yes")){
            lay_btn3.setVisibility(View.VISIBLE);
            txt_alamat_seharusnya.setVisibility(View.VISIBLE);
        }

        if(so_order.equals("0")){
            tabel_2.setVisibility(View.GONE);
        }

        lay_btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                if(!szPhone2.equals("")) {
                    //Toast.makeText(detail_timeline.this, szPhone2.substring(0,1), Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setPackage("com.whatsapp");
                    intent.setData(Uri.parse("tel:"+szPhone2));
                    startActivity(intent);
                    */
                    String contact;
                    if(szPhone2.substring(0,1).equals("0")){
                        contact = "62"+szPhone2.substring(1,12); // use country code with your phone number
                    }
                    else{
                        contact = szPhone2;
                    }

                    String url = "https://api.whatsapp.com/send?phone=" + contact;
                    try {
                        PackageManager pm = getApplicationContext().getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(detail_timeline.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Outlet tidak memiliki no telp ", Toast.LENGTH_LONG).show();
                }

            }
        });

        lay_btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                if(!szLatitude.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://maps.google.com/?q=" + szLatitude + "," + szLongitude));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Outlet tidak memiliki kordinat ", Toast.LENGTH_LONG).show();
                }

            }
        });

        lay_btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                if(!szLatitude.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.es/maps/dir/" + szLatitude + "," + szLongitude+"/"+lat_cust+","+long_cust));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Outlet tidak memiliki kordinat ", Toast.LENGTH_LONG).show();
                }

            }
        });
        //Toast.makeText(getApplicationContext(), "szDocId "+szDocId, Toast.LENGTH_LONG).show();

        txt_idcust.setText(id_customer);
        txt_nama_cust.setText(cust_name);
        txt_sales_name.setText(cust_name);
        txt_idoutlet.setText(id_customer);
        txt_alamat_outlet.setText(alamat_outlet);
        final String img_outlet = Server.URL2 + "image_upload_sfa/list_foto_outlet/" + display_image;
        Picasso.with(image_cartlist.getContext())
                .load(img_outlet)
                .fit()
                .placeholder(R.drawable.ic_camera_alt_black_48dp)
                //.resize(100, 100)
                //.onlyScaleDown()
                .centerCrop()
                .into(image_cartlist);

        Zoomy.Builder builder = new Zoomy.Builder(detail_timeline.this)
                .target(image_cartlist)
                .interpolator(new OvershootInterpolator());

        builder.register();

        if(status_toko_tutup.equals("yes")){
            get_toko_tutup();
        }
        else {
            get_brg_dijual(szDocId, id_customer);
            get_data_kunjungan2(id_customer);
        }

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();

        return true;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.page_join_visit));
        }
    }

    private void get_data_kunjungan2(final String id_cust){


        String url_edit       = url_get_phone+id_cust;
        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get Data phone", jObj.toString());
                        //tab general
                        szPhone2      = jObj.getString("szPhone2");
                        szLongitude    = jObj.getString("szLongitude");
                        szLatitude    = jObj.getString("szLatitude");
                        szAddress = jObj.getString("alamat");

                        txt_hpoutlet.setText(szPhone2);
                        txt_alamat_seharusnya.setText("Alamat seharusnya : "+szAddress+", "+jObj.getString("kelurahan")+", "+jObj.getString("kecamatan")+", "+jObj.getString("kota"));


                    } else {
                        Toast.makeText(detail_timeline.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        txt_hpoutlet.setText("-");
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    txt_hpoutlet.setText("-");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_timeline.this, error.getMessage(), Toast.LENGTH_LONG).show();
                txt_hpoutlet.setText("-");
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private void get_brg_dijual(final String nmr_jv, final String id_cust) {

        itemList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(detail_timeline.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading BRG Dijual");
        progressDialog.show();

        String url_server = url_get_brg_dijual+nmr_jv+"/"+id_cust;

        Log.d("list_brg", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_join_visit jv = new Data_join_visit();
                            jv.setId(obj.getString("nmr"));
                            jv.setnama_barang(obj.getString("nama_barang"));
                            jv.setnama_program(obj.getString("nama_file"));
                            jv.setjml_foto_stok(obj.getString("stok"));
                            itemList.add(jv);

                            final String sku = obj.getString("nama_barang");
                            final String file_foto = obj.getString("nama_file");

                            TableRow row=new TableRow(detail_timeline.this);
                            row.setBackgroundColor(getResources().getColor(R.color.white));

                            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params.setMargins(3, 1, 3, 1);
                            //params.height = 110;

                            TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            paramsImgOutlet.setMargins(3, 1, 3, 1);
                            paramsImgOutlet.height=80;
                            paramsImgOutlet.width=80;

                            TextView txt_nmr=new TextView(detail_timeline.this);
                            txt_nmr.setText(""+obj.getString("nmr"));
                            txt_nmr.setBackgroundResource(R.color.page_join_visit);
                            txt_nmr.setTextColor(getResources().getColor(R.color.white));
                            txt_nmr.setTextSize(10);
                            txt_nmr.setGravity(Gravity.CENTER);
                            txt_nmr.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_nmr, params);

                            TextView txt_brg=new TextView(detail_timeline.this);
                            txt_brg.setText(""+obj.getString("nama_barang"));
                            txt_brg.setBackgroundResource(R.color.page_join_visit);
                            txt_brg.setTextColor(getResources().getColor(R.color.white));
                            txt_brg.setTextSize(10);
                            txt_brg.setGravity(Gravity.CENTER);
                            txt_brg.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_brg, params);

                            TextView txt_stok=new TextView(detail_timeline.this);
                            txt_stok.setText(""+obj.getString("stok"));
                            txt_stok.setBackgroundResource(R.color.page_join_visit);
                            txt_stok.setTextColor(getResources().getColor(R.color.white));
                            txt_stok.setTextSize(10);
                            txt_stok.setGravity(Gravity.CENTER);
                            txt_stok.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_stok, params);

                            SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String img="";
                            try {
                                Date date = format_date.parse(test_date2);
                                Date date2 = format_date.parse(tgl_buat);
                                if(date2.before(date)){
                                    //Toast.makeText(getApplicationContext(), "Kemarin ",Toast.LENGTH_LONG).show();
                                    img = Server.URL2 + "image_upload_sfa/list_foto_join_visit/"+obj.getString("nama_file");
                                    Log.d("photo_stock", String.valueOf(img));
                                }
                                else{
                                    //Toast.makeText(getApplicationContext(), "Setelah ",Toast.LENGTH_LONG).show();
                                    img = Server.URL3 + "image_upload_sfa/list_foto_join_visit/"+obj.getString("nama_file");
                                    Log.d("photo_stock", String.valueOf(img));
                                }
                            }
                            catch (ParseException e) {
                                e.printStackTrace();
                            }

                            ImageView imgOutlet = new ImageView(detail_timeline.this);
                            imgOutlet.setBackgroundResource(R.color.page_join_visit);
                            Picasso.with(imgOutlet.getContext())
                                    .load(img)
                                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                                    .fit().centerCrop().into(imgOutlet);

                            imgOutlet.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(detail_timeline.this, detail_foto.class);
                                    intent.putExtra("sku", sku);
                                    intent.putExtra("file_foto", file_foto);
                                    intent.putExtra("tgl_buat", tgl_buat);
                                    intent.putExtra("act", "kunjungan");
                                    //startActivity(intent);
                                    startActivityForResult(intent, 1);
                                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                                }
                            });
                            // add to row
                            row.addView(imgOutlet, paramsImgOutlet);

                            tabel_1.addView(row);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    get_so(szDocId,id_customer);
                }
                else{
                    Toast.makeText(detail_timeline.this, "Barang Dijual Kosong ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    get_so(szDocId,id_customer);
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_timeline.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
        // progressDialog.dismiss();
    }

    private void get_so(final String nmr_jv, final String id_cust) {

        itemList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(detail_timeline.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading SO");
        progressDialog.show();

        String url_server = url_get_so+nmr_jv+"/"+id_cust;

        Log.d("list_so", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    int tot_order = 0;
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_join_visit jv = new Data_join_visit();
                            jv.setId(obj.getString("nmr"));
                            jv.setid_foto(obj.getString("id_product"));
                            jv.setnama_barang(obj.getString("jenis_item3"));
                            jv.setvol_tot_order(obj.getString("qty"));
                            itemList.add(jv);

                            tot_order += Integer.valueOf(obj.getString("qty"));

                            TableRow row=new TableRow(detail_timeline.this);
                            row.setBackgroundColor(getResources().getColor(R.color.white));

                            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params.setMargins(3, 1, 3, 1);
                            //params.height = 110;

                            TextView txt_nmr=new TextView(detail_timeline.this);
                            txt_nmr.setText(""+obj.getString("nmr"));
                            txt_nmr.setBackgroundResource(R.color.page_join_visit);
                            txt_nmr.setTextColor(getResources().getColor(R.color.white));
                            txt_nmr.setTextSize(10);
                            txt_nmr.setGravity(Gravity.CENTER);
                            txt_nmr.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_nmr, params);

                            TextView txt_brg=new TextView(detail_timeline.this);
                            txt_brg.setText(""+obj.getString("jenis_item3"));
                            txt_brg.setBackgroundResource(R.color.page_join_visit);
                            txt_brg.setTextColor(getResources().getColor(R.color.white));
                            txt_brg.setTextSize(10);
                            txt_brg.setGravity(Gravity.CENTER);
                            txt_brg.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_brg, params);

                            TextView txt_qty=new TextView(detail_timeline.this);
                            txt_qty.setText(""+obj.getString("qty"));
                            txt_qty.setBackgroundResource(R.color.page_join_visit);
                            txt_qty.setTextColor(getResources().getColor(R.color.white));
                            txt_qty.setTextSize(10);
                            txt_qty.setGravity(Gravity.CENTER);
                            txt_qty.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_qty, params);

                            tabel_2.addView(row);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    if(tot_order!=0){
                        TableRow row=new TableRow(detail_timeline.this);
                        row.setBackgroundColor(getResources().getColor(R.color.white));

                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                        params.setMargins(3, 1, 3, 1);

                        TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                        params2.setMargins(3, 1, 3, 1);
                        params2.span=2;

                        TextView txt_tot=new TextView(detail_timeline.this);
                        txt_tot.setText("Total");
                        txt_tot.setBackgroundResource(R.color.page_join_visit);
                        txt_tot.setTextColor(getResources().getColor(R.color.white));
                        txt_tot.setTextSize(10);
                        txt_tot.setGravity(Gravity.CENTER);
                        txt_tot.setTypeface(null, Typeface.BOLD);
                        // add to row
                        row.addView(txt_tot, params2);

                        TextView txt_tot_qty=new TextView(detail_timeline.this);
                        txt_tot_qty.setText(""+tot_order);
                        txt_tot_qty.setBackgroundResource(R.color.page_join_visit);
                        txt_tot_qty.setTextColor(getResources().getColor(R.color.white));
                        txt_tot_qty.setTextSize(10);
                        txt_tot_qty.setGravity(Gravity.CENTER);
                        txt_tot_qty.setTypeface(null, Typeface.BOLD);
                        // add to row
                        row.addView(txt_tot_qty, params);

                        tabel_2.addView(row);
                    }

                    get_foto_display(nmr_jv,id_cust);
                }
                else{
                    Toast.makeText(detail_timeline.this, "SO Empty ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    get_foto_display(nmr_jv,id_cust);
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_timeline.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
        // progressDialog.dismiss();
    }


    private void get_foto_display(final String nmr_jv, final String id_cust) {

        final ProgressDialog progressDialog = new ProgressDialog(detail_timeline.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Foto Display");
        progressDialog.show();

        String url_server = url_get_foto_display+nmr_jv+"/"+id_cust;

        Log.d("list_brg", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            final String sku = obj.getString("nama_display");
                            final String file_foto = obj.getString("nama_file");

                            TableRow row=new TableRow(detail_timeline.this);
                            row.setBackgroundColor(getResources().getColor(R.color.white));

                            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params.setMargins(3, 1, 3, 1);
                            //params.height = 110;

                            TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            paramsImgOutlet.setMargins(3, 1, 3, 1);
                            paramsImgOutlet.height=140;
                            paramsImgOutlet.width=80;

                            TextView txt_nmr=new TextView(detail_timeline.this);
                            txt_nmr.setText(""+obj.getString("nmr"));
                            txt_nmr.setBackgroundResource(R.color.page_join_visit);
                            txt_nmr.setTextColor(getResources().getColor(R.color.white));
                            txt_nmr.setTextSize(10);
                            txt_nmr.setGravity(Gravity.CENTER);
                            txt_nmr.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_nmr, params);

                            TextView txt_display=new TextView(detail_timeline.this);
                            txt_display.setText(""+obj.getString("nama_display"));
                            txt_display.setBackgroundResource(R.color.page_join_visit);
                            txt_display.setTextColor(getResources().getColor(R.color.white));
                            txt_display.setTextSize(10);
                            txt_display.setGravity(Gravity.CENTER);
                            txt_display.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_display, params);


                            //String img = Server.URL2 + "image_upload_sfa/list_foto_join_visit/"+obj.getString("nama_file");
                            SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String img="";
                            try {
                                Date date = format_date.parse(test_date2);
                                Date date2 = format_date.parse(tgl_buat);
                                if(date2.before(date)){
                                    //Toast.makeText(getApplicationContext(), "Kemarin ",Toast.LENGTH_LONG).show();
                                    img = Server.URL2 + "image_upload_sfa/list_foto_join_visit/"+obj.getString("nama_file");
                                    Log.d("photo_stock", String.valueOf(img));
                                }
                                else{
                                    //Toast.makeText(getApplicationContext(), "Setelah ",Toast.LENGTH_LONG).show();
                                    img = Server.URL3 + "image_upload_sfa/list_foto_join_visit/"+obj.getString("nama_file");
                                    Log.d("photo_stock", String.valueOf(img));
                                }
                            }
                            catch (ParseException e) {
                                e.printStackTrace();
                            }

                            ImageView imgOutlet = new ImageView(detail_timeline.this);
                            imgOutlet.setBackgroundResource(R.color.page_join_visit);
                            Picasso.with(imgOutlet.getContext())
                                    .load(img)
                                    .fit()
                                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                                    .centerCrop().into(imgOutlet);

                            imgOutlet.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(detail_timeline.this, detail_foto.class);
                                    intent.putExtra("sku", sku);
                                    intent.putExtra("file_foto", file_foto);
                                    intent.putExtra("tgl_buat", tgl_buat);
                                    intent.putExtra("act", "kunjungan");
                                    //startActivity(intent);
                                    startActivityForResult(intent, 1);
                                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                                }
                            });
                            // add to row
                            row.addView(imgOutlet, paramsImgOutlet);

                            tabel_3.addView(row);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    get_cek_program(nmr_jv,id_cust);
                }
                else{
                    Toast.makeText(detail_timeline.this, "Foto Display Kosong ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    get_cek_program(nmr_jv,id_cust);
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_timeline.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
        // progressDialog.dismiss();
    }


    private void get_cek_program(final String nmr_jv, final String id_cust) {

        final ProgressDialog progressDialog = new ProgressDialog(detail_timeline.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Program");
        progressDialog.show();

        String url_server = url_get_cek_program+nmr_jv+"/"+id_cust;

        Log.d("list_brg", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);


                            TableRow row=new TableRow(detail_timeline.this);
                            row.setBackgroundColor(getResources().getColor(R.color.white));

                            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params.setMargins(3, 1, 3, 1);
                            //params.height = 110;

                            TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            paramsImgOutlet.setMargins(3, 1, 3, 1);
                            paramsImgOutlet.height=80;
                            paramsImgOutlet.width=80;

                            TextView txt_nmr=new TextView(detail_timeline.this);
                            txt_nmr.setText(""+obj.getString("nmr"));
                            txt_nmr.setBackgroundResource(R.color.page_join_visit);
                            txt_nmr.setTextColor(getResources().getColor(R.color.white));
                            txt_nmr.setTextSize(10);
                            txt_nmr.setGravity(Gravity.CENTER);
                            txt_nmr.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_nmr, params);

                            TextView txt_nama_program=new TextView(detail_timeline.this);
                            txt_nama_program.setText(""+obj.getString("nama_program"));
                            txt_nama_program.setBackgroundResource(R.color.page_join_visit);
                            txt_nama_program.setTextColor(getResources().getColor(R.color.white));
                            txt_nama_program.setTextSize(10);
                            txt_nama_program.setGravity(Gravity.CENTER);
                            txt_nama_program.setTypeface(null, Typeface.BOLD);
                            // add to row
                            row.addView(txt_nama_program, params);

                            tabel_4.addView(row);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    get_akt_kompetitor(nmr_jv,id_cust);
                }
                else{
                    Toast.makeText(detail_timeline.this, "Program Kosong ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    get_akt_kompetitor(nmr_jv,id_cust);
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_timeline.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
        // progressDialog.dismiss();
    }


    private void get_akt_kompetitor(final String nmr_jv, final String id_cust) {

        final ProgressDialog progressDialog = new ProgressDialog(detail_timeline.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Akt Kompetitor");
        progressDialog.show();

        String url_server = url_get_akt_kompetitor+nmr_jv+"/"+id_cust;

        Log.d("akt_kompetitor", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            for(int k=1;k<=4;k++){
                                if(!obj.getString("merk"+k).equals("")) {
                                    TableRow row = new TableRow(detail_timeline.this);
                                    row.setBackgroundColor(getResources().getColor(R.color.white));

                                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                                    params.setMargins(3, 1, 3, 1);
                                    //params.height = 110;

                                    TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                                    paramsImgOutlet.setMargins(3, 1, 3, 1);
                                    paramsImgOutlet.height = 80;
                                    paramsImgOutlet.width = 80;

                                    TextView txt_nmr = new TextView(detail_timeline.this);
                                    txt_nmr.setText("" + k);
                                    txt_nmr.setBackgroundResource(R.color.page_join_visit);
                                    txt_nmr.setTextColor(getResources().getColor(R.color.white));
                                    txt_nmr.setTextSize(10);
                                    txt_nmr.setGravity(Gravity.CENTER);
                                    txt_nmr.setTypeface(null, Typeface.BOLD);
                                    // add to row
                                    row.addView(txt_nmr, params);

                                    TextView txt_merk1 = new TextView(detail_timeline.this);
                                    txt_merk1.setText("" + obj.getString("merk" + k));
                                    txt_merk1.setBackgroundResource(R.color.page_join_visit);
                                    txt_merk1.setTextColor(getResources().getColor(R.color.white));
                                    txt_merk1.setTextSize(10);
                                    txt_merk1.setGravity(Gravity.CENTER);
                                    txt_merk1.setTypeface(null, Typeface.BOLD);
                                    // add to row
                                    row.addView(txt_merk1, params);

                                    TextView txt_promo1 = new TextView(detail_timeline.this);
                                    txt_promo1.setText("" + obj.getString("promo" + k));
                                    txt_promo1.setBackgroundResource(R.color.page_join_visit);
                                    txt_promo1.setTextColor(getResources().getColor(R.color.white));
                                    txt_promo1.setTextSize(10);
                                    txt_promo1.setGravity(Gravity.CENTER);
                                    txt_promo1.setTypeface(null, Typeface.BOLD);
                                    // add to row
                                    row.addView(txt_promo1, params);

                                    tabel_5.addView(row);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();

                }
                else{
                    Toast.makeText(detail_timeline.this, "Akt Kompetitor Kosong ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_timeline.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
        // progressDialog.dismiss();
    }

    private void get_toko_tutup(){
        final ProgressDialog progressDialog = new ProgressDialog(detail_timeline.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_edit       = url_get_toko_tutup+szDocId+"/"+id_customer;
        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get Data Survey", jObj.toString());

                        String id      = jObj.getString("id");
                        String nama_file    = jObj.getString("nama_file");

                        String img = Server.URL2 + "image_upload_sfa/list_foto_join_visit/"+nama_file;
                        Picasso.with(image_toko_tutup.getContext())
                                .load(img)
                                .placeholder(R.drawable.ic_camera_alt_black_48dp)
                                .fit().centerCrop().into(image_toko_tutup);

                        Zoomy.Builder builder = new Zoomy.Builder(detail_timeline.this)
                                .target(image_toko_tutup)
                                .interpolator(new OvershootInterpolator());

                        builder.register();


                    } else {
                        Toast.makeText(detail_timeline.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_timeline.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }
}
