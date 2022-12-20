package com.project45.ilovepadma.outlet;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.project45.ilovepadma.ImageTextAdapter;
import com.project45.ilovepadma.ItemImageText;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.adapter.adapter_customer2;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class list_outlet2 extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_customer3.AdapterCallback{

    //table_timeline_like
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String id_user,username,timeStamp="00",nama_preseller="00",email,employee_id_dms3,db_user,jabatan,list_rute,id_company="",nama_company="",
            filter_outlet = "All",filter_jenis_aktifitas="All";;

    private static final String TAG = list_outlet2.class.getSimpleName();

    private static String url_select_oultet     = Server.URL + "outlet/list_outlet_semesta/";
    private static String url_select_oultet2     = Server.URL + "outlet/list_outlet_semesta2/";

    EditText edt_search;
    ImageView im_search;

    ListView list;
    SwipeRefreshLayout swipe;

    FrameLayout mFrameLayout;
    TextView textNoData;
    private boolean loadMore = true;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    List<Data_customer> itemList = new ArrayList<Data_customer>();
    adapter_customer3 adapter;
    boolean onLoading = true;

    GPSTracker gps;
    double latitude,longitude;
    String alamat="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_outlet);

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

        timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        getSupportActionBar().setTitle("Outlet Semesta");

        edt_search = findViewById(R.id.edt_search);
        im_search = findViewById(R.id.im_search);

        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        Toast.makeText(getApplicationContext(), "Company Selected " + nama_company, Toast.LENGTH_SHORT).show();

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_report);

        Iconify.with(new FontAwesomeModule());

        gps = new GPSTracker(list_outlet2.this, list_outlet2.this);
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

        adapter = new adapter_customer3(list_outlet2.this,itemList, list_outlet2.this);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_add2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_item) {

            Intent intent = new Intent(list_outlet2.this, add_outlet_semesta.class);
            intent.putExtra("act", "add_outlet");
            //startActivity(intent);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == R.id.filter_lokasi_outlet) {
            Toast.makeText(getApplicationContext(), "Lokasi outlet belum bisa digunakan",Toast.LENGTH_LONG).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                list_outlet2.this);

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
                /*
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });
                */

        // membuat alert dialog dari builder
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void callVolley(final int start,final double lt,final double lg,final String filterOutlet) {
        //imLoading.setVisibility(View.VISIBLE);
        //pbLoading.setVisibility(View.VISIBLE);


        if (start == 0) {
            itemList.clear();
            onLoading = true;
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
        }
        //swipe.setRefreshing(true);
        //Toast.makeText(getApplicationContext(), String.valueOf(start),Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(list_outlet2.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");

        if(onLoading==true) {
            progressDialog.show();


            String url_server = url_select_oultet2 + String.valueOf(start)+"/"+lt+"/"+lg + "/" + filterOutlet;
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
//                                item.setOutlet_type(obj.getString("channel_outlet"));
//                                item.setSaluran_distribusi(obj.getString("segment"));
//                                item.setSegment_level_1(obj.getString("sub_segment"));
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
                                Toast.makeText(list_outlet2.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(list_outlet2.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                }
            });

            // menambah request ke request queue
            AppController.getInstance(this).addToRequestQueue(jArr);
        }

    }

    @Override
    public void onRefresh() {
        onLoading = true;
        callVolley(0,latitude,longitude,filter_outlet);
    }

    @Override
    public void onCustomerOnClick(int position, Data_customer customer) {
//        showEditCustomerDialog(customer,position);
        showEditCustomerDialog2(customer,position);
    }

    void showEditCustomerDialog(final Data_customer customer,final int position) {
        final String PARAM_DATA = "Data";
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(list_outlet2.this);

        final ItemImageText[] items = {
                new ItemImageText("Detail", "", ""),
                new ItemImageText("Survey Toko", "", "{fa-check}")
        };



        ImageTextAdapter imageTextAdapter = new ImageTextAdapter(list_outlet2.this);
        imageTextAdapter.addAll(items);

        adb.setTitle("");
        final ItemImageText[] finalItems = items;
        adb.setAdapter(imageTextAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int n) {
                //Toast.makeText(getContext(), task.getSubject()+" "+mMember.getAccessToken(), Toast.LENGTH_SHORT).show();
                if(items[n].text.equals("Detail")){

                    Intent intent = new Intent(list_outlet2.this, detail_outlet_semesta.class);
                    intent.putExtra("act", "detail_outlet");
                    intent.putExtra(PARAM_DATA, customer);
                    startActivityForResult(intent,1);

                }
                else if(items[n].text.equals("Survey Toko")){

                    Toast.makeText(getApplicationContext(), "Under Construction", Toast.LENGTH_SHORT).show();
                }

                //addScore(id_stream, items[n].text);
            }
        });
        //adb.setNegativeButton("Cancel", null);
        adb.show();


    }

    void showEditCustomerDialog2(final Data_customer customer,final int position){
        Toast.makeText(getApplicationContext(), "Detail Outlet belum bisa digunakan",Toast.LENGTH_LONG).show();
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
}
