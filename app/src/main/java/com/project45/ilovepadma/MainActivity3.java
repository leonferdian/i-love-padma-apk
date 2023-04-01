package com.project45.ilovepadma;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.absensi.list_absen;
import com.project45.ilovepadma.aktifitas.list_aktifitas;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.berita.detail_berita;
import com.project45.ilovepadma.berita.list_berita;
import com.project45.ilovepadma.complain.list_complain_for_you;
import com.project45.ilovepadma.complain.list_complain_from_you;
import com.project45.ilovepadma.complain.menu_complain;
import com.project45.ilovepadma.data.Data_berita;
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.notes.list_note;
import com.project45.ilovepadma.notifikasi.list_notification;
import com.project45.ilovepadma.notifikasi.notification_service;
import com.project45.ilovepadma.outlet.list_outlet;
import com.project45.ilovepadma.project.list_project2;
import com.project45.ilovepadma.setting.list_master_company;
import com.project45.ilovepadma.setting.list_team_company;
import com.project45.ilovepadma.setting.main_setting;
import com.project45.ilovepadma.timeline.add_post_everything;
import com.project45.ilovepadma.timeline.list_timeline_by_notif;
import com.project45.ilovepadma.timeline.list_timeline_by_share;
import com.project45.ilovepadma.timeline.list_timeline_ilv2;
import com.project45.ilovepadma.timeline.list_timeline_post_everything2;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

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

public class MainActivity3 extends AppCompatActivity {

    String id_user, username,email,employee_id_dms3,kategori_user="",jabatan="",list_rute="",list_rute2="",company_selected="",id_company="",nama_company="";

    private static final String TAG = MainActivity.class.getSimpleName();
    int success,last_id;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";

    private static String url_post_logout_user   = Server.URL + "survey_45/post_logout_user";
    private static String url_user_company   = Server.URL + "company/list_user_company/";
    private static String url_post_versi   = Server.URL + "tracking/post_save_version";
    private static String url_user_tiket     = Server.URL + "cust_service/list_tiket_user/";
    private static String url_dashboard_tiket     = Server.URL + "cust_service/get_ticket_for_dashboard/";
    private static String url_notification    = Server.URL + "cust_service/jml_notification_user/";
    private static String url_get_berita    = Server.URL + "berita_harian/get_berita_on_main_page/";

    private static String url_get_list_company    = Server.URL + "company/list_company/";
    private static String url_get_team_company    = Server.URL + "company/list_team_company/";

    TextView txt_nama_company,txt_nama_user,txt_jml_notification,txt_view_all_berita;
    FloatingActionButton fab_notification;
    CircularImageView image_user;
    ImageView image_setting,image_company_setting,image_notification;
    Button image_company,image_team,image_tiket;
    LinearLayout lay_agenda,lay_tiket,lay_timeline,lay_project,lay_note,lay_post_everything,layout_berita;
    CardView card_absent,card_outlet;
    ProgressBar progressBarBerita;
    LinearLayout ll_company,ll_team,ll_ticket_for_you,ll_ticket_from_you;
    TextView txt_jml_company,txt_jml_team,txt_jml_ticket_for_you,txt_jml_ticket_from_you;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    Spinner spinner_company;
    private ArrayList<Data_company> list_company;
    List<Data_cust_service> itemListTiket = new ArrayList<Data_cust_service>();

    List<Data_company> itemListCompany = new ArrayList<Data_company>();
    List<Data_company> itemListTeamCompany = new ArrayList<Data_company>();

