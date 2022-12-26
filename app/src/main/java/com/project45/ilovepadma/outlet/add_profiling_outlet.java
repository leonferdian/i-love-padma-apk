package com.project45.ilovepadma.outlet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.timeline.add_post_everything;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class add_profiling_outlet extends AppCompatActivity {
    private static final String TAG = add_profiling_outlet.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static String url_get_pertanyaan_profiling_outlet = Server.URL + "outlet/get_pertanyaan_profiling_outlet";
    private static String url_post_save_profiling_outlet   = Server.URL + "outlet/post_save_profiling_outlet";
    private static String url_post_save_profiling_outlet_item   = Server.URL + "outlet/post_save_profiling_outlet_item";
    private Button btn_save_outlet;
    private String[] jawaban_pertanyaan;
    TableLayout tabel_pertanyaan;
    ArrayList<HashMap<String,Object>> dataDaftarPertanyaan = new ArrayList<HashMap<String,Object>>();
    List<EditText> list_edit_text_jawaban = new ArrayList<EditText>();
    List<EditText> list_edit_text_jawaban_summary = new ArrayList<EditText>();
//    Integer[][] id_angka_sum;
    int id_summary_volume = 0,success;
    String id_user,username,email,jabatan,id_company="",nama_company="", id_outlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profiling_outlet);

        if(getIntent().getExtras() != null) {
            Intent myIntent = getIntent(); // gets the previously created intent

            id_outlet = myIntent.getStringExtra("id_outlet");
        }

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_list_jv);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name)+" - "+id_outlet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        tabel_pertanyaan = findViewById(R.id.tabel_pertanyaan);
        btn_save_outlet = findViewById(R.id.btn_save_outlet);

        btn_save_outlet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                jawaban_pertanyaan = new String[1000];
                int nmr_sub = 0;
                for (EditText list_edit_text : list_edit_text_jawaban) {
                    jawaban_pertanyaan[nmr_sub] = list_edit_text.getText().toString();
                    nmr_sub++;
                }

                Toast.makeText(getApplicationContext(), "Save Profiling Outlet", Toast.LENGTH_LONG).show();
                save_profiling_outlet();
            }
        });

        if(id_outlet.equals("") || id_outlet.isEmpty()){
            btn_save_outlet.setVisibility(View.GONE);
            Toast.makeText(add_profiling_outlet.this, "ID Outlet tidak dapat diambil", Toast.LENGTH_SHORT).show();
        }
        else {
            clean_pertanyaan();
            get_pertanyaan_profiling_outlet();
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
            window.setStatusBarColor(getResources().getColor(R.color.black_purple));
        }
    }

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void clean_pertanyaan(){
        tabel_pertanyaan.removeAllViews();
        dataDaftarPertanyaan.clear();
        list_edit_text_jawaban.clear();
    }

    private void get_pertanyaan_profiling_outlet(){
        final ProgressDialog progressDialog = new ProgressDialog(add_profiling_outlet.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Check Data");
        progressDialog.show();

        String url_server = url_get_pertanyaan_profiling_outlet;
        Log.d("url_question", url_server);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("url_question_result", response.toString());
                if (response.length() > 0) {
                    int nomor = 1;
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String tipe_pertanyaan = obj.getString("tipe_pertanyaan");
                            final HashMap<String,Object> dataPertanyaan = new HashMap<String,Object>();
                            dataPertanyaan.put("nomor_pertanyaan",obj.getString("nomor_pertanyaan"));
                            dataPertanyaan.put("id_pertanyaan",obj.getString("id_pertanyaan"));
                            dataPertanyaan.put("pertanyaan",obj.getString("pertanyaan"));
                            dataPertanyaan.put("tipe_pertanyaan",obj.getString("tipe_pertanyaan"));
                            dataPertanyaan.put("keterangan_system",obj.getString("keterangan_system"));
                            dataPertanyaan.put("sub_question",obj.getJSONArray("sub_question"));

                            int nomor_pertanyaan = Integer.valueOf(obj.getString("nomor_pertanyaan"));
                            if (dataDaftarPertanyaan.size() < nomor_pertanyaan) {
                                dataDaftarPertanyaan.add(dataPertanyaan);
                            }
                            else {
                                dataDaftarPertanyaan.set(nomor_pertanyaan-1,dataPertanyaan);
                            }

                            TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params1.setMargins(3, 1, 3, 1);
                            params1.weight = 1;
                            params1.span = 3;

                            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params2.setMargins(3, 1, 3, 1);
                            params2.weight = 1;
                            params2.span = 1;

                            TableRow.LayoutParams params3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params3.setMargins(3, 1, 3, 1);
                            params3.weight = 1;
                            params3.span = 2;

                            TableRow row = new TableRow(add_profiling_outlet.this);
                            row.setBackgroundColor(getResources().getColor(R.color.white));

                            TextView txt_nomor = new TextView(add_profiling_outlet.this);
                            txt_nomor.setText(obj.getString("nomor_pertanyaan"));
                            txt_nomor.setBackgroundResource(R.color.holo_blue_light);
                            txt_nomor.setTextColor(getResources().getColor(R.color.white));
                            txt_nomor.setTextSize(15);
                            txt_nomor.setGravity(Gravity.CENTER);
                            txt_nomor.setTypeface(null, Typeface.BOLD);
                            row.addView(txt_nomor, params2);

                            TextView txt_pertanyaan = new TextView(add_profiling_outlet.this);
                            txt_pertanyaan.setText(obj.getString("pertanyaan"));
                            txt_pertanyaan.setBackgroundResource(R.color.holo_blue_light);
                            txt_pertanyaan.setTextColor(getResources().getColor(R.color.white));
                            txt_pertanyaan.setTextSize(15);
                            txt_pertanyaan.setGravity(Gravity.LEFT);
                            txt_pertanyaan.setTypeface(null, Typeface.BOLD);
                            row.addView(txt_pertanyaan, params3);
                            tabel_pertanyaan.addView(row);

                            if (tipe_pertanyaan.equals("text")) {
                                TableRow row_jawaban = new TableRow(add_profiling_outlet.this);
                                row_jawaban.setBackgroundColor(getResources().getColor(R.color.white));

                                final EditText txt_jawaban = new EditText(add_profiling_outlet.this);
                                txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                txt_jawaban.setTextSize(15);
                                txt_jawaban.setGravity(Gravity.LEFT);
                                txt_jawaban.setSingleLine(true);
                                txt_jawaban.setLines(1);
                                txt_jawaban.setText("");
                                // add to row
                                row_jawaban.addView(txt_jawaban, params2);
                                tabel_pertanyaan.addView(row_jawaban);
                                list_edit_text_jawaban.add(txt_jawaban);
                            }
                            else if (tipe_pertanyaan.equals("numeric")){
                                if(obj.getString("keterangan_system").equals("sum_volume1")){
                                    JSONArray jsonSub = obj.getJSONArray("sub_question");
                                    if (jsonSub.length() > 0) {
                                        for (int j = 0; j < jsonSub.length(); j++) {
                                            JSONObject objChild = jsonSub.getJSONObject(j);

                                            TableRow row_pertanyaan_detail = new TableRow(add_profiling_outlet.this);
                                            row_pertanyaan_detail.setBackgroundColor(getResources().getColor(R.color.white));

                                            TextView txt_pertanyaan_detail = new TextView(add_profiling_outlet.this);
                                            txt_pertanyaan_detail.setText(objChild.getString("pertanyaan_detail"));
                                            txt_pertanyaan_detail.setBackgroundResource(R.color.holo_blue_light);
                                            txt_pertanyaan_detail.setTextColor(getResources().getColor(R.color.white));
                                            txt_pertanyaan_detail.setTextSize(15);
                                            txt_pertanyaan_detail.setGravity(Gravity.CENTER);
                                            txt_pertanyaan_detail.setTypeface(null, Typeface.BOLD);
                                            row_pertanyaan_detail.addView(txt_pertanyaan_detail, params2);

                                            EditText txt_input = new EditText(add_profiling_outlet.this);
                                            txt_input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            txt_input.setSingleLine(true);
                                            txt_input.setBackgroundResource(R.drawable.edittext_border);
                                            txt_input.setId(nomor);
                                            txt_input.setTextSize(15);
                                            txt_input.setGravity(Gravity.CENTER);
                                            txt_input.setText("0");

                                            txt_input.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable editable) {
                                                    int total_volume = 0;
                                                    for(int x = 0; x < list_edit_text_jawaban_summary.size();x++){
                                                        total_volume += Integer.valueOf(String.valueOf(list_edit_text_jawaban_summary.get(x).getText()));
                                                    }

                                                    list_edit_text_jawaban.get(id_summary_volume).setText(String.valueOf(total_volume));
                                                }
                                            });

                                            // add to row
                                            row_pertanyaan_detail.addView(txt_input, params3);

                                            tabel_pertanyaan.addView(row_pertanyaan_detail);
                                            list_edit_text_jawaban.add(txt_input);
                                            list_edit_text_jawaban_summary.add(txt_input);
                                            nomor++;
                                        }
                                    }
                                }
                                else if(obj.getString("keterangan_system").equals("sum_volume1_akhir")){
                                    TableRow row_jawaban = new TableRow(add_profiling_outlet.this);
                                    row_jawaban.setBackgroundColor(getResources().getColor(R.color.white));

                                    EditText txt_input = new EditText(add_profiling_outlet.this);
                                    txt_input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    txt_input.setEms(10);
                                    //txt_input.setId(nomor);
                                    txt_input.setFocusable(false);
                                    txt_input.setTextSize(15);
                                    txt_input.setGravity(Gravity.CENTER);
                                    txt_input.setText("0");
                                    // add to row
                                    row_jawaban.addView(txt_input, params2);

                                    tabel_pertanyaan.addView(row_jawaban);
                                    list_edit_text_jawaban.add(txt_input);
                                    id_summary_volume = list_edit_text_jawaban.size()-1;
                                }
                                else {
                                    TableRow row_jawaban = new TableRow(add_profiling_outlet.this);
                                    row_jawaban.setBackgroundColor(getResources().getColor(R.color.white));

                                    EditText txt_input = new EditText(add_profiling_outlet.this);
                                    txt_input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    txt_input.setSingleLine(true);
                                    txt_input.setBackgroundResource(R.drawable.edittext_border);
                                    txt_input.setTextSize(15);
                                    txt_input.setGravity(Gravity.LEFT);
                                    txt_input.setText("0");
                                    // add to row
                                    row_jawaban.addView(txt_input, params2);

                                    tabel_pertanyaan.addView(row_jawaban);
                                    list_edit_text_jawaban.add(txt_input);
                                }
                            }
                            else if (tipe_pertanyaan.equals("long_text")){
                                TableRow row_jawaban = new TableRow(add_profiling_outlet.this);
                                row_jawaban.setBackgroundColor(getResources().getColor(R.color.white));

                                final EditText txt_jawaban = new EditText(add_profiling_outlet.this);
                                txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                txt_jawaban.setTextSize(10);
                                txt_jawaban.setLines(10);
                                txt_jawaban.setVerticalScrollBarEnabled(true);
                                txt_jawaban.setGravity(Gravity.LEFT);
                                txt_jawaban.setText("");
                                // add to row
                                row_jawaban.addView(txt_jawaban, params2);
                                tabel_pertanyaan.addView(row_jawaban);
                                list_edit_text_jawaban.add(txt_jawaban);
                            }

                            nomor++;
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                } else {
                    Toast.makeText(add_profiling_outlet.this, "Anda tidak memiliki akses Profiling Outlet", Toast.LENGTH_SHORT).show();
                    btn_save_outlet.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                btn_save_outlet.setVisibility(View.GONE);
                progressDialog.dismiss();
                Toast.makeText(add_profiling_outlet.this, "Koneksi ke server error", Toast.LENGTH_SHORT).show();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    private void save_profiling_outlet(){
        final ProgressDialog progressDialog = new ProgressDialog(add_profiling_outlet.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_save_profiling_outlet, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "save survey Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Log.e("Success save survey", jObj.toString());
                        Toast.makeText(getApplicationContext(), "Sukses Add Profiling Outlet", Toast.LENGTH_LONG).show();
                        save_profiling_outlet_item(progressDialog);
                        onBackPressedWithProcess();
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "save Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_outlet", id_outlet);
                params.put("id_user", id_user);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private void save_profiling_outlet_item(ProgressDialog progressDialog) throws JSONException {
        int counter = 0;
        ArrayList<HashMap<String,Object>> list =(ArrayList<HashMap<String,Object>>)dataDaftarPertanyaan;
        int nomor =1;
        for (HashMap<String, Object> row_pertanyaan : list) {
            String id_pertanyaan = row_pertanyaan.get("id_pertanyaan").toString();
            String nomor_pertanyaan = row_pertanyaan.get("nomor_pertanyaan").toString();

            if(jawaban_pertanyaan[counter] != null){
                if(row_pertanyaan.get("keterangan_system").equals("sum_volume1")){
                    JSONArray jsonSub = (JSONArray) row_pertanyaan.get("sub_question");
                    if (jsonSub.length() > 0) {
                        for (int j = 0; j < jsonSub.length(); j++) {
                            JSONObject objChild = jsonSub.getJSONObject(j);
                            final String nomor_pertanyaan_detail = objChild.getString("nomor_pertanyaan_detail");
                            final String jawaban = jawaban_pertanyaan[counter];
                            StringRequest strReq = new StringRequest(Request.Method.POST, url_post_save_profiling_outlet_item, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "save survey Response: " + response.toString());

                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        success = jObj.getInt(TAG_SUCCESS);

                                        // Check for error node in json
                                        if (success == 1) {
                                            Log.e("save jawaban pertanyaan", jObj.toString());
                                        } else {
                                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        // JSON error
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "save Error: " + error.getMessage());
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    // Posting parameters to url
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id_outlet", id_outlet);
                                    params.put("nomor_pertanyaan", nomor_pertanyaan);
                                    params.put("nomor_pertanyaan_detail", nomor_pertanyaan_detail);
                                    params.put("id_pertanyaan", id_pertanyaan);
                                    params.put("jawaban", jawaban);

                                    return params;
                                }

                            };

                            strReq.setRetryPolicy(new DefaultRetryPolicy(
                                    30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            // Adding request to request queue
                            AppController.getInstance(this).addToRequestQueue(strReq);

                            counter++;
                        }
                    }
                }
                else {
                    final String jawaban = jawaban_pertanyaan[counter];
                    StringRequest strReq = new StringRequest(Request.Method.POST, url_post_save_profiling_outlet_item, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "save survey Response: " + response.toString());

                            try {
                                JSONObject jObj = new JSONObject(response);
                                success = jObj.getInt(TAG_SUCCESS);

                                // Check for error node in json
                                if (success == 1) {
                                    Log.e("save jawaban pertanyaan", jObj.toString());
                                } else {
                                    Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "save Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {

                            // Posting parameters to url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_outlet", id_outlet);
                            params.put("nomor_pertanyaan", nomor_pertanyaan);
                            params.put("nomor_pertanyaan_detail", "1");
                            params.put("id_pertanyaan", id_pertanyaan);
                            params.put("jawaban", jawaban);

                            return params;
                        }

                    };

                    strReq.setRetryPolicy(new DefaultRetryPolicy(
                            30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    // Adding request to request queue
                    AppController.getInstance(this).addToRequestQueue(strReq);

                    counter++;
                }
            }
            else {
                continue;
            }
        }
        progressDialog.dismiss();
    }
}