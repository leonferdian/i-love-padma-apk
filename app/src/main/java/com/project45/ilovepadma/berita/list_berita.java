package com.project45.ilovepadma.berita;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_berita;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.detail_complain;
import com.project45.ilovepadma.complain.list_complain_from_you;
import com.project45.ilovepadma.data.Data_berita;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.global.Util;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_berita extends AppCompatActivity implements adapter_berita.ListJV{

    private View parent_view;
    private ListView recyclerView;
    SwipeRefreshLayout swipe;
    ImageButton bt_close;

    private static String url_select     = Server.URL + "berita_harian/list_berita_by_company/";

    String id_user, username,email,id_company="",nama_company="";

    private static final String TAG = list_berita.class.getSimpleName();

    adapter_berita adapter;
    List<Data_berita> itemList = new ArrayList<Data_berita>();
    boolean onLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_berita_page);
        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_berita);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_80), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        Util.setSystemBarColor(this, R.color.grey_5);
        Util.setSystemBarLight(this);
        //setSupportActionBar(toolbar);
        /*getSupportActionBar().setTitle("All News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Util.setSystemBarColor(this, R.color.grey_5);
        Util.setSystemBarLight(this);

         */
    }

    private void initComponent() {

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recyclerView);
        bt_close = findViewById(R.id.bt_close);

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        adapter = new adapter_berita(list_berita.this, itemList);
        recyclerView.setAdapter(adapter);
        // menamilkan widget refresh
        swipe.setOnRefreshListener(swipeRefreshDo);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //swipe.setRefreshing(true);
                           //itemList.clear();
                           //adapter.notifyDataSetChanged();
                           callVolley(0,id_company);

                       }
                   }
        );

        recyclerView.setOnScrollListener(listOnScroll);

        bt_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                onBackPressed();

            }
        });

    }

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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_list_timeline2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filter_timeline) {
            Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            //DialogFilter("Filter");
        }


        return super.onOptionsItemSelected(item);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onLoading = true;
            callVolley(0,id_company);
        }
    };

    private void callVolley(final int start,final String idCompany) {

        if(start == 0) {
            itemList.clear();
            onLoading = true;
            adapter.notifyDataSetChanged();
        }
        //swipe.setRefreshing(true);
        //Toast.makeText(getApplicationContext(), String.valueOf(start),Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(list_berita.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        if(onLoading==true) {

            progressDialog.show();

            String url_server = url_select+start+"/"+idCompany;

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

                                itemList.add(jv);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
                    }
                    else{

                        if (start == 0) {
                            onLoading = true;
                        } else {

                            if (onLoading == true) {
                                Toast.makeText(list_berita.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                            }
                            onLoading = false;
                        }

                    }
                    // notifikasi adanya perubahan data pada adapter
                    // notifikasi adanya perubahan data pada adapter
                    adapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
                    progressDialog.dismiss();



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(list_berita.this, "Error Connection ", Toast.LENGTH_LONG).show();
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
        }
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
                callVolley(adapter.getCount(),id_company);
                //callVolley(adapter.getCount());
            }
        }


    };

    @Override
    public void onBeritaClick(int position, Data_berita berita) {
        //Toast.makeText(list_berita.this, "judul "+berita.getjudul_berita(), Toast.LENGTH_LONG).show();
        final String PARAM_DATA = "Data";
        Intent intent = new Intent(list_berita.this, detail_berita.class);
        intent.putExtra("act", "detail_berita");
        intent.putExtra(PARAM_DATA, berita);
        startActivityForResult(intent,1);
    }
}
