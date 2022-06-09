package com.project45.ilovepadma.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.absensi.list_absen;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.detail_deskripsi_complain_multi_file;
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class list_team_company extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,id_company="",nama_company="",timeStamp="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = list_team_company.class.getSimpleName();

    private static String url_get_team_company    = Server.URL + "company/list_team_company/";
    private static String url_get_team_last_seen    = Server.URL + "tracking/list_team_last_seen/";
    private static String url_get_team_version    = Server.URL + "tracking/list_team_version/";
    private static String url_get_team_absen    = Server.URL + "absen/get_list_team_absen/";
    private static String url_get_team_ticket    = Server.URL + "company/list_team_tiket_company/";

    TableLayout tabel_1a,tabel_1b,tabel_2a,tabel_2b,tabel_3a,tabel_3b,tabel_4a,tabel_4b;
    LinearLayout layout_last_seen,layout_team,layout_version,layout_tiket;

    List<Data_company> itemList = new ArrayList<Data_company>();
    List<Data_company> itemListLastSeen = new ArrayList<Data_company>();
    List<Data_company> itemListVersion = new ArrayList<Data_company>();
    List<Data_company> itemListTicket = new ArrayList<Data_company>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_team_company2);

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

        timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        tabel_1a = findViewById(R.id.tabel_1a);
        tabel_1b = findViewById(R.id.tabel_1b);
        layout_team = findViewById(R.id.layout_team);
        layout_last_seen = findViewById(R.id.layout_last_seen);
        layout_version = findViewById(R.id.layout_version);
        layout_tiket = findViewById(R.id.layout_tiket);
        tabel_2a = findViewById(R.id.tabel_2a);
        tabel_2b = findViewById(R.id.tabel_2b);
        tabel_3a = findViewById(R.id.tabel_3a);
        tabel_3b = findViewById(R.id.tabel_3b);
        tabel_4a = findViewById(R.id.tabel_4a);
        tabel_4b = findViewById(R.id.tabel_4b);

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
            Intent intentku= new Intent(list_team_company.this, form_join_company.class);
            intentku.putExtra("act", "join_company");
            startActivityForResult(intentku,1);
        }


        return super.onOptionsItemSelected(item);
    }

    public void onRadioLBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_list_team:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "aal ", Toast.LENGTH_LONG).show();
                    layout_team.setVisibility(View.VISIBLE);
                    layout_last_seen.setVisibility(View.GONE);
                    //layout_last_seen.removeAllViews();
                    layout_version.setVisibility(View.GONE);
                    layout_tiket.setVisibility(View.GONE);
                    cleanTable(tabel_2a);
                    cleanTable(tabel_2b);
                    cleanTable(tabel_3a);
                    cleanTable(tabel_3b);
                    cleanTable(tabel_4a);
                    cleanTable(tabel_4b);
                    getCompany(id_user);
                break;
            case R.id.radio_last_seen:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_LONG).show();
                    layout_team.setVisibility(View.GONE);
                    layout_last_seen.setVisibility(View.VISIBLE);
                    layout_version.setVisibility(View.GONE);
                    layout_tiket.setVisibility(View.GONE);
                    cleanTable(tabel_1a);
                    cleanTable(tabel_1b);
                    cleanTable(tabel_2a);
                    cleanTable(tabel_2b);
                    cleanTable(tabel_4a);
                    cleanTable(tabel_4b);
                    //getLastSeen(id_company);
                    getTeamAbsen(id_company,timeStamp);
                break;
            case R.id.radio_app_version:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    layout_team.setVisibility(View.GONE);
                    layout_last_seen.setVisibility(View.GONE);
                    //layout_last_seen.removeAllViews();
                    layout_version.setVisibility(View.VISIBLE);
                    layout_tiket.setVisibility(View.GONE);
                    cleanTable(tabel_1a);
                    cleanTable(tabel_1b);
                    cleanTable(tabel_3a);
                    cleanTable(tabel_3b);
                    cleanTable(tabel_4a);
                    cleanTable(tabel_4b);
                    getTeamVersion(id_company);

                break;
            case R.id.radio_tiket:
                if (checked)
                    //Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    layout_team.setVisibility(View.GONE);
                    layout_last_seen.setVisibility(View.GONE);
                    //layout_last_seen.removeAllViews();
                    layout_version.setVisibility(View.GONE);
                    layout_tiket.setVisibility(View.VISIBLE);
                    cleanTable(tabel_1a);
                    cleanTable(tabel_1b);
                    cleanTable(tabel_2a);
                    cleanTable(tabel_2b);
                    cleanTable(tabel_3a);
                    cleanTable(tabel_3b);
                    getTeamTicket(id_company);

                break;

        }
    }

    private void getCompany(final String id_user) {

        itemList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_team_company.this,
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
                            jv.setcompany_creator(obj.getString("creator_company"));
                            jv.setcodeCompany(obj.getString("code_company"));
                            jv.setuser_status(obj.getString("status_hak_akses"));

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
                    Toast.makeText(list_team_company.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_team_company.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

            TableRow row=new TableRow(list_team_company.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 200;

            final String szId = sel.getszId();
            final String company_creator = sel.getcompany_creator();
            final String code_company = sel.getcodeCompany();
            final String status_hak_akses = sel.getuser_status();

            TextView txt_nmr=new TextView(list_team_company.this);
            txt_nmr.setText(""+nmr);
            txt_nmr.setBackgroundResource(R.color.boxdepanAtas);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(10,0,10,0);
            txt_nmr.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_nmr, params);

            TextView txt_nama_company=new TextView(list_team_company.this);
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

                    Intent intent = new Intent(list_team_company.this, detail_team_user.class);
                    intent.putExtra(PARAM_DATA, sel);
                    intent.putExtra("id_user", szId);
                    intent.putExtra("company_creator", company_creator);
                    intent.putExtra("code_company", code_company);
                    intent.putExtra("status_hak_akses", status_hak_akses);
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

            TableRow row=new TableRow(list_team_company.this);
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

            ImageView imgOutlet = new ImageView(list_team_company.this);
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

            TextView txt_telp=new TextView(list_team_company.this);
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

            TextView txt_tgl=new TextView(list_team_company.this);
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

    private void getLastSeen(final String idCompany) {

        itemListLastSeen.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_team_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_team_last_seen+id_company;

        Log.d("list_last", url_server);
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
                            jv.setlatitude(obj.getString("latitude"));
                            jv.setlongitude(obj.getString("longitude"));
                            jv.setszAddress(obj.getString("alamat"));
                            jv.setstatus_user(obj.getString("status_user"));
                            jv.setdtmLastUpdated(obj.getString("date_update"));
                            jv.setfoto_user(obj.getString("foto_user"));
                            jv.setabsen_today(obj.getString("absen_today"));
                            jv.settime_check_in(obj.getString("time_check_in"));
                            jv.settime_check_out(obj.getString("time_check_out"));
                            jv.setaddress_check_in(obj.getString("address_check_in"));
                            jv.setaddress_check_out(obj.getString("address_check_out"));

                            itemListLastSeen.add(jv);


                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    //Toast.makeText(getApplicationContext(), "Klik Nama untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callLastSeen();
                }
                else{
                    Toast.makeText(list_team_company.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_team_company.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

    private void callLastSeen() {
        if (getApplicationContext() != null) {

            for (int i = 0; i < itemListLastSeen.size(); i++) {
                Data_company sel = itemListLastSeen.get(i);

                LayoutInflater inflater = LayoutInflater.from(list_team_company.this);
                View v = inflater.inflate(R.layout.item_last_seen, null, false);
                CircularImageView image_user = v.findViewById(R.id.image_user);
                TextView txt_nama = v.findViewById(R.id.txt_nama);
                TextView txt_waktu = v.findViewById(R.id.txt_waktu);
                TextView txt_check_in = v.findViewById(R.id.txt_check_in);
                TextView txt_alamat_check_in = v.findViewById(R.id.txt_alamat_check_in);
                TextView txt_check_out = v.findViewById(R.id.txt_check_out);
                TextView txt_alamat_check_out = v.findViewById(R.id.txt_alamat_check_out);

                final String bm = Server.URL2 + "image_upload/list_foto_pribadi/" + sel.getfoto_user();
                Picasso.with(image_user.getContext())
                        .load(bm)
                        .fit()
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        //.resize(100, 100)
                        //.onlyScaleDown()
                        .centerCrop()
                        .into(image_user);

                image_user.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getApplicationContext(), detail_foto.class);
                        intent.putExtra("sku", "Photo Profil");
                        intent.putExtra("file_foto", sel.getfoto_user());
                        intent.putExtra("tgl_buat", "");
                        intent.putExtra("act", "photo_profil");
                        //startActivity(intent);
                        startActivityForResult(intent, 1);
                    }
                });

                txt_nama.setText(sel.getszName());
                txt_waktu.setText("Last Seen "+sel.getdtmLastUpdated());

                if(sel.getabsen_today().equals("no"))
                {
                    txt_alamat_check_in.setText(sel.getszAddress());
                    txt_check_in.setVisibility(View.GONE);
                    txt_check_out.setVisibility(View.GONE);
                    txt_alamat_check_out.setVisibility(View.GONE);
                }
                else{
                    txt_alamat_check_in.setText(sel.getaddress_check_in());
                    txt_check_in.setVisibility(View.VISIBLE);
                    txt_check_out.setVisibility(View.VISIBLE);
                    txt_alamat_check_out.setVisibility(View.VISIBLE);
                    txt_check_in.setText("Check In "+sel.gettime_check_in());
                    txt_check_out.setText("Check Out "+sel.gettime_check_out());
                    txt_alamat_check_out.setText(sel.getaddress_check_out());
                }


                layout_last_seen.addView(v);
            }
        }

    }

    private void getTeamVersion(final String idCompany) {

        itemListVersion.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_team_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_team_version+idCompany;

        Log.d("list_version", url_server);
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
                            jv.setapp_version(obj.getString("app_version"));
                            jv.setfoto_user(obj.getString("foto_user"));
                            jv.setdtmLastUpdated(obj.getString("date_update"));

                            itemListVersion.add(jv);


                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    //Toast.makeText(getApplicationContext(), "Klik Nama untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callTV1();
                    callTV2();
                }
                else{
                    Toast.makeText(list_team_company.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_team_company.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

    private void callTV1() {
        int nmr = 1;
        for (int i = 0; i < itemListVersion.size(); i++) {
            final Data_company sel = itemListVersion.get(i);

            TableRow row=new TableRow(list_team_company.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 200;

            final String szId = sel.getszId();

            TextView txt_nmr=new TextView(list_team_company.this);
            txt_nmr.setText(""+nmr);
            txt_nmr.setBackgroundResource(R.color.boxdepanAtas);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(10,0,10,0);
            txt_nmr.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_nmr, params);

            TextView txt_nama_company=new TextView(list_team_company.this);
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

                    Intent intent = new Intent(list_team_company.this, detail_team_user.class);
                    intent.putExtra(PARAM_DATA, sel);
                    intent.putExtra("id_user", szId);
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });

            // add to row
            row.addView(txt_nama_company, params);

            tabel_2a.addView(row);

            nmr++;
        }


    }

    private void callTV2() {
        int nmr = 1;

        for (int i = 0; i < itemListVersion.size(); i++) {
            final Data_company sel = itemListVersion.get(i);

            TableRow row=new TableRow(list_team_company.this);
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

            ImageView imgOutlet = new ImageView(list_team_company.this);
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


            TextView txt_tgl=new TextView(list_team_company.this);
            txt_tgl.setText(""+sel.getapp_version());
            txt_tgl.setBackgroundResource(R.color.boxdepanAtas);
            txt_tgl.setTextColor(getResources().getColor(R.color.white));
            txt_tgl.setTextSize(10);
            txt_tgl.setGravity(Gravity.CENTER);
            txt_tgl.setPadding(10,0,10,0);
            txt_tgl.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_tgl, params);

            tabel_2b.addView(row);

            nmr++;
        }


    }

    private void getTeamAbsen(final String idCompany,final String tglAbsen) {

        itemListLastSeen.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_team_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_team_absen+idCompany+"/"+tglAbsen;

        Log.d("list_version", url_server);
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
                            jv.setid_absen(obj.getString("id_absen"));
                            jv.setszName(obj.getString("nama_user"));
                            jv.settime_check_in(obj.getString("time_check_in"));
                            jv.settime_check_out(obj.getString("time_check_out"));
                            jv.setfoto_user(obj.getString("image_absen"));

                            itemListLastSeen.add(jv);


                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    Toast.makeText(getApplicationContext(), "Klik Check In untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callAbsen1();
                    callAbsen2();
                }
                else{
                    Toast.makeText(list_team_company.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_team_company.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

    private void callAbsen1() {
        int nmr = 1;
        for (int i = 0; i < itemListLastSeen.size(); i++) {
            final Data_company sel = itemListLastSeen.get(i);

            TableRow row=new TableRow(list_team_company.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 200;

            final String szId = sel.getszId();

            TextView txt_nmr=new TextView(list_team_company.this);
            txt_nmr.setText(""+nmr);
            txt_nmr.setBackgroundResource(R.color.boxdepanAtas);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(10,0,10,0);
            txt_nmr.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_nmr, params);

            TextView txt_nama_company=new TextView(list_team_company.this);
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

                    Intent intent = new Intent(list_team_company.this, detail_team_user.class);
                    intent.putExtra(PARAM_DATA, sel);
                    intent.putExtra("id_user", szId);
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });

            // add to row
            row.addView(txt_nama_company, params);

            tabel_3a.addView(row);

            nmr++;
        }


    }

    private void callAbsen2() {
        int nmr = 1;

        for (int i = 0; i < itemListLastSeen.size(); i++) {
            final Data_company sel = itemListLastSeen.get(i);

            TableRow row=new TableRow(list_team_company.this);
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
            final String id_absen = sel.getid_absen();

            ImageView imgOutlet = new ImageView(list_team_company.this);
            if(file_foto.equals("default_photo.png")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                imgOutlet.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_photo,options));
            }
            else{
                String img = Server.URL2 + "image_upload/list_foto_absen/"+file_foto;
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
                    intent.putExtra("sku", "Image Absen");
                    intent.putExtra("file_foto", file_foto);
                    intent.putExtra("tgl_buat", "");
                    intent.putExtra("act", "image_absen");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            });
            // add to row
            row.addView(imgOutlet, paramsImgOutlet);


            TextView txt_check_in=new TextView(list_team_company.this);
            txt_check_in.setText(""+sel.gettime_check_in());
            txt_check_in.setBackgroundResource(R.color.boxdepanAtas);
            txt_check_in.setTextColor(getResources().getColor(R.color.white));
            txt_check_in.setTextSize(10);
            txt_check_in.setGravity(Gravity.CENTER);
            txt_check_in.setPadding(10,0,10,0);
            txt_check_in.setTypeface(null, Typeface.BOLD);
            txt_check_in.getPaint().setUnderlineText(true);
            txt_check_in.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getApplicationContext(), detail_absen_user2.class);
                    intent.putExtra("id_absen", id_absen);
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            });

            // add to row
            row.addView(txt_check_in, params);

            TextView txt_check_out=new TextView(list_team_company.this);
            txt_check_out.setText(""+sel.gettime_check_out());
            txt_check_out.setBackgroundResource(R.color.boxdepanAtas);
            txt_check_out.setTextColor(getResources().getColor(R.color.white));
            txt_check_out.setTextSize(10);
            txt_check_out.setGravity(Gravity.CENTER);
            txt_check_out.setPadding(10,0,10,0);
            txt_check_out.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_check_out, params);

            tabel_3b.addView(row);

            nmr++;
        }


    }

    private void getTeamTicket(final String idCompany) {

        itemListTicket.clear();
        final ProgressDialog progressDialog = new ProgressDialog(list_team_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_team_ticket+idCompany;

        Log.d("list_tiket", url_server);
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
                            jv.setfoto_user(obj.getString("foto_user"));
                            jv.settiket_all(obj.getString("for_you_all"));
                            jv.settiket_close(obj.getString("for_you_close"));

                            itemListTicket.add(jv);


                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    //Toast.makeText(getApplicationContext(), "Klik Nama untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callListTiket1();
                    callListTiket2();
                }
                else{
                    Toast.makeText(list_team_company.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(list_team_company.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

    private void callListTiket1() {
        int nmr = 1;
        for (int i = 0; i < itemListTicket.size(); i++) {
            final Data_company sel = itemListTicket.get(i);

            TableRow row=new TableRow(list_team_company.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 200;

            final String szId = sel.getszId();

            TextView txt_nmr=new TextView(list_team_company.this);
            txt_nmr.setText(""+nmr);
            txt_nmr.setBackgroundResource(R.color.boxdepanAtas);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setPadding(10,0,10,0);
            txt_nmr.setTypeface(null, Typeface.BOLD);

            // add to row
            row.addView(txt_nmr, params);

            TextView txt_nama_company=new TextView(list_team_company.this);
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

                    Intent intent = new Intent(list_team_company.this, detail_team_user.class);
                    intent.putExtra(PARAM_DATA, sel);
                    intent.putExtra("id_user", szId);
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                }
            });

            // add to row
            row.addView(txt_nama_company, params);

            tabel_4a.addView(row);

            nmr++;
        }


    }

    private void callListTiket2() {
        int nmr = 1;

        for (int i = 0; i < itemListTicket.size(); i++) {
            final Data_company sel = itemListTicket.get(i);

            TableRow row=new TableRow(list_team_company.this);
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

            ImageView imgOutlet = new ImageView(list_team_company.this);
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

            TextView txt_telp=new TextView(list_team_company.this);
            txt_telp.setText(""+sel.gettiket_close()+" / "+sel.gettiket_all());
            txt_telp.setBackgroundResource(R.color.boxdepanAtas);
            txt_telp.setTextColor(getResources().getColor(R.color.white));
            txt_telp.setTextSize(10);
            txt_telp.setGravity(Gravity.CENTER);
            txt_telp.setPadding(10,0,10,0);
            txt_telp.setTypeface(null, Typeface.BOLD);
            //txt_telp.getPaint().setUnderlineText(true);

            // add to row
            row.addView(txt_telp, params);

            tabel_4b.addView(row);

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
