package com.project45.ilovepadma.outlet;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class show_outlet_coordinat extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String id_user, username,nmr_customer="",email,employee_id_dms3,db_user,jabatan,radius,jml_outlet,subSegment,latt,longg,act="";

    private static final String TAG = list_coordinat.class.getSimpleName();
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";

    private static String url_get_list_coordinat     = Server.URL + "outlet/list_coordinat_outlet_dms/";
    private static String url_get_list_coordinat2     = Server.URL + "outlet/list_coordinat_outlet_prospect/";

    private GoogleMap mMap;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    int zoom_value=15;
    String[] separated ;

    GPSTracker gps;

    ArrayList<Data_customer> itemList = new ArrayList<Data_customer>();

    //Declare HashMap to store mapping of marker to Activity
    HashMap<String, String> markerMap = new HashMap<String, String>();

    FloatingActionButton btn_marker;
    LinearLayout layout_keterangan;
    TextView lokasi_anda,txt_pilih_lokasi;
    ImageView confirm_address_map_custom_marker;

    Boolean isTampil ;
    String check_moving;

    double latitude;
    double longitude;
    String alamatt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_coordinat2);

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

        btn_marker = (FloatingActionButton) findViewById(R.id.btn_marker);
        layout_keterangan = findViewById(R.id.layout_keterangan);
        lokasi_anda = findViewById(R.id.lokasi_anda);
        confirm_address_map_custom_marker = findViewById(R.id.confirm_address_map_custom_marker);
        txt_pilih_lokasi  = findViewById(R.id.txt_pilih_lokasi);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        isTampil=true;

        Intent intentku = getIntent(); // gets the previously created intent
        radius = intentku.getStringExtra("radius");
        jml_outlet = intentku.getStringExtra("jml_outlet");
        subSegment = intentku.getStringExtra("subSegment");
        latt = intentku.getStringExtra("latt");
        longg = intentku.getStringExtra("longg");
        act = intentku.getStringExtra("act");

        btn_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(isTampil==true){

                    if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( show_outlet_coordinat.this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                                MY_PERMISSION_ACCESS_COARSE_LOCATION );

                    }

                    if ( ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( show_outlet_coordinat.this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                                MY_PERMISSION_ACCESS_COARSE_LOCATION );

                    }

                    mMap.setMyLocationEnabled(false);
                    // this hide button current location
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    confirm_address_map_custom_marker.setVisibility(View.VISIBLE);
                    layout_keterangan.setVisibility(View.VISIBLE);
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
                            alamatt = setAddressFromLatLng(lat, lng);

                            lokasi_anda.setText(alamatt);


                        }
                    });
                }
                else{
                    mMap.setMyLocationEnabled(true);
                    // this hide button current location
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    confirm_address_map_custom_marker.setVisibility(View.GONE);
                    layout_keterangan.setVisibility(View.GONE);
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

        //Toast.makeText(getApplicationContext(), "radius "+String.valueOf(radius)+" subSegment "+subSegment, Toast.LENGTH_LONG).show();

        txt_pilih_lokasi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                if(alamatt.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Pilih Alamat Terlebih Dahulu ", Toast.LENGTH_LONG).show();
                }
                else {
                    /*
                    Intent intent = new Intent(show_outlet_coordinat.this, add_outlet.class);
                    intent.putExtra(TAG_USERNAME, username);
                    intent.putExtra(TAG_ID, id_user);
                    intent.putExtra("lat", String.valueOf(latitude));
                    intent.putExtra("lng", String.valueOf(longitude));
                    intent.putExtra("alamat", alamatt);
                    intent.putExtra("act", "add_outlet_by_coordinat");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);

                     */
                }

            }
        });

        if(act.equals("outlet_dms")) {
            get_coordinat_dms();
        }
        else if(act.equals("outlet_prospect")){
            get_coordinat_prospect();
        }

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

        LatLng latLng2 = new LatLng(Double.valueOf(latt), Double.valueOf(longg));
        /*String alamat_outlet2      = setAddressFromLatLng(Double.valueOf(latt), Double.valueOf(longg));
        mMap.addMarker(new MarkerOptions().position(latLng2).title("Lokasi Anda").draggable(false)
                .snippet(alamat_outlet2)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));*/
        /* marker with custom image
        mMap.addMarker(new MarkerOptions().position(latLng2).
                icon(BitmapDescriptorFactory.fromBitmap(
                        map_marker.createCustomMarker(show_outlet_coordinat.this,R.drawable.dummy_pic,"Nupur")))).setTitle("Subway Sector 16 Noida");
        */

        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Toast.makeText(
                        show_outlet_coordinat.this,
                        "Lat : " + latLng.latitude + " , "
                                + "Long : " + latLng.longitude,
                        Toast.LENGTH_LONG).show();

            }
        });*/

        //mMap.setOnInfoWindowClickListener(show_outlet_coordinat.this);



    }

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

            gps = new GPSTracker(show_outlet_coordinat.this, show_outlet_coordinat.this);
            if(gps.canGetLocation()) {
                latitudee = gps.getLatitude();
                longitudee = gps.getLongitude();

                LatLng redmond = new LatLng(latitudee, longitudee);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(redmond, zoom_value));
            }
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

                        LatLng latLng = new LatLng(lat, longt);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_value));
                        //lokasi_anda.setText(setAddressFromLatLng(lat,longt));

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

    private void get_coordinat_dms(){

        itemList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(show_outlet_coordinat.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_edit       = url_get_list_coordinat+"0"+"/"+latt+"/"+longg+"/"+jml_outlet+"/"+radius+"/"+subSegment;
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
                            final JSONObject jObj = response.getJSONObject(i);


                            Data_customer item = new Data_customer();

                            item.setId_external(jObj.getString("ID"));
                            item.setName(jObj.getString("name"));
                            item.setPhone(jObj.getString("phone"));
                            item.setAddress(jObj.getString("Address"));
                            item.setLat(jObj.getDouble("lat"));
                            item.setLng(jObj.getDouble("lng"));
                            item.setimage_customer(jObj.getString("file_image"));
                            item.setid_survey(jObj.getString("id_cust_survey"));
                            item.setcreate_by(jObj.getString("nama_user"));

                            // menambah item ke array
                            itemList.add(item);

                            final String szid      = jObj.getString("ID");
                            String lat_outlet      = jObj.getString("lat");
                            String long_outlet      = jObj.getString("lng");
                            final String nama_cust     = jObj.getString("name");
                            final String display_image = jObj.getString("file_image");
                            final String phone = jObj.getString("phone");
                            final String alamat_outlet;
                            if(jObj.getString("Address").equals(""))
                            {
                                alamat_outlet      = setAddressFromLatLng(Double.valueOf(lat_outlet), Double.valueOf(long_outlet));
                            }
                            else{
                                alamat_outlet      = jObj.getString("Address");
                            }

                            LatLng latLng = new LatLng(Double.valueOf(lat_outlet), Double.valueOf(long_outlet));
                            if(i==0){
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_value));
                            }

                            if(jObj.getString("szHierarchyId").equals("M1")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else if(jObj.getString("szHierarchyId").equals("F2")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else if(jObj.getString("szHierarchyId").equals("N5")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else if(jObj.getString("szHierarchyId").equals("I5") || jObj.getString("szHierarchyId").equals("I7")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else if(jObj.getString("szHierarchyId").equals("I2") || jObj.getString("szHierarchyId").equals("O2") || jObj.getString("szHierarchyId").equals("L4")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else if(jObj.getString("szHierarchyId").equals("I8")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else if(jObj.getString("szHierarchyId").equals("N1")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else if(jObj.getString("szHierarchyId").equals("L6")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        //.snippet("Sub Segment :  " + jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }
                            else{
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                                String idOne = markerOne.getId();
                                String idtwo = markerOne.getTitle();
                                markerMap.put(idOne, szid);
                                markerMap.put(idtwo, display_image);
                            }

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(show_outlet_coordinat.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(show_outlet_coordinat.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(show_outlet_coordinat.this);
                                    snippet.setTextColor(Color.GRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });

                            final String PARAM_DATA = "Data";
                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {

                                    String actionId = markerMap.get(marker.getId());
                                    String gmbr = markerMap.get(marker.getTitle());
                                    /*
                                    Intent intent = new Intent(getApplicationContext(), detail_outlet_dms.class);
                                    intent.putExtra(TAG_USERNAME, username);
                                    intent.putExtra(TAG_ID, id_user);
                                    intent.putExtra("szid", actionId);
                                    intent.putExtra("display_image", gmbr);
                                    intent.putExtra("nama_cust", nama_cust);
                                    intent.putExtra("alamat", alamat_outlet);
                                    intent.putExtra("handphone", phone);
                                    intent.putExtra("act", "detail_outlet_kordinat");
                                    //startActivity(intent);
                                    startActivityForResult(intent,11);
                                    */
                                    //Toast.makeText(getApplicationContext(), "actionId "+String.valueOf(actionId), Toast.LENGTH_LONG).show();
                                }
                            });

                            /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                            {

                                @Override
                                public boolean onMarkerClick(Marker arg0) {
                                   // if(arg0 != null && arg0.getTitle().equals(markerOptions2.getTitle().toString())); // if marker  source is clicked
                                    Toast.makeText(show_outlet_coordinat.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                                    return true;
                                }

                            });*/

                            // menambah item ke array
                            //itemList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(show_outlet_coordinat.this, "Tidak bisa mendapatkan kordinat ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                // notifikasi adanya perubahan data pada adapter



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(show_outlet_coordinat.this, "No More Items Availablee", Toast.LENGTH_SHORT).show();
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

    private void get_coordinat_prospect(){
        final ProgressDialog progressDialog = new ProgressDialog(show_outlet_coordinat.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_edit       = url_get_list_coordinat2+"0"+"/"+latt+"/"+longg+"/"+jml_outlet+"/"+radius+"/"+subSegment;
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

                            final Data_customer item = new Data_customer();

                            item.setId(jObj.getInt("ID"));
                            item.setName(jObj.getString("name"));
                            item.setPhone(jObj.getString("phone"));
                            item.setAddress(jObj.getString("Address"));
                            item.setLat(jObj.getDouble("lat"));
                            item.setLng(jObj.getDouble("lng"));
                            item.setid_survey(jObj.getString("id_cust_survey"));
                            item.setcreate_by(jObj.getString("nama_user"));

                            // menambah item ke array
                            itemList.add(item);

                            //Data_join_visit item = new Data_join_visit();
                            //item.setId(obj.getString("nmr"));
                            String szid      = jObj.getString("ID");
                            String lat_outlet      = jObj.getString("lat");
                            String long_outlet      = jObj.getString("lng");
                            String nama_cust     = jObj.getString("name");
                            String alamat_outlet="";
                            if(jObj.getString("Address").equals(""))
                            {
                                alamat_outlet      = setAddressFromLatLng(Double.valueOf(lat_outlet), Double.valueOf(long_outlet));
                            }
                            else{
                                alamat_outlet      = jObj.getString("Address");
                            }

                            LatLng latLng = new LatLng(Double.valueOf(lat_outlet), Double.valueOf(long_outlet));
                            if(i==0){
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_value));
                            }

                            /*mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                    .snippet("Outlet :  "+nama_cust)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));*/

                            if(jObj.getString("szHierarchyId").equals("M1")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else if(jObj.getString("szHierarchyId").equals("I8")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else if(jObj.getString("szHierarchyId").equals("N5")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else if(jObj.getString("szHierarchyId").equals("I5") || jObj.getString("szHierarchyId").equals("I7")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else if(jObj.getString("szHierarchyId").equals("I2") || jObj.getString("szHierarchyId").equals("O2") || jObj.getString("szHierarchyId").equals("L4")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else if(jObj.getString("szHierarchyId").equals("I8")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else if(jObj.getString("szHierarchyId").equals("N1")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else if(jObj.getString("szHierarchyId").equals("L6")) {
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        //.snippet("Sub Segment :  " + jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }
                            else{
                                Marker markerOne = mMap.addMarker(new MarkerOptions().position(latLng).title(alamat_outlet).draggable(false)
                                        .snippet("Outlet :  " + nama_cust+"\n"+"Sub Segment : "+jObj.getString("sub_segment"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                                String idOne = markerOne.getId();
                                markerMap.put(idOne, szid);
                            }

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(show_outlet_coordinat.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(show_outlet_coordinat.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(show_outlet_coordinat.this);
                                    snippet.setTextColor(Color.GRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });

                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {

                                    String actionId = markerMap.get(marker.getId());
                                    String gmbr = markerMap.get(marker.getTitle());
                                    final String PARAM_DATA = "Data";
                                    /*
                                    Intent intent = new Intent(getApplicationContext(), list_survey_volume.class);
                                    intent.putExtra("id_customer", actionId);
                                    intent.putExtra(TAG_USERNAME, username);
                                    intent.putExtra(TAG_ID, id_user);
                                    //startActivity(intent);
                                    startActivityForResult(intent,22);
                                    */
                                    //Toast.makeText(getApplicationContext(), "actionId "+String.valueOf(actionId), Toast.LENGTH_LONG).show();
                                }
                            });

                            // menambah item ke array
                            //itemList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(show_outlet_coordinat.this, "Tidak bisa mendapatkan kordinat ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                // notifikasi adanya perubahan data pada adapter



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(show_outlet_coordinat.this, "No More Items Availablee", Toast.LENGTH_SHORT).show();
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
