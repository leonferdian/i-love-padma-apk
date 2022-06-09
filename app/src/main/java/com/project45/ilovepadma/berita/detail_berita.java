package com.project45.ilovepadma.berita;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_berita;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.setting.add_company;
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.timeline.list_timeline_post_everything2;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class detail_berita extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,company_create="",id_company="",nama_company="",act="",content_timeline="",photo_timeline="",id_timeline="",
            date_post="",nama_user="",foto_user="",jml_komen="",jml_like="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_company.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static String url_get_isi_post_everything = Server.URL + "timeline/get_isi_post_everything/";
    int success;

    CircularImageView image_user;
    TextView txt_nama_user,txt_tgl_berita,txt_judul,txt_isi_berita;
    ImageView image_berita;
    FloatingActionButton fab_like;

    Data_berita detBerita ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_berita);


        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        changeStatusBarColor();

        image_user = findViewById(R.id.image_user);
        txt_nama_user = findViewById(R.id.txt_nama_user);
        txt_tgl_berita = findViewById(R.id.txt_tgl_berita);
        txt_judul = findViewById(R.id.txt_judul);
        txt_isi_berita = findViewById(R.id.txt_isi_berita);
        image_berita = findViewById(R.id.image_berita);
        fab_like = findViewById(R.id.fab_like);

        Intent intentku = getIntent(); // gets the previously created intent
        detBerita = (Data_berita) intentku.getSerializableExtra(Api.PARAM_DATA);
        if (detBerita != null) {

            String img = Server.URL2 + "image_upload/list_foto_pribadi/"+detBerita.getfoto_user();
            Picasso.with(image_user.getContext())
                    .load(img)
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    .fit().centerCrop().into(image_user);

            txt_nama_user.setText(detBerita.getnama_user());
            txt_tgl_berita.setText(detBerita.gettgl_berita());
            txt_judul.setText(detBerita.getjudul_berita());
            //txt_isi_berita.setText(detBerita.getisi_berita());
            txt_isi_berita.setText(Html.fromHtml(detBerita.getisi_berita(),new URLImageParser(txt_isi_berita, detail_berita.this),null));

            String img_berita = Server.URL2 + "inc/berita/file_gambar/"+detBerita.getgambar_berita();
            Picasso.with(image_berita.getContext())
                    .load(img_berita)
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    .fit()
                    //.centerCrop()
                    .into(image_berita);

            image_berita.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(detail_berita.this, detail_foto.class);
                    intent.putExtra("sku", "Image Berita");
                    intent.putExtra("file_foto", detBerita.getgambar_berita());
                    intent.putExtra("tgl_buat", "");
                    intent.putExtra("act", "image_berita");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);

                }
            });
        }

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
