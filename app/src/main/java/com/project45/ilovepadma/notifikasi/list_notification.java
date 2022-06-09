package com.project45.ilovepadma.notifikasi;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_note2;
import com.project45.ilovepadma.adapter.adapter_notification;
import com.project45.ilovepadma.aktifitas.aktifitas_from_timeline;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.list_complain_by_notif;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.data.Data_note;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.notes.list_complain_by_note;
import com.project45.ilovepadma.notes.list_note;
import com.project45.ilovepadma.project.list_project2;
import com.project45.ilovepadma.timeline.list_timeline_by_notif;
import com.project45.ilovepadma.timeline.list_timeline_ilv2;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_notification extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_notification.CustomerListener{

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";

    String id_user, username,bm,email,id_company="",nama_company="",timeStamp="";
    ListView list;
    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    View dialogView;
    TextView textNoData;

    boolean onLoading = true;

    AlertDialog.Builder dialog;
    private static final String TAG = list_project2.class.getSimpleName();

    private static String url_get_list_notif   = Server.URL + "cust_service/list_notification_user/";

    List<Data_cust_service> itemList = new ArrayList<Data_cust_service>();
    adapter_notification adapter;

    LayoutInflater inflater;
    String tahun="",bulan="",jenis_note = "all";
    Spinner spinner_tahun,spinner_bulan,spinner_filter_project;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_notification);

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

        tahun = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        timeStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        //getSupportActionBar().setTitle("List Note " + tahun + " - " + bulan);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_report);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        adapter = new adapter_notification(list_notification.this, itemList);
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
                           callVolley(0,id_user);

                       }
                   }
        );

        list.setOnScrollListener(listOnScroll);

        //clear all notification
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    public void onRefresh() {
        onLoading = true;
        callVolley(0,id_user);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onLoading = true;
            callVolley(0,id_user);
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
            window.setStatusBarColor(getResources().getColor(R.color.cyan_400));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        onLoading = true;
        callVolley(0,id_user);
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
                callVolley(adapter.getCount(),id_user);
                //callVolley(adapter.getCount());
            }
        }


    };

    private void callVolley(final int start,final String idUser) {
        //imLoading.setVisibility(View.VISIBLE);
        //pbLoading.setVisibility(View.VISIBLE);


        if(start == 0) {
            itemList.clear();
            onLoading = true;
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
        }
        //swipe.setRefreshing(true);
        //Toast.makeText(getApplicationContext(), String.valueOf(start),Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(list_notification.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        if(onLoading==true) {
            progressDialog.show();


            String url_server = url_get_list_notif + String.valueOf(start) + "/" + idUser;
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
                                item.setkat_complain(obj.getString("kategori"));
                                item.setnmr_project(obj.getString("id_relasi"));
                                item.setsign_to(obj.getString("notification_for"));
                                item.setstatus_complain(obj.getString("status_read"));
                                item.setstatus_sent(obj.getString("status_sent"));
                                item.setid_pelanggan(obj.getString("create_by"));
                                item.setcreated_date(obj.getString("date_create"));
                                item.setnama_pelanggan(obj.getString("nama_user"));
                                item.setsubject_complain(obj.getString("email_user"));
                                item.setisi_complain(obj.getString("isi_notif"));

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
                            onLoading = true;
                        } else {

                            if (onLoading == true) {
                                Toast.makeText(list_notification.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(list_notification.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                }
            });

            // menambah request ke request queue
            AppController.getInstance(this).addToRequestQueue(jArr);
        }

    }

    @Override
    public void onCustomerOnClick(int position, Data_cust_service customer) {
        //Toast.makeText(getApplicationContext(), "kat : "+customer.getkat_complain(), Toast.LENGTH_SHORT).show();
        if(customer.getkat_complain().equals("Tiket")) {
            Intent ntfIntent = new Intent(list_notification.this, list_complain_by_notif.class);
            ntfIntent.putExtra("id_relasi",customer.getnmr_project());
            ntfIntent.putExtra("id_notif",customer.getId());
            ntfIntent.putExtra("nmr_notif",customer.getId());
            startActivity(ntfIntent);
        }
        else if(customer.getkat_complain().equals("Respon Tiket")) {
            Intent ntfIntent = new Intent(list_notification.this, list_complain_by_notif.class);
            ntfIntent.putExtra("id_relasi",customer.getnmr_project());
            ntfIntent.putExtra("id_notif",customer.getId());
            ntfIntent.putExtra("nmr_notif",customer.getId());
            startActivity(ntfIntent);
        }
        else if(customer.getkat_complain().equals("Komentar Timeline")) {
            Intent ntfIntent = new Intent(list_notification.this, list_timeline_by_notif.class);
            ntfIntent.putExtra("id_timeline",customer.getnmr_project());
            ntfIntent.putExtra("id_notif",customer.getId());
            startActivity(ntfIntent);
        }
    }
}
