package com.project45.ilovepadma.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.global.Api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class menu_supervisi extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,kategori_user;
    CardView cv_agenda,cv_note,cv_project,cv_tiket;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_supervisi);

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

        cv_agenda = findViewById(R.id.cv_agenda);
        cv_note = findViewById(R.id.cv_note);
        cv_project = findViewById(R.id.cv_project);
        cv_tiket = findViewById(R.id.cv_tiket);

        cv_agenda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), supervisi_agenda.class);
                startActivity(intent);
            }
        });

        cv_note.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), supervisi_note.class);
                startActivity(intent);
            }
        });

        cv_project.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), supervisi_project.class);
                startActivity(intent);
            }
        });

        cv_tiket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update login session ke FALSE dan mengosongkan nilai id dan username
                //Toast.makeText(getApplicationContext(), "Setting tes ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), supervisi_tiket.class);
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
            window.setStatusBarColor(getResources().getColor(R.color.page_join_visit));
        }
    }
}
