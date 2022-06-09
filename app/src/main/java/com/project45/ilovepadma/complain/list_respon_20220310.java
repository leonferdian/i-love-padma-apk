package com.project45.ilovepadma.complain;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class list_respon_20220310 extends AppCompatActivity {
    String id_user, username,bm,email,jabatan,employee_id_dms3="",id_complain="",complain_status="",complain_to="";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static final String TAG = list_respon_20220310.class.getSimpleName();

    private static String url_list_complain_deskripsi     = Server.URL + "cust_service/get_list_complain_respon/";
    private static String url_post_delete_deskripsi_padma_complain     = Server.URL + "cust_service/post_delete_respon_padma_complain";
    private static String url_post_update_read_respon     = Server.URL + "cust_service/post_update_read_respon";

    TableLayout tabel_1;
    List<Data_work_report> itemList = new ArrayList<Data_work_report>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_respon);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_aktifitas);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        employee_id_dms3 = Api.sharedpreferences.getString(Api.TAG_ID_EMPLOYEE_DMS3, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);

        tabel_1 = findViewById(R.id.tabel_1b);

        Intent intentku = getIntent(); // gets the previously created intent
        id_complain = intentku.getStringExtra("id_complain");
        complain_status = intentku.getStringExtra("complain_status");
        complain_to = intentku.getStringExtra("complain_to");
        //Toast.makeText(list_respon.this, "id_complain "+id_complain, Toast.LENGTH_LONG).show();
        getDeskripsi(id_complain);
        proc_update_read_respon(id_complain);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Menerapkan menu terpilih di menu-> empat.xml
        if(!complain_status.equals("close")) {
            getMenuInflater().inflate(R.menu.menu_so, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.add_so) {
            //Toast.makeText(list_respon.this, "Ini menu add", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(list_respon_20220310.this, add_respon_complain.class);
            //intent.putExtra("act", "edit_outlet");
            intent.putExtra("id_complain", id_complain);
            intent.putExtra("complain_to", complain_to);
            startActivityForResult(intent,111);
            return true;



        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == 11) {
                Toast.makeText(this, "Respon added ", Toast.LENGTH_LONG).show();
                cleanTable(tabel_1);
                getDeskripsi(id_complain);
            }
        }
    }

    private void getDeskripsi(final String id_complainn) {
        itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(list_respon_20220310.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server = url_list_complain_deskripsi + id_complainn;
        Log.d("list_complain_respon", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_work_report jv = new Data_work_report();

                            jv.setid(obj.getString("id_respon"));
                            jv.setid_job_list(obj.getString("complain_nomor"));
                            jv.setketerangan(obj.getString("isi_respon"));
                            jv.setcreate_by(obj.getString("nama_user"));
                            jv.setcomplain_foto(obj.getString("respon_foto"));
                            jv.setid_user(obj.getString("create_by"));

                            itemList.add(jv);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Klik RO untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callList1();

                }
                else{
                    Toast.makeText(list_respon_20220310.this, "Data Respon Kosong ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_respon_20220310.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

    private void callList1() {
        int nmr = 1;
        for (int i = 0; i < itemList.size(); i++) {
            final Data_work_report sel = itemList.get(i);

            TableRow row = new TableRow(list_respon_20220310.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;
            params.width = 310;

            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params2.setMargins(3, 1, 3, 1);
            //params2.height = 120;

            TableRow.LayoutParams paramsImgTag = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgTag.setMargins(3, 1, 3, 1);
            //paramsImgTag.height = 120;

            TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgOutlet.setMargins(3, 1, 3, 1);
            paramsImgOutlet.height=80;
            paramsImgOutlet.width=80;

            TextView txt_nmr = new TextView(list_respon_20220310.this);
            txt_nmr.setText("" + nmr);
            if(sel.getid_user().equals(id_user))
            {
                txt_nmr.setBackgroundResource(R.color.page_tbl_total);
            }
            else {
                txt_nmr.setBackgroundResource(R.color.page_join_visit);
            }
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(3, 0, 3, 0);
            txt_nmr.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_nmr, params2);

            TextView txt_keterangan = new TextView(list_respon_20220310.this);
            txt_keterangan.setText("" + Html.fromHtml(sel.getketerangan()));
            if(sel.getid_user().equals(id_user))
            {
                txt_keterangan.setBackgroundResource(R.color.page_tbl_total);
            }
            else {
                txt_keterangan.setBackgroundResource(R.color.page_join_visit);
            }

            txt_keterangan.setTextColor(getResources().getColor(R.color.white));
            txt_keterangan.setTextSize(10);
            txt_keterangan.setGravity(Gravity.CENTER);
            txt_keterangan.setPadding(5, 0, 5, 0);
            txt_keterangan.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_keterangan, params2);

            TextView txt_create_by = new TextView(list_respon_20220310.this);
            txt_create_by.setText("" + sel.getcreate_by());
            if(sel.getid_user().equals(id_user))
            {
                txt_create_by.setBackgroundResource(R.color.page_tbl_total);
            }
            else {
                txt_create_by.setBackgroundResource(R.color.page_join_visit);
            }

            txt_create_by.setTextColor(getResources().getColor(R.color.white));
            txt_create_by.setTextSize(10);
            txt_create_by.setGravity(Gravity.CENTER);
            txt_create_by.setPadding(5, 0, 5, 0);
            txt_create_by.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_create_by, params2);

            ImageView imgOutlet = new ImageView(list_respon_20220310.this);
            if(sel.getcomplain_foto().equals("")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                imgOutlet.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_photo,options));
            }
            else{
                String img = Server.URL2 + "image_upload/list_foto_complain_respon/"+sel.getcomplain_foto();
                Picasso.with(imgOutlet.getContext())
                        .load(img)
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        .fit().centerCrop().into(imgOutlet);
            }
            if(sel.getid_user().equals(id_user))
            {
                imgOutlet.setBackgroundResource(R.color.page_tbl_total);
            }
            else {
                imgOutlet.setBackgroundResource(R.color.page_join_visit);
            }

            imgOutlet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(list_respon_20220310.this, detail_foto.class);
                    intent.putExtra("sku", "Foto Respon Complain");
                    intent.putExtra("file_foto", sel.getcomplain_foto());
                    intent.putExtra("tgl_buat", "");
                    intent.putExtra("act", "foto_respon_complain");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });
            // add to row
            row.addView(imgOutlet, paramsImgOutlet);

            ImageView img_coaching = new ImageView(list_respon_20220310.this);
            img_coaching.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_clear_white_48));
            if(sel.getid_user().equals(id_user))
            {
                img_coaching.setBackgroundResource(R.color.page_tbl_total);
            }
            else {
                img_coaching.setBackgroundResource(R.color.page_join_visit);
            }

            img_coaching.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                    if(!complain_status.equals("close")) {
                        CloseAlert(sel.getid());
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Anda tidak bisa menghapus respon. complain sudah diclose ", Toast.LENGTH_LONG).show();
                    }
                }
            });
            // add to row
            row.addView(img_coaching, paramsImgTag);

            tabel_1.addView(row);

            nmr++;
        }
    }

    private void CloseAlert(final String id_report){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Delete Warning");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Anda yakin ingin menghapus ?")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                /*.setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something
                        dialogInterface.cancel();
                    }
                })*/
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        proc_del_deskripsi(id_report);

                    }
                })

                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });


        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void proc_del_deskripsi(final String id_report){

        Log.e(TAG, "id_report: " + id_report);

        final ProgressDialog progressDialog = new ProgressDialog(list_respon_20220310.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Delete...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_delete_deskripsi_padma_complain, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Save", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        cleanTable(tabel_1);
                        getDeskripsi(id_complain);
                        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Delete list work report", "Delete list work report on "+tgl_report,id_report);


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Del Work report Error: " + error.getMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_report", id_report);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

        //end proses
        progressDialog.dismiss();
    }

    private void proc_update_read_respon(final String nmr_komplain){

        Log.e(TAG, "nmr_komplain: " + nmr_komplain);

        final ProgressDialog progressDialog = new ProgressDialog(list_respon_20220310.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Update Respon...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_update_read_respon, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Update", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Delete list work report", "Delete list work report on "+tgl_report,id_report);


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "update report Error: " + error.getMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nmr_komplain", nmr_komplain);
                params.put("id_user", id_user);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

        //end proses
        progressDialog.dismiss();
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}
