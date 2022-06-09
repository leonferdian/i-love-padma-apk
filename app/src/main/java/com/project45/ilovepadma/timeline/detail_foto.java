package com.project45.ilovepadma.timeline;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ablanco.zoomy.Zoomy;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class detail_foto extends AppCompatActivity {

    String id_user, username,bm,email,act,db_user,szName,sku,file_foto;
    TextView task_name;
    ImageView image_outlet;
    String img ="",tgl_buat="",test_date2 = "2020-02-11";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_image);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_detail_foto);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);

        task_name =  findViewById(R.id.task_name);
        image_outlet =  findViewById(R.id.image_outlet);

        Intent intentku = getIntent(); // gets the previously created intent
        sku = intentku.getStringExtra("sku");
        file_foto = intentku.getStringExtra("file_foto");
        act = intentku.getStringExtra("act");
        if(act.equals("kunjungan")){
            tgl_buat = intentku.getStringExtra("tgl_buat");
            SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            //img = Server.URL2 + "image_upload_sfa/list_foto_join_visit/"+file_foto;
            try {
                Date date = format_date.parse(test_date2);
                Date date2 = format_date.parse(tgl_buat);
                if(date2.before(date)){
                    img = Server.URL2 + "image_upload_sfa/list_foto_join_visit/"+file_foto;
                    Log.d("photo_stock", String.valueOf(img));
                }
                else{
                    img = Server.URL3 + "image_upload_sfa/list_foto_join_visit/"+file_foto;
                    Log.d("photo_stock", String.valueOf(img));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(act.equals("team")){
            img = Server.URL2 + "image_upload/list_foto_pribadi/"+file_foto;
        }
        else if(act.equals("foto_complain")){
            img = Server.URL2 + "image_upload/list_foto_complain/"+file_foto;
        }
        else if(act.equals("foto_respon_complain")){
            img = Server.URL2 + "image_upload/list_foto_complain_respon/"+file_foto;
        }
        else if(act.equals("photo_profil")){
            img = Server.URL2 + "image_upload/list_foto_pribadi/"+file_foto;
        }
        else if(act.equals("foto_timeline")){
            img = Server.URL2 + "image_upload/list_foto_timeline/"+file_foto;
        }
        else if(act.equals("image_berita")){
            img = Server.URL2 + "inc/berita/file_gambar/"+file_foto;
        }
        else if(act.equals("image_absen")){
            img = Server.URL2 + "image_upload/list_foto_absen/"+file_foto;
        }
        else if(act.equals("foto_outlet")){
            img = Server.URL2 + "image_upload/list_foto_outlet/"+file_foto;
        }

        task_name.setText(sku);

        Picasso.with(image_outlet.getContext())
                .load(img)
                .fit()
                .placeholder(R.drawable.ic_camera_alt_black_48dp)
                //.resize(6000, 2000)
                //.centerCrop()
                .centerInside()
                .into(image_outlet);

        Zoomy.Builder builder = new Zoomy.Builder(detail_foto.this)
                .target(image_outlet)
                .interpolator(new OvershootInterpolator());

        builder.register();
        Log.d("img", img);

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
