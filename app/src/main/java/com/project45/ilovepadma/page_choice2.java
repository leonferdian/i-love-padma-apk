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

import com.project45.ilovepadma.setting.add_company;
import com.project45.ilovepadma.setting.form_join_company;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class page_choice2 extends AppCompatActivity {

    Button btn_create_company,btn_join_company;
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
        setContentView(R.layout.page_choice2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);

        btn_create_company = findViewById(R.id.btn_create_company);
        btn_join_company = findViewById(R.id.btn_join_company);

        //btn_signup_choice.setVisibility(View.GONE);
        btn_create_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentku= new Intent(page_choice2.this, add_company.class);
                intentku.putExtra("act", "input_company_by_regist");
                startActivityForResult(intentku,1);
            }

        });

        btn_join_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentku= new Intent(page_choice2.this, form_join_company.class);
                intentku.putExtra("act", "input_company_by_regist");
                startActivityForResult(intentku,1);
            }

        });




    }


}
