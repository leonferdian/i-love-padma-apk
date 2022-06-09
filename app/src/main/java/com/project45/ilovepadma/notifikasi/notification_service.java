package com.project45.ilovepadma.notifikasi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.list_complain_by_notif;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.timeline.list_timeline_by_notif;
import com.project45.ilovepadma.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class notification_service extends Service {

    Handler handler;
    Runnable test;

    String id_user;
    static Context mContext;
    //int success = 0;
    final String CHANNEL_ID = "exampleChannel";
    int randomNumber;

    private static NotificationManagerCompat notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            id_user = intent.getStringExtra("id_user");
        }



        handler = new Handler();
        test = new Runnable() {
            @Override
            public void run() {

                getNotification(id_user);

                handler.postDelayed(test, 20000);  //wait 10 sec and run again
                //handler.postDelayed(test, 20000);  //wait 20 sec and run again
                // handler.post(test, 4000); //wait 4 sec and run again
            }
        };

        handler.postDelayed(test, 20000);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    public static void PostNotification(Context context) {

        mContext = context;
        notificationManager = NotificationManagerCompat.from(mContext);
    }

    private void getNotification(final String id_userr) {
        String url_server = Server.URL + "cust_service/cek_notification/"+id_userr;
        Log.d("list_notifikasi", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", response.toString());

                Intent[] notifIntent;
                if(response.length()>0) {

                    // Parsing json
                    int nmr = 1;
                    notifIntent = new Intent[response.length()+3];
                    //add vibrator
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 2 seconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
                        //Log.d("vib", "vib1");
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(2000);
                        //Log.d("vib", "vib2");
                    }

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_work_report jv = new Data_work_report();

                            int angkaRandom = nmr+generateRandomNumbers(100001,999999);
                            int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
                            //notifiasi PendingIntent.getActivity harus angka acak. supaya ketika diklik bisa berubah variable extranya

                            jv.setid(obj.getString("id"));
                            jv.setid_job_list(obj.getString("notification_for"));
                            jv.setketerangan(obj.getString("kategori"));
                            jv.setcreate_by(obj.getString("notification_from"));
                            jv.setnmr_wr(obj.getString("id_relasi"));
                            addNotification(obj.getString("id"),obj.getString("kategori"),obj.getString("notification_from"),obj.getString("id_relasi"),iUniqueId,notifIntent[nmr]);
                            postStatusNotifikasi(obj.getString("id"));
                            nmr++;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else{

                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());

            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    private void addNotification(final String id_notif, final String kategori, final String complain_from, final String id_relasi, final int nmr, Intent ntfIntent) {

        Uri soundUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.mario_notif);
        //Uri soundUri = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/mario_notif.mp3" ) ;
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    id_relasi,
                    "Example Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern( new long[]{ 500, 500 }) ;
            channel.setSound(soundUri, attributes);

            channel.setLockscreenVisibility(1);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }


        if(kategori.equals("complain")){

            long[] vibrate = { 1000, 1000, 1000, 1000, 1000 };
            //Uri soundUri = RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );

            //Uri soundUri =  Uri.parse("android.resource://"+ this.getPackageName() + "/" + getResources().openRawResource(R.raw));
                /*
                Uri.parse("android.resource://"
                    + this.getPackageName() + "/" + getResources().openRawResource(R.drawable.mario));
                 Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/mysound");

                 */
            Notification notification = new NotificationCompat.Builder(mContext, id_relasi)
                    .setSmallIcon(R.drawable.ic_surat_tugas_blue_48)
                    .setContentTitle("Tiket")
                    .setContentText("Anda mendapatkan tiket dari "+complain_from)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(0)
                    .setVibrate(new long[] { 1000, 1000}) // for vibrate Notification.DEFAULT_VIBRATE;
                    .setSound(soundUri)
                    .setLights(Color.RED, 3000, 3000)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .build();

            //add sound
            notification.defaults = 0;
            MediaPlayer mp= MediaPlayer.create(getApplicationContext(), R.raw.mario_notif);
            mp.start();

            ntfIntent = new Intent(this, list_complain_by_notif.class);
            ntfIntent.putExtra("id_relasi",id_relasi);
            ntfIntent.putExtra("id_notif",id_notif);
            ntfIntent.putExtra("nmr_notif",nmr);
            ntfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent activity = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                activity = PendingIntent.getActivity(this, nmr, ntfIntent, activity.FLAG_IMMUTABLE);
                notification.contentIntent = activity;
            }
            else{
                activity = PendingIntent.getActivity(this, nmr, ntfIntent, 0);
                notification.contentIntent = activity;
            }


            notificationManager.notify(nmr, notification);

           // Log.d("notip", id_relasi+" ~ "+id_notif);
        }

        else if(kategori.equals("complain_respon")){

            long[] vibrate = { 1000, 1000, 1000, 1000, 1000 };
            Notification notification2 = new NotificationCompat.Builder(this, id_relasi)
                    .setSmallIcon(R.drawable.ic_surat_tugas_blue_48)
                    .setContentTitle("Tiket Respon")
                    .setContentText(complain_from+" menambah respon ditiket anda")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(0)
                    .setVibrate(vibrate) // for vibrate Notification.DEFAULT_VIBRATE;
                    .setSound(soundUri)
                    .setLights(Color.RED, 3000, 3000)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .build();

            notification2.defaults = 0;
            MediaPlayer mp= MediaPlayer.create(getApplicationContext(), R.raw.mario_notif);
            mp.start();

            ntfIntent = new Intent(this, list_complain_by_notif.class);
            ntfIntent.putExtra("id_relasi",id_relasi);
            ntfIntent.putExtra("id_notif",id_notif);
            ntfIntent.putExtra("nmr_notif",nmr);
            ntfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            PendingIntent activity = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                activity = PendingIntent.getActivity(this, nmr, ntfIntent, activity.FLAG_IMMUTABLE);
                notification2.contentIntent = activity;
            }
            else{
                activity = PendingIntent.getActivity(this, nmr, ntfIntent, 0);
                notification2.contentIntent = activity;
            }

            notificationManager.notify(nmr, notification2);
        }
        else if(kategori.equals("tl_komen")){

            long[] vibrate = { 1000, 1000, 1000, 1000, 1000 };
            Notification notification2 = new NotificationCompat.Builder(this, id_relasi)
                    .setSmallIcon(R.drawable.ic_surat_tugas_blue_48)
                    .setContentTitle("Timeline Komen")
                    .setContentText(complain_from+" memberi komen ditimeline")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(0)
                    .setVibrate(vibrate) // for vibrate Notification.DEFAULT_VIBRATE;
                    .setSound(soundUri)
                    .setLights(Color.RED, 3000, 3000)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .build();

            notification2.defaults = 0;
            MediaPlayer mp= MediaPlayer.create(getApplicationContext(), R.raw.mario_notif);
            mp.start();

            ntfIntent = new Intent(this, list_timeline_by_notif.class);
            ntfIntent.putExtra("id_timeline",id_relasi);
            ntfIntent.putExtra("id_notif",id_notif);
            ntfIntent.putExtra("nmr_notif",nmr);
            ntfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent activity = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                activity = PendingIntent.getActivity(this, nmr, ntfIntent, activity.FLAG_IMMUTABLE);
                notification2.contentIntent = activity;
            }
            else{
                activity = PendingIntent.getActivity(this, nmr, ntfIntent, 0);
                notification2.contentIntent = activity;
            }

            notificationManager.notify(nmr, notification2);
        }


    }

    private void postStatusNotifikasi(final String id_notif){
        String url_post_status_notification     = Server.URL + "cust_service/post_edit_status_notification2";

        final String TAG_SUCCESS = "success";
        final String TAG = "notif";
        //proses save to database using api
        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_status_notification, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Add/update", jObj.toString());
                        //Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();




                    } else {
                        Toast.makeText(getApplicationContext(),
                                "error " + jObj.getString("Fail to edit notification"), Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "error " + error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_notification", id_notif);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private int generateRandomNumbers(int min, int max) {
        // min & max will be changed as per your requirement. In my case, I've taken min = 2 & max = 32

        //Random r = new Random();
        //int randomNumber = r.nextInt(100);
        int randomNumberCount = 10;

        int dif = max - min;
        if (dif < (randomNumberCount * 3)) {
            dif = (randomNumberCount * 3);
        }

        int margin = (int) Math.ceil((float) dif / randomNumberCount);
        List<Integer> randomNumberList = new ArrayList<>();



        Random random = new Random();

        for (int i = 0; i < randomNumberCount; i++) {
            int range = (margin * i) + min; // 2, 5, 8

            int randomNum = random.nextInt(margin);
            if (randomNum == 0) {
                randomNum = 1;
            }

            int number = (randomNum + range);
            randomNumber = number;
            //randomNumberList.add(number);
        }
        //Toast.makeText(getApplicationContext(), " random "+String.valueOf(randomNumber), Toast.LENGTH_LONG).show();
        return randomNumber;
    }
}
