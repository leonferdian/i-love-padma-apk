package com.project45.ilovepadma.timeline;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.adapter_list_timeline;
import com.project45.ilovepadma.app.AppController;
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

public class list_timeline_summary extends AppCompatActivity implements adapter_list_timeline.ListJV{

    //table_timeline_like
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String id_user, username,timeStamp="00",nama_preseller="00",email,employee_id_dms3,db_user,jabatan,list_rute;

    private static String url_get_timeline_summary     = Server.URL + "sfa_leader/get_timeline_summary/";


    private static final String TAG = list_timeline_summary.class.getSimpleName();
    List<Data_join_visit> itemList = new ArrayList<Data_join_visit>();
    ArrayList<Data_join_visit> arrJoin = new ArrayList<Data_join_visit>();

    adapter_list_timeline adapter;

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
    EditText txt_tgl_awal,txt_nama_preseller;
    Spinner spinner_qty,spinner_tipe,spinner_kriteria;

    int lastLastitem=0;
    private String jenis_act = "tl3";
    String tipe_filter,criteria_filter="00";

    private String[] jml_klik;

    Data_join_visit data_join = new Data_join_visit();

    int jml_klik2 = 0;
    private String test_date = "2020-02-11",test_date2 = "2020-02-12";

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


        tipe_filter="by_preseller";
        timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        /*SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        try {
            Date date = format_date.parse(test_date);
            Date date2 = format_date.parse(test_date2);
            //System.out.println(date);
            if(date2.before(date)){
                Toast.makeText(getApplicationContext(), "Kemarin ",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Setelah ",Toast.LENGTH_LONG).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        getSupportActionBar().setTitle("Time Line "+timeStamp);

        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);

        Intent intentku = getIntent(); // gets the previously created intent
        list_rute = intentku.getStringExtra("list_rute");
        //Toast.makeText(getApplicationContext(), "list_rute "+list_rute,Toast.LENGTH_LONG).show();

        adapter = new adapter_list_timeline(list_timeline_summary.this, itemList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(swipeRefreshDo);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //swipe.setRefreshing(true);
                           //itemList.clear();
                           //adapter.notifyDataSetChanged();
                           callVolley(0,list_rute,timeStamp,nama_preseller,jenis_act);

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
            callVolley(0,list_rute,timeStamp,nama_preseller,jenis_act);
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
            final int lastItem = firstVisibleItem + visibleItemCount;
            if(lastItem == totalItemCount) {
                if (lastLastitem != lastItem) { //to avoid multiple calls for last item, declare it as a field in your Activity
                    lastLastitem = lastItem;
                    // Your async task here
                    //Toast.makeText(getApplicationContext(), "lastLastitem "+lastLastitem,Toast.LENGTH_LONG).show();
                    callVolley(lastLastitem,list_rute,timeStamp,nama_preseller,jenis_act);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filter_list) {

            DialogFilter("Filter");
            return true;
        }
        else if (id == R.id.image_coordinat) {
            final String PARAM_DATA = "Data";
                       //Toast.makeText(getApplicationContext(), "Kordinat "+get_id,Toast.LENGTH_LONG).show();
            //ArrayList<String> arr = new ArrayList<>();
            //arr.add("Hello");
            //arr.add("CoderzHeaven");
            if(jml_klik2==0){
                Toast.makeText(getApplicationContext(), "Pilih minimal 1 outlet ", Toast.LENGTH_LONG).show();
            }
            else {
                Intent intent = new Intent(list_timeline_summary.this, list_coordinat.class);
                intent.putExtra(PARAM_DATA, getJoin());
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_timeline_summary.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.filter_tgl_name, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Timeline");

        /*final ProgressDialog progressDialog = new ProgressDialog(persiapan_service_level2.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();*/

        txt_tgl_awal = dialogView.findViewById(R.id.txt_tgl);
        txt_nama_preseller = dialogView.findViewById(R.id.txt_nama_preseller);
        spinner_qty = dialogView.findViewById(R.id.spinner_qty);
        spinner_tipe = dialogView.findViewById(R.id.spinner_tipe);
        spinner_kriteria = dialogView.findViewById(R.id.spinner_kriteria);
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

