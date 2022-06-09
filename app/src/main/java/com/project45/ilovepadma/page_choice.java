package com.project45.ilovepadma;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class page_choice extends AppCompatActivity {

    Button btn_signup_choice,btn_login_choice;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "45ilv_shared_preferences";
    public static final String session_status = "session_status";

    Boolean session = false;

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    String id, username,email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_choice);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btn_signup_choice = findViewById(R.id.btn_signup_choice);
        btn_login_choice = findViewById(R.id.btn_login_choice);

        //btn_signup_choice.setVisibility(View.GONE);
        btn_signup_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentku= new Intent(page_choice.this, registration_form.class);
                startActivity(intentku);
            }

        });

        btn_login_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentku= new Intent(page_choice.this, login_form.class);
                startActivity(intentku);
            }

        });

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);

        //firebase notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("NotifApps", "NotifyApps", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        if (session) {
            //Intent intent = new Intent(page_choice.this, MainActivity2.class);
            Intent intent = new Intent(page_choice.this, MainActivity3.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_EMAIL, email);
            intent.putExtra("act_notif","");
            finish();
            startActivity(intent);
        }

    }


}
