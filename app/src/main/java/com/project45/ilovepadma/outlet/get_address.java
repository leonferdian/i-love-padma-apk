package com.project45.ilovepadma.outlet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class get_address extends AppCompatActivity implements OnMapReadyCallback {

    double lat_cust,lng_cust;
    double latitude;
    double longitude;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "padma_sfa_shared_preferences";

    ConnectivityManager conMgr;
    TextView lokasi_anda,lat_anda,lng_anda;
    EditText txt_input_lokasi;
    GPSTracker gps;

    //GoogleMap googleMap;
    private GoogleMap mMap;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    FloatingActionButton btn_marker;
    ImageView confirm_address_map_custom_marker;
    Boolean isTampil ;
    String check_moving;
    int zoom_value=20;
    String alamat,alamat2,lat2,lng2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_address);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_audit_get_address);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);



        lokasi_anda = findViewById(R.id.lokasi_anda);
        lat_anda = findViewById(R.id.lat_anda);
        lng_anda = findViewById(R.id.lng_anda);
        btn_marker = (FloatingActionButton) findViewById(R.id.btn_marker);
        confirm_address_map_custom_marker = findViewById(R.id.confirm_address_map_custom_marker);
        //txt_input_lokasi= findViewById(R.id.txt_input_lokasi);

        isTampil=true;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        Toast.makeText(getApplicationContext(), "Gunakan tombol lokasi warna hitam untuk memilih lokasi outlet", Toast.LENGTH_SHORT).show();

        btn_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lokasi_anda.setText(alamat2);
                lat_anda.setText(lat2);
                lng_anda.setText(lng2);

                if(isTampil==true){

                    if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( get_address.this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                                MY_PERMISSION_ACCESS_COARSE_LOCATION );

                    }

                    if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( get_address.this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                                MY_PERMISSION_ACCESS_COARSE_LOCATION );

                    }

                    mMap.setMyLocationEnabled(false);
                    // this hide button current location
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    confirm_address_map_custom_marker.setVisibility(View.VISIBLE);
                    isTampil =false;
                    check_moving="true";
                    //Toast.makeText(getApplicationContext(), "check_moving "+check_moving, Toast.LENGTH_LONG).show();

                    mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {
                            //Toast.makeText(getApplicationContext(), "The camera is moving.",Toast.LENGTH_SHORT).show();
                            lokasi_anda.setText("Moving");
                        }
                    });

                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            //String mPosition = mMap.getCameraPosition().target;
                            //String mZoom = mMap.getCameraPosition().zoom;

                            double lat = mMap.getCameraPosition().target.latitude;
                            double lng = mMap.getCameraPosition().target.longitude;
                            latitude = lat;
                            longitude = lng;
                            String alamatt = setAddressFromLatLng(lat, lng);

                            lokasi_anda.setText(alamatt);
                            lat_anda.setText(String.valueOf(lat));
                            lng_anda.setText(String.valueOf(lng));

                        }
                    });
                }
                else{
                    mMap.setMyLocationEnabled(true);
                    // this hide button current location
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    confirm_address_map_custom_marker.setVisibility(View.GONE);
                    isTampil =true;
                    check_moving="false";
                    //Toast.makeText(getApplicationContext(), "check_moving "+check_moving, Toast.LENGTH_LONG).show();

                    mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {

                        }
                    });

                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            //String mPosition = mMap.getCameraPosition().target;
                            //String mZoom = mMap.getCameraPosition().zoom;


                        }
                    });
                }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_outlet, menu);


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.action_save) {

            //onBackPressedWithProcess();
            check_address();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMinZoomPreference(20);

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

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if(mMap.getMyLocation()!=null) {
                        //mMap.setMinZoomPreference(20);
                        double lat = mMap.getMyLocation().getLatitude();
                        double longt = mMap.getMyLocation().getLongitude();
                        latitude = mMap.getMyLocation().getLatitude();
                        longitude = mMap.getMyLocation().getLongitude();
                        LatLng latLng = new LatLng(lat, longt);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_value));
                        //lokasi_anda.setText(setAddressFromLatLng(lat,longt));
                        alamat2 = setAddressFromLatLng(lat, longt);
                        lat2 = String.valueOf(lat);
                        lng2 = String.valueOf(longt);
                    /*mMap.addMarker(new MarkerOptions().position(latLng).title(alamat).draggable(false)
                            .snippet("Lokasi Anda")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    */
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

        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            gps = new GPSTracker(get_address.this, get_address.this);
            if(gps.canGetLocation()) {
                double latitudee;
                double longitudee;
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                LatLng redmond = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(redmond, zoom_value));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
                //lokasi_anda.setText(setAddressFromLatLng(latitude,longitude));
                alamat2=setAddressFromLatLng(latitude,longitude);
                lat2 = String.valueOf(gps.getLatitude());
                lng2 = String.valueOf(gps.getLongitude());
            }
            else {
                LatLng redmond = new LatLng(-7.2473625, 112.78135546875);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(redmond, zoom_value));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
                //lokasi_anda.setText(setAddressFromLatLng(-7.2473625,112.78135546875));
                alamat2=setAddressFromLatLng(-7.2473625,112.78135546875);
                lat2 = String.valueOf(-7.2473625);
                lng2 = String.valueOf(112.78135546875);
            }
        }
    }

    public String setAddressFromLatLng(double lat, double lng) {

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

    private void check_address(){
        final String selected_address = lokasi_anda.getText().toString();
        if(selected_address.equals("")){
            alert_confirm();
        }
        else{
            onBackPressedWithProcess();
        }
    }

    public void onBackPressedWithProcess() {
        final String selected_address = lokasi_anda.getText().toString();
        final String selected_lat = lat_anda.getText().toString();
        final String selected_lng = lng_anda.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        returnIntent.putExtra("lat_address", selected_lat);
        returnIntent.putExtra("lng_address", selected_lng);
        returnIntent.putExtra("address", selected_address);
        setResult(22,returnIntent);
        finish();
    }

    private void alert_confirm(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Warning");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Anda Belum Memilih Alamat")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                /*.setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something
                        dialogInterface.cancel();
                    }
                })*/
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });


        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}
