package com.project45.ilovepadma.aktifitas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class add_aktifitas extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,employee_id_dms3="",id_aktifitas="",timeStamp="",act_form="",tanggal_act="",id_company="",nama_company="";
    int randomNumber;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static final String TAG = add_aktifitas.class.getSimpleName();

    private static String url_post_aktifitas     = Server.URL + "aktifitas/post_aktifitas";
    private static String url_list_aktifitas     = Server.URL + "aktifitas/get_list_deskripsi/";
    private static String url_post_del_report     = Server.URL + "aktifitas/post_del_aktifitas_detail";

    EditText txt_tgl_aktifitas;
    TableLayout tabel_1a,tabel_1b;
    Button btn_deskripsi;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    List<Data_work_report> itemList = new ArrayList<Data_work_report>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_aktifitas);

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
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        txt_tgl_aktifitas = findViewById(R.id.txt_tgl_aktifitas);
        btn_deskripsi = findViewById(R.id.btn_deskripsi);
        tabel_1a = findViewById(R.id.tabel_1a);
        tabel_1b = findViewById(R.id.tabel_1b);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intentku = getIntent(); // gets the previously created intent
        act_form = intentku.getStringExtra("act");
        tanggal_act = intentku.getStringExtra("tanggal");

        timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());
        get_id_aktifitas(timeStamp);

        txt_tgl_aktifitas.setText(tanggal_act);
        txt_tgl_aktifitas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_absen = "filter1";
                showDateDialog(tgl_absen,txt_tgl_aktifitas);

            }
        });

        btn_deskripsi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String tanggal = txt_tgl_aktifitas.getText().toString();
                Intent intent = new Intent(getApplicationContext(), add_deskripsi.class);
                intent.putExtra("act_form", act_form);
                intent.putExtra("id_aktifitas", id_aktifitas);
                intent.putExtra("tanggal_aktifitas", tanggal);
                startActivityForResult(intent, 111);

                //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
            }
        });



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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_user, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            //Toast.makeText(getApplicationContext(), "Test ", Toast.LENGTH_LONG).show();
            if(itemList.size()==0){
                Toast.makeText(getApplicationContext(), "Anda Harus Menambahkan Deskripsi  ", Toast.LENGTH_LONG).show();
            }
            else {
                postAktifitas();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == 11) {
                Toast.makeText(this, "Deskripsi added ", Toast.LENGTH_LONG).show();
                cleanTable(tabel_1a);
                cleanTable(tabel_1b);
                getDeskripsi(id_aktifitas);
            }
        }
    }

    private void showDateDialog(final String absen, final EditText tgl_awal){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                if(absen.equals("filter1"))
                {
                    tgl_awal.setText(dateFormatter.format(newDate.getTime()));
                }


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    private void get_id_aktifitas(final String tanggal){
        id_aktifitas =  id_user+"-"+tanggal+"-"+ String.valueOf(generateRandomNumbers(100001,999999));
        getDeskripsi(id_aktifitas);
        //Toast.makeText(getApplicationContext(), " id_survey "+id_survey, Toast.LENGTH_LONG).show();
    }

    private int generateRandomNumbers(int min, int max) {
        // min & max will be changed as per your requirement. In my case, I've taken min = 2 & max = 32

        //Random r = new Random();
        //int randomNumber = r.nextInt(100);
        int randomNumberCount = 10;

        int dif = max - min;
        if (dif < (randomNumberCount * 3)) {
            dif = (randomNumberCount * 3);
        }

        int margin = (int) Math.ceil((float) dif / randomNumberCount);
        List<Integer> randomNumberList = new ArrayList<>();



        Random random = new Random();

        for (int i = 0; i < randomNumberCount; i++) {
            int range = (margin * i) + min; // 2, 5, 8

            int randomNum = random.nextInt(margin);
            if (randomNum == 0) {
                randomNum = 1;
            }

            int number = (randomNum + range);
            randomNumber = number;
            //randomNumberList.add(number);
        }
        //Toast.makeText(getApplicationContext(), " random "+String.valueOf(randomNumber), Toast.LENGTH_LONG).show();
        return randomNumber;
    }

    private void getDeskripsi(final String id_aktifitass) {
        itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(add_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server = url_list_aktifitas + id_aktifitass;
        Log.d("url_rute_hari", url_server);
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

                            jv.setid(obj.getString("id_aktifitas"));
                            jv.setid_job_list(obj.getString("nmr_aktifitas"));
                            jv.setketerangan(obj.getString("detail_aktifitas"));
                            jv.setwaktu_mulai(obj.getString("jam_awal"));
                            jv.setwaktu_selesai(obj.getString("jam_akhir"));

                            itemList.add(jv);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Klik RO untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callList1();
                    callList2();
                }
                else{
                    Toast.makeText(add_aktifitas.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(add_aktifitas.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

            TableRow row = new TableRow(add_aktifitas.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;
            params.width = 310;

            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params2.height = 120;

            TextView txt_nmr = new TextView(add_aktifitas.this);
            txt_nmr.setText("" + nmr);
            txt_nmr.setBackgroundResource(R.color.page_join_visit);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_nmr, params2);

            tabel_1a.addView(row);

            nmr++;
        }
    }

    private void callList2() {

        for (int i = 0; i < itemList.size(); i++) {
            final Data_work_report sel = itemList.get(i);

            TableRow row = new TableRow(add_aktifitas.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;

            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params2.setMargins(3, 1, 3, 1);
            params2.height = 120;

            TableRow.LayoutParams paramsImgTag = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgTag.setMargins(3, 1, 3, 1);
            paramsImgTag.height = 120;

            TextView txt_keterangan = new TextView(add_aktifitas.this);
            txt_keterangan.setText("" + Html.fromHtml(sel.getketerangan()));
            txt_keterangan.setBackgroundResource(R.color.page_join_visit);
            txt_keterangan.setTextColor(getResources().getColor(R.color.white));
            txt_keterangan.setTextSize(10);
            txt_keterangan.setGravity(Gravity.CENTER);
            txt_keterangan.setPadding(3, 0, 3, 0);
            txt_keterangan.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_keterangan, params2);

            TextView txt_waktu = new TextView(add_aktifitas.this);
            txt_waktu.setText("" + sel.getwaktu_mulai() + " - "+sel.getwaktu_selesai());
            txt_waktu.setBackgroundResource(R.color.page_join_visit);
            txt_waktu.setTextColor(getResources().getColor(R.color.white));
            txt_waktu.setTextSize(10);
            txt_waktu.setGravity(Gravity.CENTER);
            txt_waktu.setPadding(3, 0, 3, 0);
            txt_waktu.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_waktu, params2);

            ImageView img_coaching = new ImageView(add_aktifitas.this);
            img_coaching.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_clear_white_48));
            img_coaching.setBackgroundResource(R.color.page_join_visit);
            img_coaching.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    proc_del_deskripsi(sel.getid());

                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });
            // add to row
            row.addView(img_coaching, paramsImgTag);

            tabel_1b.addView(row);

        }
    }

    private void postAktifitas(){
        final String tanggal = txt_tgl_aktifitas.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(add_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        //proses save to database using api
        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_aktifitas, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Add/update", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        //end proses
                        progressDialog.dismiss();
                        onBackPressed();
                        //onBackPressedWithProcess();


                    } else {
                        Toast.makeText(getApplicationContext(),
                                "error " + jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "error " + error.getMessage(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nmr_aktifitas", id_aktifitas);
                params.put("jabatan", jabatan);
                params.put("tanggal", tanggal);
                params.put("id_company", id_company);
                params.put("create_by", id_user);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

    }

    private void proc_del_deskripsi(final String id_report){

        Log.e(TAG, "id_report: " + id_report);

        final ProgressDialog progressDialog = new ProgressDialog(add_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Delete...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_del_report, new Response.Listener<String>() {

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

                        cleanTable(tabel_1a);
                        cleanTable(tabel_1b);
                        getDeskripsi(id_aktifitas);
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

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