    List<Data_berita> itemListBerita = new ArrayList<Data_berita>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity3);

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

        txt_nama_company = findViewById(R.id.txt_nama_company);
        txt_nama_user = findViewById(R.id.txt_nama_user);
        txt_jml_notification = findViewById(R.id.txt_jml_notification);
        fab_notification = findViewById(R.id.fab_notification);
        image_user = findViewById(R.id.image_user);
        image_setting = findViewById(R.id.image_setting);
        image_company_setting = findViewById(R.id.image_company_setting);
        image_notification = findViewById(R.id.image_notification);
        image_company = findViewById(R.id.image_company);
        image_team = findViewById(R.id.image_team);
        image_tiket = findViewById(R.id.image_tiket);
        lay_agenda = findViewById(R.id.lay_agenda);
        lay_tiket = findViewById(R.id.lay_tiket);
        lay_timeline = findViewById(R.id.lay_timeline);
        lay_project = findViewById(R.id.lay_project);
        lay_note = findViewById(R.id.lay_note);
        lay_post_everything = findViewById(R.id.lay_post_everything);
        txt_view_all_berita = findViewById(R.id.txt_view_all_berita);
        layout_berita = findViewById(R.id.layout_berita);
        progressBarBerita = findViewById(R.id.progressBarBerita);
        card_absent = findViewById(R.id.card_absent);
        card_outlet = findViewById(R.id.card_outlet);
        ll_company = findViewById(R.id.ll_company);
        ll_team = findViewById(R.id.ll_team);
        ll_ticket_for_you = findViewById(R.id.ll_ticket_for_you);
        ll_ticket_from_you = findViewById(R.id.ll_ticket_from_you);
        txt_jml_company = findViewById(R.id.txt_jml_company);
        txt_jml_team = findViewById(R.id.txt_jml_team);
        txt_jml_ticket_for_you = findViewById(R.id.txt_jml_ticket_for_you);
        txt_jml_ticket_from_you = findViewById(R.id.txt_jml_ticket_from_you);

        if(id_company== null || id_company.equals(""))
        {
            DialogConfirmLocation("Select");
        }
        else{
            txt_nama_company.setText(nama_company);
        }

        txt_nama_user.setText("Hi "+username );
        get_detail_user(id_user);

        txt_nama_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), account_user.class);
                startActivity(intent);
            }
        });

        image_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), account_user.class);
                startActivity(intent);
            }
        });

        image_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), main_setting.class);
                startActivity(intent);
            }
        });

        image_company_setting.setOnClickListener(new View.OnClickListener() {

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

        image_notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_notification.class);
                startActivity(intent);
            }
        });

        fab_notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_notification.class);
                startActivity(intent);
            }
        });

        lay_agenda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_aktifitas.class);
                intent.putExtra("aktifitas_dari", "you");
                startActivity(intent);
            }
        });

        lay_tiket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), menu_complain.class);
                startActivity(intent);
            }
        });

        lay_timeline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_timeline_ilv2.class);
                startActivity(intent);

            }
        });

        lay_project.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_project2.class);
                startActivity(intent);
            }
        });

        lay_note.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_note.class);
                startActivity(intent);
            }
        });

        lay_post_everything.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_timeline_post_everything2.class);
                startActivity(intent);

            }
        });
        ll_company.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_master_company.class);
                startActivity(intent);
            }
        });

        image_company.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_master_company.class);
                startActivity(intent);
            }
        });

        ll_team.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_team_company.class);
                startActivity(intent);
            }
        });

        image_team.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_team_company.class);
                startActivity(intent);
            }
        });

        ll_ticket_from_you.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_complain_from_you.class);
                startActivity(intent);
            }
        });

        image_tiket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_complain_from_you.class);
                startActivity(intent);
            }
        });

        ll_ticket_for_you.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), list_complain_for_you.class);
                startActivity(intent);
            }
        });


        //background get notifikasi
        Intent serviceIntent = new Intent(MainActivity3.this, notification_service.class);
        serviceIntent.putExtra("id_user", id_user);
        startService(serviceIntent);
        notification_service.PostNotification(MainActivity3.this);

        Uri data = this.getIntent().getData();
        if (data != null) {

            String id_timeline = data.getQueryParameter("id_timeline");

            Intent ntfIntent = new Intent(MainActivity3.this, list_timeline_by_share.class);
            ntfIntent.putExtra("id_timeline",id_timeline);
            ntfIntent.putExtra("id_notif","");
            startActivity(ntfIntent);
        }
        else{
            //Toast.makeText(MainActivity3.this, "gagal akses halaman "+action, Toast.LENGTH_SHORT).show();
        }



        /* uji coba bottom sheet*/
        /*
        txt_view_all_berita.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity3.this);

                        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_timeline,(LinearLayout)findViewById(R.id.modalBottomSheetContainer));

                        bottomSheetView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity3.this, "Folder Clicked..", Toast.LENGTH_SHORT).show();bottomSheetDialog.dismiss();
                            }
                        });

                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                    }
         });

         */
        /*  end uji coba*/

        txt_view_all_berita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), list_berita.class);
                startActivity(intent);
            }

        });

        card_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), list_absen.class);
                startActivity(intent);
            }

        });

        card_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), list_outlet.class);
                startActivity(intent);
            }

        });

        String versionName = BuildConfig.VERSION_NAME;
        //Toast.makeText(getApplicationContext(), "versionName "+versionName, Toast.LENGTH_LONG).show();
        proc_version(versionName);

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.cyan_400));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCompany(id_user);
        if(id_company!= null) {
            getTeamCompany(id_company);
            //getTiketUser(id_user,id_company);
            getNotification(id_user);
            get_dashboard_tiket(id_user,id_company);
            getBerita(id_company);
        }
    }

    private void DialogConfirmLocation(String button) {
        dialog = new AlertDialog.Builder(MainActivity3.this);
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

        txt_nama_company.setText(nama_company);
        Toast.makeText(getApplicationContext(), "You select "+nama_company+" as main company", Toast.LENGTH_LONG).show();
        getTeamCompany(id_company);
        //getTiketUser(id_user,id_company);
        get_dashboard_tiket(id_user,id_company);
        getBerita(id_company);
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
                    Toast.makeText(MainActivity3.this, " Data Company tidak ditemukan ", Toast.LENGTH_LONG).show();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity3.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

                    image_company.setText(String.valueOf(itemListCompany.size()));
                    txt_jml_company.setText(String.valueOf(itemListCompany.size()));

                }
                else{
                    //Toast.makeText(getApplicationContext(), "Tidak bisa mendapatkan data company ", Toast.LENGTH_LONG).show();
                    image_company.setText("0");
                    txt_jml_company.setText("0");

                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                image_company.setText("0");
                txt_jml_company.setText("0");
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

                    image_team.setText(String.valueOf(itemListTeamCompany.size()));
                    txt_jml_team.setText(String.valueOf(itemListTeamCompany.size()));
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Tidak bisa mendapatkan data team", Toast.LENGTH_LONG).show();
                    image_team.setText("0");
                    txt_jml_team.setText("0");
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Connection ", Toast.LENGTH_LONG).show();
                image_team.setText("0");
                txt_jml_team.setText("0");
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

    private void getTiketUser(final String idUser,final String idCompany) {

        itemListTiket.clear();

        String url_server = url_user_tiket+idUser+"/"+idCompany;

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

                            Data_cust_service item = new Data_cust_service();

                            item.setId(obj.getString("id"));
                            item.setid_pelanggan(obj.getString("id_pelanggan"));
                            item.setnama_pelanggan(obj.getString("nama_pelanggan"));
                            item.setjenis_complain(obj.getString("jenis_complain"));
                            item.setsubject_complain(obj.getString("subject"));

                            itemListTiket.add(item);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    image_tiket.setText(String.valueOf(itemListTiket.size()));

                }
                else{
                    //Toast.makeText(getApplicationContext(), "Tidak bisa mendapatkan data tiket", Toast.LENGTH_LONG).show();
                    image_tiket.setText("0");

                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Connection ", Toast.LENGTH_LONG).show();
                image_tiket.setText("0");
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

    private void get_detail_user(final String id_user){
        String url_edit       = Server.URL + "get_detail_user/"+id_user;
        Log.d("list_account", url_edit);
        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String alamat      = jObj.getString("alamat");
                        String phone    = jObj.getString("phone");
                        String ktp  = jObj.getString("ktp");
                        String kode_user  = jObj.getString("kode_user");
                        String foto_pribadi  = jObj.getString("foto_pribadi");
                        String nik  = jObj.getString("nik");
                        String email_atasan  = jObj.getString("email_atasan");
                        //Toast.makeText(account_user.this, jabatan, Toast.LENGTH_LONG).show();


                        if(!foto_pribadi.equals("")){

                            String bm =Server.URL2 +"image_upload/list_foto_pribadi/"+foto_pribadi;
                            Picasso.with(getApplicationContext())
                                    .load(bm)
                                    //.fit()
                                    .resize(100, 100)
                                    .onlyScaleDown()
                                    .centerCrop()
                                    .into(image_user);
                        }
                        else{
                            Picasso.with(getApplicationContext())
                                    .load(R.drawable.dummy_pic)
                                    .fit()
                                    .centerCrop()
                                    .into(image_user);
                        }
                        //adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(MainActivity3.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity3.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private void getNotification(final String idUser) {


        String url_server = url_notification+idUser;

        Log.d("list_team", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    txt_jml_notification.setVisibility(View.VISIBLE);
                    fab_notification.setVisibility(View.VISIBLE);
                    txt_jml_notification.setText(String.valueOf(response.length()));

                }
                else{
                    //Toast.makeText(getApplicationContext(), "Tidak bisa mendapatkan data team", Toast.LENGTH_LONG).show();
                    txt_jml_notification.setVisibility(View.GONE);
                    fab_notification.setVisibility(View.GONE);
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Connection ", Toast.LENGTH_LONG).show();
                txt_jml_notification.setVisibility(View.GONE);
                fab_notification.setVisibility(View.GONE);
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

    private void getBerita(final String idCompany) {

        itemListBerita.clear();
        layout_berita.removeAllViews();
        progressBarBerita.setVisibility(View.VISIBLE);

        String url_server = url_get_berita+idCompany;

        Log.d("list_berita", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_berita jv = new Data_berita();

                            jv.setid(obj.getString("id"));
                            jv.setjudul_berita(obj.getString("judul_berita"));
                            jv.setisi_berita(obj.getString("isi_berita"));
                            jv.setgambar_berita(obj.getString("gambar_berita"));
                            jv.settgl_berita(obj.getString("tgl_berita"));
                            jv.setnama_user(obj.getString("nama_user"));
                            jv.setfoto_user(obj.getString("foto_user"));

                            itemListBerita.add(jv);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                    callListBerita();

                    progressBarBerita.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(MainActivity3.this, "Data Berita Kosong ", Toast.LENGTH_LONG).show();
                    progressBarBerita.setVisibility(View.GONE);


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity3.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressBarBerita.setVisibility(View.GONE);

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

    private void callListBerita(){
        if (getApplicationContext()!=null) {

            for (int i = 0; i < itemListBerita.size(); i++) {
                final Data_berita sel = itemListBerita.get(i);

                LayoutInflater inflater = LayoutInflater.from(MainActivity3.this);
                View v = inflater.inflate(R.layout.item_berita_main, null, false);
                ImageView image_berita = v.findViewById(R.id.image_berita);
                TextView judul_berita = v.findViewById(R.id.judul_berita);
                TextView isi_berita = v.findViewById(R.id.isi_berita);
                LinearLayout lay_main_berita = v.findViewById(R.id.lay_main_berita);

                judul_berita.setText(sel.getjudul_berita());
                //isi_berita.setText(sel.getisi_berita());
                isi_berita.setText(Html.fromHtml(sel.getisi_berita(),new URLImageParser(isi_berita, MainActivity3.this),null));

                String img = Server.URL2 + "inc/berita/file_gambar/"+sel.getgambar_berita();

                Picasso.with(image_berita.getContext())
                        .load(img)
                        .fit()
                        .placeholder(R.drawable.default_photo)
                        //.resize(300, 100)
                        //.onlyScaleDown()
                        .centerCrop()
                        .into(image_berita);

                lay_main_berita.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // update login session ke FALSE dan mengosongkan nilai id dan username
                        //Toast.makeText(MainActivity3.this, "judul "+sel.getjudul_berita(), Toast.LENGTH_SHORT).show();
                        final String PARAM_DATA = "Data";
                        Intent intent = new Intent(MainActivity3.this, detail_berita.class);
                        intent.putExtra("act", "detail_berita");
                        intent.putExtra(PARAM_DATA, sel);
                        startActivityForResult(intent,1);
                    }
                });

                layout_berita.addView(v);
            }
        }
    }

    private void get_dashboard_tiket(final String idUser,final String idCompany){
        String url_edit       = url_dashboard_tiket+idUser+"/"+idCompany;
        Log.d("dashboard_tiket", url_edit);
        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String from_you_all      = jObj.getString("from_you_all");
                        String from_you_close    = jObj.getString("from_you_close");
                        String for_you_all  = jObj.getString("for_you_all");
                        String for_you_close  = jObj.getString("for_you_close");

                        txt_jml_ticket_from_you.setText(from_you_close+" / "+from_you_all);
                        txt_jml_ticket_for_you.setText(for_you_close+" / "+for_you_all);

                        //adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(MainActivity3.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        txt_jml_ticket_from_you.setText("0 / 0");
                        txt_jml_ticket_for_you.setText("0 / 0");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    txt_jml_ticket_from_you.setText("0 / 0");
                    txt_jml_ticket_for_you.setText("0 / 0");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity3.this, error.getMessage(), Toast.LENGTH_LONG).show();
                txt_jml_ticket_from_you.setText("0 / 0");
                txt_jml_ticket_for_you.setText("0 / 0");
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private void proc_version(final String app_version){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_versi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Save_version", jObj.toString());


                    } else {
                        Log.d("Save_version", jObj.toString());
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Save version Error: " + error.getMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", id_user);
                params.put("id_company", id_company);
                params.put("app_version", app_version);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);


    }
}
