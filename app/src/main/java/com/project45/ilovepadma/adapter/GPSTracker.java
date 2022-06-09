package com.project45.ilovepadma.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker extends Service implements LocationListener {
    public final Context mContext;
    public final Activity activity;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude
    float bearing; // bearing
    String alamat;// get alamat
    float twoDigitsF;
    String twoDigitsF2;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    public GPSTracker(Context context, Activity activity) {
        this.mContext = context;
        this.activity = activity;
        getLocation();
    }



    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {

                    if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                                MY_PERMISSION_ACCESS_COARSE_LOCATION );

                    }

                    if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                                MY_PERMISSION_ACCESS_COARSE_LOCATION );

                    }

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            bearing = location.getAccuracy();
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                                    MY_PERMISSION_ACCESS_COARSE_LOCATION );

                        }

                        if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                                    MY_PERMISSION_ACCESS_COARSE_LOCATION );

                        }


                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                bearing = location.getAccuracy();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public float getBearing(){
        if(location != null){
            bearing = location.getBearing();
        }

        // return longitude
        return bearing;
    }

    public float getDistance(){
        if(location != null) {
            LatLng latLngA = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng latLngB = new LatLng(-7.4455381, 112.6208952);

            Location locationA = new Location("point A");
            locationA.setLatitude(latLngA.latitude);
            locationA.setLongitude(latLngA.longitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(latLngB.latitude);
            locationB.setLongitude(latLngB.longitude);

            //float distance = locationA.distanceTo(locationB); //in meter
            float distance = locationA.distanceTo(locationB) / 1000; //in kilometer
            //DecimalFormat decimalFormat = new DecimalFormat("#.##");
            //twoDigitsF = Float.valueOf(decimalFormat.format(distance));
            //String numberAsString = String.format ("%.2f", distance);
            twoDigitsF = distance;
            return twoDigitsF;
        }
        else{
            twoDigitsF=0;
            return twoDigitsF;
        }
    }

    public float getDistance2(double lat,double lng){

        if(location != null){
            if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSION_ACCESS_COARSE_LOCATION );

            }

            if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                        MY_PERMISSION_ACCESS_COARSE_LOCATION );

            }

            LatLng latLngA = new LatLng(location.getLatitude(),location.getLongitude());
            LatLng latLngB = new LatLng(lat,lng);

            Location locationA = new Location("point A");
            locationA.setLatitude(latLngA.latitude);
            locationA.setLongitude(latLngA.longitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(latLngB.latitude);
            locationB.setLongitude(latLngB.longitude);

            //float distance = locationA.distanceTo(locationB); //in meter
            float  distance = locationA.distanceTo(locationB)/1000; //in kilometer
            //DecimalFormat decimalFormat = new DecimalFormat("#.##");
            //twoDigitsF = Float.valueOf(decimalFormat.format(distance));
            twoDigitsF = distance;

        }
        else{
            twoDigitsF=0;
        }

        return twoDigitsF;

    }

    public float getDistance3(double lat,double lng,double lat_call,double lng_call){

        if(location != null){
            if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSION_ACCESS_COARSE_LOCATION );

            }

            if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                        MY_PERMISSION_ACCESS_COARSE_LOCATION );

            }

            LatLng latLngA = new LatLng(lat_call,lng_call);
            LatLng latLngB = new LatLng(lat,lng);

            Location locationA = new Location("point A");
            locationA.setLatitude(latLngA.latitude);
            locationA.setLongitude(latLngA.longitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(latLngB.latitude);
            locationB.setLongitude(latLngB.longitude);

            float distance = locationA.distanceTo(locationB); //in meter
            //float  distance = locationA.distanceTo(locationB)/1000; //in kilometer
            //DecimalFormat decimalFormat = new DecimalFormat("#.##");
            //twoDigitsF = Float.valueOf(decimalFormat.format(distance));
            twoDigitsF = distance;

        }
        else{
            twoDigitsF=0;
        }

        return twoDigitsF;

    }

    public float getDistanceInMeter(double lat,double lng){

        if(location != null){
            if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSION_ACCESS_COARSE_LOCATION );

            }

            if ( ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                        MY_PERMISSION_ACCESS_COARSE_LOCATION );

            }

            LatLng latLngA = new LatLng(location.getLatitude(),location.getLongitude());
            LatLng latLngB = new LatLng(lat,lng);

            Location locationA = new Location("point A");
            locationA.setLatitude(latLngA.latitude);
            locationA.setLongitude(latLngA.longitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(latLngB.latitude);
            locationB.setLongitude(latLngB.longitude);

            float distance = locationA.distanceTo(locationB); //in meter
            //float  distance = locationA.distanceTo(locationB)/1000; //in kilometer
            //DecimalFormat decimalFormat = new DecimalFormat("#.##");
            //twoDigitsF = Float.valueOf(decimalFormat.format(distance));
            twoDigitsF = distance;

        }
        else{
            twoDigitsF=0;
        }

        return twoDigitsF;

    }

    public String setAddressFromLatLng() {

        if(location != null) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
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
        else{
            alamat = "";
            return alamat;
        }
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}
