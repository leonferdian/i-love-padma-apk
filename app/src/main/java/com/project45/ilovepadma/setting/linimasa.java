package com.project45.ilovepadma.setting;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class linimasa extends AppCompatActivity implements
        GoogleMap.OnPolylineClickListener {
    String ListuserURL = Server.URL + "showusers";
    private static final String TAG = linimasa.class.getSimpleName();
    Button loadMap;
    AutoCompleteTextView UserId;
    private ProgressDialog pDialog;
    private GoogleMap mMap;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    EditText date, user_id;
    DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormatter;

    List<LatLng> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_linimasa);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_main_ms);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        LinearLayout linearLayout = findViewById(R.id.filter_layout);

        ImageButton ToggFilter = findViewById(R.id.toggle);

        // Set initial visibility
        linearLayout.setVisibility(View.VISIBLE);

        // Set click listener for the button
        ToggFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between visible and invisible
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                    ToggFilter.setBackgroundResource(R.drawable.baseline_arrow_drop_up_grey_24);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    ToggFilter.setBackgroundResource(R.drawable.ic_arrow_drop_grey);
                }
            }
        });

        pDialog = new ProgressDialog(linimasa.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait...");
        list_users();
        loadMap = (Button) findViewById(R.id.loadMap);

        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH) + 1; // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // initiate the date picker and a button
        date = (EditText) findViewById(R.id.date);
        date.setText(mYear + "-" + mMonth + "-" + mDay);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_absen = "filter1";
                showDateDialog(tgl_absen, date);
            }
        });

        loadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                // Do something when the button is clicked
                showDialog();
                // Close the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        // Attach a listener to the date_changed event of the timeline control
                        mMap = googleMap;
                        if (!user_id.getText().toString().equals("")) {
                            LoadMapTrack(user_id.getText().toString(), date.getText().toString(), "7");
                        } else {
                            Toast.makeText(linimasa.this, "The filter fields cannot be empty",Toast.LENGTH_SHORT).show();
                        }
                        // Position the map's camera near Alice Springs in the center of Australia,
                        // and set the zoom factor so most of Australia shows on the screen.
                        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-32.491, 147.309), 4));

                        if (ActivityCompat.checkSelfPermission(linimasa.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(linimasa.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        mMap.setMyLocationEnabled(true);
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
                        // Set listeners for click events.
                        mMap.setOnPolylineClickListener(linimasa.this);

                        hideDialog();
                    }
                });
            }
        });
    }

    /**
     * Listens for clicks on a polyline.
     * @param polyline The polyline object that the user has clicked.
     */
    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }

//        Toast.makeText(this, "Route: " + polyline.getTag().toString(), Toast.LENGTH_SHORT).show();
    }

    private void list_users() {
        user_id = (EditText) findViewById(R.id.user_id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ListuserURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Data Response: " + response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> values = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String user_id = String.valueOf(jsonObject.getInt("user_id"));
                        String nama_user = jsonObject.getString("nama_user");
                        values.add(nama_user);
                    }

                    AutoCompleteTextView autoCompleteTextView = findViewById(R.id.list_user);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(linimasa.this, android.R.layout.simple_dropdown_item_1line, values);
                    autoCompleteTextView.setAdapter(adapter);

                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // When an item is selected, get the corresponding key from the JSONArray
                            String selectedValue = parent.getItemAtPosition(position).toString();
                            String selectedKey = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String key = null;
                                try {
                                    key = jsonObject.getString("user_id");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String value = null;
                                try {
                                    value = jsonObject.getString("nama_user");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                if (value.equals(selectedValue)) {
                                    selectedKey = key;
                                    break;
                                }
                            }

                            // Use the selected key as needed
                            user_id.setText(selectedKey);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("_token", csrfToken);
//                params.put("email", email);
//                params.put("password", password);
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("X-CSRF-Token", csrfToken);
//                return headers;
//            }
        };

        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void LoadMapTrack(final String user_id, final String tanggal, final String id_company) {
        String ListTrackURL = Server.URL + "list_track/" + user_id + "/" + id_company + "/" + tanggal;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ListTrackURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Data Response: " + response.toString());
                PolylineOptions polylineOptions = new PolylineOptions();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String kategori = jsonObject.getString("kategori");
                        String detail_aktifitas = jsonObject.getString("detail_aktifitas");
                        double latitude = jsonObject.getDouble("latitude");
                        double longitude = jsonObject.getDouble("longitude");
                        String address = jsonObject.getString("address");
                        String time = jsonObject.getString("time");
//                        points.add(new LatLng(latitude, longitude));

                        // Create start marker
                        Marker pointMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.traveler))
                                .title(kategori.replace("_", " "))
                                .snippet("(" + time + ") " + address)
                        );

                        polylineOptions.add(new LatLng(latitude, longitude));
//                        polylineOptions.endCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.oxygen_icons_org_oxygen_actions_media_playback_stop_48), 18));
//                        polylineOptions.startCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.designbolts_free_multimedia_stop_48), 18));
                        polylineOptions.clickable(true);
                        polylineOptions.color(Color.BLUE);
                        polylineOptions.width(12);
                        polylineOptions.jointType(JointType.ROUND);
                        Polyline polyline = mMap.addPolyline(polylineOptions);
//                        polyline.setTag(address);
                    }
                    //                polylineOptions.addAll(points);
                    Polyline polyline = mMap.addPolyline(polylineOptions);
                    if (jsonArray.length() > 0) {
                        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                        for (LatLng latLng : polyline.getPoints()) {
                            boundsBuilder.include(latLng);
                        }

                        LatLngBounds bounds = boundsBuilder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 4);
                        mMap.animateCamera(cameraUpdate);

                    } else {
                        // Get the user's current location
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(linimasa.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(linimasa.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        // Check if the location is not null
                        if (location != null) {
                            // Get the latitude and longitude of the location
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            // Create a new LatLng object with the location coordinates
                            LatLng latLng = new LatLng(lat, lng);

                            // Create a new CameraUpdate object and move the camera to the user's location
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            mMap.moveCamera(cameraUpdate);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Polyline polyline2 = mMap.addPolyline(new PolylineOptions()
//                        .clickable(true)
//                        .add(
//                                new LatLng(-7.9482736, 112.6495355),
//                                new LatLng(-7.9797308, 112.6117154),
//                                new LatLng(-8.0641366, 112.6099945)
//                        )
//                        .color(Color.BLUE)
//                        .endCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.oxygen_icons_org_oxygen_actions_media_playback_stop_48),18))
//                        .startCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.designbolts_free_multimedia_stop_48),18))
//                        .width(12)
//                        .jointType(JointType.ROUND)
//                );
//
//                polyline2.setTag("B");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("_token", csrfToken);
//                params.put("user_id", user_id);
//                params.put("date", tanggal);
//                params.put("id_company", id_company);
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("X-CSRF-Token", csrfToken);
//                return headers;
//            }
        };

        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showDateDialog(final String absen, final EditText tgl_awal) {

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                if (absen.equals("filter1")) {
                    tgl_awal.setText(dateFormatter.format(newDate.getTime()));
                }


            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
