package com.project45.ilovepadma.complain;

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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project45.ilovepadma.ImageTextAdapter;
import com.project45.ilovepadma.ItemImageText;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_cust_service2;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.notes.list_complain_by_note;
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

public class list_complain_from_you extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_cust_service2.CustomerListener {
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

    boolean onLoading = true;

    AlertDialog.Builder dialog;
    private static final String TAG = list_complain_from_you.class.getSimpleName();

    //private static String url_select     = Server.URL + "cust_service/list_padma_complain/";
    private static String url_select     = Server.URL + "cust_service/list_padma_complain2/";
    private static String url_select_complain_with_filter     = Server.URL + "cust_service/list_padma_complain_by_filter/";

    List<Data_cust_service> itemList = new ArrayList<Data_cust_service>();

    adapter_cust_service2 adapter;

    LayoutInflater inflater;
    String tahun="",bulan="",complain_status = "all";
    Spinner spinner_tahun,spinner_bulan,spinner_filter_complain;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_padma_tiket);

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

        Toast.makeText(getApplicationContext(), "Company Selected "+nama_company, Toast.LENGTH_SHORT).show();

        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        //Toast.makeText(list_cust_service.this, "name "+username, Toast.LENGTH_SHORT).show();

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        adapter = new adapter_cust_service2(list_complain_from_you.this, itemList);
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
                           callVolley(0);

                       }
                   }
        );

        list.setOnScrollListener(listOnScroll);

    }

    public void onRefresh() {
        callVolley(0);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            onLoading = true;
            callVolley(0);
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
            window.setStatusBarColor(getResources().getColor(R.color.page_cust_service));
        }
    }

    public void onRadioLBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_leader_all:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "aal ", Toast.LENGTH_LONG).show();
                    onLoading = true;
                    complain_status = "all";
                    callVolley(0);
                break;
            case R.id.radio_leader_open:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_LONG).show();
                    onLoading = true;
                    complain_status = "OPEN";
                    callVolley(0);
                break;
            case R.id.radio_leader_accept:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    onLoading = true;
                    complain_status = "ACCEPT";
                    callVolley(0);
                break;
            case R.id.radio_leader_proses:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "proses", Toast.LENGTH_LONG).show();
                    onLoading = true;
                    complain_status = "PROSES";
                    callVolley(0);

                break;
            case R.id.radio_leader_finish:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_LONG).show();
                    onLoading = true;
                    complain_status = "FINISH";
                    callVolley(0);
                break;
            case R.id.radio_leader_close:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "close", Toast.LENGTH_LONG).show();
                    onLoading = true;
                    complain_status = "CLOSE";
                    callVolley(0);
                break;
        }
    }

    private void callVolley(final int start) {
        //imLoading.setVisibility(View.VISIBLE);
        //pbLoading.setVisibility(View.VISIBLE);
        final ProgressDialog progressDialog = new ProgressDialog(list_complain_from_you.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        //progressDialog.show();

        if(start == 0) {
            itemList.clear();
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
        }
        //swipe.setRefreshing(true);
        //Toast.makeText(getApplicationContext(), String.valueOf(start),Toast.LENGTH_LONG).show();
        if(onLoading==true) {
            progressDialog.show();
            String url_server = url_select + String.valueOf(start) + "/" + id_user + "/" + id_company+"/"+complain_status;
            Log.d("url_server", url_server);
            //Toast.makeText(getApplicationContext(), String.valueOf(url_server),Toast.LENGTH_LONG).show();
            // membuat request JSON
            JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());

                    if (response.length() > 0) {
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
                                item.setdue_date(obj.getString("due_date"));
                                item.setperiode_awal(obj.getString("periode_awal"));
                                item.setperiode_akhir(obj.getString("periode_akhir"));
                                item.setisi_complain(obj.getString("isi_complain"));
                                item.setcreated_date(obj.getString("date_create"));
                                item.setfoto_pelanggan(obj.getString("file_image"));
                                item.setsign_to(obj.getString("sign_to"));
                                item.setstatus_complain(obj.getString("status_complain"));
                                item.setkat_complain(obj.getString("kat_complain"));
                                item.setnmr_project(obj.getString("nmr_project"));
                                item.setnmr_sub_project(obj.getString("nmr_sub_project"));
                                item.setl1(obj.getString("l1"));
                                item.setl2(obj.getString("l2"));
                                item.setl3(obj.getString("l3"));
                                item.setpic2(obj.getString("pic2"));
                                item.setjml_respon(obj.getString("respon"));
                                item.setrespon_new(obj.getString("respon_new"));

                                // menambah item ke array
                                itemList.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        onLoading = true;
                    } else {
                        if (start == 0) {
                            textNoData.setVisibility(View.VISIBLE);
                        } else {
                            if (onLoading == true) {
                                Toast.makeText(getApplicationContext(), "No More Items Available For ", Toast.LENGTH_SHORT).show();
                            }
                            onLoading = false;
                        }
                    }

                    // notifikasi adanya perubahan data pada adapter
                    adapter.notifyDataSetChanged();

                    swipe.setRefreshing(false);
                    //pbLoading.setVisibility(View.GONE);
                    //imLoading.setVisibility(View.GONE);
                    progressDialog.dismiss();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    swipe.setRefreshing(false);
                    progressDialog.dismiss();
                    Toast.makeText(list_complain_from_you.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                }
            });

            // menambah request ke request queue
            AppController.getInstance(this).addToRequestQueue(jArr);
        }

    }

    private void listComplainWithFilter(final String tahunn, final String bulann, final String status_complain) {
        //imLoading.setVisibility(View.VISIBLE);
        //pbLoading.setVisibility(View.VISIBLE);
        onLoading = false;
        itemList.clear();
        textNoData.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(list_complain_from_you.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();


        //swipe.setRefreshing(true);
        //Toast.makeText(getApplicationContext(), String.valueOf(start),Toast.LENGTH_LONG).show();

        String url_server       = url_select_complain_with_filter+id_user+"/"+tahunn+"/"+bulann+"/"+status_complain+"/"+id_company;
        Log.d("url_server", url_server);
        // membuat request JSON
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
                            item.setdue_date(obj.getString("due_date"));
                            item.setperiode_awal(obj.getString("periode_awal"));
                            item.setperiode_akhir(obj.getString("periode_akhir"));
                            item.setisi_complain(obj.getString("isi_complain"));
                            item.setcreated_date(obj.getString("date_create"));
                            item.setfoto_pelanggan(obj.getString("file_image"));
                            item.setsign_to(obj.getString("sign_to"));
                            item.setstatus_complain(obj.getString("status_complain"));
                            item.setkat_complain(obj.getString("kat_complain"));
                            item.setnmr_project(obj.getString("nmr_project"));
                            item.setnmr_project(obj.getString("nmr_project"));
                            item.setl1(obj.getString("l1"));
                            item.setl2(obj.getString("l2"));
                            item.setl3(obj.getString("l3"));
                            item.setpic2(obj.getString("pic2"));
                            item.setjml_respon(obj.getString("respon"));
                            item.setrespon_new(obj.getString("respon_new"));

                            // menambah item ke array
                            itemList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    textNoData.setVisibility(View.VISIBLE);
                    Toast.makeText(list_complain_from_you.this, "Data komplain kosong ", Toast.LENGTH_SHORT).show();

                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
                //pbLoading.setVisibility(View.GONE);
                //imLoading.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                progressDialog.dismiss();
                Toast.makeText(list_complain_from_you.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    @Override
    public void onCustomerOnClick(int position, Data_cust_service customer) {
        showEditCustomerDialog(customer);
    }

    void showEditCustomerDialog(final Data_cust_service customer) {

        final String PARAM_DATA = "Data";
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        final ItemImageText[] items = {
                new ItemImageText("Detail", "", "")
        };

        ImageTextAdapter imageTextAdapter = new ImageTextAdapter(this);
        imageTextAdapter.addAll(items);

        adb.setTitle("");
        final ItemImageText[] finalItems = items;

        adb.setAdapter(imageTextAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int n) {
                if(items[n].text.equals("Detail")){


                    Intent intent = new Intent(list_complain_from_you.this, detail_complain.class);
                    intent.putExtra("act", "detail_complain");
                    intent.putExtra(PARAM_DATA, customer);
                    startActivityForResult(intent,1);




                }
            }

            });
            //adb.setNegativeButton("Cancel", null);
        adb.show();
    }

    private Scroller listOnScroll = new Scroller() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        @Override
        public void scrollCompleted(int currentFirstVisibleItem, int currentVisibleItemCount, int scrollState) {
            //super.scrollCompleted(currentFirstVisibleItem, currentVisibleItemCount, scrollState);
            if(currentFirstVisibleItem + currentVisibleItemCount >= (adapter.getCount() - 2)){
                if (onLoading == true) {
                    callVolley(adapter.getCount());
                    //callVolley(adapter.getCount());
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Menerapkan menu terpilih di menu-> empat.xml
        getMenuInflater().inflate(R.menu.menu_list_cust_service, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.add_complain_menu) {
            //Toast.makeText(list_complain.this, "Ini menu add", Toast.LENGTH_LONG).show();
            //return true;

            Intent intent = new Intent(list_complain_from_you.this, add_complain.class);
            //intent.putExtra("act", "edit_outlet");
            intent.putExtra("act", "add_complain");
            startActivityForResult(intent,1);


        }
        else if(id == R.id.filter_report_sales){
            DialogFilter("Filter");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_complain_from_you.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_filter_complain, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Complain");

        spinner_tahun = dialogView.findViewById(R.id.spinner_tahun);
        spinner_bulan = dialogView.findViewById(R.id.spinner_bulan);
        spinner_filter_complain = dialogView.findViewById(R.id.spinner_filter_complain);

        addItemsOnSpinnerTahun(spinner_tahun);
        addItemsOnSpinnerBulan(spinner_bulan);

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tahun = String.valueOf(spinner_tahun.getSelectedItem());
                bulan = String.valueOf(spinner_bulan.getSelectedItem());
                complain_status = String.valueOf(spinner_filter_complain.getSelectedItem());

                listComplainWithFilter(tahun,bulan,complain_status);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "Tes", Toast.LENGTH_SHORT).show();
                onLoading = true;
                callVolley(0);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
