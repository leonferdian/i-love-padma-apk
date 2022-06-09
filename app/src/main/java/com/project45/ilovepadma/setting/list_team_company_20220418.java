package com.project45.ilovepadma.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class list_team_company_20220418 extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,id_company="",nama_company="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = list_team_company_20220418.class.getSimpleName();

    private static String url_get_team_company    = Server.URL + "company/list_team_company/";


    TableLayout tabel_1a,tabel_1b;

    List<Data_company> itemList = new ArrayList<Data_company>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_team_company);

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


        tabel_1a = findViewById(R.id.tabel_1a);
        tabel_1b = findViewById(R.id.tabel_1b);

        cleanTable(tabel_1a);
        cleanTable(tabel_1b);
        getCompany(id_user);

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



    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        //getMenuInflater().inflate(R.menu.menu_list_company, menu);
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
            Intent intentku= new Intent(list_team_company_20220418.this, form_join_company.class);
            intentku.putExtra("act", "join_company");
            startActivityForResult(intentku,1);
        }


        return super.onOptionsItemSelected(item);
    }

    private void getCompany(final String id_user) {

        itemList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_team_company_20220418.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_team_company+id_company;

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

                            jv.setszId(obj.getString("id_user"));
                            jv.setszName(obj.getString("nama_user"));
                            jv.setszDescription(obj.getString("telp"));
                            jv.setdtmCreated(obj.getString("date_join"));
                            jv.setfoto_user(obj.getString("foto_user"));
                            jv.settgl_lahir_user(obj.getString("tgl_lahir"));

                            itemList.add(jv);


                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    Toast.makeText(getApplicationContext(), "Klik Nama untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callList1();
                    callList2();
                }
                else{
                    Toast.makeText(list_team_company_20220418.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_team_company_20220418.this, "Error Connection ", Toast.LENGTH_LONG).show();
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
            final Data_company sel = itemList.get(i);

            TableRow row=new TableRow(list_team_company_20220418.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 200;

            final String szId = sel.getszId();

            TextView txt_nmr=new TextView(list_team_company_20220418.this);
            txt_nmr.setText(""+nmr);
            txt_nmr.setBackgroundResource(R.color.boxdepanAtas);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(10,0,10,0);
            txt_nmr.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_nmr, params);

            TextView txt_nama_company=new TextView(list_team_company_20220418.this);
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

                    Intent intent = new Intent(list_team_company_20220418.this, detail_team_user.class);
                    intent.putExtra(PARAM_DATA, sel);
                    intent.putExtra("id_user", szId);
                    startActivity(intent);
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

            TableRow row=new TableRow(list_team_company_20220418.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            //params.width=180;
            params.height = 200;

            TableRow.LayoutParams paramsImgOutlet = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgOutlet.setMargins(3, 1, 3, 1);
            paramsImgOutlet.height=200;
            paramsImgOutlet.width=180;

            final String szId = sel.getszId();
            final String file_foto = sel.getfoto_user();
            final String szPhone = sel.getszDescription();

            ImageView imgOutlet = new ImageView(list_team_company_20220418.this);
            if(file_foto.equals("default_photo.png")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                imgOutlet.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_photo,options));
            }
            else{
                String img = Server.URL2 + "image_upload/list_foto_pribadi/"+file_foto;
                Picasso.with(imgOutlet.getContext())
                        .load(img)
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        .fit()
                        //.resize(300, 100)
                        .centerCrop()
                        //.centerInside()
                        .into(imgOutlet);
            }
            imgOutlet.setBackgroundResource(R.color.page_join_visit);
            imgOutlet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getApplicationContext(), detail_foto.class);
                    intent.putExtra("sku", "Photo Profil");
                    intent.putExtra("file_foto", file_foto);
                    intent.putExtra("tgl_buat", "");
                    intent.putExtra("act", "photo_profil");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            });
            // add to row
            row.addView(imgOutlet, paramsImgOutlet);

            TextView txt_telp=new TextView(list_team_company_20220418.this);
            txt_telp.setText(""+sel.getszDescription());
            txt_telp.setBackgroundResource(R.color.boxdepanAtas);
            txt_telp.setTextColor(getResources().getColor(R.color.white));
            txt_telp.setTextSize(10);
            txt_telp.setGravity(Gravity.CENTER);
            txt_telp.setPadding(10,0,10,0);
            txt_telp.setTypeface(null, Typeface.BOLD);
            txt_telp.getPaint().setUnderlineText(true);
            txt_telp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String contact="";
                    if(szPhone.substring(0,1).equals("0")){
                        int pjg = szPhone.length();
                        contact = "62"+szPhone.substring(1,pjg); // use country code with your phone number
                    }
                    else if(szPhone.substring(0,1).equals("+")){
                        int pjg = szPhone.length();
                        contact = "62"+szPhone.substring(3,pjg);
                    }
                    else{
                        contact = szPhone;
                    }

                    String url = "https://api.whatsapp.com/send?phone=" + contact;
                    /*
                    try {
                        PackageManager pm = getApplicationContext().getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(list_team_company.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    */
                    try {


                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(contact)+"@s.whatsapp.net");
                        startActivity(sendIntent);

                    } catch(Exception e) {
                        Log.e(TAG, "ERROR_OPEN_MESSANGER"+e.toString());
                    }
                }
            });

            // add to row
            row.addView(txt_telp, params);

            TextView txt_tgl=new TextView(list_team_company_20220418.this);
            txt_tgl.setText(""+sel.gettgl_lahir_user());
            txt_tgl.setBackgroundResource(R.color.boxdepanAtas);
            txt_tgl.setTextColor(getResources().getColor(R.color.white));
            txt_tgl.setTextSize(10);
            txt_tgl.setGravity(Gravity.CENTER);
            txt_tgl.setPadding(10,0,10,0);
            txt_tgl.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_tgl, params);

            tabel_1b.addView(row);

            nmr++;
        }


    }

    private boolean isAppInstalled(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
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
