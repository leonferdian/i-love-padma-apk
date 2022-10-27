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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.project45.ilovepadma.MainActivity3;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_list_timeline_pe2;
import com.project45.ilovepadma.aktifitas.aktifitas_from_timeline;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.list_complain_from_timeline;
import com.project45.ilovepadma.data.Data_join_visit;
import com.project45.ilovepadma.data.Data_pertanyaan_post_everything;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Scroller;
import com.project45.ilovepadma.notes.edit_note;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_timeline_post_everything2 extends AppCompatActivity implements adapter_list_timeline_pe2.ListJV{

    //table_timeline_like
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    int globalcurrentVisible = 0;
    String id_user, username,timeStamp="00",nama_preseller="00",email,employee_id_dms3,db_user,jabatan,list_rute,id_company="",nama_company="",
            filter_jenis_aktifitas="All";

    private static String url_get_timeline     = Server.URL + "timeline/list_timeline_post_everything/";
    private static String url_get_timeline2     = Server.URL + "timeline/list_timeline_post_everything2/";
    private static String url_post_del_timeline     = Server.URL + "timeline/post_del_timeline";

    private static final String TAG = list_timeline_summary.class.getSimpleName();
    List<Data_join_visit> itemList = new ArrayList<Data_join_visit>();
    ArrayList<Data_join_visit> arrJoin = new ArrayList<Data_join_visit>();

    adapter_list_timeline_pe2 adapter;

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
    Spinner spinner_filter;

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

        getSupportActionBar().setTitle("Post Everything "+timeStamp);

        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        Toast.makeText(getApplicationContext(), "Company Selected "+nama_company, Toast.LENGTH_SHORT).show();

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);

        adapter = new adapter_list_timeline_pe2(list_timeline_post_everything2.this, itemList);
        list.setAdapter(adapter);
        swipe.setOnRefreshListener(swipeRefreshDo);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           callVolley(0,id_company,timeStamp);
                       }
                   }
        );

        //list.setOnScrollListener(listOnScroll);


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
            callVolley(0,id_company,timeStamp);
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
            /*
            if(currentFirstVisibleItem + currentVisibleItemCount >= (adapter.getCount() - 2)){
               // callVolley(adapter.getCount());
                //callVolley(adapter.getCount(),id_company,timeStamp);

            }
            */

        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_list_timeline, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_timeline) {


            //Intent intent = new Intent(list_timeline_post_everything2.this, add_timeline2.class);
            Intent intent = new Intent(list_timeline_post_everything2.this, add_post_everything.class);
            intent.putExtra("act", "add_timeline");
            //startActivity(intent);
            startActivityForResult(intent, 3);


            return true;
        }
        else if (id == R.id.filter_timeline) {
            // Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            DialogFilter("Filter");
        }


        return super.onOptionsItemSelected(item);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_timeline_post_everything2.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.filter_tgl_timeline, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Timeline");

        txt_tgl_awal = dialogView.findViewById(R.id.txt_tgl);
        spinner_filter = dialogView.findViewById(R.id.spinner_filter);

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

        spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {

                    filter_jenis_aktifitas = item.toString();

                }
                else{
                    filter_jenis_aktifitas = "All";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
                filter_jenis_aktifitas = "All";
            }
        });

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get_get_call_plan(employee_id_dms3,txt_tgl_awal.getText().toString(),txt_tgl_akhir.getText().toString());
                timeStamp = txt_tgl_awal.getText().toString();
                getSupportActionBar().setTitle("Post Everything "+timeStamp);
                callVolley(0,id_company,timeStamp);


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
                //callVolleyForBack(id_company,timeStamp);
                String row_position = data.getStringExtra("row_position");
                String id_timeline = data.getStringExtra("nmr_relasi1");
                String jml_item = data.getStringExtra("jml_item");
                //Toast.makeText(getApplicationContext(), "row_position "+row_position, Toast.LENGTH_SHORT).show();
                callVolleyForBack2(id_timeline,Integer.valueOf(row_position),jml_item);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                callVolleyForBack(id_company,timeStamp);
                String likeOrNot = data.getStringExtra("likeOrNot");
                String row_position = data.getStringExtra("row_position");
                //callVolleyForBack3(likeOrNot,Integer.valueOf(row_position));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "Tes back", Toast.LENGTH_SHORT).show();
                //callVolley(0,id_company,timeStamp);
                callVolley(0,id_company,timeStamp);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void callVolley(final int start, String id_company, final String tanggal) {

        final ProgressDialog progressDialog = new ProgressDialog(list_timeline_post_everything2.this,
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


        final String idUser = id_user+"-"+filter_jenis_aktifitas;
        String url_server       = url_get_timeline2 + id_company + "/" + tanggal+"/"+idUser;

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
                            item.setjenis_post(obj.getString("jenis_post"));
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
                            item.setjenis_file(obj.getString("jenis_file"));

                            for(int x = 0; x < obj.getJSONArray("data_pertanyaan").length(); x++){
                                JSONObject data_pertanyaan = obj.getJSONArray("data_pertanyaan").getJSONObject(x);
                                Data_pertanyaan_post_everything pertanyaan = new Data_pertanyaan_post_everything();

                                pertanyaan.setNomor(data_pertanyaan.getString("nmr"));
                                pertanyaan.setId_timeline_jawaban(data_pertanyaan.getString("id_timeline_jawaban"));
                                pertanyaan.setId_timeline(data_pertanyaan.getString("id_timeline"));
                                pertanyaan.setKategori(data_pertanyaan.getString("kategori"));
                                pertanyaan.setJenis_post(data_pertanyaan.getString("jenis_post"));
                                pertanyaan.setId_timeline_pertanyaan(data_pertanyaan.getString("id_timeline_pertanyaan"));
                                pertanyaan.setPertanyaan(data_pertanyaan.getString("pertanyaan"));
                                pertanyaan.setJawaban(data_pertanyaan.getString("jawaban"));

                                item.setPertanyaan_post_everything(pertanyaan);
                            }

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
                    Toast.makeText(list_timeline_post_everything2.this, "Double klik untuk melihat detail ", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    textNoData.setVisibility(View.VISIBLE);
                }

                // notifikasi adanya perubahan data pada adapter



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                swipe.setRefreshing(false);
                Toast.makeText(list_timeline_post_everything2.this, "No More Items Availablee", Toast.LENGTH_SHORT).show();
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

    private void callVolleyForBack(String id_company, final String tanggal){

        itemList.clear();
        arrJoin.clear();

        final String idUser = id_user+"-"+filter_jenis_aktifitas;
        String url_server       = url_get_timeline2 + id_company + "/" + tanggal+"/"+idUser;

        Log.d("url_ForBack", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_join_visit item = new Data_join_visit();

                            item.setId(obj.getString("nmr"));
                            item.setnmr_jv(obj.getString("id_timeline"));
                            item.setalamat_cust(obj.getString("kategori"));
                            item.setjenis_post(obj.getString("jenis_post"));
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
                            item.setjenis_file(obj.getString("jenis_file"));

                            for(int x = 0; x < obj.getJSONArray("data_pertanyaan").length(); x++){
                                JSONObject data_pertanyaan = obj.getJSONArray("data_pertanyaan").getJSONObject(x);
                                Data_pertanyaan_post_everything pertanyaan = new Data_pertanyaan_post_everything();

                                pertanyaan.setNomor(data_pertanyaan.getString("nmr"));
                                pertanyaan.setId_timeline_jawaban(data_pertanyaan.getString("id_timeline_jawaban"));
                                pertanyaan.setId_timeline(data_pertanyaan.getString("id_timeline"));
                                pertanyaan.setKategori(data_pertanyaan.getString("kategori"));
                                pertanyaan.setJenis_post(data_pertanyaan.getString("jenis_post"));
                                pertanyaan.setId_timeline_pertanyaan(data_pertanyaan.getString("id_timeline_pertanyaan"));
                                pertanyaan.setPertanyaan(data_pertanyaan.getString("pertanyaan"));
                                pertanyaan.setJawaban(data_pertanyaan.getString("jawaban"));

                                item.setPertanyaan_post_everything(pertanyaan);
                            }

                            // menambah item ke array
                            itemList.add(item);
                            arrJoin.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    adapter.notifyDataSetChanged();

                    Toast.makeText(list_timeline_post_everything2.this, "Double klik untuk melihat detail ", Toast.LENGTH_SHORT).show();
                }
                else{

                }

                // notifikasi adanya perubahan data pada adapter



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    private void callVolleyForBack2(String id_timeline,int position,String jml_item){
        /*
        TextView txt_comment = (TextView) list.getChildAt(position).findViewById(R.id.txt_comment);
        TextView txt_isi_timeline = (TextView) list.getChildAt(position).findViewById(R.id.txt_isi_timeline);
        txt_comment.setText("100 komen");
        txt_isi_timeline.setText(id_timeline);

         */

        View view = list.getChildAt(position - list.getFirstVisiblePosition());
        if(view==null){
            Toast.makeText(list_timeline_post_everything2.this, "Error Page ", Toast.LENGTH_SHORT).show();
        }
        else {
            TextView txt_comment = view.findViewById(R.id.txt_comment);
            txt_comment.setText(jml_item+" komen");

        }
    }

    private void callVolleyForBack3(String actLike,int position){

        View view = list.getChildAt(position - list.getFirstVisiblePosition());
        if(view==null){
            Toast.makeText(list_timeline_post_everything2.this, "Error Page", Toast.LENGTH_SHORT).show();
        }
        else {
            ImageView image_like = view.findViewById(R.id.image_like);
            if(actLike.equals("like"))
            {
                image_like.setImageResource(R.drawable.like_icon_active);
            }
            else if(actLike.equals("unlike"))
            {
                image_like.setImageResource(R.drawable.like_icon);
            }

        }


        //adapter.updateText(position);

    }

    @Override
    public void onSuratOnClick(int position, Data_join_visit berita) {

        if(berita.getalamat_cust().equals("post_everything")) {
            if(berita.getjenis_file().equals("non_video")) {
                //Intent intent = new Intent(list_timeline_post_everything2.this, add_timeline.class);
                Intent intent = new Intent(list_timeline_post_everything2.this, detail_post_everything.class);
                intent.putExtra("act", "detail_timeline");
                intent.putExtra("isi_timeline", ""); //berita.getvol_tot_order()
                intent.putExtra("photo_timeline", berita.getjv_foto());
                intent.putExtra("id_timeline", berita.getnmr_jv());
                intent.putExtra("date_post", berita.getdate_create());
                intent.putExtra("nama_user", berita.getnama_salesman());
                intent.putExtra("foto_user", berita.getdepo());
                intent.putExtra("jml_komen", berita.getjml_komen());
                intent.putExtra("jml_like", berita.getjml_like());
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(list_timeline_post_everything2.this, timeline_det_video.class);
                intent.putExtra("act", "detail_timeline");
                intent.putExtra("isi_timeline", ""); //berita.getvol_tot_order()
                intent.putExtra("photo_timeline", berita.getjv_foto());
                intent.putExtra("id_timeline", berita.getnmr_jv());
                startActivity(intent);
            }

        }
        else if(berita.getalamat_cust().equals("aktifitas")) {
            Intent intent = new Intent(list_timeline_post_everything2.this, aktifitas_from_timeline.class);
            intent.putExtra("tgl_report", berita.getkunjugan_ke());
            intent.putExtra("id_sales", berita.geturutas_st());
            intent.putExtra("nmr_aktifitas", berita.getjml_brg_dijual());
            startActivity(intent);
        }
        else if(berita.getalamat_cust().equals("complain")) {
            Intent intent = new Intent(list_timeline_post_everything2.this, list_complain_from_timeline.class);
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

        //Toast.makeText(list_timeline_post_everything2.this, "row "+String.valueOf(globalcurrentVisible), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(list_timeline_post_everything2.this, timeline_comment.class);
        intent.putExtra("nmr_relasi1", berita.getnmr_jv());
        intent.putExtra("nmr_relasi2", berita.getjml_brg_dijual());
        intent.putExtra("kategori", berita.getalamat_cust());
        intent.putExtra("nama_user", berita.getnama_salesman());
        intent.putExtra("row_position", String.valueOf(position));
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onLikeClick(int position, Data_join_visit berita) {
        Intent intent = new Intent(list_timeline_post_everything2.this, timeline_like.class);
        intent.putExtra("nmr_relasi1", berita.getnmr_jv());
        intent.putExtra("nmr_relasi2", berita.getjml_brg_dijual());
        intent.putExtra("status_like", berita.getstatus_like());
        intent.putExtra("kategori", berita.getalamat_cust());
        intent.putExtra("nama_user", berita.getnama_salesman());
        intent.putExtra("row_position", String.valueOf(position));
        //startActivity(intent);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onPictClick(int position, Data_join_visit berita) {

        Intent intent = new Intent(list_timeline_post_everything2.this, detail_foto.class);
        intent.putExtra("sku", "Foto Timeline");
        intent.putExtra("file_foto", berita.getjv_foto());
        intent.putExtra("tgl_buat", "");
        intent.putExtra("act", "foto_timeline");
        //startActivity(intent);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onMoreMenuClick(int position, Data_join_visit berita) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(list_timeline_post_everything2.this);

        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_timeline,(LinearLayout)findViewById(R.id.modalBottomSheetContainer));

        bottomSheetView.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(list_timeline_post_everything2.this, "del Clicked.. "+berita.getnmr_jv(), Toast.LENGTH_SHORT).show();bottomSheetDialog.dismiss();
                bottomSheetDialog.dismiss();
                if(berita.getjenis_file().equals("non_video")) {
                    final String PARAM_DATA = "Data";
                    Intent intent = new Intent(list_timeline_post_everything2.this, edit_post_everything.class);
                    intent.putExtra("act", "detail_pe");
                    intent.putExtra(PARAM_DATA, berita);
                    startActivityForResult(intent, 3);
                }
                else{
                    Toast.makeText(list_timeline_post_everything2.this, "Post dengan video tidak bisa diedit ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(list_timeline_post_everything2.this, "del Clicked.. "+berita.getnmr_jv(), Toast.LENGTH_SHORT).show();bottomSheetDialog.dismiss();
                bottomSheetDialog.dismiss();
                DeleteAlert(berita.getnmr_jv());
            }
        });

        bottomSheetView.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(list_timeline_post_everything2.this, "del Clicked.. "+berita.getnmr_jv(), Toast.LENGTH_SHORT).show();bottomSheetDialog.dismiss();
                bottomSheetDialog.dismiss();
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "Post Everything Share");
                share.putExtra(Intent.EXTRA_TEXT, "http://ilove.padmatirtagroup.com/pet?id_timeline="+berita.getnmr_jv());
                startActivity(Intent.createChooser(share, "Share Post!"));
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void DeleteAlert(final String idPost){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Post");

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
                        proc_delete_post(idPost);
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

    private void proc_delete_post(final String idPost){

        final ProgressDialog progressDialog = new ProgressDialog(list_timeline_post_everything2.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_del_timeline, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Delete", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        callVolley(0,id_company,timeStamp);
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
                Log.e(TAG, "Delete post Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_post", idPost);

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
