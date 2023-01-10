package com.project45.ilovepadma.outlet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_customer;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.timeline.list_coordinat;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class show_coordinat_outlet_semesta extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final String TAG = show_coordinat_outlet_semesta.class.getSimpleName();
    private static String url_get_list_koordinat_outlet_semesta     = Server.URL + "outlet/list_koordinat_outlet_semesta/";
    private GoogleMap mMap;
    GPSTracker gps;
    Boolean isTampil;
    HashMap<String, String> markerMap = new HashMap<String, String>();
    int zoom_value = 15;
    String id_user, username,email,jabatan, radius,jml_outlet,segment_tiv,latitude,longitude, check_moving, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_coordinat_outlet_semesta);

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

        isTampil = true;

        Intent intentku = getIntent(); // gets the previously created intent
        radius = intentku.getStringExtra("radius");
        jml_outlet = intentku.getStringExtra("jml_outlet");
        segment_tiv = intentku.getStringExtra("segment_tiv");
        latitude = intentku.getStringExtra("latitude");
        longitude = intentku.getStringExtra("longitude");

        get_koordinat_outlet_semesta();
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

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        enableMyLocationIfPermitted();

        if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }

        if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {
            if(mMap.getMyLocation()!=null) {
                double lat = mMap.getMyLocation().getLatitude();
                double longt = mMap.getMyLocation().getLongitude();

                LatLng latLng = new LatLng(lat, longt);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_value));
            }
            else{
                Toast.makeText(getApplicationContext(), "Aktifkan fitur gps ", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    };

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        }
        else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            double latitudee;
            double longitudee;

            gps = new GPSTracker(show_coordinat_outlet_semesta.this, show_coordinat_outlet_semesta.this);
            if(gps.canGetLocation()) {
                latitudee = gps.getLatitude();
                longitudee = gps.getLongitude();

                LatLng redmond = new LatLng(latitudee, longitudee);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(redmond, zoom_value));
            }
        }

    }

    private void get_koordinat_outlet_semesta(){
        final ProgressDialog progressDialog = new ProgressDialog(show_coordinat_outlet_semesta.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_edit       = url_get_list_koordinat_outlet_semesta+"0"+"/"+latitude+"/"+longitude+"/"+jml_outlet+"/"+radius+"/"+segment_tiv;
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

                            String id_outlet = jObj.getString("id_outlet");
                            String nama_outlet = jObj.getString("nama_outlet");
                            String latitude = jObj.getString("latitude");
                            String longitude = jObj.getString("longitude");
                            String segment_tiv = jObj.getString("segment_tiv");
                            String channel = jObj.getString("channel");
                            String segment = jObj.getString("segment");
                            String subsegment = jObj.getString("subsegment");
                            String alamat_outlet = "";

                            if(jObj.getString("alamat").equals("")) {
                                alamat_outlet = setAddressFromLatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                            }
                            else {
                                alamat_outlet = jObj.getString("alamat");
                            }

                            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                            if(i==0){
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_value));
                            }

                            Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                    .snippet(
                                            "Outlet :  " + nama_outlet + "\n" +
                                                    "Segment TIV : " + segment_tiv + "\n" +
                                                    "Channel : " + channel + "\n" +
                                                    "Segment : " + segment + "\n" +
                                                    "Subsegment : " + subsegment
                                    )
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                            String idOne = markerOne.getId();
                            markerMap.put(idOne, id_outlet);

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    LinearLayout info = new LinearLayout(show_coordinat_outlet_semesta.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(show_coordinat_outlet_semesta.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(show_coordinat_outlet_semesta.this);
                                    snippet.setTextColor(Color.GRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(show_coordinat_outlet_semesta.this, "Tidak bisa mendapatkan koordinat outlet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(show_coordinat_outlet_semesta.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
        String alamat = "";
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