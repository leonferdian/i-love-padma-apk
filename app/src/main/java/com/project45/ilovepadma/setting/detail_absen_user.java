package com.project45.ilovepadma.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
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

public class detail_absen_user extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,id_company="",nama_company="",timeStamp="",id_absen="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = list_team_company.class.getSimpleName();

    private static String url_get_team_last_seen    = Server.URL + "absen/detail_team_absen/";

    List<Data_company> itemList = new ArrayList<Data_company>();

    LinearLayout layout_team;

    List<Data_company> itemListLastSeen = new ArrayList<Data_company>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_absen_user);

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

        layout_team = findViewById(R.id.layout_team);

        Intent intentku = getIntent(); // gets the previously created intent
        id_absen = intentku.getStringExtra("id_absen");
        getLastSeen(id_absen);
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

    private void getLastSeen(final String idAbsen) {

        itemListLastSeen.clear();
        final ProgressDialog progressDialog = new ProgressDialog(detail_absen_user.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server = url_get_team_last_seen+idAbsen;

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
                            jv.setfoto_user(obj.getString("image_absen"));
                            jv.settime_check_in(obj.getString("time_check_in"));
                            jv.settime_check_out(obj.getString("time_check_out"));
                            jv.setaddress_check_in(obj.getString("address_check_in"));
                            jv.setaddress_check_out(obj.getString("address_check_out"));
                            jv.setlatitude(obj.getString("lat_check_in"));
                            jv.setlongitude(obj.getString("lng_check_in"));
                            jv.setlatitude2(obj.getString("lat_check_out"));
                            jv.setlongitude2(obj.getString("lng_check_out"));
                            jv.setdtmLastUpdated(obj.getString("last_seen"));


                            itemListLastSeen.add(jv);


                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    Toast.makeText(getApplicationContext(), "Klik Alamat untuk melihat lokasi ", Toast.LENGTH_LONG).show();
                    callLastSeen();
                }
                else{
                    Toast.makeText(detail_absen_user.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();


                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_absen_user.this, "Error Connection ", Toast.LENGTH_LONG).show();
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

                LayoutInflater inflater = LayoutInflater.from(detail_absen_user.this);
                View v = inflater.inflate(R.layout.item_last_seen, null, false);
                CircularImageView image_user = v.findViewById(R.id.image_user);
                TextView txt_nama = v.findViewById(R.id.txt_nama);
                TextView txt_waktu = v.findViewById(R.id.txt_waktu);
                TextView txt_check_in = v.findViewById(R.id.txt_check_in);
                TextView txt_alamat_check_in = v.findViewById(R.id.txt_alamat_check_in);
                TextView txt_check_out = v.findViewById(R.id.txt_check_out);
                TextView txt_alamat_check_out = v.findViewById(R.id.txt_alamat_check_out);

                String img = Server.URL2 + "image_upload/list_foto_absen/"+sel.getfoto_user();
                Picasso.with(image_user.getContext())
                        .load(img)
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        .fit()
                        //.resize(300, 100)
                        .centerCrop()
                        //.centerInside()
                        .into(image_user);

                image_user.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getApplicationContext(), detail_foto.class);
                        intent.putExtra("sku", "Image Absen");
                        intent.putExtra("file_foto", sel.getfoto_user());
                        intent.putExtra("tgl_buat", "");
                        intent.putExtra("act", "image_absen");
                        //startActivity(intent);
                        startActivityForResult(intent, 1);
                    }
                });

                txt_nama.setText(sel.getszName());
                txt_waktu.setText("Last Seen "+sel.getdtmLastUpdated());
                txt_check_in.setText("Check In "+sel.gettime_check_in());
                txt_alamat_check_in.setText(sel.getaddress_check_in());
                txt_check_out.setText("Check Out "+sel.gettime_check_out());
                txt_alamat_check_out.setText(sel.getaddress_check_out());

                txt_alamat_check_in.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String uri = "http://maps.google.com/maps?q=loc:" + sel.getlatitude() + "," + sel.getlongitude();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                });

                txt_alamat_check_out.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String uri = "http://maps.google.com/maps?q=loc:" + sel.getlatitude2() + "," + sel.getlongitude2();
                        //String uri = "geo:" + destinationLatitude + "," + destinationLongitude; tnp pin
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                });


                layout_team.addView(v);
            }
        }

    }
}
