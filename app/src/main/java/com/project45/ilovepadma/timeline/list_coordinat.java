package com.project45.ilovepadma.timeline;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_join_visit;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class list_coordinat extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String id_user, username,nmr_customer="",email,employee_id_dms3,db_user,jabatan,list_rute;

    private static final String TAG = list_coordinat.class.getSimpleName();

    private static String url_get_list_coordinat     = Server.URL + "sfa_leader/list_coordinat/";

    private GoogleMap mMap;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    int zoom_value=15;
    String[] separated ;

    ArrayList<Data_join_visit> itemList = new ArrayList<Data_join_visit>();
    ArrayList<Data_join_visit> arrJoin = new ArrayList<Data_join_visit>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_coordinat);

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


        Intent intentku = getIntent(); // gets the previously created intent
        //nmr_customer = intentku.getStringExtra("nmr_customer");
        //separated = nmr_customer.split(",");
        itemList = (ArrayList<Data_join_visit>) intentku.getSerializableExtra(Api.PARAM_DATA);
        //Toast.makeText(getApplicationContext(), "separated "+separated.length,Toast.LENGTH_LONG).show();
        getOutlet();
        //Toast.makeText(getApplicationContext(), "nmr "+nmr_customer,Toast.LENGTH_LONG).show();

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION );

        }

        if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION );

        }
    }

    private void getOutlet(){
        int nmr = 1;
        for (int i = 0; i < itemList.size(); i++) {
            final Data_join_visit sel = itemList.get(i);
            if(sel.getnilai_klik().equals("1")) {
                //Toast.makeText(getApplicationContext(), "nmr " + sel.getId()+" Nama "+sel.getnama_customer(), Toast.LENGTH_LONG).show();
                get_coordinat(nmr,sel.getnmr_jv(),sel.getid_customer(),sel.getnama_customer(),sel.getdate_create());
                nmr++;
            }
        }
    }

    private void get_coordinat(final int nomor, final String id_doc, final String id_cust, final String nama_cust, final String jam_kunjungan2){
        final ProgressDialog progressDialog = new ProgressDialog(list_coordinat.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_edit       = url_get_list_coordinat+id_doc+"/"+id_cust;
        Log.d("url_server", url_edit);
        JsonArrayRequest jArr = new JsonArrayRequest(url_edit, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    //Log.d(TAG, response.toString());
                    Log.d(TAG, "jml_data "+ String.valueOf(response.length()));
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jObj = response.getJSONObject(i);

                            //Data_join_visit item = new Data_join_visit();
                            //item.setId(obj.getString("nmr"));
                            String lat_outlet      = jObj.getString("lat_user");
                            String long_outlet      = jObj.getString("long_user");
                            String jam_kunjungan      = jObj.getString("jam_kunjungan");
                            String alamat_outlet="";
                            if(jObj.getString("alamat_outlet").equals(""))
                            {
                                alamat_outlet      = setAddressFromLatLng(Double.valueOf(lat_outlet), Double.valueOf(long_outlet));
                            }
                            else{
                                alamat_outlet      = jObj.getString("alamat_outlet");
                            }

                            LatLng latLng = new LatLng(Double.valueOf(lat_outlet), Double.valueOf(long_outlet));
                            if(nomor==1){
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_value));
                            }

                            mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                    .snippet(nama_cust+" Dikunjungi pukul "+jam_kunjungan2)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                            // menambah item ke array
                            //itemList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(list_coordinat.this, "Tidak bisa mendapatkan kordinat ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                // notifikasi adanya perubahan data pada adapter



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(list_coordinat.this, "No More Items Availablee", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    public String setAddressFromLatLng(double lat, double lng) {
        String alamat="";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                alamat = address.getAddressLine(0);
                //sb.append(address.getSubLocality()).append("\n");
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable connect to Geocoder", e);
        }

        return alamat;

    }
}
