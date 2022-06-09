package com.project45.ilovepadma.timeline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_like;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_point;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class timeline_like extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_like.ListPoint {

    int success,total_point = 0;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = timeline_like.class.getSimpleName();

    String id_user, username,bm,email,nmr_relasi1="",nmr_relasi2="",kategori="",status_like="",nama_user="",likeOrNot="",row_position="";
    private static String url_point     = Server.URL + "timeline/list_timeline_like/";
    private static String url_post_komen     = Server.URL + "timeline/post_save_comment_like";

    ListView list;
    SwipeRefreshLayout swipe;

    FrameLayout mFrameLayout;
    TextView textNoData,txt_tot_point,txt_tukar_point;

    List<Data_point> itemList = new ArrayList<Data_point>();
    adapter_like adapter;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_input_komen;

    Menu menu_like,menu_unlike;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_like_timeline);

        Intent i = getIntent();
        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_list_point);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Api.conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (Api.conMgr.getActiveNetworkInfo() != null
                    && Api.conMgr.getActiveNetworkInfo().isAvailable()
                    && Api.conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);

        Intent intentku = getIntent(); // gets the previously created intent
        nmr_relasi1 = intentku.getStringExtra("nmr_relasi1");
        nmr_relasi2 = intentku.getStringExtra("nmr_relasi2");
        kategori = intentku.getStringExtra("kategori");
        status_like = intentku.getStringExtra("status_like");
        nama_user = intentku.getStringExtra("nama_user");
        row_position = intentku.getStringExtra("row_position");
        //Toast.makeText(timeline_comment.this, "nmr_relasi1 "+nmr_relasi1+" nmr_relasi2 "+nmr_relasi2, Toast.LENGTH_SHORT).show();

        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);
        txt_tot_point = findViewById(R.id.txt_tot_point);
        txt_tukar_point = findViewById(R.id.txt_tukar_point);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_data);

        adapter = new adapter_like(timeline_like.this, itemList);
        list.setAdapter(adapter);

        txt_tukar_point.setText(kategori);
        /*
        txt_tukar_point.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Fitur masih belum tersedia", Toast.LENGTH_LONG).show();
            }
        });

         */

        swipe.setOnRefreshListener(swipeRefreshDo);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //swipe.setRefreshing(true);
                           //itemList.clear();
                           //adapter.notifyDataSetChanged();
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley(0);

                       }
                   }
        );

        list.setOnScrollListener(listOnScroll);

    }

    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley(0);


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

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.page_point));
        }
    }

    private void callVolley(final int start) {

        final ProgressDialog progressDialog = new ProgressDialog(timeline_like.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        total_point = 0;
        if (start == 0) {
            itemList.clear();
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
            swipe.setRefreshing(true);
        }

        String url_server       = url_point+start+"/"+nmr_relasi1+"/"+nmr_relasi2;
        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    int nmr = 1;
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_point item = new Data_point();

                            item.setId(obj.getString("id"));
                            item.setkategori(obj.getString("kategori"));
                            item.setisi_komen(obj.getString("isi_komen"));
                            item.setkomen_from(obj.getString("from"));
                            item.setdate_create(obj.getString("date_create"));

                            total_point+=nmr;

                            // menambah item ke array
                            itemList.add(item);
                            if(i==response.length()-1){
                                txt_tot_point.setText("Jumlah Like "+ String.valueOf(total_point));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                }
                else{
                    if(start == 0) {
                        textNoData.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(timeline_like.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    }
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
                Toast.makeText(timeline_like.this, "No More Items Available", Toast.LENGTH_SHORT).show();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_save_like, menu);
        this.menu_like = menu;
        this.menu_unlike = menu;
        if(status_like.equals("0")){
            menu_like.findItem(R.id.action_like).setVisible(true);
            menu_unlike.findItem(R.id.action_unlike).setVisible(false);
        }
        else if(status_like.equals("1")){
            menu_like.findItem(R.id.action_like).setVisible(false);
            menu_unlike.findItem(R.id.action_unlike).setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_like) {
            //Toast.makeText(list_sales_order.this, "Add Test", Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "Tes add", Toast.LENGTH_LONG).show();
            postLike();
            //upload();
            return true;
        }
        else  if (id == R.id.action_unlike) {
            //Toast.makeText(list_sales_order.this, "Add Test", Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "Tes add", Toast.LENGTH_LONG).show();
            postLike();
            //upload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuratOnClick(int position, Data_point berita) {

    }



    private void postLike(){

        final ProgressDialog progressDialog = new ProgressDialog(timeline_like.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        //proses save to database using api
        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_komen, new Response.Listener<String>() {

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
                        progressDialog.dismiss();
                        //callVolley(0);
                        likeOrNot = jObj.getString("act");
                        onBackPressed();


                    } else {
                        Toast.makeText(getApplicationContext(),
                                "error " + jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "error " + error.getMessage(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nmr_relasi1", nmr_relasi1);
                params.put("nmr_relasi2", nmr_relasi2);
                params.put("create_by", id_user);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        returnIntent.putExtra("row_position", row_position);
        returnIntent.putExtra("likeOrNot", likeOrNot);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