        spinner_tipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {

                    //Toast.makeText(getApplicationContext(), "rute "+String.valueOf(tipe_filter), Toast.LENGTH_LONG).show();
                    if(item.toString().equals("By Preseller")) {
                        tipe_filter = "by_preseller";
                        txt_nama_preseller.setHint("Masukkan Nama Preseller");
                    }
                    else if(item.toString().equals("By HOT")) {
                        tipe_filter = "by_hot";
                        txt_nama_preseller.setHint("Masukkan Nama HOT");
                    }
                    else if(item.toString().equals("By HOS")) {
                        tipe_filter = "by_hos";
                        txt_nama_preseller.setHint("Masukkan Nama HOS");
                    }
                }
                else{
                    tipe_filter = "by_preseller";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
                tipe_filter = "by_preseller";
            }
        });

        spinner_kriteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {

                    if(!item.toString().equals("--Pilih Kriteria--")) {
                        if(item.toString().equals("Toko Tutup")) {
                            criteria_filter = "toko_tutup";
                        }
                        else if(item.toString().equals("Out Arena")) {
                            criteria_filter = "out_arena";
                        }
                        //Toast.makeText(getApplicationContext(), "criteria_filter "+String.valueOf(criteria_filter), Toast.LENGTH_LONG).show();
                    }
                    else{
                        criteria_filter = "00";
                        //Toast.makeText(getApplicationContext(), "criteria_filter "+String.valueOf(criteria_filter), Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    criteria_filter = "00";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
                criteria_filter = "00";
            }
        });



        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get_get_call_plan(employee_id_dms3,txt_tgl_awal.getText().toString(),txt_tgl_akhir.getText().toString());
                timeStamp = txt_tgl_awal.getText().toString();
                if(!txt_nama_preseller.getText().toString().equals("")) {
                    nama_preseller = txt_nama_preseller.getText().toString();
                }
                else{
                    nama_preseller = "00";
                }
                getSupportActionBar().setTitle("Time Line "+timeStamp);
                //Toast.makeText(getApplicationContext(), "qty "+String.valueOf(spinner_qty.getSelectedItem()), Toast.LENGTH_LONG).show();
                if(String.valueOf(spinner_qty.getSelectedItem()).equals("0")) {
                    jenis_act = "tl4";
                    callVolley(0, list_rute, timeStamp, nama_preseller,jenis_act);
                }
                else{
                    jenis_act = "tl3";
                    callVolley(0, list_rute, timeStamp, nama_preseller,jenis_act);
                }
                //callVolley(0, list_rute, timeStamp, nama_preseller);


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

    private void callVolley(final int start, String id_rute, final String tanggal, final String preseller, final String jns) {

        final ProgressDialog progressDialog = new ProgressDialog(list_timeline_summary.this,
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

        final String nama = preseller.replaceAll(" ", "%20");
        String url_server       = url_get_timeline_summary + start + "/" + id_rute + "/" + tanggal + "/" + nama+ "/" + criteria_filter+"/"+tipe_filter+"/"+jns+"/"+id_user;

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
                            item.setnmr_jv(obj.getString("szDocId"));
                            item.senama_customer(obj.getString("cust_name"));
                            item.setid_customer(obj.getString("id_customer"));
                            item.setalamat_cust(obj.getString("alamat_outlet"));
                            item.setdate_create(obj.getString("date_create"));
                            item.setnama_salesman(obj.getString("create_by"));
                            item.setvol_tot_order(obj.getString("qty"));
                            item.setjml_brg_dijual(obj.getString("brg_dijual"));
                            item.setjv_foto(obj.getString("display_image"));
                            item.setnama_hot(obj.getString("hot_name"));
                            item.setdepo(obj.getString("depo"));
                            item.setnilai_klik("0");
                            item.settoko_tutup(obj.getString("status_toko_tutup"));
                            item.setout_arena(obj.getString("status_out_area"));
                            item.setlat_cust(obj.getString("lat_outlet"));
                            item.setlong_cust(obj.getString("long_outlet"));
                            item.settgl_foto(obj.getString("date_create2"));
                            item.seturutas_st(obj.getString("urutas_st"));
                            item.setkunjugan_ke(obj.getString("kunjugan_ke"));
                            item.setjml_komen(obj.getString("jml_komen"));
                            item.setstatus_like(obj.getString("status_like"));
                            item.setjml_like(obj.getString("jml_like"));

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
                    Toast.makeText(list_timeline_summary.this, "Double klik untuk melihat detail / tekan lama untuk pilih outlet ", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    if(start == 0) {
                        textNoData.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(list_timeline_summary.this, "No More Items Available For ", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(list_timeline_summary.this, "No More Items Availablee", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(list_timeline_summary.this, detail_timeline.class);
        intent.putExtra("id_customer", berita.getid_customer());
        intent.putExtra("cust_name", berita.getnama_customer());
        intent.putExtra("szDocId", berita.getnmr_jv());
        intent.putExtra("so_order", berita.getvol_tot_order());
        intent.putExtra("alamat_outlet", berita.getalamat_cust());
        intent.putExtra("display_image", berita.getjv_foto());
        intent.putExtra("status_toko_tutup", berita.gettoko_tutup());
        intent.putExtra("status_out_arena", berita.getout_arena());
        intent.putExtra("lat_cust", berita.getlat_cust());
        intent.putExtra("long_cust", berita.getlong_cust());
        intent.putExtra("tgl_buat", berita.gettgl_foto());
        startActivity(intent);
    }

    @Override
    public void onSuratOnClick2(int position, Data_join_visit berita) {
        if(berita.getnilai_klik().equals("0")) {
            Toast.makeText(list_timeline_summary.this, berita.getnama_customer() + " Selected ", Toast.LENGTH_SHORT).show();
            //jml_klik[position]=berita.getId()+",";
            jml_klik2++;
        }
        else{
            Toast.makeText(list_timeline_summary.this, berita.getnama_customer() + " Unselect ", Toast.LENGTH_SHORT).show();
            //jml_klik[position]="";
            jml_klik2--;
        }

    }

    @Override
    public void onCommentClick(int position, Data_join_visit berita) {
        Intent intent = new Intent(list_timeline_summary.this, timeline_comment.class);
        intent.putExtra("nmr_relasi1", berita.getnmr_jv());
        intent.putExtra("nmr_relasi2", berita.getid_customer());
        intent.putExtra("kategori", "Kunjungan");
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onLikeClick(int position, Data_join_visit berita) {
        Intent intent = new Intent(list_timeline_summary.this, timeline_like.class);
        intent.putExtra("nmr_relasi1", berita.getnmr_jv());
        intent.putExtra("nmr_relasi2", berita.getid_customer());
        intent.putExtra("status_like", berita.getstatus_like());
        intent.putExtra("kategori", "Kunjungan");
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(), "Tes", Toast.LENGTH_SHORT).show();
                callVolley(0, list_rute, timeStamp, nama_preseller, jenis_act);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
