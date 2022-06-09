package com.project45.ilovepadma.timeline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.setting.add_company;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class detail_post_everything extends AppCompatActivity {

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

    ImageView img_post;
    ImageButton bt_close;
    CircularImageView image_user_post;
    TextView txt_nama_user,txt_tgl_post,txt_isi_post,txt_jml_komen;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post_everything);


        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        changeStatusBarColor();

        img_post = findViewById(R.id.img_post);
        bt_close = findViewById(R.id.bt_close);
        image_user_post = findViewById(R.id.image_user_post);
        txt_nama_user = findViewById(R.id.txt_nama_user);
        txt_tgl_post = findViewById(R.id.txt_tgl_post);
        txt_isi_post = findViewById(R.id.txt_isi_post);
        txt_jml_komen = findViewById(R.id.txt_jml_komen);

        Intent intentku = getIntent(); // gets the previously created intent
        act = intentku.getStringExtra("act");

        if(act.equals("detail_timeline")) {
            content_timeline = intentku.getStringExtra("isi_timeline");
            photo_timeline = intentku.getStringExtra("photo_timeline");
            id_timeline = intentku.getStringExtra("id_timeline");
            date_post = intentku.getStringExtra("date_post");
            nama_user = intentku.getStringExtra("nama_user");
            foto_user = intentku.getStringExtra("foto_user");
            jml_komen = intentku.getStringExtra("jml_komen");
            jml_like = intentku.getStringExtra("jml_like");

            txt_nama_user.setText(nama_user);
            txt_tgl_post.setText(date_post);
            txt_jml_komen.setText("Komen : "+jml_komen+" Like : "+jml_like);

            get_isi_post(id_timeline);

            if(!photo_timeline.equals("")){

                String img = Server.URL2 + "image_upload/list_foto_timeline/"+photo_timeline;
                Picasso.with(img_post.getContext())
                        .load(img)
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        .fit()
                        //.centerCrop()
                        .into(img_post);

                img_post.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // update login session ke FALSE dan mengosongkan nilai id dan username
                        Intent intent = new Intent(detail_post_everything.this, detail_foto.class);
                        intent.putExtra("sku", "Foto Timeline");
                        intent.putExtra("file_foto", photo_timeline);
                        intent.putExtra("tgl_buat", "");
                        intent.putExtra("act", "foto_timeline");
                        //startActivity(intent);
                        startActivityForResult(intent, 1);

                    }
                });



            }
            else{
                Picasso.with(img_post.getContext())
                        .load(R.drawable.default_photo)
                        .fit()
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        //.resize(100, 100)
                        //.onlyScaleDown()
                        .centerCrop()
                        .into(img_post);
            }

            if(!foto_user.equals("")){
                String img = Server.URL2 + "image_upload/list_foto_pribadi/"+foto_user;
                Picasso.with(image_user_post.getContext())
                        .load(img)
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        .fit().centerCrop().into(image_user_post);
            }
            else{
                Picasso.with(image_user_post.getContext())
                        .load(R.drawable.dummy_pic)
                        .fit()
                        .placeholder(R.drawable.ic_camera_alt_black_48dp)
                        //.resize(100, 100)
                        //.onlyScaleDown()
                        .centerCrop()
                        .into(image_user_post);
            }

        }

        bt_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                onBackPressed();

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

    private void get_isi_post(final String id_timeline){
        final ProgressDialog progressDialog = new ProgressDialog(detail_post_everything.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ");
        progressDialog.show();

        String url_server       = url_get_isi_post_everything+id_timeline;
        Log.d("url_server", url_server);
        StringRequest strReq = new StringRequest(Request.Method.GET, url_server, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get_note", jObj.toString());
                        String isi_timeline      = jObj.getString("isi_timeline");
                        /*
                        txt_isi_note.setText(Html.fromHtml(isi_note));
                        */
                        txt_isi_post.setText(Html.fromHtml(isi_timeline,new URLImageParser(txt_isi_post, detail_post_everything.this),null));
                        //txt_isi_note.setText(Html.fromHtml(isi_note,new URLImageParser(txt_isi_note, detail_note.this),null));

                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(detail_post_everything.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_post_everything.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

}
