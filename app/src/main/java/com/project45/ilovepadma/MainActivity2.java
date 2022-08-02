package com.project45.ilovepadma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.project45.ilovepadma.aktifitas.list_aktifitas;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.list_complain;
import com.project45.ilovepadma.complain.menu_complain;
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.data.Data_customer;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.notes.list_note;
import com.project45.ilovepadma.notifikasi.notification_service;
import com.project45.ilovepadma.project.list_project;
import com.project45.ilovepadma.project.list_project2;
import com.project45.ilovepadma.setting.add_company;
import com.project45.ilovepadma.setting.list_team_company;
import com.project45.ilovepadma.setting.main_setting;
import com.project45.ilovepadma.timeline.list_timeline_ilv;
import com.project45.ilovepadma.timeline.list_timeline_ilv2;
import com.project45.ilovepadma.timeline.list_timeline_post_everything;
import com.project45.ilovepadma.timeline.list_timeline_post_everything2;
import com.project45.ilovepadma.timeline.list_timeline_summary;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity2 extends AppCompatActivity {
    String id_user, username,email,employee_id_dms3,kategori_user="",jabatan="",list_rute="",list_rute2="",company_selected="",id_company="",nama_company="";
    //test
    private static final String TAG = MainActivity.class.getSimpleName();
    int success,last_id;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";

    private static String url_post_logout_user   = Server.URL + "survey_45/post_logout_user";
    private static String url_user_company   = Server.URL + "company/list_user_company/";

    private static String url_get_team_hot     = Server.URL + "sfa_leader/get_team_rute_hot/";
    private static String url_get_team_hos     = Server.URL + "sfa_leader/get_team_rute_hos/";
    private static String url_get_team_sm     = Server.URL + "sfa_leader/get_team_rute_sm/";
    private static String url_get_team_gm     = Server.URL + "sfa_leader/get_team_rute_gm/";

    private static String url_get_list_company    = Server.URL + "company/list_company/";
    private static String url_get_team_company    = Server.URL + "company/list_team_company/";

    private LinearLayout layout_isi_slide;
    HorizontalScrollView hrsv_main;

    TextView message_welcome,txt_sum_bulan_ini,txt_sum_minggu_ini,txt_sum_hari_ini,txt_total_score,title_poin;
    ImageView button_shutdown,button_setting;

    CardView card_aktifitas,card_complain,card_timeline,card_project,card_note,card_post_everything;

    private BarChart mBarChart;
    private ProgressBar progressBar2;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    Spinner spinner_company;
    private ArrayList<Data_company> list_company;

    List<Data_company> itemListCompany = new ArrayList<Data_company>();
    List<Data_company> itemListTeamCompany = new ArrayList<Data_company>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        //Toast.makeText(getApplicationContext(), "jabatan "+jabatan, Toast.LENGTH_LONG).show();

        message_welcome = findViewById(R.id.message_welcome);
        txt_sum_bulan_ini = findViewById(R.id.txt_sum_bulan_ini);
        txt_sum_minggu_ini = findViewById(R.id.txt_sum_minggu_ini);
        txt_sum_hari_ini = findViewById(R.id.txt_sum_hari_ini);
        txt_total_score = findViewById(R.id.txt_total_score);
        title_poin = findViewById(R.id.title_poin);

        card_aktifitas = findViewById(R.id.card_aktifitas);
        card_complain = findViewById(R.id.card_complain);
        card_timeline = findViewById(R.id.card_timeline);
        card_project = findViewById(R.id.card_project);
        card_note = findViewById(R.id.card_note);
        card_post_everything = findViewById(R.id.card_post_everything);

        button_shutdown = findViewById(R.id.button_shutdown);
        button_setting = findViewById(R.id.button_setting);

        progressBar2 = findViewById(R.id.progressBar2);
        mBarChart = (BarChart)findViewById(R.id.chart1);
        mBarChart.setDrawValueAboveBar(true);

        title_poin.setVisibility(View.GONE);

        LinearLayout layout = (LinearLayout)findViewById(R.id.layoutMain);

        message_welcome.setText("Hi , "+username );

        /*
        if(jabatan.equals("HOT")){
            getOutletHot();
        }
        else if(jabatan.equals("HOS")){
            getOutletHos();
        }
        else if(jabatan.equals("SM")){
            getOutletSM();
        }
        else if(jabatan.equals("GM")){
            getOutletGM();
        }

        */
        if(id_company== null || id_company.equals(""))
        {
            DialogConfirmLocation("Select");
        }

        txt_sum_bulan_ini.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(intent);
            }
        });

        message_welcome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), account_user.class);
                startActivity(intent);
            }
        });

        button_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), main_setting.class);
                startActivity(intent);
            }
        });

        button_shutdown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                logout_user(id_user);
                // update login session ke FALSE dan mengosongkan nilai id dan username

                SharedPreferences.Editor editor = Api.sharedpreferences.edit();
                editor.putBoolean(login_form.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.putString(TAG_EMAIL, null);
                editor.commit();

                Intent intent = new Intent(MainActivity2.this, page_choice.class);
                finish();
                startActivity(intent);*/

                DialogConfirmLocation("Select");

            }
        });

        card_complain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), menu_complain.class);
                startActivity(intent);
            }
        });

        card_timeline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_timeline_ilv2.class);
                startActivity(intent);

            }
        });

        card_aktifitas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_aktifitas.class);
                intent.putExtra("aktifitas_dari", "you");
                startActivity(intent);
            }
        });

        card_project.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_project2.class);
                startActivity(intent);
            }
        });

        card_post_everything.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_timeline_post_everything2.class);
                startActivity(intent);

            }
        });

        card_note.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_note.class);
                startActivity(intent);
            }
        });

        //getCompany(id_user);
        //getTeamCompany(id_company);

        //background get notifikasi
        Intent serviceIntent = new Intent(MainActivity2.this, notification_service.class);
        serviceIntent.putExtra("id_user", id_user);
        startService(serviceIntent);
        notification_service.PostNotification(MainActivity2.this);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCompany(id_user);
        if(id_company!= null) {
            getTeamCompany(id_company);
        }
    }

    private void logout_user(final String userId) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_logout_user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();



            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", userId);

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


    private void getOutletHot() {

        String url_server = url_get_team_hot+id_user;
        //String url_server = url_get_team_hot+13;
        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    int nmr = 0;
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            list_rute += "%27"+obj.getString("id_employee")+"%27,";
                            list_rute2 += "%27"+obj.getString("id_route")+"%27,";

                            Data_customer item = new Data_customer();
                            item.setSaluran_distribusi("%27"+obj.getString("id_employee")+"%27,");
                            item.setdt_base(obj.getString("database_name"));

                            // menambah item ke array
                            //itemList.add(item);
                            nmr++;
                            //getOutlet(list_rute,dt_base,i,response.length());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else{
                    Toast.makeText(MainActivity2.this, "Gagal Mendapatkan List Team ", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity2.this, "Error Connection ", Toast.LENGTH_LONG).show();

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

    private void getOutletHos() {

        String url_server = url_get_team_hos+id_user;
        //String url_server = url_get_team_hot+13;
        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    int nmr = 0;
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            list_rute += "%27"+obj.getString("id_employee")+"%27,";
                            list_rute2 += "%27"+obj.getString("id_route")+"%27,";


                            Data_customer item = new Data_customer();
                            item.setSaluran_distribusi("%27"+obj.getString("id_employee")+"%27,");
                            item.setdt_base(obj.getString("database_name"));

                            // menambah item ke array
                            //itemList.add(item);
                            nmr++;
                            //getOutlet(list_rute,dt_base,i,response.length());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else{
                    Toast.makeText(MainActivity2.this, "Gagal Mendapatkan List Team ", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity2.this, "Error Connection ", Toast.LENGTH_LONG).show();

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

    private void getOutletSM() {

        String url_server = url_get_team_sm+id_user;
        //String url_server = url_get_team_hot+13;
        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    int nmr = 0;
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            list_rute += "%27"+obj.getString("id_employee")+"%27,";
                            list_rute2 += "%27"+obj.getString("id_route")+"%27,";

                            Data_customer item = new Data_customer();
                            item.setSaluran_distribusi("%27"+obj.getString("id_employee")+"%27,");
                            item.setdt_base(obj.getString("database_name"));

                            // menambah item ke array
                            //itemList.add(item);
                            nmr++;
                            //getOutlet(list_rute,dt_base,i,response.length());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else{
                    Toast.makeText(MainActivity2.this, "Gagal Mendapatkan List Team ", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity2.this, "Error Connection ", Toast.LENGTH_LONG).show();

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

    private void getOutletGM() {

        String url_server = url_get_team_gm+id_user;
        //String url_server = url_get_team_hot+13;
        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    int nmr = 0;
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            list_rute += "%27"+obj.getString("id_employee")+"%27,";
                            list_rute2 += "%27"+obj.getString("id_route")+"%27,";

                            Data_customer item = new Data_customer();
                            item.setSaluran_distribusi("%27"+obj.getString("id_employee")+"%27,");
                            item.setdt_base(obj.getString("database_name"));

                            // menambah item ke array
                            //itemList.add(item);
                            nmr++;
                            //getOutlet(list_rute,dt_base,i,response.length());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else{
                    Toast.makeText(MainActivity2.this, "Gagal Mendapatkan List Team ", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity2.this, "Error Connection ", Toast.LENGTH_LONG).show();

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

    private void DialogConfirmLocation(String button) {
        dialog = new AlertDialog.Builder(MainActivity2.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_filter_company, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.baseline_store_black_48);
        dialog.setTitle("Select Company");

        spinner_company =  dialogView.findViewById(R.id.spinner_company);

        list_company = new ArrayList<Data_company>();

        spinner_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Data_company sel = getCompany().get(position);
                    id_company = String.valueOf(sel.getszId());
                    nama_company = String.valueOf(sel.getszName());
                    //Toast.makeText(getApplicationContext(), "id_company "+String.valueOf(id_company)+" nama_company "+nama_company, Toast.LENGTH_LONG).show();

                }
                else{
                    id_company = "";
                    nama_company = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
                id_company = "";
                nama_company = "";
            }
        });

        getListCompany();

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save_confirm_outrange(txt_reason_outrange.getText().toString());
                post_update_company();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //kosong();
            }
        });

        dialog.show();

    }

    public void post_update_company() {
        // menyimpan login ke session
        SharedPreferences.Editor editor = Api.sharedpreferences.edit();
        editor.putString(Api.TAG_COMPANY_USER_ID, id_company);
        editor.putString(Api.TAG_COMPANY_USER_NAME, nama_company);
        editor.commit();

        Toast.makeText(getApplicationContext(), "You select "+nama_company+" as main company", Toast.LENGTH_LONG).show();
    }

    public static String formatNumber(String number){
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }

    private void getListCompany() {
        list_company.clear();

        String url_server = url_user_company+id_user;
        Log.d(TAG, "url_company: " + url_server);

        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Data_company st = new Data_company();
                            st.setszId(obj.getString("id"));
                            st.setszName(obj.getString("nama_company"));
                            list_company.add(st);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();
                    addItemsOnCompany();
                    getCompany();
                    //txt_sum_bulan_ini.setText(String.valueOf(list_company.size()));
                }
                else{

                    list_company.clear();
                    addItemsOnCompany();
                    Toast.makeText(MainActivity2.this, " Data Company tidak ditemukan ", Toast.LENGTH_LONG).show();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity2.this, "Error Connection ", Toast.LENGTH_LONG).show();
                list_company.clear();
                addItemsOnCompany();

            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    public void addItemsOnCompany() {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < list_company.size(); i++) {
            list.add(list_company.get(i).getszName());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_company.setAdapter(dataAdapter);
        if (nama_company == null || nama_company.equals("")) {
            Toast.makeText(getApplicationContext(), "Setting Your Main Company ", Toast.LENGTH_LONG).show();
        }
        else{
            int spinnerPosition2 = dataAdapter.getPosition(nama_company);
            spinner_company.setSelection(spinnerPosition2);
        }
    }

    public List<Data_company> getCompany(){
        return list_company;
    }

    private void getCompany(final String id_user) {

        itemListCompany.clear();


        String url_server = url_get_list_company+id_user;

        Log.d("list_company", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_company jv = new Data_company();

                            jv.setiInternalId(obj.getString("iInternalId"));
                            jv.setiId(obj.getString("iId"));
                            jv.setszId(obj.getString("szId"));
                            jv.setszName(obj.getString("szName"));
                            jv.setszDescription(obj.getString("szDescription"));
                            jv.setszLedgerId(obj.getString("szLedgerId"));
                            jv.setszCurrencyId(obj.getString("szCurrencyRateId"));
                            jv.setszTaxNo(obj.getString("szTaxNo"));
                            jv.setszPKPNo(obj.getString("szPKPNo"));
                            jv.setdtmNPWP(obj.getString("dtmNPWP"));
                            jv.setszNPWPNo(obj.getString("szNPWPNo"));
                            jv.setszSiup(obj.getString("szSiup"));
                            jv.setszAddress(obj.getString("szAddress"));
                            jv.setszCurrencyId(obj.getString("szCurrencyId"));
                            jv.setcodeCompany(obj.getString("codeCompany"));
                            jv.setszUserCreatedId(obj.getString("szUserCreatedId"));
                            jv.setdtmCreated(obj.getString("dtmCreated"));
                            jv.setdtmLastUpdated(obj.getString("dtmLastUpdated"));
                            jv.setnama_user(obj.getString("nama_user"));

                            itemListCompany.add(jv);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    txt_sum_bulan_ini.setText(String.valueOf(itemListCompany.size()));

                }
                else{
                    Toast.makeText(getApplicationContext(), "Tidak bisa mendapatkan data company ", Toast.LENGTH_LONG).show();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

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

    private void getTeamCompany(final String idCompany) {

        itemListTeamCompany.clear();

        String url_server = url_get_team_company+idCompany;

        Log.d("list_team", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_company jv = new Data_company();

                            jv.setszName(obj.getString("nama_user"));
                            jv.setszDescription(obj.getString("telp"));
                            jv.setdtmCreated(obj.getString("date_join"));

                            itemListTeamCompany.add(jv);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    txt_sum_hari_ini.setText(String.valueOf(itemListTeamCompany.size()));

                }
                else{
                    Toast.makeText(getApplicationContext(), "Tidak bisa mendapatkan data team", Toast.LENGTH_LONG).show();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Connection ", Toast.LENGTH_LONG).show();

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
}
