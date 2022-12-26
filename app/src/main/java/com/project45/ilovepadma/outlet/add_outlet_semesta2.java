package com.project45.ilovepadma.outlet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.complain.list_complain_from_you;
import com.project45.ilovepadma.global.Api;

public class add_outlet_semesta2 extends AppCompatActivity {
    CardView cv_add_listed_outlet, cv_add_profiling_outlet, cv_add_dealing_outlet, cv_add_result_dealing;
    String id_user,username,email,jabatan,id_company="",nama_company="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_outlet_semesta2);

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
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        cv_add_listed_outlet = findViewById(R.id.cv_add_listed_outlet);
        cv_add_profiling_outlet = findViewById(R.id.cv_add_profiling_outlet);
        cv_add_dealing_outlet = findViewById(R.id.cv_add_dealing_outlet);
        cv_add_result_dealing = findViewById(R.id.cv_add_result_dealing);

        cv_add_listed_outlet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username

                Intent intent = new Intent(add_outlet_semesta2.this, add_listed_outlet.class);
//                intent.putExtra("act", "add_outlet");
                startActivityForResult(intent, 1);
            }
        });

        cv_add_profiling_outlet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username

                Intent intent = new Intent(add_outlet_semesta2.this, list_listed_outlet.class);
//                intent.putExtra("act", "add_outlet");
                startActivityForResult(intent, 1);
            }
        });

        cv_add_dealing_outlet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username

                Intent intent = new Intent(add_outlet_semesta2.this, list_profiling_reject_outlet.class);
//                intent.putExtra("act", "add_outlet");
                startActivityForResult(intent, 1);
            }
        });

        cv_add_result_dealing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username

                Intent intent = new Intent(add_outlet_semesta2.this, list_being_dealing_outlet.class);
//                intent.putExtra("act", "add_outlet");
                startActivityForResult(intent, 1);
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
            window.setStatusBarColor(getResources().getColor(R.color.black_purple));
        }
    }
}