package com.project45.ilovepadma.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_cust_service;
import com.project45.ilovepadma.adapter.adapter_project;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.detail_complain;
import com.project45.ilovepadma.complain.list_complain;
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_project2 extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_project.ProjectListener {

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";

    String id_user, username,bm,email,id_company="",nama_company="";
    ListView list;
    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    View dialogView;
    TextView textNoData;

    AlertDialog.Builder dialog;
    private static final String TAG = list_project2.class.getSimpleName();

    private static String url_get_list_project    = Server.URL + "project/list_project_by_user3/";

    List<Data_company> itemList = new ArrayList<Data_company>();
    adapter_project adapter;

    LayoutInflater inflater;
    String tahun="",bulan="",project_kat = "Digitalisasi";
    Spinner spinner_tahun,spinner_bulan,spinner_filter_project;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_project3);

        Intent i = getIntent();
        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_list_cust_service);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        changeStatusBarColor();

        //locationMangaer = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        //Toast.makeText(list_cust_service.this, "name "+username, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Company Selected "+nama_company, Toast.LENGTH_SHORT).show();

        //getSupportActionBar().setTitle("List Project "+tahun+" - "+bulan);
        getSupportActionBar().setTitle("List Project ");

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        adapter = new adapter_project(list_project2.this, itemList);
        list.setAdapter(adapter);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);
        // menamilkan widget refresh
        swipe.setOnRefreshListener(swipeRefreshDo);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //swipe.setRefreshing(true);
                           //itemList.clear();
                           //adapter.notifyDataSetChanged();
                           getProject(id_company,id_user,project_kat,tahun,bulan);

                       }
                   }
        );


    }

    public void onRefresh() {
        getProject(id_company,id_user,project_kat,tahun,bulan);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getProject(id_company,id_user,project_kat,tahun,bulan);
        }
    };

    public boolean onSupportNavigateUp(){
        //onBackPressed();
        onBackPressed();
        return true;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_item) {

            Intent intent = new Intent(list_project2.this, add_project.class);
            //intent.putExtra("act", "edit_outlet");
            intent.putExtra("act", "add_project");
            startActivityForResult(intent,1);
            return true;
        }
        /*
        else if (id == R.id.filter_report_sales) {
            //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            DialogFilter("Filter");
        }
        */

        return super.onOptionsItemSelected(item);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_project2.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_filter_project, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Project");

        spinner_tahun = dialogView.findViewById(R.id.spinner_tahun);
        spinner_bulan = dialogView.findViewById(R.id.spinner_bulan);
        spinner_filter_project = dialogView.findViewById(R.id.spinner_filter_project);

        addItemsOnSpinnerTahun(spinner_tahun);
        addItemsOnSpinnerBulan(spinner_bulan);

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tahun = String.valueOf(spinner_tahun.getSelectedItem());
                bulan = String.valueOf(spinner_bulan.getSelectedItem());
                project_kat = String.valueOf(spinner_filter_project.getSelectedItem());

                getSupportActionBar().setTitle("List Project "+tahun+" - "+bulan);

                getProject(id_company,id_user,project_kat,tahun,bulan);
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

    public void addItemsOnSpinnerTahun(final Spinner spinner_thn) {
        List<String> list = new ArrayList<String>();
        int tahun_lalu = Integer.valueOf(tahun)-1;
        for (int i = tahun_lalu; i <= Integer.valueOf(tahun); i++) {
            list.add(String.valueOf(i));
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_thn.setAdapter(dataAdapter);
        int spinnerPosition2 = dataAdapter.getPosition(tahun);
        spinner_thn.setSelection(spinnerPosition2);
    }

    public void addItemsOnSpinnerBulan(final Spinner spinner_bln) {
        List<String> list = new ArrayList<String>();

        for (int i = 1; i <=12; i++) {
            list.add(String.valueOf(i));
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bln.setAdapter(dataAdapter);
        int spinnerPosition2 = dataAdapter.getPosition(bulan);
        spinner_bln.setSelection(spinnerPosition2);
    }

    public void onRadioLBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_leader_digitalisasi:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "aal ", Toast.LENGTH_LONG).show();
                    project_kat = "Digitalisasi";
                getProject(id_company,id_user,project_kat,tahun,bulan);
                break;
            case R.id.radio_leader_reguler:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_LONG).show();
                    project_kat = "Reguler";
                getProject(id_company,id_user,project_kat,tahun,bulan);
                break;
            case R.id.radio_leader_katalog:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    project_kat = "all";
                getProject(id_company,"all",project_kat,tahun,bulan);
                break;

        }
    }

    private void getProject(final String idCompany,final String idUser,final String jnsProject,final String thn,final String bln) {

        itemList.clear();
        textNoData.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(list_project2.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_list_project+idCompany+"/"+idUser+"/"+jnsProject+"/"+thn+"/"+bln;

        Log.d("list_project", url_server);
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

                            jv.setszLedgerId(obj.getString("id"));
                            jv.setszCurrencyId(obj.getString("nmr_project"));
                            jv.setiInternalId(obj.getString("nama_project"));
                            jv.setiId(obj.getString("jenis_project"));
                            jv.setdivisi(obj.getString("divisi"));
                            jv.setszId(obj.getString("main_project"));
                            jv.setszName(obj.getString("sub_project"));
                            jv.setszDescription(obj.getString("date_create"));
                            jv.setszUserCreatedId(obj.getString("create_by"));
                            jv.setmember(obj.getString("member"));
                            jv.setstatus_project(obj.getString("status_project"));
                            jv.setnama_divisi(obj.getString("nama_divisi"));
                            jv.settotal_selesai(obj.getString("total_selesai"));
                            jv.settask_list(obj.getString("task_list"));
                            jv.settipe_open(obj.getString("tipe_open"));

                            itemList.add(jv);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // notifikasi adanya perubahan data pada adapter
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    //Toast.makeText(getApplicationContext(), "Klik Nama project untuk melihat detail ", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(list_project2.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    textNoData.setVisibility(View.VISIBLE);
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_project2.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                swipe.setRefreshing(false);
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

    @Override
    public void onProjectClick(int position, Data_company project) {

        //intent.putExtra("act", "edit_outlet");
        final String szId = project.getszLedgerId();
        final String nmr_project = project.getszCurrencyId();
        final String createBy = project.getszUserCreatedId();
        final String member = project.getmember();

        if(project.gettipe_open().equals("non_katalog")) {
            if (project.getszUserCreatedId().equals(id_user)) {

                Intent intent = new Intent(list_project2.this, edit_project.class);
                intent.putExtra("act", "edit_project");
                intent.putExtra("id_project", nmr_project);
                intent.putExtra("nmr_project", szId);
                intent.putExtra("create_by", createBy);
                intent.putExtra("member", member);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(list_project2.this, edit_project2.class);
                //intent.putExtra("act", "edit_outlet");
                intent.putExtra("act", "edit_project");
                intent.putExtra("id_project", nmr_project);
                intent.putExtra("nmr_project", nmr_project);
                intent.putExtra("create_by", "");
                intent.putExtra("member", member);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    public void onSubProjectClick(int position, Data_company project) {
        final String szId = project.getszCurrencyId();
        Intent intent = new Intent(list_project2.this, list_sub_project.class);
        intent.putExtra("nmr_project", szId);
        intent.putExtra("jenis_project", project.getiId());
        intent.putExtra("divisi", project.getdivisi());
        intent.putExtra("nama_project", project.getiInternalId());
        intent.putExtra("main_project", project.getszId());
        startActivityForResult(intent,1);
    }

    @Override
    public void onTiketClick(int position, Data_company project) {
        final String szId = project.getszCurrencyId();
        Intent intent = new Intent(list_project2.this, list_complain_by_project.class);
        intent.putExtra("nmr_project", szId);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(list_join_visit.this, "back  ", Toast.LENGTH_SHORT).show();
                getProject(id_company,id_user,project_kat,tahun,bulan);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
