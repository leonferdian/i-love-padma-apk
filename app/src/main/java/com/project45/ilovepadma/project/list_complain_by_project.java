package com.project45.ilovepadma.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.project45.ilovepadma.ImageTextAdapter;
import com.project45.ilovepadma.ItemImageText;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_cust_service2;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.detail_complain;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.notes.add_note_ticket;
import com.project45.ilovepadma.notes.list_existing_ticket;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_complain_by_project extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_cust_service2.CustomerListener {
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    String id_user, username,bm,email,id_company="",nama_company="";
    ListView list;
    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    View dialogView;
    TextView textNoData;

    boolean onLoading = true;

    AlertDialog.Builder dialog;
    private static final String TAG = list_complain_by_project.class.getSimpleName();

    private static String url_select     = Server.URL + "project/list_padma_complain_by_nmr_project/";
    private static String url_select_complain_with_filter     = Server.URL + "cust_service/list_padma_complain_by_filter/";
    private static String url_post_delete_ticket     = Server.URL + "note/post_delete_ticket";

    List<Data_cust_service> itemList = new ArrayList<Data_cust_service>();
    adapter_cust_service2 adapter;

    LayoutInflater inflater;
    String tahun="",bulan="",complain_status = "all",nmr_project="",status_tiket = "All";
    Spinner spinner_tahun,spinner_bulan,spinner_filter_complain;

    android.app.AlertDialog.Builder alertDialogBuilder;
    android.app.AlertDialog alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_complain_by_note);

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

        //Toast.makeText(getApplicationContext(), "Company Selected "+nama_company, Toast.LENGTH_SHORT).show();

        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        //Toast.makeText(list_cust_service.this, "name "+username, Toast.LENGTH_SHORT).show();

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        Intent intentku = getIntent(); // gets the previously created intent
        nmr_project = intentku.getStringExtra("nmr_project");

        adapter = new adapter_cust_service2(list_complain_by_project.this, itemList);
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
                           callVolley(0,status_tiket);

                       }
                   }
        );

        list.setOnScrollListener(listOnScroll);

    }

    public void onRefresh() {
        callVolley(0,status_tiket);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onLoading = true;
            callVolley(0,status_tiket);
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
                    status_tiket = "All";
                    callVolley(0,status_tiket);
                break;
            case R.id.radio_leader_open:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_LONG).show();
                    status_tiket = "OPEN";
                    callVolley(0,status_tiket);
                break;
            case R.id.radio_leader_accept:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    status_tiket = "ACCEPT";
                    callVolley(0,status_tiket);
                break;
            case R.id.radio_leader_proses:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "proses", Toast.LENGTH_LONG).show();
                    status_tiket = "PROSES";
                    callVolley(0,status_tiket);

                break;
            case R.id.radio_leader_finish:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_LONG).show();
                    status_tiket = "FINISH";
                    callVolley(0,status_tiket);
                break;
            case R.id.radio_leader_close:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "close", Toast.LENGTH_LONG).show();
                    status_tiket = "CLOSE";
                    callVolley(0,status_tiket);
                break;
        }
    }

    private void callVolley(final int start,final String tiketStatus) {
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

        final ProgressDialog progressDialog = new ProgressDialog(list_complain_by_project.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        if(onLoading==true) {
            progressDialog.show();


            String url_server = url_select + String.valueOf(start) + "/" + nmr_project+"/"+tiketStatus;
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
                            onLoading = true;
                        } else {

                            if (onLoading == true) {
                                Toast.makeText(list_complain_by_project.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(list_complain_by_project.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                }
            });

            // menambah request ke request queue
            AppController.getInstance(this).addToRequestQueue(jArr);
        }

    }

    private void listComplainWithFilter(final String tahunn, final String bulann, final String status_complain) {
        //imLoading.setVisibility(View.VISIBLE);
        //pbLoading.setVisibility(View.VISIBLE);
        itemList.clear();
        textNoData.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(list_complain_by_project.this,
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

                            // menambah item ke array
                            itemList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    textNoData.setVisibility(View.VISIBLE);
                    Toast.makeText(list_complain_by_project.this, "Data komplain kosong ", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(list_complain_by_project.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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


                    Intent intent = new Intent(list_complain_by_project.this, detail_complain.class);
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
                callVolley(adapter.getCount(),status_tiket);
                //callVolley(adapter.getCount());
            }
        }


    };


    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_complain_by_project.this);
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
                callVolley(0,status_tiket);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }



    private void DeleteTicketAlert(final String nmrTicket,final Data_cust_service customer){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Ticket");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ? ")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                /*.setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something
                        dialogInterface.cancel();
                    }
                })*/
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        proc_delete_ticket(nmrTicket,customer);
                        dialog.cancel();

                    }
                })

                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });


        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void proc_delete_ticket(final String nmrTicket,final Data_cust_service customer){

        final String complain_nomor = customer.getid_pelanggan();
        //Toast.makeText(getApplicationContext(), "nmr_note "+nmrTicket+" nmr_complain "+customer.getid_pelanggan(), Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(list_complain_by_project.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_delete_ticket, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("save_ticket", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        callVolley(0,status_tiket);
                        //end proses
                        progressDialog.dismiss();

                    }
                    else{
                        //end proses
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //end proses
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Save ticket Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nmr_note", nmrTicket);
                params.put("complain_nomor", complain_nomor);

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
