package com.project45.ilovepadma.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.add_complain;
import com.project45.ilovepadma.complain.list_complain;
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.setting.add_company;
import com.project45.ilovepadma.setting.form_join_company;
import com.project45.ilovepadma.setting.list_master_company;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_project extends AppCompatActivity {

    String id_user, username,bm,email,jabatan;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = list_master_company.class.getSimpleName();

    private static String url_get_list_project    = Server.URL + "project/list_project_by_user/";

    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    TextView textNoData;
    TableLayout tabel_1a,tabel_1b;

    String id_company="",nama_company="";

    List<Data_company> itemList = new ArrayList<Data_company>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_project);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        tabel_1a = findViewById(R.id.tabel_1a);
        tabel_1b = findViewById(R.id.tabel_1b);

        swipe.setOnRefreshListener(swipeRefreshDo);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //swipe.setRefreshing(true);
                           cleanTable(tabel_1a);
                           cleanTable(tabel_1b);
                           getProject(id_company,id_user);

                       }
                   }
        );

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
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            cleanTable(tabel_1a);
            cleanTable(tabel_1b);
            getProject(id_company,id_user);
        }
    };

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

            Intent intent = new Intent(list_project.this, add_project.class);
            //intent.putExtra("act", "edit_outlet");
            intent.putExtra("act", "add_project");
            startActivityForResult(intent,1);
            return true;
        }
        else if (id == R.id.filter_report_sales) {
            Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();

        }


        return super.onOptionsItemSelected(item);
    }

    private void getProject(final String idCompany,final String idUser) {

        itemList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_project.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_list_project+idCompany+"/"+idUser;

        Log.d("list_project", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_company jv = new Data_company();

                            jv.setszLedgerId(obj.getString("id"));
                            jv.setszCurrencyId(obj.getString("nmr_project"));
                            jv.setiInternalId(obj.getString("nama_project"));
                            jv.setiId(obj.getString("jenis_project"));
                            jv.setdivisi(obj.getString("divisi"));
                            jv.setszId(obj.getString("main_project"));
                            jv.setszName(obj.getString("sub_project"));
                            jv.setszDescription(obj.getString("date_create"));
                            jv.setszUserCreatedId(obj.getString("create_by"));

                            itemList.add(jv);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    swipe.setRefreshing(false);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Klik Nama project untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callList1();
                    callList2();
                }
                else{
                    Toast.makeText(list_project.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    swipe.setRefreshing(false);

                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_project.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

    private void callList1() {
        int nmr = 1;
        for (int i = 0; i < itemList.size(); i++) {
            final Data_company sel = itemList.get(i);

            TableRow row=new TableRow(list_project.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;

            final String szId = sel.getszLedgerId();
            final String nmr_project = sel.getszCurrencyId();
            final String createBy = sel.getszUserCreatedId();

            TextView txt_nmr=new TextView(list_project.this);
            txt_nmr.setText(""+nmr);
            txt_nmr.setBackgroundResource(R.color.boxdepanAtas);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(10,0,10,0);
            txt_nmr.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_nmr, params);

            TextView txt_nama_company=new TextView(list_project.this);
            txt_nama_company.setText(""+sel.getiInternalId());
            txt_nama_company.setBackgroundResource(R.color.boxdepanAtas);
            txt_nama_company.setTextColor(getResources().getColor(R.color.white));
            txt_nama_company.setTextSize(10);
            txt_nama_company.setGravity(Gravity.CENTER);
            txt_nama_company.setPadding(10,0,10,0);
            txt_nama_company.setTypeface(null, Typeface.BOLD);
            txt_nama_company.getPaint().setUnderlineText(true);
            txt_nama_company.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final String PARAM_DATA = "Data";
                    Intent intent = new Intent(list_project.this, edit_project.class);
                    //intent.putExtra("act", "edit_outlet");
                    intent.putExtra("act", "edit_project");
                    intent.putExtra("id_project", nmr_project);
                    intent.putExtra("nmr_project", szId);
                    intent.putExtra("create_by", createBy);
                    startActivityForResult(intent,1);

                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });

            // add to row
            row.addView(txt_nama_company, params);

            tabel_1a.addView(row);

            nmr++;
        }


    }

    private void callList2() {
        int nmr = 1;

        for (int i = 0; i < itemList.size(); i++) {
            final Data_company sel = itemList.get(i);

            TableRow row=new TableRow(list_project.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.width=190;
            params.height = 90;

            TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgOutlet.setMargins(3, 1, 3, 1);
            paramsImgOutlet.height=90;
            paramsImgOutlet.width=80;

            final String szId = sel.getszCurrencyId();

            TextView txt_tgl=new TextView(list_project.this);
            txt_tgl.setText(""+sel.getiId());
            txt_tgl.setBackgroundResource(R.color.boxdepanAtas);
            txt_tgl.setTextColor(getResources().getColor(R.color.white));
            txt_tgl.setTextSize(10);
            txt_tgl.setGravity(Gravity.CENTER);
            txt_tgl.setPadding(10,0,10,0);
            txt_tgl.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_tgl, params);

            TextView txt_siup=new TextView(list_project.this);
            txt_siup.setText(""+sel.getszId());
            txt_siup.setBackgroundResource(R.color.boxdepanAtas);
            txt_siup.setTextColor(getResources().getColor(R.color.white));
            txt_siup.setTextSize(10);
            txt_siup.setGravity(Gravity.CENTER);
            txt_siup.setPadding(10,0,10,0);
            txt_siup.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_siup, params);

            TextView txt_sub_project=new TextView(list_project.this);
            txt_sub_project.setText(""+sel.getszName());
            txt_sub_project.setBackgroundResource(R.color.boxdepanAtas);
            txt_sub_project.setTextColor(getResources().getColor(R.color.white));
            txt_sub_project.setTextSize(10);
            txt_sub_project.setGravity(Gravity.CENTER);
            txt_sub_project.setPadding(10,0,10,0);
            txt_sub_project.setTypeface(null, Typeface.BOLD);
            txt_sub_project.getPaint().setUnderlineText(true);
            txt_sub_project.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final String PARAM_DATA = "Data";
                    Intent intent = new Intent(list_project.this, list_sub_project.class);
                    //intent.putExtra("act", "edit_outlet");
                    intent.putExtra("nmr_project", szId);
                    intent.putExtra("jenis_project", sel.getiId());
                    intent.putExtra("divisi", sel.getdivisi());
                    intent.putExtra("nama_project", sel.getiInternalId());
                    intent.putExtra("main_project", sel.getszId());
                    startActivityForResult(intent,1);

                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });

            // add to row
            row.addView(txt_sub_project, params);

            TextView txt_date_create=new TextView(list_project.this);
            txt_date_create.setText(""+sel.getszDescription());
            txt_date_create.setBackgroundResource(R.color.boxdepanAtas);
            txt_date_create.setTextColor(getResources().getColor(R.color.white));
            txt_date_create.setTextSize(10);
            txt_date_create.setGravity(Gravity.CENTER);
            txt_date_create.setPadding(10,0,10,0);
            txt_date_create.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_date_create, params);

            tabel_1b.addView(row);

            nmr++;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(list_cust_st.this, "req "+requestCode, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(list_join_visit.this, "back  ", Toast.LENGTH_SHORT).show();
                cleanTable(tabel_1a);
                cleanTable(tabel_1b);
                getProject(id_company,id_user);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}
