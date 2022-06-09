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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.ImageTextAdapter;
import com.project45.ilovepadma.ItemImageText;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_cust_service2;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_complain_by_notif extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_cust_service2.CustomerListener {
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    String id_user, username,bm,email,id_relasi="",id_notif="",nmr_notif="";
    ListView list;
    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    View dialogView;
    TextView textNoData;

    AlertDialog.Builder dialog;
    private static final String TAG = list_complain_by_notif.class.getSimpleName();

    private static String url_select     = Server.URL + "cust_service/list_padma_complain_by_notif/";
    private static String url_post_status_notification     = Server.URL + "cust_service/post_edit_status_notification";

    List<Data_cust_service> itemList = new ArrayList<Data_cust_service>();
    adapter_cust_service2 adapter;

    LayoutInflater inflater;
    String tahun="",bulan="",complain_status = "all";
    Spinner spinner_tahun,spinner_bulan,spinner_filter_complain;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_cust_service);

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

        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

        //Toast.makeText(list_cust_service.this, "name "+username, Toast.LENGTH_SHORT).show();

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        adapter = new adapter_cust_service2(list_complain_by_notif.this, itemList);
        list.setAdapter(adapter);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        Intent intentku = getIntent(); // gets the previously created intent
        id_relasi = intentku.getStringExtra("id_relasi");
        id_notif = intentku.getStringExtra("id_notif");
        nmr_notif = intentku.getStringExtra("nmr_notif");
        //Toast.makeText(list_complain_by_notif.this, "id_relasi "+id_relasi+" id_notif "+id_notif, Toast.LENGTH_SHORT).show();
        postStatusNotifikasi();

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

    private void callVolley(final int start) {
        //imLoading.setVisibility(View.VISIBLE);
        //pbLoading.setVisibility(View.VISIBLE);
        final ProgressDialog progressDialog = new ProgressDialog(list_complain_by_notif.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        if(start == 0) {
            itemList.clear();
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
        }
        //swipe.setRefreshing(true);
        //Toast.makeText(getApplicationContext(), String.valueOf(start),Toast.LENGTH_LONG).show();

        String url_server       = url_select+ String.valueOf(start)+"/"+id_relasi+"/"+id_user;
        Log.d("url_server", url_server);
        //Toast.makeText(getApplicationContext(), String.valueOf(url_server),Toast.LENGTH_LONG).show();
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
                            item.setnmr_sub_project(obj.getString("nmr_sub_project"));
                            item.setpic2(obj.getString("pic2"));
                            item.setjml_respon(obj.getString("respon"));
                            item.setrespon_new(obj.getString("respon_new"));
                            item.settiket_creator(obj.getString("tiket_creator"));

                            // menambah item ke array
                            itemList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    if(start == 0) {
                        textNoData.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(list_complain_by_notif.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(list_complain_by_notif.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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

                    Intent intent = new Intent(list_complain_by_notif.this, detail_complain_for_you.class);
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
                callVolley(adapter.getCount());
                //callVolley(adapter.getCount());
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "Tes", Toast.LENGTH_SHORT).show();
                callVolley(0);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void postStatusNotifikasi(){
        //proses save to database using api
        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_status_notification, new Response.Listener<String>() {

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




                    } else {
                        Toast.makeText(getApplicationContext(),
                                "error " + jObj.getString("Fail to edit notification"), Toast.LENGTH_LONG).show();


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


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_notification", id_notif);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }
}
