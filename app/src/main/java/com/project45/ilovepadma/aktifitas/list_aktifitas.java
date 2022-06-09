package com.project45.ilovepadma.aktifitas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_work_report_perdate;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.util.Server;
import com.project45.ilovepadma.work_report.ProjectTreeViewActivity;
import com.project45.ilovepadma.work_report.TreeViewListener;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class list_aktifitas extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_work_report_perdate.ListWR, TreeViewListener {

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";
    private static final String TAG = list_aktifitas.class.getSimpleName();

    String id_user, username,bm,email,timeStamp="",aktifitas_dari="you",nama_sales="",id_company="",nama_company="";
    SharedPreferences sharedpreferences;

    int success,last_id;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LASTID = "last_id";

    private static String url_list_work_report     = Server.URL + "work_report/list_work_report/";
    private static String url_list_work_report_by_month     = Server.URL + "work_report/list_work_report_by_month/";
    private static String url_list_work_report_by_week     = Server.URL + "aktifitas/list_aktifitas_by_week/";
    private static String url_list_work_report_by_week2     = Server.URL + "aktifitas/list_aktifitas_by_week2/";
    private static String url_list_work_report_by_month_and_year     = Server.URL + "work_report/list_work_report_by_month_and_year/";
    private static String url_list_work_report_by_week_and_year     = Server.URL + "aktifitas/list_aktifitas_by_week_and_year/";

    ListView list;
    SwipeRefreshLayout swipe;

    FrameLayout mFrameLayout;
    TextView textNoData;
    private boolean loadMore = true;

    List<Data_work_report> itemList = new ArrayList<Data_work_report>();
    adapter_work_report_perdate adapter;

    LinearLayout layout_1,layout_2;
    private AndroidTreeView mTreeView;
    private TreeNode root = TreeNode.root();
    private TreeViewListener mTreeViewListener;

    String tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    String bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    Spinner spinner_tahun,spinner_bulan;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_work_report);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_list_work_report);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        Toast.makeText(getApplicationContext(), "Company Selected "+nama_company, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "id_company "+String.valueOf(id_company)+" nama_company "+nama_company, Toast.LENGTH_LONG).show();

        Intent intentku = getIntent();
        aktifitas_dari = intentku.getStringExtra("aktifitas_dari");
        if(aktifitas_dari.equals("you"))
        {
            id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        }
        else {
            id_user = intentku.getStringExtra("id_sales");
            nama_sales = intentku.getStringExtra("nama_sales");
            getSupportActionBar().setTitle("Aktifitas "+nama_sales);
        }

        timeStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);

        layout_1 = findViewById(R.id.layout_1);
        layout_2 = findViewById(R.id.layout_2);

        adapter = new adapter_work_report_perdate(list_aktifitas.this, itemList);
        list.setAdapter(adapter);

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

        //TrackingActivity.startTrackActivity(list_aktifitas.this, list_aktifitas.this, "List work report", "Open List work report ", "Open List work report ","");

        mTreeViewListener = (TreeViewListener) this;

        Iconify.with(new FontAwesomeModule());

        callVolley2(0);


        //Toast.makeText(list_work_report.this, "bulan "+bulan, Toast.LENGTH_SHORT).show();
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
            callVolley(0);
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
                callVolley(adapter.getCount());
                //callVolley(adapter.getCount());
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        if(aktifitas_dari.equals("you")) {
            getMenuInflater().inflate(R.menu.menu_list_cust_service, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.menu_filter, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(aktifitas_dari.equals("you")) {
            if (id == R.id.add_complain_menu) {
                Intent intent = new Intent(getApplicationContext(), add_aktifitas.class);
                intent.putExtra(TAG_USERNAME, username);
                intent.putExtra(TAG_ID, id_user);
                intent.putExtra("act", "add_wr");
                intent.putExtra("tanggal", timeStamp);
                startActivityForResult(intent, 1);

                return true;
            } else if (id == R.id.filter_report_sales) {
                //Toast.makeText(list_work_report.this, "filter ", Toast.LENGTH_SHORT).show();
                DialogFilter("OK");
                return true;
            }
        }
        else{
            if (id == R.id.filter_list) {
                //Toast.makeText(list_complain.this, "Ini menu add", Toast.LENGTH_LONG).show();
                //return true;
                DialogFilter("Filter");
                return true;


            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "Tes", Toast.LENGTH_SHORT).show();
                callVolley2(0);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void callVolley(final int start) {

        final ProgressDialog progressDialog = new ProgressDialog(list_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        if (start == 0) {
            loadMore = true;
            itemList.clear();
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
            //swipe.setRefreshing(true);
        }

        if(!loadMore){
            progressDialog.dismiss();
            return;
        }


        String url_server       = url_list_work_report+start+"/"+id_user;
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

                            item.setdate_create(obj.getString("tanggal"));
                            item.setjml_report(obj.getString("jml_report"));
                            item.setdurasi(obj.getString("durasi"));

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
                        Toast.makeText(list_aktifitas.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    }

                    // sudah tidak ada data lagi
                    loadMore = false;
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
                Toast.makeText(list_aktifitas.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    private void callVolley2(final int start) {

        final ProgressDialog progressDialog = new ProgressDialog(list_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server       = url_list_work_report_by_week+id_user+"/"+tahun+"/"+bulan+"/"+id_company;
        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    root = TreeNode.root();
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject jsonObject = response.getJSONObject(i);
                            Data_work_report report = new Data_work_report(jsonObject);

                            final TreeNode parent = new TreeNode(report).setViewHolder(new ProjectTreeViewActivity.ProjectTreeHolder(list_aktifitas.this, mTreeViewListener));
                            root.addChild(parent);

                            parent.setClickListener(new TreeNode.TreeNodeClickListener() {
                                @Override
                                public void onClick(TreeNode treeNode, Object o) {
//
                                    Data_work_report dwr = (Data_work_report) treeNode.getValue();
                                    if (treeNode.isExpanded()==false) {
                                        if (treeNode.getChildren().size() == 0) {
                                            get_report_by_week_year(dwr.gettahun_wr(), dwr.getminggu_wr(), parent);
                                            //Toast.makeText(list_work_report.this, "bulan "+dwr.getbulan_wr(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    layout_2.removeAllViews();
                    mTreeView = new AndroidTreeView(list_aktifitas.this, root);
                    mTreeView.setDefaultAnimation(true);
                    //mTreeView.setDefaultAnimation(false);

                    layout_2.addView(mTreeView.getView());

                    progressDialog.dismiss();

                }
                else{

                    Toast.makeText(list_aktifitas.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    // sudah tidak ada data lagi
                    loadMore = false;
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

                //pbLoading.setVisibility(View.GONE);
                //imLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                progressDialog.dismiss();
                Toast.makeText(list_aktifitas.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    private void get_report_by_month_year(final String tahun, final String bulan, final TreeNode parentNode) {

        final ProgressDialog progressDialog = new ProgressDialog(list_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server       = url_list_work_report_by_month_and_year+id_user+"/"+tahun+"/"+bulan;
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

                            item.setdate_create(obj.getString("tanggal"));
                            item.setjml_report(obj.getString("jml_report"));
                            item.setdurasi(obj.getString("durasi"));

                            final TreeNode treeNode = new TreeNode(item).setViewHolder(new ProjectTreeViewActivity.ProjectSubHolder(list_aktifitas.this, mTreeViewListener));
                            parentNode.addChild(treeNode);




                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    parentNode.setExpanded(false);
                    mTreeView.expandNode(parentNode);

                    progressDialog.dismiss();

                }
                else{

                    Toast.makeText(list_aktifitas.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    // sudah tidak ada data lagi
                    loadMore = false;
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

                //pbLoading.setVisibility(View.GONE);
                //imLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                progressDialog.dismiss();
                Toast.makeText(list_aktifitas.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    private void get_report_by_week_year(final String tahun, final String week, final TreeNode parentNode) {

        final ProgressDialog progressDialog = new ProgressDialog(list_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server       = url_list_work_report_by_week_and_year+id_user+"/"+tahun+"/"+week+"/"+id_company;
        Log.d("aktByWeek", url_server);
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

                            item.setdate_create(obj.getString("tanggal"));
                            item.setjml_report(obj.getString("jml_report"));
                            item.setdurasi(obj.getString("durasi"));

                            final TreeNode treeNode = new TreeNode(item).setViewHolder(new ProjectTreeViewActivity.ProjectSubHolder(list_aktifitas.this, mTreeViewListener));
                            parentNode.addChild(treeNode);




                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    parentNode.setExpanded(false);
                    mTreeView.expandNode(parentNode);

                    progressDialog.dismiss();

                }
                else{

                    Toast.makeText(list_aktifitas.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    // sudah tidak ada data lagi
                    loadMore = false;
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

                //pbLoading.setVisibility(View.GONE);
                //imLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                progressDialog.dismiss();
                Toast.makeText(list_aktifitas.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onTanggalClick(int position, Data_work_report berita) {
        //Toast.makeText(getApplicationContext(), berita.getdate_create(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), aktifitas_perdate.class);
        intent.putExtra(TAG_USERNAME, username);
        intent.putExtra(TAG_ID, id_user);
        intent.putExtra("tgl_report", berita.getdate_create());
        intent.putExtra("id_sales", id_user);
        startActivityForResult(intent,1);
    }

    @Override
    public void onTreeClick(Data_work_report wr) {
        //Toast.makeText(getApplicationContext(), "ss", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), aktifitas_perdate.class);
        intent.putExtra(TAG_USERNAME, username);
        intent.putExtra(TAG_ID, id_user);
        intent.putExtra("tgl_report", wr.getdate_create());
        intent.putExtra("id_sales", id_user);
        startActivityForResult(intent,1);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_aktifitas.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_filter_wr, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Report");

        spinner_tahun =  dialogView.findViewById(R.id.spinner_tahun);
        spinner_bulan =  dialogView.findViewById(R.id.spinner_bulan);

        addItemsOnSpinnerTahun(spinner_tahun);
        addItemsOnSpinnerBulan(spinner_bulan);

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtered_wr(String.valueOf(spinner_tahun.getSelectedItem()), String.valueOf(spinner_bulan.getSelectedItem()));

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

    private void filtered_wr(final String thn, final String bln) {

        final ProgressDialog progressDialog = new ProgressDialog(list_aktifitas.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server       = url_list_work_report_by_week2+id_user+"/"+thn+"/"+bln;
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    root = TreeNode.root();
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject jsonObject = response.getJSONObject(i);
                            Data_work_report report = new Data_work_report(jsonObject);

                            final TreeNode parent = new TreeNode(report).setViewHolder(new ProjectTreeViewActivity.ProjectTreeHolder(list_aktifitas.this, mTreeViewListener));
                            root.addChild(parent);

                            parent.setClickListener(new TreeNode.TreeNodeClickListener() {
                                @Override
                                public void onClick(TreeNode treeNode, Object o) {
//
                                    Data_work_report dwr = (Data_work_report) treeNode.getValue();
                                    if (treeNode.isExpanded()==false) {
                                        if (treeNode.getChildren().size() == 0) {
                                            get_report_by_week_year(dwr.gettahun_wr(), dwr.getminggu_wr(), parent);
                                            //Toast.makeText(list_work_report.this, "bulan "+dwr.getbulan_wr(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    layout_2.removeAllViews();
                    mTreeView = new AndroidTreeView(list_aktifitas.this, root);
                    mTreeView.setDefaultAnimation(true);
                    //mTreeView.setDefaultAnimation(false);

                    layout_2.addView(mTreeView.getView());

                    progressDialog.dismiss();

                }
                else{

                    Toast.makeText(list_aktifitas.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    // sudah tidak ada data lagi
                    loadMore = false;
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

                //pbLoading.setVisibility(View.GONE);
                //imLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
                progressDialog.dismiss();
                Toast.makeText(list_aktifitas.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }
}
