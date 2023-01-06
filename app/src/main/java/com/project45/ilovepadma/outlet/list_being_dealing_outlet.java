package com.project45.ilovepadma.outlet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.adapter.adapter_customer3;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_customer;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
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

public class list_being_dealing_outlet extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_customer3.AdapterCallback{
    private static final String TAG = list_profiling_reject_outlet.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static String url_select_being_dealing_oultet = Server.URL + "outlet/list_being_dealing_outlet/";
    private static String url_post_save_result_dealing_outlet = Server.URL + "outlet/post_save_result_dealing_outlet";
    AlertDialog.Builder dialog;
    EditText edt_search, txt_reason_reject, txt_id_outlet;
    FrameLayout mFrameLayout;
    ImageView im_search;
    LayoutInflater inflater;
    TextView textNoData;
    View dialogView;
    adapter_customer3 adapter;
    GPSTracker gps;
    Spinner spinner_reason_dealing;
    List<Data_customer> itemList = new ArrayList<Data_customer>();
    ListView list;
    SwipeRefreshLayout swipe;
    boolean onLoading = true;
    double latitude,longitude;
    int success;
    String id_user,username,email,jabatan,id_company="",nama_company="",alamat="", filter_outlet = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_being_dealing_outlet);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_list_jv);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        edt_search = findViewById(R.id.edt_search);
        im_search = findViewById(R.id.im_search);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_report);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        Iconify.with(new FontAwesomeModule());

        gps = new GPSTracker(list_being_dealing_outlet.this, list_being_dealing_outlet.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if(gps.setAddressFromLatLng()!=null) {
                alamat = gps.setAddressFromLatLng();
            }
            else{
                alamat ="Address Undetected";
            }
        }
        else{
            latitude = 0;
            longitude = 0;
        }

        adapter = new adapter_customer3(list_being_dealing_outlet.this,itemList, list_being_dealing_outlet.this);
        list.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(swipeRefreshDo);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //swipe.setRefreshing(true);
                           //itemList.clear();
                           //adapter.notifyDataSetChanged();
                           callVolley(0,latitude,longitude,filter_outlet);
                       }
                   }
        );

        list.setOnScrollListener(listOnScroll);

        im_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username

                filter_outlet = edt_search.getText().toString();
                if(filter_outlet.equals("")){
                    showDialog();
                    filter_outlet = "All";
                }
                else{
                    callVolley(0,latitude,longitude,filter_outlet);
                }

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
            window.setStatusBarColor(getResources().getColor(R.color.black_purple));
        }
    }

    @Override
    public void onRefresh() {
        onLoading = true;
        callVolley(0,latitude,longitude,filter_outlet);
    }

    @Override
    public void onCustomerOnClick(int position, Data_customer customer) {
        add_result_dealing_dialog(customer.getcode_customer());
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onLoading = true;
            callVolley(0,latitude,longitude,filter_outlet);
        }
    };

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
                callVolley(adapter.getCount(),latitude,longitude,filter_outlet);
                //callVolley(adapter.getCount());
            }
        }
    };

    private void callVolley(final int start,final double lt,final double lg,final String filterOutlet) {
        if (start == 0) {
            itemList.clear();
            onLoading = true;
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
        }
        //swipe.setRefreshing(true);
        //Toast.makeText(getApplicationContext(), String.valueOf(start),Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(list_being_dealing_outlet.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");

        if(onLoading==true) {
            progressDialog.show();


            String url_server = url_select_being_dealing_oultet + String.valueOf(start)+"/"+lt+"/"+lg + "/" + filterOutlet;
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

                                Data_customer item = new Data_customer();

                                item.setId(obj.getInt("id"));
                                item.setcode_customer(obj.getString("id_outlet"));
                                item.setName(obj.getString("nama_outlet"));
                                item.setLat(obj.getDouble("latitude"));
                                item.setLng(obj.getDouble("longitude"));
                                item.setAddress(obj.getString("alamat"));
                                item.setKelurahan(obj.getString("kelurahan"));
                                item.setKecamatan(obj.getString("kecamatan"));
                                item.setZip_code(obj.getString("zipcode"));
                                item.setCity(obj.getString("city"));
                                item.setProvince(obj.getString("province"));
                                item.setCountry(obj.getString("country"));
                                item.setkoreksi_alamat(obj.getString("koreksi_alamat"));
                                item.setSegment_tiv(obj.getString("segment_tiv"));
                                item.setOutlet_type(obj.getString("channel_outlet"));
                                item.setSaluran_distribusi(obj.getString("segment"));
                                item.setSegment_level_1(obj.getString("sub_segment"));
                                item.setimage_customer(obj.getString("foto_outlet"));
                                item.setStatus_general(obj.getString("status_general"));
                                item.setStatus_detail(obj.getString("status_detail"));
                                item.setcreate_by(obj.getString("create_by"));
                                item.setOwner_name(obj.getString("create_by_name"));
                                item.setdate_create(obj.getString("date_create"));

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
                                Toast.makeText(list_being_dealing_outlet.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(list_being_dealing_outlet.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                }
            });

            // menambah request ke request queue
            AppController.getInstance(this).addToRequestQueue(jArr);
        }

    }

    private void showDialog(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                list_being_dealing_outlet.this);

        // set title dialog
        alertDialogBuilder.setTitle("Warning");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Text Search tidak boleh kosong")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something
                        dialogInterface.cancel();
                    }
                });

        // membuat alert dialog dari builder
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "Tes", Toast.LENGTH_SHORT).show();
                onLoading = true;
                callVolley(0,latitude,longitude,filter_outlet);
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void add_result_dealing_dialog(String id_outlet) {
        dialog = new AlertDialog.Builder(list_being_dealing_outlet.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_result_dealing_outlet, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Add Result Dealing");

        txt_id_outlet = dialogView.findViewById(R.id.txt_id_outlet);
        spinner_reason_dealing = dialogView.findViewById(R.id.spinner_reason_dealing);
        txt_reason_reject = dialogView.findViewById(R.id.txt_reason_reject);

        txt_id_outlet.setText(id_outlet);

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog2 = dialog.create();
        dialog2.show();

        dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(spinner_reason_dealing.getSelectedItem().toString().equals("Reject") && TextUtils.isEmpty(txt_reason_reject.getText())){
                    Toast.makeText(getApplicationContext(), "Wajib mengisi Reason Reject!", Toast.LENGTH_LONG).show();
                } else {
                    save_result_dealing_outlet(txt_id_outlet.getText().toString(),spinner_reason_dealing.getSelectedItem().toString(),txt_reason_reject.getText().toString());
                    dialog2.dismiss();
                }
            }
        });
    }

    private void save_result_dealing_outlet(String id_outlet,String result_dealing,String reason){
        final ProgressDialog progressDialog = new ProgressDialog(list_being_dealing_outlet.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_save_result_dealing_outlet , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "save Dealing outlet: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Log.e("Success dealing outlet", jObj.toString());
                        Toast.makeText(getApplicationContext(), "Sukses Save Result Dealing Outlet", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        callVolley(0,latitude,longitude,filter_outlet);
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "save Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_outlet", id_outlet);
                params.put("result_dealing", result_dealing);
                params.put("reason", reason);
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
}