package com.project45.ilovepadma.complain;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_jenis_complain;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.project.edit_project;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class add_complain extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,employee_id_dms3="",id_complain="",timeStamp="",act_form="",tanggal_act="",complain_purpose="";
    int randomNumber;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static final String TAG = add_complain.class.getSimpleName();

    private static String url_post_complain     = Server.URL + "cust_service/post_save_padma_complain";
    private static String url_post_delete_deskripsi_padma_complain     = Server.URL + "cust_service/post_delete_deskripsi_padma_complain";
    //private static String url_list_master_complain_to     = Server.URL + "cust_service/list_master_complain_to";
    private static String url_list_master_complain_to     = Server.URL + "project/list_member_project/";
    private static String url_list_complain_deskripsi     = Server.URL + "cust_service/get_list_complain_deskripsi/";
    private static String url_get_list_project    = Server.URL + "project/list_project_by_user3/";
    private static String url_get_list_subproject    = Server.URL + "project/list_sub_project_by_user/";

    Spinner spinner_complain_purpose,spinner_jenis_complain,spinner_project,spinner_subproject;
    EditText txt_complain_from,txt_subject,txt_due_date,txt_periode_awal,txt_periode_akhir,txt_l1,txt_l2,txt_l3;
    AutoCompleteTextView multi_ac_to,multi_pic2;
    TableLayout tabel_1a,tabel_1b;
    Button btn_deskripsi;
    TextView txt_durasi;
    LinearLayout layout_durasi,layout_project,layout_subproject,layout_l;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private ArrayList<Data_jenis_complain> jenis_complainList ;
    List<Data_work_report> itemList = new ArrayList<Data_work_report>();
    String id_company="",nama_company="",nmr_project="",jml_subproject="0",nmr_subproject="",global_pic2="",check_id_complain_to="";
    String tahun="",bulan="";

    private ArrayList<Data_work_report> list_project,list_subproject;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_complain);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_aktifitas);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        employee_id_dms3 = Api.sharedpreferences.getString(Api.TAG_ID_EMPLOYEE_DMS3, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        jenis_complainList = new ArrayList<Data_jenis_complain>();
        list_project = new ArrayList<Data_work_report>();
        list_subproject = new ArrayList<Data_work_report>();

        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intentku = getIntent(); // gets the previously created intent
        act_form = intentku.getStringExtra("act");

        timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());


        txt_complain_from = findViewById(R.id.txt_complain_from);
        txt_subject = findViewById(R.id.txt_subject);
        txt_due_date = findViewById(R.id.txt_due_date);
        txt_periode_awal = findViewById(R.id.txt_periode_awal);
        txt_periode_akhir = findViewById(R.id.txt_periode_akhir);
        multi_ac_to = findViewById(R.id.multi_ac_to);
        spinner_complain_purpose = findViewById(R.id.spinner_complain_purpose);
        spinner_jenis_complain = findViewById(R.id.spinner_jenis_complain);
        spinner_project = findViewById(R.id.spinner_project);
        spinner_subproject = findViewById(R.id.spinner_subproject);
        btn_deskripsi = findViewById(R.id.btn_deskripsi);
        tabel_1a = findViewById(R.id.tabel_1a);
        tabel_1b = findViewById(R.id.tabel_1b);
        txt_durasi = findViewById(R.id.txt_durasi);
        layout_durasi = findViewById(R.id.layout_durasi);
        layout_project = findViewById(R.id.layout_project);
        layout_subproject = findViewById(R.id.layout_subproject);
        layout_l = findViewById(R.id.layout_l);
        txt_l1 = findViewById(R.id.txt_l1);
        txt_l2 = findViewById(R.id.txt_l2);
        txt_l3 = findViewById(R.id.txt_l3);
        multi_pic2 = findViewById(R.id.multi_pic2);

        layout_l.setVisibility(View.GONE);
        txt_l1.setVisibility(View.GONE);
        txt_l2.setVisibility(View.GONE);
        txt_l3.setVisibility(View.GONE);

        get_id_id_complain(timeStamp);

        getListComplainTo();

        spinner_complain_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    //Toast.makeText(getApplicationContext(), "jenis: "+String.valueOf(item.toString()), Toast.LENGTH_LONG).show();
                    if(item.equals("internal")){

                        txt_complain_from.setText(username);
                        txt_complain_from.setEnabled(false);
                    }
                    else if(item.equals("external")){

                        txt_complain_from.setText("");
                        txt_complain_from.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinner_jenis_complain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    //Toast.makeText(getApplicationContext(), "jenis: "+String.valueOf(item.toString()), Toast.LENGTH_LONG).show();
                    if(item.equals("Proak")){

                        txt_durasi.setVisibility(View.VISIBLE);
                        layout_durasi.setVisibility(View.VISIBLE);
                        layout_project.setVisibility(View.GONE);
                        layout_subproject.setVisibility(View.GONE);
                        //txt_l1.setVisibility(View.VISIBLE);
                        //txt_l2.setVisibility(View.VISIBLE);
                        //txt_l3.setVisibility(View.VISIBLE);
                        //layout_l.setVisibility(View.VISIBLE);
                        nmr_subproject = "";
                        nmr_project = "";
                    }
                    else if(item.equals("Project")){

                        txt_durasi.setVisibility(View.GONE);
                        layout_durasi.setVisibility(View.GONE);
                        layout_project.setVisibility(View.VISIBLE);
                        getListProject(id_company,id_user);
                        txt_l1.setVisibility(View.GONE);
                        txt_l2.setVisibility(View.GONE);
                        txt_l3.setVisibility(View.GONE);
                        layout_l.setVisibility(View.GONE);
                        nmr_subproject = "";
                        nmr_project = "";
                    }
                    else {
                        txt_durasi.setVisibility(View.GONE);
                        layout_durasi.setVisibility(View.GONE);
                        layout_project.setVisibility(View.GONE);
                        layout_subproject.setVisibility(View.GONE);
                        //txt_l1.setVisibility(View.VISIBLE);
                        //txt_l2.setVisibility(View.VISIBLE);
                        //txt_l3.setVisibility(View.VISIBLE);
                        //layout_l.setVisibility(View.VISIBLE);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinner_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Data_work_report sel = getProject().get(position);
                    nmr_project = String.valueOf(sel.getid_job_list());
                    jml_subproject = String.valueOf(sel.getsub_project());
                    //Toast.makeText(getApplicationContext(), "sub_project "+String.valueOf(jml_subproject), Toast.LENGTH_LONG).show();
                    if(!jml_subproject.equals("0")){
                        layout_subproject.setVisibility(View.VISIBLE);
                        getListSubProject(nmr_project,id_user);
                    }
                    else{
                        layout_subproject.setVisibility(View.GONE);
                        nmr_subproject = "";
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinner_subproject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Data_work_report sel = getSubProject().get(position);
                    nmr_subproject = String.valueOf(sel.getid_job_list());
                    //Toast.makeText(getApplicationContext(), "nmr_subproject "+String.valueOf(nmr_subproject), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        btn_deskripsi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //Intent intent = new Intent(getApplicationContext(), add_deskripsi_complain.class);
                Intent intent = new Intent(getApplicationContext(), add_deskripsi_complain_multi_file.class);
                intent.putExtra("act_form", act_form);
                intent.putExtra("id_complain", id_complain);
                startActivityForResult(intent, 111);

                //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
            }
        });

        txt_due_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_absen = "filter1";
                showDateDialog(tgl_absen,txt_due_date);

            }
        });

        txt_periode_awal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_absen = "filter1";
                showDateDialog(tgl_absen,txt_periode_awal);

            }
        });

        txt_periode_akhir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_absen = "filter1";
                showDateDialog(tgl_absen,txt_periode_akhir);

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
            window.setStatusBarColor(getResources().getColor(R.color.page_join_visit));
        }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_user, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {

            final String complain_to_name = multi_ac_to.getText().toString();
            boolean trueName = false;

            if(itemList.size()==0){
                Toast.makeText(getApplicationContext(), "Anda Harus Menambahkan Deskripsi  ", Toast.LENGTH_LONG).show();
            }
            else {
                //postComplain();
                for (int i = 0; i < jenis_complainList.size(); i++) {
                    if(jenis_complainList.get(i).getName().equals(complain_to_name))
                    {
                        trueName = true;
                    }

                }
                if(trueName == true){
                    //Toast.makeText(getApplicationContext(), "Complain To sesuai", Toast.LENGTH_LONG).show();
                    postComplain();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Complain To tidak sesuai", Toast.LENGTH_LONG).show();
                }
            }


        }

        return super.onOptionsItemSelected(item);
    }

    private void get_id_id_complain(final String tanggal){
        id_complain =  id_user+"-"+tanggal+"-"+ String.valueOf(generateRandomNumbers(100001,999999));
        //getDeskripsi(id_complain);
        //Toast.makeText(getApplicationContext(), " id_survey "+id_survey, Toast.LENGTH_LONG).show();
    }

    private int generateRandomNumbers(int min, int max) {
        // min & max will be changed as per your requirement. In my case, I've taken min = 2 & max = 32

        //Random r = new Random();
        //int randomNumber = r.nextInt(100);
        int randomNumberCount = 10;

        int dif = max - min;
        if (dif < (randomNumberCount * 3)) {
            dif = (randomNumberCount * 3);
        }

        int margin = (int) Math.ceil((float) dif / randomNumberCount);
        List<Integer> randomNumberList = new ArrayList<>();



        Random random = new Random();

        for (int i = 0; i < randomNumberCount; i++) {
            int range = (margin * i) + min; // 2, 5, 8

            int randomNum = random.nextInt(margin);
            if (randomNum == 0) {
                randomNum = 1;
            }

            int number = (randomNum + range);
            randomNumber = number;
            //randomNumberList.add(number);
        }
        //Toast.makeText(getApplicationContext(), " random "+String.valueOf(randomNumber), Toast.LENGTH_LONG).show();
        return randomNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == 11) {
                Toast.makeText(this, "Deskripsi added ", Toast.LENGTH_LONG).show();
                cleanTable(tabel_1a);
                cleanTable(tabel_1b);
                getDeskripsi(id_complain);
            }
        }
    }

    private void getListComplainTo() {
        String url_server = url_list_master_complain_to+id_company;
        Log.d("complain_url",url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Data_jenis_complain jenis = new Data_jenis_complain();
                            jenis.setId(obj.getString("id"));
                            jenis.setName(obj.getString("nama_user"));
                            jenis_complainList.add(jenis);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    addItemsOnSpinnerJenisComplain();
                    //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();

                }
                else{

                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);


    }

    public void addItemsOnSpinnerJenisComplain() {
        List<String> list = new ArrayList<String>();
        /*list.add("list 1");
        list.add("list 2");
        list.add("list 3");*/
        //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();
        for (int i = 0; i < jenis_complainList.size(); i++) {
            list.add(jenis_complainList.get(i).getName());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multi_ac_to.setAdapter(dataAdapter);
        multi_ac_to.setThreshold(2);
        /*
        multi_ac_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    Data_jenis_complain sel = jenis_complainList.get(position);
                    check_id_complain_to = sel.getId();
                    Toast.makeText(getApplicationContext(), "check_id_complain_to "+check_id_complain_to, Toast.LENGTH_LONG).show();
                }
            }
        });

         */

        multi_pic2.setAdapter(dataAdapter);
        multi_pic2.setThreshold(2);
        //multi_ac_to.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        multi_pic2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    Data_jenis_complain sel = jenis_complainList.get(position);
                    global_pic2 = sel.getId();
                    //Toast.makeText(getApplicationContext(), "global_pic2 "+global_pic2, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getDeskripsi(final String id_complainn) {
        itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(add_complain.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server = url_list_complain_deskripsi + id_complainn;
        Log.d("list_complain_deskripsi", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_work_report jv = new Data_work_report();

                            jv.setid(obj.getString("id_complain"));
                            jv.setid_job_list(obj.getString("complain_nomor"));
                            jv.setketerangan(obj.getString("deskripsi"));

                            itemList.add(jv);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Klik RO untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callList1();
                    callList2();
                }
                else{
                    Toast.makeText(add_complain.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(add_complain.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
        // progressDialog.dismiss();
    }

    private void callList1() {
        int nmr = 1;
        for (int i = 0; i < itemList.size(); i++) {
            final Data_work_report sel = itemList.get(i);

            TableRow row = new TableRow(add_complain.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;
            params.width = 310;

            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params2.height = 120;

            TextView txt_nmr = new TextView(add_complain.this);
            txt_nmr.setText("" + nmr);
            txt_nmr.setBackgroundResource(R.color.page_join_visit);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_nmr, params2);

            tabel_1a.addView(row);

            nmr++;
        }
    }

    private void callList2() {

        for (int i = 0; i < itemList.size(); i++) {
            final Data_work_report sel = itemList.get(i);

            TableRow row = new TableRow(add_complain.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;

            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params2.setMargins(3, 1, 3, 1);
            params2.height = 120;

            TableRow.LayoutParams paramsImgTag = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgTag.setMargins(3, 1, 3, 1);
            paramsImgTag.height = 120;

            TextView txt_keterangan = new TextView(add_complain.this);
            txt_keterangan.setText("" + Html.fromHtml(sel.getketerangan()));
            txt_keterangan.setBackgroundResource(R.color.page_join_visit);
            txt_keterangan.setTextColor(getResources().getColor(R.color.white));
            txt_keterangan.setTextSize(10);
            txt_keterangan.setGravity(Gravity.CENTER);
            txt_keterangan.setPadding(3, 0, 3, 0);
            txt_keterangan.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_keterangan, params2);

            ImageView img_coaching = new ImageView(add_complain.this);
            img_coaching.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_clear_white_48));
            img_coaching.setBackgroundResource(R.color.page_join_visit);
            img_coaching.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    proc_del_deskripsi(sel.getid());

                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });
            // add to row
            row.addView(img_coaching, paramsImgTag);

            tabel_1b.addView(row);

        }
    }

    private void proc_del_deskripsi(final String id_report){

        Log.e(TAG, "id_report: " + id_report);

        final ProgressDialog progressDialog = new ProgressDialog(add_complain.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Delete...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_delete_deskripsi_padma_complain, new Response.Listener<String>() {

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

                        cleanTable(tabel_1a);
                        cleanTable(tabel_1b);
                        getDeskripsi(id_complain);
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

    private void postComplain(){
        final String complain_purpose = String.valueOf(spinner_complain_purpose.getSelectedItem());
        final String complain_subject = txt_subject.getText().toString();
        final String complain_from = txt_complain_from.getText().toString();
        final String complain_to = multi_ac_to.getText().toString();
        final String due_date = txt_due_date.getText().toString();
        final String periode_awal = txt_periode_awal.getText().toString();
        final String periode_akhir = txt_periode_akhir.getText().toString();
        final String jenis_complain = String.valueOf(spinner_jenis_complain.getSelectedItem());
        String l1 ;
        String l2 ;
        String l3 ;
        String pic2;

        // Get the device's current location and address
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(add_complain.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);

        if(spinner_jenis_complain.getSelectedItem().equals("Project"))
        {
            l1 = spinner_project.getSelectedItem().toString();
            l2 = spinner_subproject.getSelectedItem().toString();
            l3 = "";
            pic2 = global_pic2;
        }
        else{
            l1 = txt_l1.getText().toString();
            l2 = txt_l2.getText().toString();
            l3 = txt_l3.getText().toString();
            pic2="";
        }

        if(complain_from.equals("")){
            Toast.makeText(getApplicationContext(), "Complain From harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(complain_to.equals("")){
            Toast.makeText(getApplicationContext(), "Complain To harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(complain_subject.equals("")){
            Toast.makeText(getApplicationContext(), "Subject harus diisi ", Toast.LENGTH_LONG).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(add_complain.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();

            //proses save to database using api
            StringRequest strReq = new StringRequest(Request.Method.POST, url_post_complain, new Response.Listener<String>() {

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

                            //end proses
                            progressDialog.dismiss();
                            onBackPressedWithProcess();


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
                    params.put("complain_nomor", id_complain);
                    params.put("complain_subject", complain_subject);
                    params.put("complain_purpose", complain_purpose);
                    params.put("complain_from", complain_from);
                    params.put("complain_to", complain_to);
                    params.put("due_date", due_date);
                    params.put("periode_awal", periode_awal);
                    params.put("periode_akhir", periode_akhir);
                    params.put("create_by", id_user);
                    params.put("id_company", id_company);
                    params.put("jenis_complain", jenis_complain);
                    params.put("nmr_project", nmr_project);
                    params.put("nmr_sub_project", nmr_subproject);
                    params.put("l1", l1);
                    params.put("l2", l2);
                    params.put("l3", l3);
                    params.put("pic2", pic2);
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("address", address);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(strReq);
        }

    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    private void getListProject(final String idCompany,final String idUser) {

        final ProgressDialog progressDialog = new ProgressDialog(add_complain.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Get project...");
        progressDialog.show();

        String url_server = url_get_list_project+idCompany+"/"+idUser+"/all/"+tahun+"/"+bulan;
        Log.d("url_project", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Data_work_report rute = new Data_work_report();
                            rute.setid(obj.getString("id"));
                            rute.setid_job_list(obj.getString("nmr_project"));
                            rute.setketerangan(obj.getString("nama_project"));
                            rute.setsub_project(obj.getString("sub_project"));
                            list_project.add(rute);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();
                    addItemsOnSpinnerProject();
                    getProject();
                    //end proses
                    progressDialog.dismiss();
                }
                else{
                    //end proses
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    public void addItemsOnSpinnerProject() {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < list_project.size(); i++) {
            list.add(list_project.get(i).getketerangan());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_project.setAdapter(dataAdapter);

    }

    public List<Data_work_report> getProject(){
        return list_project;
    }

    private void getListSubProject(final String nmrProject,final String idUser) {

        list_subproject.clear();
        final ProgressDialog progressDialog = new ProgressDialog(add_complain.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Get project...");
        progressDialog.show();

        String url_server = url_get_list_subproject+nmrProject+"/"+idUser;
        Log.d("url_subproject", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Data_work_report rute = new Data_work_report();
                            rute.setid(obj.getString("id"));
                            rute.setid_job_list(obj.getString("nmr_sub_project"));
                            rute.setketerangan(obj.getString("sub_project"));
                            list_subproject.add(rute);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();
                    addItemsOnSpinnerSubProject();
                    getSubProject();
                    //end proses
                    progressDialog.dismiss();
                }
                else{
                    //end proses
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    public void addItemsOnSpinnerSubProject() {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < list_subproject.size(); i++) {
            list.add(list_subproject.get(i).getketerangan());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subproject.setAdapter(dataAdapter);

    }

    public List<Data_work_report> getSubProject(){
        return list_subproject;
    }

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
