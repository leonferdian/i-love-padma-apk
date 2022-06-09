package com.project45.ilovepadma.timeline;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_list_timeline_ilv;
import com.project45.ilovepadma.aktifitas.aktifitas_from_timeline;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.list_complain_from_timeline;
import com.project45.ilovepadma.data.Data_join_visit;
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
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_timeline_ilv_with_scroll extends AppCompatActivity implements adapter_list_timeline_ilv.ListJV{

    //table_timeline_like
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String id_user, username,timeStamp="00",nama_preseller="00",email,employee_id_dms3,db_user,jabatan,list_rute,id_company="",nama_company="";

    private static String url_get_timeline     = Server.URL + "timeline/list_timeline_ilv/";
    private static String url_get_timeline_by_anak_buah     = Server.URL + "timeline/list_timeline_by_anak_buah/";
    private static String url_get_check_anak_buah     = Server.URL + "timeline/check_anak_buah/";

    private static final String TAG = list_timeline_summary.class.getSimpleName();
    List<Data_join_visit> itemList = new ArrayList<Data_join_visit>();
    ArrayList<Data_join_visit> arrJoin = new ArrayList<Data_join_visit>();

    adapter_list_timeline_ilv adapter;

    ListView list;
    SwipeRefreshLayout swipe;

    FrameLayout mFrameLayout;
    TextView textNoData;
    private boolean loadMore = true;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    Data_join_visit data_join = new Data_join_visit();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String[] jml_klik;
    int lastLastitem=0;
    EditText txt_tgl_awal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_timeline);

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

        getSupportActionBar().setTitle("Time Line "+timeStamp);

        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        Toast.makeText(getApplicationContext(), "Company Selected "+nama_company, Toast.LENGTH_SHORT).show();

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);

        adapter = new adapter_list_timeline_ilv(list_timeline_ilv_with_scroll.this, itemList);
        list.setAdapter(adapter);
        swipe.setOnRefreshListener(swipeRefreshDo);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {

                           //callVolley(0,id_company,timeStamp);
                           check_anak_buah(0,id_user,email);
                       }
                   }
        );

        list.setOnScrollListener(listOnScroll);


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
            //callVolley(0,id_company,timeStamp);
            check_anak_buah(0,id_user,email);
        }
    };
    /*
    private Scroller listOnScroll = new Scroller() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            final int lastItem = firstVisibleItem + visibleItemCount;
            if(lastItem == totalItemCount) {
                if (lastLastitem != lastItem) { //to avoid multiple calls for last item, declare it as a field in your Activity
                    lastLastitem = lastItem;
                    // Your async task here
                    //Toast.makeText(getApplicationContext(), "lastLastitem "+lastLastitem,Toast.LENGTH_LONG).show();
                    callVolley(lastLastitem,id_company,timeStamp);
                }
            }
        }

        @Override
        public void scrollCompleted(int currentFirstVisibleItem, int currentVisibleItemCount, int scrollState) {
            //super.scrollCompleted(currentFirstVisibleItem, currentVisibleItemCount, scrollState);
            if(currentFirstVisibleItem + currentVisibleItemCount >= (adapter.getCount() - 2)){
                //callVolley(adapter.getCount(),list_rute,timeStamp);

            }
        }
    };
    */

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
               // callVolley(adapter.getCount());
               // callVolley(adapter.getCount(),id_company,timeStamp);
                check_anak_buah(adapter.getCount(),id_user,email);
            }
        }
    };

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
            // Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            DialogFilter("Filter");
        }


        return super.onOptionsItemSelected(item);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_timeline_ilv_with_scroll.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.filter_tgl_timeline, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Timeline");

        txt_tgl_awal = dialogView.findViewById(R.id.txt_tgl);

        txt_tgl_awal.setText(timeStamp);

        txt_tgl_awal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_absen = "filter1";
                showDateDialog(tgl_absen,txt_tgl_awal);

            }
        });

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get_get_call_plan(employee_id_dms3,txt_tgl_awal.getText().toString(),txt_tgl_akhir.getText().toString());
                timeStamp = txt_tgl_awal.getText().toString();
                //callVolley(0,id_company,timeStamp);
                check_anak_buah(0,id_user,email);
                getSupportActionBar().setTitle("Time Line "+timeStamp);

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

    private void showDateDialog(final String absen, final EditText tgl_awal){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                if(absen.equals("filter1"))
                {
                    tgl_awal.setText(dateFormatter.format(newDate.getTime()));
                }

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "Tes back", Toast.LENGTH_SHORT).show();
                //callVolley(0,id_company,timeStamp);
                check_anak_buah(adapter.getCount(),id_user,email);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void callVolley(final int start, String id_company, final String tanggal) {

        final ProgressDialog progressDialog = new ProgressDialog(list_timeline_ilv_with_scroll.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        if (start == 0) {
            loadMore = true;
            itemList.clear();
            arrJoin.clear();
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
            //swipe.setRefreshing(true);
        }

        if(!loadMore){
            progressDialog.dismiss();
            return;
        }

        String url_server       = url_get_timeline + start + "/" + id_company + "/" + tanggal+"/"+id_user;

        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    jml_klik = new String[response.length()];
                    //Log.d(TAG, response.toString());
                    Log.d(TAG, "jml_data "+ String.valueOf(response.length()));
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_join_visit item = new Data_join_visit();

                            item.setId(obj.getString("nmr"));
                            item.setnmr_jv(obj.getString("id_timeline"));
                            item.setalamat_cust(obj.getString("kategori"));
                            item.setvol_tot_order(obj.getString("isi_timeline"));
                            item.setjml_brg_dijual(obj.getString("id_relasi"));
                            item.setnama_hot(obj.getString("id_company"));
                            item.setjv_foto(obj.getString("photo_timeline"));
                            item.setnama_salesman(obj.getString("nama_user"));
                            item.seturutas_st(obj.getString("id_user"));
                            item.setdate_create(obj.getString("date_create"));
                            item.setkunjugan_ke(obj.getString("tgl_timeline"));
                            item.setdepo(obj.getString("foto_user"));
                            item.setjml_komen(obj.getString("jml_komen"));
                            item.setstatus_like(obj.getString("status_like"));
                            item.setjml_like(obj.getString("jml_like"));
                            item.setnilai_klik("0");

                            jml_klik[i]="";
                            // menambah item ke array
                            itemList.add(item);
                            arrJoin.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getJoin();
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    Toast.makeText(list_timeline_ilv_with_scroll.this, "Double klik untuk melihat detail ", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    if(start == 0) {
                        textNoData.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(list_timeline_ilv_with_scroll.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    }

                    // sudah tidak ada data lagi
                    loadMore = false;
                }

                // notifikasi adanya perubahan data pada adapter



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                swipe.setRefreshing(false);
                Toast.makeText(list_timeline_ilv_with_scroll.this, "No More Items Availablee", Toast.LENGTH_SHORT).show();
                textNoData.setVisibility(View.VISIBLE);
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    private void callVolleyByAnakBuah(final int start, String id_company, final String tanggal, final String email_atasan) {

        final ProgressDialog progressDialog = new ProgressDialog(list_timeline_ilv_with_scroll.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        if (start == 0) {
            loadMore = true;
            itemList.clear();
            arrJoin.clear();
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(View.GONE);
            //swipe.setRefreshing(true);
        }

        if(!loadMore){
            progressDialog.dismiss();
            return;
        }

        String url_server       = url_get_timeline_by_anak_buah + start + "/" + id_company + "/" + tanggal+"/"+id_user+"/"+email_atasan;

        Log.d("url_server", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    jml_klik = new String[response.length()];
                    //Log.d(TAG, response.toString());
                    Log.d(TAG, "jml_data "+ String.valueOf(response.length()));
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_join_visit item = new Data_join_visit();

                            item.setId(obj.getString("nmr"));
                            item.setnmr_jv(obj.getString("id_timeline"));
                            item.setalamat_cust(obj.getString("kategori"));
                            item.setvol_tot_order(obj.getString("isi_timeline"));
                            item.setjml_brg_dijual(obj.getString("id_relasi"));
                            item.setnama_hot(obj.getString("id_company"));
                            item.setjv_foto(obj.getString("photo_timeline"));
                            item.setnama_salesman(obj.getString("nama_user"));
                            item.seturutas_st(obj.getString("id_user"));
                            item.setdate_create(obj.getString("date_create"));
                            item.setkunjugan_ke(obj.getString("tgl_timeline"));
                            item.setdepo(obj.getString("foto_user"));
                            item.setjml_komen(obj.getString("jml_komen"));
                            item.setstatus_like(obj.getString("status_like"));
                            item.setjml_like(obj.getString("jml_like"));
                            item.setnilai_klik("0");

                            jml_klik[i]="";
                            // menambah item ke array
                            itemList.add(item);
                            arrJoin.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getJoin();
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    Toast.makeText(list_timeline_ilv_with_scroll.this, "Double klik untuk melihat detail ", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    if(start == 0) {
                        textNoData.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(list_timeline_ilv_with_scroll.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
                    }

                    // sudah tidak ada data lagi
                    loadMore = false;
                }

                // notifikasi adanya perubahan data pada adapter



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                swipe.setRefreshing(false);
                Toast.makeText(list_timeline_ilv_with_scroll.this, "No More Items Availablee", Toast.LENGTH_SHORT).show();
                textNoData.setVisibility(View.VISIBLE);
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

    }

    public ArrayList<Data_join_visit> getJoin(){
        return arrJoin;
    }

    @Override
    public void onSuratOnClick(int position, Data_join_visit berita) {

        if(berita.getalamat_cust().equals("post_everything")) {

            Intent intent = new Intent(list_timeline_ilv_with_scroll.this, add_timeline.class);
            intent.putExtra("act", "detail_timeline");
            intent.putExtra("isi_timeline", berita.getvol_tot_order());
            intent.putExtra("photo_timeline", berita.getjv_foto());
            startActivity(intent);
        }
        else if(berita.getalamat_cust().equals("aktifitas")) {
            Intent intent = new Intent(list_timeline_ilv_with_scroll.this, aktifitas_from_timeline.class);
            intent.putExtra("tgl_report", berita.getkunjugan_ke());
            intent.putExtra("id_sales", berita.geturutas_st());
            intent.putExtra("nmr_aktifitas", berita.getjml_brg_dijual());
            startActivity(intent);
        }
        else if(berita.getalamat_cust().equals("complain")) {
            Intent intent = new Intent(list_timeline_ilv_with_scroll.this, list_complain_from_timeline.class);
            intent.putExtra("tgl_report", berita.getkunjugan_ke());
            intent.putExtra("id_sales", berita.geturutas_st());
            intent.putExtra("complain_nomor", berita.getjml_brg_dijual());
            startActivity(intent);
        }
    }

    @Override
    public void onSuratOnClick2(int position, Data_join_visit berita) {

    }

    @Override
    public void onCommentClick(int position, Data_join_visit berita) {
        Intent intent = new Intent(list_timeline_ilv_with_scroll.this, timeline_comment.class);
        intent.putExtra("nmr_relasi1", berita.getnmr_jv());
        intent.putExtra("nmr_relasi2", berita.getjml_brg_dijual());
        intent.putExtra("kategori", berita.getalamat_cust());
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onLikeClick(int position, Data_join_visit berita) {
        Intent intent = new Intent(list_timeline_ilv_with_scroll.this, timeline_like.class);
        intent.putExtra("nmr_relasi1", berita.getnmr_jv());
        intent.putExtra("nmr_relasi2", berita.getjml_brg_dijual());
        intent.putExtra("status_like", berita.getstatus_like());
        intent.putExtra("kategori", berita.getalamat_cust());
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    private void check_anak_buah(final int start,final String id_user,final String email){
        String url_edit       = url_get_check_anak_buah+id_company+"/"+id_user+"/"+email;
        Log.d("list_account", url_edit);
        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String jml_anak_buah      = jObj.getString("jml_anak_buah");

                        callVolleyByAnakBuah(start,id_company,timeStamp,email);

                    } else {
                        //Toast.makeText(list_timeline_ilv.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        callVolley(start,id_company,timeStamp);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    callVolley(start,id_company,timeStamp);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_timeline_ilv_with_scroll.this, error.getMessage(), Toast.LENGTH_LONG).show();
                callVolley(start,id_company,timeStamp);
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }
}
