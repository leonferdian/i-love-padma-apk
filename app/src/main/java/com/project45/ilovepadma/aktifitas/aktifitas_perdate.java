package com.project45.ilovepadma.aktifitas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.project45.ilovepadma.adapter.adapter_work_report_bydate;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class aktifitas_perdate extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_work_report_bydate.ListWR {

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";
    private static final String TAG = aktifitas_perdate.class.getSimpleName();
    String id_user, username,work_report,email,tgl_report,id_aktifitas="",id_company="",nama_company="";
    SharedPreferences sharedpreferences;

    int success,last_id;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LASTID = "last_id";

    private static String url_list_work_report_by_date     = Server.URL + "aktifitas/list_aktifitas_by_date/";
    private static String url_post_del_report     = Server.URL + "aktifitas/post_del_aktifitas_detail";

    ListView list;
    SwipeRefreshLayout swipe;

    FrameLayout mFrameLayout;
    TextView textNoData;
    private boolean loadMore = true;

    List<Data_work_report> itemList = new ArrayList<Data_work_report>();
    adapter_work_report_bydate adapter;

    String timeStamp ="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_work_report_by_date);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_work_report_perdate);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        //id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        Intent intentku = getIntent(); // gets the previously created intent
        tgl_report = intentku.getStringExtra("tgl_report");
        id_user = intentku.getStringExtra("id_sales");
        getSupportActionBar().setTitle("Agenda tgl "+tgl_report);

        timeStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        //Toast.makeText(getApplicationContext(), "tgl_report "+tgl_report+" / "+timeStamp, Toast.LENGTH_SHORT).show();

        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);

        adapter = new adapter_work_report_bydate(aktifitas_perdate.this, itemList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(swipeRefreshDo);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //swipe.setRefreshing(true);
                           //itemList.clear();
                           //adapter.notifyDataSetChanged();
                           callVolley();

                       }
                   }
        );

        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Open list work report per date ", "Open list work report per date "+tgl_report,"");

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.blue_color));
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            callVolley();
        }
    };

    private void callVolley() {

        itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(aktifitas_perdate.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();


        String url_server       = url_list_work_report_by_date+tgl_report+"/"+id_user+"/"+id_company;
        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_work_report item = new Data_work_report();

                            item.setwaktu_mulai(obj.getString("waktu_mulai"));
                            item.setwaktu_selesai(obj.getString("waktu_selesai"));
                            item.setjob_list(obj.getString("job_list"));
                            item.setdivisi(obj.getString("divisi"));
                            item.setdepartment(obj.getString("department"));
                            item.setpilar(obj.getString("pilar"));
                            item.setketerangan(obj.getString("keterangan"));
                            item.setid(obj.getString("id"));
                            item.setdurasi(obj.getString("durasi"));
                            item.setnmr_wr(obj.getString("nmr_wr"));
                            item.setstatus_agenda(obj.getString("status_agenda"));

                            id_aktifitas = obj.getString("nmr_wr");

                            // menambah item ke array
                            itemList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                else{
                    textNoData.setVisibility(View.VISIBLE);

                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
                progressDialog.dismiss();
                //pbLoading.setVisibility(View.GONE);
                //imLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                progressDialog.dismiss();
                textNoData.setVisibility(View.VISIBLE);
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onReportClick(int position, Data_work_report berita) {
        showEditCustomerDialog(berita,position);
        //Toast.makeText(getApplicationContext(), berita.getwaktu_mulai(), Toast.LENGTH_SHORT).show();
    }

    void showEditCustomerDialog(final Data_work_report jv,final int position) {
        final String PARAM_DATA = "Data";
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        final ItemImageText[] items = {
                new ItemImageText("Edit", "", ""),
                new ItemImageText("Delete", "", "")
        };

        ImageTextAdapter imageTextAdapter = new ImageTextAdapter(this);
        imageTextAdapter.addAll(items);

        adb.setTitle("");
        final ItemImageText[] finalItems = items;
        adb.setAdapter(imageTextAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int n) {
                //Toast.makeText(getContext(), task.getSubject()+" "+mMember.getAccessToken(), Toast.LENGTH_SHORT).show();
                if(items[n].text.equals("Edit")){
                    Intent intent = new Intent(aktifitas_perdate.this, edit_deskripsi.class);
                    intent.putExtra(PARAM_DATA, jv);
                    intent.putExtra(TAG_USERNAME, username);
                    intent.putExtra(TAG_ID, id_user);
                    intent.putExtra("id_aktifitas", jv.getid());
                    intent.putExtra("deskripsi", jv.getketerangan());
                    intent.putExtra("jam_awal", jv.getwaktu_mulai());
                    intent.putExtra("jam_akhir", jv.getwaktu_selesai());
                    intent.putExtra("status_agenda", jv.getstatus_agenda());
                    intent.putExtra("act", "edit_wr");
                    startActivityForResult(intent,1);

                    //Toast.makeText(list_join_visit.this, jv.getId(), Toast.LENGTH_SHORT).show();
                }
                else if(items[n].text.equals("Delete")){
                    //Toast.makeText(getApplicationContext(), jv.getid(), Toast.LENGTH_SHORT).show();
                    CloseAlert(position,jv);
                }

                //addScore(id_stream, items[n].text);
            }
        });
        //adb.setNegativeButton("Cancel", null);
        adb.show();


    }

    private void CloseAlert(final int position,final Data_work_report jv){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Delete Warning");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Anda yakin ingin menghapus ?")
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

                        proc_save_wr(jv.getid(),position);

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

    private void proc_save_wr(final String id_report, final int position){

        Log.e(TAG, "id_report: " + id_report);

        final ProgressDialog progressDialog = new ProgressDialog(aktifitas_perdate.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Delete...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_del_report, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Save", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.getSurat().remove(position);
                        adapter.notifyDataSetChanged();
                        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Delete list work report", "Delete list work report on "+tgl_report,id_report);


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Del Work report Error: " + error.getMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_report", id_report);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

        //end proses
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 11) {
                //Toast.makeText(getApplicationContext(), "Tes", Toast.LENGTH_SHORT).show();
                callVolley();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        /*
        if(timeStamp.equals(tgl_report)) {
            getMenuInflater().inflate(R.menu.menu_so, menu);
        }

         */
        getMenuInflater().inflate(R.menu.menu_so, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_so) {
            //Toast.makeText(list_sales_order.this, "Add Test", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), add_deskripsi.class);
            intent.putExtra("act_form", "add_wr");
            intent.putExtra("id_aktifitas", id_aktifitas);
            intent.putExtra("tanggal_aktifitas", tgl_report);
            startActivityForResult(intent, 1);

            //upload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
