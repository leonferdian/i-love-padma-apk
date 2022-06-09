package com.project45.ilovepadma.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.MainActivity3;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class detail_absen_user2 extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,id_company="",nama_company="",timeStamp="",id_absen="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = list_team_company.class.getSimpleName();

    private static String url_get_team_last_seen    = Server.URL + "absen/detail_team_absen2/";

    ImageView image_user;
    TextView txt_nama,txt_waktu,txt_check_in,txt_alamat_check_in,txt_check_out,txt_alamat_check_out;
    ImageButton image_check_in,image_check_out;

    int success;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_absen_user2);


        initToolbar();
        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        image_user = findViewById(R.id.image_user);
        txt_nama = findViewById(R.id.txt_nama);
        txt_waktu = findViewById(R.id.txt_waktu);
        txt_check_in = findViewById(R.id.txt_check_in);
        txt_alamat_check_in = findViewById(R.id.txt_alamat_check_in);
        txt_check_out = findViewById(R.id.txt_check_out);
        txt_alamat_check_out = findViewById(R.id.txt_alamat_check_out);
        image_check_in = findViewById(R.id.image_check_in);
        image_check_out = findViewById(R.id.image_check_out);

        Intent intentku = getIntent(); // gets the previously created intent
        id_absen = intentku.getStringExtra("id_absen");
        get_dashboard_tiket(id_absen);
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();

        return true;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            //window.setStatusBarColor(getResources().getColor(R.color.blue_black));
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Absen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void get_dashboard_tiket(final String idAbsen){

        final ProgressDialog progressDialog = new ProgressDialog(detail_absen_user2.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_edit       = url_get_team_last_seen+idAbsen;
        Log.d("dashboard_tiket", url_edit);
        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("detail_absen", jObj.toString());
                        String id_user      = jObj.getString("id_user");
                        String nama_user    = jObj.getString("nama_user");
                        String image_absen  = jObj.getString("image_absen");
                        String time_check_in  = jObj.getString("time_check_in");
                        String time_check_out      = jObj.getString("time_check_out");
                        String address_check_in    = jObj.getString("address_check_in");
                        String address_check_out  = jObj.getString("address_check_out");
                        String lat_check_in  = jObj.getString("lat_check_in");
                        String lng_check_in      = jObj.getString("lng_check_in");
                        String lat_check_out    = jObj.getString("lat_check_out");
                        String lng_check_out  = jObj.getString("lng_check_out");
                        String last_seen  = jObj.getString("last_seen");

                        String img = Server.URL2 + "image_upload/list_foto_absen/"+image_absen;
                        Picasso.with(image_user.getContext())
                                .load(img)
                                .placeholder(R.drawable.ic_camera_alt_black_48dp)
                                .fit()
                                //.resize(300, 100)
                                //.centerCrop()
                                .centerInside()
                                .into(image_user);

                        txt_nama.setText(nama_user);
                        txt_waktu.setText("Last Seen "+last_seen);
                        txt_check_in.setText("Check In "+time_check_in);
                        txt_check_out.setText("Check Out "+time_check_out);
                        txt_alamat_check_in.setText(address_check_in);
                        txt_alamat_check_out.setText(address_check_out);

                        image_user.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent(getApplicationContext(), detail_foto.class);
                                intent.putExtra("sku", "Image Absen");
                                intent.putExtra("file_foto", image_absen);
                                intent.putExtra("tgl_buat", "");
                                intent.putExtra("act", "image_absen");
                                //startActivity(intent);
                                startActivityForResult(intent, 1);
                            }
                        });

                        txt_alamat_check_in.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                String uri = "http://maps.google.com/maps?q=loc:" + lat_check_in + "," + lng_check_in;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
                            }
                        });

                        image_check_in.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                String uri = "http://maps.google.com/maps?q=loc:" + lat_check_in + "," + lng_check_in;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
                            }
                        });

                        txt_alamat_check_out.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                String uri = "http://maps.google.com/maps?q=loc:" + lat_check_out + "," + lng_check_out;
                                //String uri = "geo:" + destinationLatitude + "," + destinationLongitude; tnp pin
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
                            }
                        });

                        image_check_out.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                String uri = "http://maps.google.com/maps?q=loc:" + lat_check_out + "," + lng_check_out;
                                //String uri = "geo:" + destinationLatitude + "," + destinationLongitude; tnp pin
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
                            }
                        });

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Klik Alamat untuk melihat lokasi ", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(detail_absen_user2.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_absen_user2.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }
}
