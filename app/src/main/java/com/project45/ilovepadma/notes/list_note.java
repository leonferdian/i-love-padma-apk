package com.project45.ilovepadma.notes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
import com.project45.ilovepadma.adapter.adapter_note2;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_note;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.project.add_project;
import com.project45.ilovepadma.project.list_project2;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class list_note extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,adapter_note2.NoteListener{

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";

    String id_user, username,bm,email,id_company="",nama_company="",timeStamp="";
    ListView list;
    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    View dialogView;
    TextView textNoData;

    AlertDialog.Builder dialog;
    private static final String TAG = list_project2.class.getSimpleName();

    private static String url_get_list_note    = Server.URL + "note/list_note_by_user/";

    List<Data_note> itemList = new ArrayList<Data_note>();
    adapter_note2 adapter;

    LayoutInflater inflater;
    String tahun="",bulan="",jenis_note = "all";
    Spinner spinner_tahun,spinner_bulan,spinner_filter_project;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_project2);

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

        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        timeStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        Toast.makeText(getApplicationContext(), "Company Selected "+nama_company, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle("List Note "+tahun+" - "+bulan);

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        list    = (ListView)findViewById(R.id.list_report);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        adapter = new adapter_note2(list_note.this, itemList);
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
                           geNote(id_company,id_user,jenis_note,tahun,bulan);

                       }
                   }
        );

        String File_DIRECTORY = "ilv_note";
        File file =  new File(Environment.getExternalStorageDirectory()
                + "/" + File_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }


    }

    public void onRefresh() {
        geNote(id_company,id_user,jenis_note,tahun,bulan);
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            geNote(id_company,id_user,jenis_note,tahun,bulan);
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
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_list_cust_service, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_complain_menu) {

            Intent intent = new Intent(list_note.this, add_note.class);
            intent.putExtra("act", "add_note");
            intent.putExtra("tanggal", timeStamp);
            startActivityForResult(intent,1);
            return true;
        }
        else if (id == R.id.filter_report_sales) {
            //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            DialogFilter("Filter");
        }


        return super.onOptionsItemSelected(item);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_note.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_filter_note, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Note");

        spinner_tahun = dialogView.findViewById(R.id.spinner_tahun);
        spinner_bulan = dialogView.findViewById(R.id.spinner_bulan);
        spinner_filter_project = dialogView.findViewById(R.id.spinner_filter_note);

        addItemsOnSpinnerTahun(spinner_tahun);
        addItemsOnSpinnerBulan(spinner_bulan);

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tahun = String.valueOf(spinner_tahun.getSelectedItem());
                bulan = String.valueOf(spinner_bulan.getSelectedItem());
                jenis_note = String.valueOf(spinner_filter_project.getSelectedItem());

                getSupportActionBar().setTitle("List Note "+tahun+" - "+bulan);

                geNote(id_company,id_user,jenis_note,tahun,bulan);
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

    private void geNote(final String idCompany,final String idUser,final String jnsNote,final String thn,final String bln) {

        itemList.clear();
        textNoData.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(list_note.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_list_note+idCompany+"/"+idUser+"/"+jnsNote+"/"+thn+"/"+bln;

        Log.d("list_note", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_note jv = new Data_note();

                            jv.setid(obj.getString("id"));
                            jv.setnmr_note(obj.getString("nmr_note"));
                            jv.settgl_note(obj.getString("tgl_note"));
                            jv.setsubject_note(obj.getString("subject_note"));
                            jv.setjenis_note(obj.getString("jenis_note"));
                            jv.setisi_note(obj.getString("isi_note"));
                            jv.setcreate_by(obj.getString("create_by"));
                            jv.setnama_user(obj.getString("nama_user"));
                            jv.setparticipan(obj.getString("participan"));
                            jv.setstatus_note(obj.getString("status_note"));
                            jv.setfile_note(obj.getString("file_note"));
                            jv.setnmr_tiket(obj.getString("jml_tiket"));

                            itemList.add(jv);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // notifikasi adanya perubahan data pada adapter
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    //Toast.makeText(getApplicationContext(), "Klik Nama project untuk melihat detail ", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(list_note.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);
                    textNoData.setVisibility(View.VISIBLE);
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_note.this, "Error Connection ", Toast.LENGTH_LONG).show();
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
        // progressDialog.dismiss();
    }

    @Override
    public void onTiketClick(int position, Data_note note) {

        final String nmr_note = note.getnmr_note();
        final String tgl_note = note.gettgl_note();
        final String PARAM_DATA = "Data";
        //Toast.makeText(list_note.this, "nmr_note "+nmr_note, Toast.LENGTH_LONG).show();
        if(note.getcreate_by().equals(id_user)) {

            Intent intent = new Intent(list_note.this, edit_note.class);
            intent.putExtra("act", "edit_note");
            intent.putExtra("tanggal", tgl_note);
            intent.putExtra(PARAM_DATA, note);
            startActivityForResult(intent, 1);

        }
        else{

            Intent intent = new Intent(list_note.this, detail_note.class);
            intent.putExtra("act", "edit_note");
            intent.putExtra("tanggal", tgl_note);
            intent.putExtra(PARAM_DATA, note);
            startActivityForResult(intent, 1);

        }
    }

    @Override
    public void onJmlTiketClick(int position, Data_note note) {

        final String jml_tiket = note.getnmr_tiket();
        final String nmr_note = note.getnmr_note();
        //Toast.makeText(list_note.this, "jml_tiket "+jml_tiket, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(list_note.this, list_complain_by_note.class);
        intent.putExtra("nmr_note", nmr_note);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(list_join_visit.this, "back  ", Toast.LENGTH_SHORT).show();
                geNote(id_company,id_user,jenis_note,tahun,bulan);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
