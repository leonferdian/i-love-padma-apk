package com.project45.ilovepadma.bg_service;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;


public class OurApplication extends Application implements LifecycleObserver {



    private Activity activity;
    Context context;
    SharedPreferences sharedpreferences;
    static final String my_shared_preferences = "45ilv_shared_preferences";
    static String id_user,id_company;
    final String TAG_ID = "id";
    final String TAG_COMPANY_USER_ID = "company_user_id";
    static GPSTracker gps;
    static double latitude;
    static double longitude;
    static String alamat;

    private static String url_post_tracking     = Server.URL + "tracking/post_last_seen";

    static int success;
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        context = getApplicationContext();
        activity = this.activity;
        gps = new GPSTracker(context, activity);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id_user = sharedpreferences.getString(TAG_ID, null);
        id_company = sharedpreferences.getString(TAG_COMPANY_USER_ID, null);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //Logger.localLog("APP BACKGROUNDED");
        //activity = (Activity) getApplicationContext();
        Log.d("Note", "APP BACKGROUNDED");
        startTrackActivity(context,id_company,"offline",id_user);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        //Logger.localLog("APP FOREGROUNDED");
        Log.d("Note", "APP FOREGROUNDED");
        startTrackActivity(context,id_company,"online",id_user);
    }

    public static void startTrackActivity(Context context, final String idCompany,final String status,final String idUser){
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            alamat = gps.setAddressFromLatLng();
            Log.d("foo", "user " + idUser + " Your Location is " + alamat);
            StringRequest strReq = new StringRequest(Request.Method.POST, url_post_tracking, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e("response", "Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        // Check for error node in json
                        if (success == 1) {

                            Log.d("Tracking succes", jObj.toString());


                        } else {
                            //Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            Log.d("Tracking error", jObj.toString());

                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("response", "Tracking Error: " + error.getMessage());
                    //Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();


                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user", idUser);
                    params.put("id_company", idCompany);
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("alamat", alamat);
                    params.put("status_user", status);
                    params.put("create_by", idUser);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance(context).addToRequestQueue(strReq);
            //end proses
        }
    }

}
