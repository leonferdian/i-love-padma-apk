package com.project45.ilovepadma.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.page_choice2;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class list_master_company extends AppCompatActivity {

    String id_user, username,bm,email,jabatan;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = list_master_company.class.getSimpleName();

    private static String url_get_list_company    = Server.URL + "company/list_company/";

    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    TextView textNoData;
    TableLayout tabel_1a,tabel_1b;

    List<Data_company> itemList = new ArrayList<Data_company>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_master_company);

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
                           getCompany(id_user);

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
            getCompany(id_user);
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_list_company, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_complain_menu) {

            Intent intent = new Intent(getApplicationContext(), add_company.class);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_ID, id_user);
            intent.putExtra("act", "input_company");
            startActivityForResult(intent,1);



            return true;
        }
        else if (id == R.id.join_company) {
           // Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            Intent intentku= new Intent(list_master_company.this, form_join_company.class);
            intentku.putExtra("act", "join_company");
            startActivityForResult(intentku,1);
        }


        return super.onOptionsItemSelected(item);
    }

    private void getCompany(final String id_user) {

        itemList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_master_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_list_company+id_user;

        Log.d("list_team", url_server);
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

                            jv.setiInternalId(obj.getString("iInternalId"));
                            jv.setiId(obj.getString("iId"));
                            jv.setszId(obj.getString("szId"));
                            jv.setszName(obj.getString("szName"));
                            jv.setszDescription(obj.getString("szDescription"));
                            jv.setszLedgerId(obj.getString("szLedgerId"));
                            jv.setszCurrencyId(obj.getString("szCurrencyRateId"));
                            jv.setszTaxNo(obj.getString("szTaxNo"));
                            jv.setszPKPNo(obj.getString("szPKPNo"));
                            jv.setdtmNPWP(obj.getString("dtmNPWP"));
                            jv.setszNPWPNo(obj.getString("szNPWPNo"));
                            jv.setszSiup(obj.getString("szSiup"));
                            jv.setszAddress(obj.getString("szAddress"));
                            jv.setszCurrencyId(obj.getString("szCurrencyId"));
                            jv.setcodeCompany(obj.getString("codeCompany"));
                            jv.setszUserCreatedId(obj.getString("szUserCreatedId"));
                            jv.setdtmCreated(obj.getString("dtmCreated"));
                            jv.setdtmLastUpdated(obj.getString("dtmLastUpdated"));
                            jv.setnama_user(obj.getString("nama_user"));
                            jv.setuser_status(obj.getString("status_hak_akses"));

                            itemList.add(jv);


                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    swipe.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Klik Nama perusahaan untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callList1();
                    callList2();
                }
                else{
                    Toast.makeText(list_master_company.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
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
                Toast.makeText(list_master_company.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

            TableRow row=new TableRow(list_master_company.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;


            TextView txt_nmr=new TextView(list_master_company.this);
            txt_nmr.setText(""+nmr);
            txt_nmr.setBackgroundResource(R.color.boxdepanAtas);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(10,0,10,0);
            txt_nmr.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_nmr, params);

            TextView txt_nama_company=new TextView(list_master_company.this);
            txt_nama_company.setText(""+sel.getszName());
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

                    Intent intent = new Intent(list_master_company.this, add_company.class);
                    intent.putExtra(PARAM_DATA, sel);
                    intent.putExtra("act", "edit_company");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);



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

            TableRow row=new TableRow(list_master_company.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.width=120;
            params.height = 90;

            TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgOutlet.setMargins(3, 1, 3, 1);
            paramsImgOutlet.height=90;
            paramsImgOutlet.width=80;

            final String szId = sel.getszId();

            TextView txt_tgl=new TextView(list_master_company.this);
            txt_tgl.setText(""+sel.getdtmCreated());
            txt_tgl.setBackgroundResource(R.color.boxdepanAtas);
            txt_tgl.setTextColor(getResources().getColor(R.color.white));
            txt_tgl.setTextSize(10);
            txt_tgl.setGravity(Gravity.CENTER);
            txt_tgl.setPadding(10,0,10,0);
            txt_tgl.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_tgl, params);

            TextView txt_siup=new TextView(list_master_company.this);
            txt_siup.setText(""+sel.getnama_user());
            txt_siup.setBackgroundResource(R.color.boxdepanAtas);
            txt_siup.setTextColor(getResources().getColor(R.color.white));
            txt_siup.setTextSize(10);
            txt_siup.setGravity(Gravity.CENTER);
            txt_siup.setPadding(10,0,10,0);
            txt_siup.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_siup, params);

            tabel_1b.addView(row);

            nmr++;
        }


    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
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
                getCompany(id_user);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
