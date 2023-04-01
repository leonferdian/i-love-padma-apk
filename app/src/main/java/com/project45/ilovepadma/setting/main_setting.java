package com.project45.ilovepadma.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.global.Api;

public class main_setting extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,kategori_user;
    CardView cv_company,cv_web_user,cv_karyawan,cv_supervisi,cv_reservasi_meeting;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_setting);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_main_ms);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        kategori_user = Api.sharedpreferences.getString(Api.TAG_KATEGORI_USER, null);

        cv_company = findViewById(R.id.cv_company);
        cv_web_user = findViewById(R.id.cv_web_user);
        cv_karyawan = findViewById(R.id.cv_karyawan);
        cv_supervisi = findViewById(R.id.cv_supervisi);
        cv_reservasi_meeting = findViewById(R.id.cv_reservasi_meeting);

        cv_company.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), list_master_company.class);
                startActivity(intent);
            }
        });

        cv_karyawan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), list_team_company.class);
                startActivity(intent);
            }
        });

        cv_web_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), user_web.class);
                startActivity(intent);
            }
        });

        cv_supervisi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), menu_supervisi.class);
                startActivity(intent);
            }
        });

        cv_reservasi_meeting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), reservasi_meeting.class);
                startActivity(intent);
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
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }
}
