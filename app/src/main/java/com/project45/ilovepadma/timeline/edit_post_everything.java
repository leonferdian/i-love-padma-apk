package com.project45.ilovepadma.timeline;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.data.Data_join_visit;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.global.Util;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class edit_post_everything extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,company_create="",id_company="",nama_company="",act="",content_timeline="",photo_timeline="",act_form="",id_timeline="",
           imagePost = "", judul_pertanyaan = "";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = edit_post_everything.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static String url_post_timeline    = Server.URL + "timeline/post_edit_timeline";
    private static String url_post_timeline2    = Server.URL + "timeline/post_edit_timeline2";
    public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_timeline.php";
    private static String url_get_pertanyaan_timeline   = Server.URL + "timeline/get_pertanyaan_timeline/";
    private static String url_post_timeline2_jawaban_detail    = Server.URL + "timeline/post_timeline2_jawaban_detail";
    int success, id_summary_score = 0;

    EditText txt_post;
    TextView txt_nama_user;
    CircularImageView image_user_post;
    AppCompatButton btn_submit;
    ImageView img_post;
    Button btn_camera_foto,btn_galery_foto,btn_camera_video,btn_galery_video;

    Spinner spinner_jenis_post;
    ArrayList<String> list_jenis_post = new ArrayList<String>();
    TableLayout table_pertanyaan;
    ArrayList<HashMap<String,Object>> dataDaftarPertanyaan = new ArrayList<HashMap<String,Object>>();
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, jenis_post;
    private boolean first_load = true;

    List<EditText> list_edit_text_jawaban = new ArrayList<EditText>();
    List<JSONArray> list_edit_text_jawaban_score = new ArrayList<JSONArray>();
    private String[] jawaban_pertanyaan;

    GPSTracker gps;
    double latitude,longitude;
    String alamat="";
    private Matrix matrix;
    private Bitmap bitmapLast;

    //private static final int CAMERA_REQUEST = 1888;
    private Bitmap bitmap;
    private String fileImagePath,image_name = "",url_foto_stok="",pathFoto;
    private File destFile;
    Uri imageCaptureUri,mCropImageUri;

    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    String cameraPermission[];
    String storagePermission[];

    int RESULT_LOAD_IMG = 9;

    android.app.AlertDialog.Builder alertDialogBuilder;
    android.app.AlertDialog alertDialog;

    /*  for upload video from camera*/
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUriVideo; // file url to store image/video
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "ilv_video";
    /*  end upload video from camera*/
    /*  for upload video from gallery*/
    private static final int GALLERY_VIDEO_REQUEST_CODE = 212;
    private Uri fileUriGalleryVideo;
    /*  end upload video from gallery*/

    Data_join_visit detPost;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post_everything);


        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        changeStatusBarColor();

        txt_post = findViewById(R.id.txt_post);
        txt_nama_user = findViewById(R.id.txt_nama_user);
        image_user_post = findViewById(R.id.image_user_post);
        btn_submit = findViewById(R.id.btn_submit);
        img_post = findViewById(R.id.img_post);
        btn_camera_foto = findViewById(R.id.btn_camera_foto);
        btn_galery_foto = findViewById(R.id.btn_galery_foto);
        btn_camera_video = findViewById(R.id.btn_camera_video);
        btn_galery_video = findViewById(R.id.btn_galery_video);

        spinner_jenis_post = findViewById(R.id.spinner_jenis_post);
        table_pertanyaan = findViewById(R.id.table_pertanyaan);

        txt_nama_user.setText(username);

        get_detail_user(id_user);

        gps = new GPSTracker(edit_post_everything.this, edit_post_everything.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if(gps.setAddressFromLatLng()!=null) {
                alamat = gps.setAddressFromLatLng();
            }
            else{
                alamat ="Address Undetected";
            }
        }

        Intent intentku = getIntent(); // gets the previously created intent
        act_form = intentku.getStringExtra("act");
        detPost = (Data_join_visit) intentku.getSerializableExtra(Api.PARAM_DATA);
        if (detPost != null) {
            txt_post.setText(Html.fromHtml(detPost.getvol_tot_order(),new URLImageParser(txt_post, edit_post_everything.this),null));
            id_timeline = detPost.getnmr_jv();
            imagePost = detPost.getjv_foto();
            //Toast.makeText(getApplicationContext(), "id_timeline "+id_timeline, Toast.LENGTH_LONG).show();
            String img = Server.URL2 + "image_upload/list_foto_timeline/"+detPost.getjv_foto();
            Picasso.with(img_post.getContext())
                    .load(img)
                    .fit()
                    .placeholder(R.drawable.default_photo)
                    //.resize(300, 100)
                    //.onlyScaleDown()
                    .centerInside()
                    .into(img_post);

            list_jenis_post.removeAll(list_jenis_post);
            list_jenis_post.add("select_spinner");
            list_jenis_post.add("briefing_pagi");
            list_jenis_post.add("briefing_sore");
            list_jenis_post.add("visit_outlet");
            list_jenis_post.add("meeting");
            list_jenis_post.add("sidak_audit");
            list_jenis_post.add("prospecting_dealing_outlet");
            list_jenis_post.add("branding");
            list_jenis_post.add("event");
            list_jenis_post.add("ops_loading");
            list_jenis_post.add("ops_unloading");
            list_jenis_post.add("ops_badstock");
            list_jenis_post.add("ops_gwp");
            list_jenis_post.add("ams_kendaraan");
            list_jenis_post.add("soe_rr_kunjungan");
            list_jenis_post.add("soe_mv_delivery");
            list_jenis_post.add("other");

            spinner_jenis_post.setSelection(list_jenis_post.indexOf(detPost.getjenis_post()));
        }

        spinner_jenis_post.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if (item != null) {
//                    Toast.makeText(getApplicationContext(), "jenis Post : "+String.valueOf(item.toString()), Toast.LENGTH_LONG).show();
                    clean_pertanyaan();
                    if(item.equals("Briefing Pagi")){
                        jenis_post = "briefing_pagi";
                        get_pertanyaan("briefing_pagi");
                    }
                    else if(item.equals("Briefing Sore")){
                        jenis_post = "briefing_sore";
                        get_pertanyaan("briefing_sore");
                    }
                    else if(item.equals("Visit Outlet (outlet RO)")){
                        jenis_post = "visit_outlet";
                        get_pertanyaan("visit_outlet");
                    }
                    else if(item.equals("Meeting")){
                        jenis_post = "meeting";
                        get_pertanyaan("meeting");
                    }
                    else if(item.equals("Sidak / Audit")){
                        jenis_post = "sidak_audit";
                        get_pertanyaan("sidak_audit");
                    }
                    else if(item.equals("Prospecting & Dealing Outlet")){
                        jenis_post = "prospecting_dealing_outlet";
                        get_pertanyaan("prospecting_dealing_outlet");
                    }
                    else if(item.equals("Branding")){
                        jenis_post = "branding";
                        get_pertanyaan("branding");
                    }
                    else if(item.equals("Event")){
                        jenis_post = "event";
                        get_pertanyaan("event");
                    }
                    else if(item.equals("OPS - Loading")){
                        jenis_post = "ops_loading";
                        get_pertanyaan("ops_loading");
                    }
                    else if(item.equals("OPS - Unloading")){
                        jenis_post = "ops_unloading";
                        get_pertanyaan("ops_unloading");
                    }
                    else if(item.equals("OPS - Badstock")){
                        jenis_post = "ops_badstock";
                        get_pertanyaan("ops_badstock");
                    }
                    else if(item.equals("OPS - GWP")){
                        jenis_post = "ops_gwp";
                        get_pertanyaan("ops_gwp");
                    }
                    else if(item.equals("AMS - Kendaraan")){
                        jenis_post = "ams_kendaraan";
                        get_pertanyaan("ams_kendaraan");
                    }
                    else if(item.equals("SOE - RR Kunjungan")){
                        jenis_post = "soe_rr_kunjungan";
                        get_pertanyaan("soe_rr_kunjungan");
                    }
                    else if(item.equals("SOE - MV Delivery")){
                        jenis_post = "soe_mv_delivery";
                        get_pertanyaan("soe_mv_delivery");
                    }
                    else if(item.equals("Other")){
                        jenis_post = "other";
                        clean_pertanyaan();
                    }
                    else {
                        clean_pertanyaan();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_camera_foto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                                11 );

                    }
                    else if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.CAMERA  },
                                12 );

                    }
                    else {
                        pilihImageByCamera();
                    }

            }
        });

        btn_galery_foto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                 StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                                11 );

                    }
                    else if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.CAMERA  },
                                12 );

                    }
                    else {

                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                    }

            }
        });

        btn_camera_video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {

                    final String isi_timeline = txt_post.getText().toString();
                    if(isi_timeline.equals("")){
                        Toast.makeText(getApplicationContext(), "Caption harus diisi ", Toast.LENGTH_LONG).show();
                    }
                    else {
                        recordVideo();
                    }
                }

            }
        });

        btn_galery_video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( edit_post_everything.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( edit_post_everything.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {

                    final String isi_timeline = txt_post.getText().toString();
                    if(isi_timeline.equals("")){
                        Toast.makeText(getApplicationContext(), "Caption harus diisi ", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent videoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        videoPickerIntent.setType("video/*");
                        startActivityForResult(videoPickerIntent, GALLERY_VIDEO_REQUEST_CODE);
                    }
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                jawaban_pertanyaan = new String[1000];
                int nmr_sub = 0;
                for (EditText list_edit_text : list_edit_text_jawaban) {
                    jawaban_pertanyaan[nmr_sub] = list_edit_text.getText().toString();
                    nmr_sub++;
                }
                list_edit_text_jawaban.clear();

                Toast.makeText(getApplicationContext(), "Save Post Everything", Toast.LENGTH_LONG).show();
                proc_save_posting();
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

    private void get_detail_user(final String id_user){
        String url_edit       = Server.URL + "get_detail_user/"+id_user;
        Log.d("list_account", url_edit);
        StringRequest strReq = new StringRequest(Request.Method.GET, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String alamat      = jObj.getString("alamat");
                        String phone    = jObj.getString("phone");
                        String ktp  = jObj.getString("ktp");
                        String kode_user  = jObj.getString("kode_user");
                        String foto_pribadi  = jObj.getString("foto_pribadi");
                        String nik  = jObj.getString("nik");
                        String email_atasan  = jObj.getString("email_atasan");
                        //Toast.makeText(account_user.this, jabatan, Toast.LENGTH_LONG).show();


                        if(!foto_pribadi.equals("")){

                            bm =Server.URL2 +"image_upload/list_foto_pribadi/"+foto_pribadi;
                            Picasso.with(getApplicationContext())
                                    .load(bm)
                                    //.fit()
                                    .resize(100, 100)
                                    .onlyScaleDown()
                                    .centerCrop()
                                    .into(image_user_post);
                        }
                        else{
                            Picasso.with(getApplicationContext())
                                    .load(R.drawable.dummy_pic)
                                    .fit()
                                    .centerCrop()
                                    .into(image_user_post);
                        }
                        //adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(edit_post_everything.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(edit_post_everything.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private void pilihImageByCamera() {
        String IMAGE_DIRECTORY = "ILV";
        File file =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        image_name="img_" + timeStamp + ".png";
        destFile = new File(file, "img_" + timeStamp + ".png");
        imageCaptureUri = Uri.fromFile(destFile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* galery foto*/
        if(requestCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    //String resultUri = imageUri.getPath();
                    File filePhoto = new File(getPath(imageUri));
                    if (filePhoto.exists()) {
                        url_foto_stok = filePhoto.getAbsolutePath();
                        Log.d("url_foto_stok1", url_foto_stok);
                        AddMark(username, url_foto_stok, alamat, String.valueOf(latitude), String.valueOf(longitude));
                        pathFoto = "ok";
                        img_post.setImageBitmap(selectedImage);
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(edit_post_everything.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(edit_post_everything.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
        /* galery video*/
        if(requestCode == GALLERY_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                fileUriGalleryVideo = imageUri;
                launchUploadActivity2(false);
            }
            else {
                Toast.makeText(edit_post_everything.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
        /* camera foto*/
        if (requestCode == CAMERA_REQUEST){
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = imageCaptureUri;
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri;

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(imageUri);
                }
            }
        }
        /* image crop*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Uri resultUri = result.getUri();
                //Picasso.with(this).load(resultUri).into(image_outlet);
                //Toast.makeText(getApplicationContext(), "tes "+String.valueOf(requestCode), Toast.LENGTH_LONG).show();
                String resultUri = result.getUri().getPath();
                File filePhoto = new File(resultUri);
                if (filePhoto.exists()) {
                    fileImagePath = filePhoto.getAbsolutePath();
                    Uri url_foto = result.getUri();
                    url_foto_stok=filePhoto.getAbsolutePath();
                    Log.d("url_foto_stok1", url_foto_stok);
                    AddMark(username,url_foto_stok,alamat, String.valueOf(latitude), String.valueOf(longitude));
                    pathFoto="ok";
                    //image_outlet.setImageBitmap(bitmap);
                    Picasso.with(this).load(url_foto).into(img_post);
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        }
        /* camera video / video recording*/
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if (requestCode == 77) {
            if (resultCode == Activity.RESULT_OK) {
                onBackPressedWithProcess();
            }
        }

    }

    private void startCropImageActivity(Uri imageUri) {

        //Toast.makeText(account_user.this,String.valueOf(imageUri), Toast.LENGTH_LONG).show();
        CropImage.activity(imageUri)
                .setRequestedSize(500, 500, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setInitialCropWindowPaddingRatio(0)
                .start(this);
        //CropImage.startPickImageActivity(this);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void  setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void AddMark(String watermark, String imageName, String alamatt, String lat, String lng) {
        try{

            final int IMAGE_MAX_SIZE = 500000;
            //String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PADMA/"+imageName;
            String filePath = imageName;
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

            setMatrix(Util.getRotateMatrix(filePath));

            //set scale
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            int scale = 1;
            while ((options.outWidth * options.outHeight) * (1/ Math.pow(scale,2)) > IMAGE_MAX_SIZE){
                scale ++;
            }

            Bitmap bitmap = null;
            if (scale > 1){
                scale --;
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                bitmap = BitmapFactory.decodeFile(filePath, options);

                if (this.getMatrix() != null) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), this.getMatrix(), true);
                }

                // Delete original file
                //this.deleteUnusedFile(this.getFilePath());

                bitmapLast = bitmap;
            }
            else{
                bitmapLast = BitmapFactory.decodeFile(filePath);
            }
            //

            //Bitmap bmp = BitmapFactory.decodeFile(filePath);
            Bitmap drawableBitmap = bitmapLast.copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(drawableBitmap);
            canvas.drawBitmap(bitmapLast, 0, 0, null);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            //paint.setAlpha(40);
            paint.setTextSize(12);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            Paint paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            //paint.setAlpha(40);
            paint2.setTextSize(10);
            paint2.setAntiAlias(true);
            //float textHeight = paint2.descent() - paint2.ascent();
            float textHeight = 60;
            Util.drawTextAndBreakLine(canvas, paint2, 7,textHeight, 280, alamatt);

            canvas.drawText(watermark+" "+timeStamp,7, 30, paint);
            canvas.drawText("Lat: "+lat+" Lng: "+lng,7, 45, paint);
            //canvas.drawText(alamatt,7, 55, paint2);

            int oriWidth = drawableBitmap.getWidth();
            int oriHeight = drawableBitmap.getHeight();

            float width = oriWidth;
            float height = oriHeight;

            Log.d("compressPhoto", "1 width: "+width+" height: "+height);

            Bitmap tampBitmap;
            tampBitmap = Bitmap.createScaledBitmap(drawableBitmap, (int) width, (int) height, true);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            tampBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            String IMAGE_DIRECTORY = "PADMA";
            File file =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + "/" + IMAGE_DIRECTORY);
            if (!file.exists()) {
                file.mkdirs();
            }

            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            image_name = "tL_" + id_user + "_" + timeStamp2 + ".png";

            File outputPhoto = new File(file, image_name);
            url_foto_stok = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PADMA/"+image_name;
            Log.d("url_foto_stok2", url_foto_stok);
            Log.d("compressPhoto", "output : " + outputPhoto.getName());
            try{
                outputPhoto.createNewFile();
                FileOutputStream fo = new FileOutputStream(outputPhoto);
                fo.write(outStream.toByteArray());
                fo.flush();
                fo.close();


            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUriVideo = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriVideo); // set the image file
        // name
        //limit record
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void launchUploadActivity(boolean isImage){

        final String isi_timeline = txt_post.getText().toString();

        Intent i = new Intent(edit_post_everything.this, uploadVideo.class);
        i.putExtra("filePath", fileUriVideo.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("isi_timeline", isi_timeline);
        startActivityForResult(i, 77);

    }

    private void launchUploadActivity2(boolean isImage){

        final String isi_timeline = txt_post.getText().toString();

        Intent i = new Intent(edit_post_everything.this, uploadVideo.class);
        i.putExtra("filePath", getPath(fileUriGalleryVideo));
        i.putExtra("isImage", isImage);
        i.putExtra("isi_timeline", isi_timeline);
        startActivityForResult(i, 77);

    }

    private void proc_save_posting(){
        final ProgressDialog progressDialog = new ProgressDialog(edit_post_everything.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Save...");
        progressDialog.show();

        final String isi_timeline = txt_post.getText().toString();
        final String kategori = "post_everything";

        int jumlah_pertanyaan = dataDaftarPertanyaan.size();
        boolean jawaban_terisi = true;
        for (int x = 0; x < jumlah_pertanyaan;x++){
            if(jawaban_pertanyaan[x] != null){
                continue;
            }
            else {
                jawaban_terisi = false;
            }
        }

        if(!jawaban_terisi){
            Toast.makeText(getApplicationContext(), "Semua Pertanyaan harus diisi ", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        } else {
            if (isi_timeline.equals("")) {
                Toast.makeText(getApplicationContext(), "Caption harus diisi ", Toast.LENGTH_LONG).show();
            } else {
                StringRequest strReq = new StringRequest(Request.Method.POST, url_post_timeline2, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            // Check for error node in json
                            if (success == 1) {

                                Log.d("Save", jObj.toString());

                                if(!jenis_post.equals("other")){
                                    String id_timeline_jawaban = jObj.getString("id_timeline_jawaban");
                                    proc_save_pertanyaan(progressDialog,id_timeline_jawaban);
                                }

                                Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                if (image_name.equals("")) {
                                    //end proses
                                    progressDialog.dismiss();
                                    onBackPressedWithProcess();
                                } else {
                                    save_foto();
                                }

                            } else {
                                //end proses
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            //end proses
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Save Company Error: " + error.getMessage());
                        //end proses
                        progressDialog.dismiss();

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("kategori", kategori);
                        params.put("isi_timeline", isi_timeline);
                        params.put("id_relasi", "");
                        params.put("id_company", id_company);
                        params.put("photo_timeline", image_name);
                        params.put("create_by", id_user);
                        params.put("id_timeline", id_timeline);
                        params.put("image_post", imagePost);
                        params.put("jenis_post", jenis_post);

                        return params;
                    }

                };

                strReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                // Adding request to request queue
                AppController.getInstance(this).addToRequestQueue(strReq);

            }
        }
    }

    private void save_foto(){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bm = BitmapFactory.decodeFile(url_foto_stok);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        if(bm !=null) {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            String ba3 = Base64.encodeToString(ba, Base64.DEFAULT);

            // Upload image to server
            new uploadToServer(1, ba3).execute();
        }

    }

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private int param1;
        private String ba3;

        public uploadToServer(int param1, String ba3) {
            this.param1 = param1;
            this.ba3 = ba3;
        }

        private ProgressDialog pd = new ProgressDialog(edit_post_everything.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image " + param1 + " uploading! ");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba3));
            nameValuePairs.add(new BasicNameValuePair("ImageName", image_name));
            nameValuePairs.add(new BasicNameValuePair("cek_gambar", pathFoto));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url_image_upload);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }

            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.hide();
            pd.dismiss();
            int hitung = param1+1;

            // Memanggil main activity
            Toast.makeText(getApplicationContext(),"Upload Image Selesai", Toast.LENGTH_LONG).show();
            onBackPressedWithProcess();


        }

    }

    private void clean_pertanyaan(){
        table_pertanyaan.removeAllViews();
        dataDaftarPertanyaan.clear();
    }

    private void get_pertanyaan_jawaban(String jenis_post) {
        final ProgressDialog progressDialog = new ProgressDialog(edit_post_everything.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Get Pertanyaan Post Everything.....");
        progressDialog.show();

        String url_server = url_get_pertanyaan_timeline + jenis_post;
        Log.d("url_question", url_server);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("url_question_result", response.toString());
                if (response.length() > 0) {
                    int nomor = 1;
                    list_edit_text_jawaban.clear();

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        final int penomoran = nomor ;
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String tipe_pertanyaan = obj.getString("tipe_pertanyaan");
                            final HashMap<String,Object> dataPertanyaan = new HashMap<String,Object>();
                            dataPertanyaan.put("nomor",String.valueOf(penomoran));
                            dataPertanyaan.put("id_pertanyaan",obj.getInt("id_pertanyaan"));
                            dataPertanyaan.put("judul_pertanyaan",obj.getString("judul_pertanyaan"));
                            dataPertanyaan.put("pertanyaan",obj.getString("pertanyaan"));
                            dataPertanyaan.put("keterangan_system",obj.getString("keterangan_system"));

                            int nomor_pertanyaan = Integer.valueOf(obj.getString("nomor_pertanyaan"));
                            if (dataDaftarPertanyaan.size() < nomor_pertanyaan) {
                                dataDaftarPertanyaan.add(dataPertanyaan);
                            }
                            else {
                                dataDaftarPertanyaan.set(nomor_pertanyaan-1,dataPertanyaan);
                            }

                            TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params1.setMargins(3, 1, 3, 1);
                            params1.weight = 1;
                            params1.span = 3;

                            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params2.setMargins(3, 1, 3, 1);
                            params2.weight = 1;
                            params2.span = 1;

                            TableRow.LayoutParams params3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            params3.setMargins(3, 1, 3, 1);
                            params3.weight = 1;
                            params3.span = 2;

                            if(jenis_post.equals("ops_gwp") && !obj.getString("judul_pertanyaan").equals(judul_pertanyaan) && !obj.getString("judul_pertanyaan").equals("")){
                                judul_pertanyaan = obj.getString("judul_pertanyaan");
                                TableRow row_judul = new TableRow(edit_post_everything.this);
                                row_judul.setBackgroundColor(getResources().getColor(R.color.white));

                                TextView txt_judul_pertanyaan = new TextView(edit_post_everything.this);
                                txt_judul_pertanyaan.setText(obj.getString("judul_pertanyaan"));
                                txt_judul_pertanyaan.setBackgroundResource(R.color.orange_800);
                                txt_judul_pertanyaan.setTextColor(getResources().getColor(R.color.white));
                                txt_judul_pertanyaan.setTextSize(15);
                                txt_judul_pertanyaan.setGravity(Gravity.LEFT);
                                txt_judul_pertanyaan.setTypeface(null, Typeface.BOLD);
                                row_judul.addView(txt_judul_pertanyaan, params3);
                                table_pertanyaan.addView(row_judul);
                            }

                            TableRow row = new TableRow(edit_post_everything.this);
                            row.setBackgroundColor(getResources().getColor(R.color.white));

                            TextView txt_pertanyaan = new TextView(edit_post_everything.this);
                            txt_pertanyaan.setText(obj.getString("pertanyaan"));
                            if(obj.getString("keterangan_system").equals("sum_score_akhir")){
                                txt_pertanyaan.setBackgroundResource(R.color.orange_800);
                            } else {
                                txt_pertanyaan.setBackgroundResource(R.color.holo_blue_light);
                            }
                            txt_pertanyaan.setTextColor(getResources().getColor(R.color.white));
                            txt_pertanyaan.setTextSize(15);
                            txt_pertanyaan.setGravity(Gravity.LEFT);
                            txt_pertanyaan.setTypeface(null, Typeface.BOLD);
                            // add to row
                            if(obj.getString("keterangan_system").equals("sistem_lokasi")){
                                row.addView(txt_pertanyaan, params3);
                                table_pertanyaan.addView(row);
                            }
                            else if(obj.getString("keterangan_system").equals("no_yes_question")){
                                row.addView(txt_pertanyaan, params3);
                                table_pertanyaan.addView(row);
                            }
                            else if(obj.getString("keterangan_system").equals("ada_tidak_question")){
                                row.addView(txt_pertanyaan, params3);
                                table_pertanyaan.addView(row);
                            }
                            else if(obj.getString("keterangan_system").equals("n_p_y_na_question")){
                                row.addView(txt_pertanyaan, params3);
                                table_pertanyaan.addView(row);
                            }
                            else {
                                row.addView(txt_pertanyaan, params2);
                            }
                            
                            if (tipe_pertanyaan.equals("date")) {
                                if(obj.getString("keterangan_system").equals("sistem_tanggal")){
                                    final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.LEFT);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setLines(1);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    txt_jawaban.setEnabled(false);
                                    // add to row
                                    row.addView(txt_jawaban, params3);
                                    table_pertanyaan.addView(row);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                }
                                else {
                                    final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.LEFT);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setLines(1);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row.addView(txt_jawaban, params3);
                                    table_pertanyaan.addView(row);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                }
                            }
                            else if (tipe_pertanyaan.equals("text")) {
                                if(obj.getString("keterangan_system").equals("sistem_lokasi")){
                                    TableRow row2 = new TableRow(edit_post_everything.this);
                                    row2.setBackgroundColor(getResources().getColor(R.color.white));

                                    final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.LEFT);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setLines(1);
                                    txt_jawaban.setEnabled(false);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row2.addView(txt_jawaban, params3);

                                    Button button = new Button(edit_post_everything.this);
                                    button.setText("Ambil Titik Lokasi");
                                    button.setTextSize(10);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v){
                                            GPSTracker gps = new GPSTracker(edit_post_everything.this, edit_post_everything.this);
                                            if(gps.canGetLocation()) {
                                                latitude = gps.getLatitude();
                                                longitude = gps.getLongitude();
                                            }
                                            txt_jawaban.setText(latitude+" | "+longitude);
                                        }
                                    });
                                    row2.addView(button, params2);
                                    table_pertanyaan.addView(row2);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                }
                                else {
                                    final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.LEFT);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setLines(1);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row.addView(txt_jawaban, params3);
                                    table_pertanyaan.addView(row);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                }
                            }
                            else if (tipe_pertanyaan.equals("numeric")) {
                                if(obj.getString("keterangan_system").equals("sum_score_akhir")){
                                    EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setBackgroundResource(R.color.orange_800);
                                    txt_jawaban.setFocusable(false);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.CENTER);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row.addView(txt_jawaban, params3);

                                    table_pertanyaan.addView(row);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                    id_summary_score = list_edit_text_jawaban.size()-1;
                                }
                                else {
                                    EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.CENTER);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row.addView(txt_jawaban, params3);

                                    table_pertanyaan.addView(row);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                }
                            }
                            else if (tipe_pertanyaan.equals("spinner")){
                                if(obj.getString("keterangan_system").equals("no_yes_question")){
                                    TableRow row2 = new TableRow(edit_post_everything.this);
                                    row2.setBackgroundColor(getResources().getColor(R.color.white));

                                    final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.LEFT);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setLines(1);
                                    txt_jawaban.setEnabled(false);
                                    txt_jawaban.setVisibility(View.GONE);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row2.addView(txt_jawaban, params3);

                                    Spinner spinner = new Spinner(edit_post_everything.this);
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(edit_post_everything.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.slc_no_yes_question));
                                    spinner.setAdapter(spinnerArrayAdapter);

                                    String[] spinner_jawaban = getResources().getStringArray(R.array.slc_no_yes_question);
                                    for(int v = 0; v < spinner_jawaban.length;v++){
                                        if(spinner_jawaban[v].equals(detPost.getPertanyaan_post_everything().get(i).getJawaban())){
                                            spinner.setSelection(v);
                                        }
                                    }

                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            txt_jawaban.setText(adapterView.getItemAtPosition(i).toString());
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    row2.addView(spinner, params3);
                                    table_pertanyaan.addView(row2);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                }
                                else if(obj.getString("keterangan_system").equals("ada_tidak_question")){
                                    TableRow row2 = new TableRow(edit_post_everything.this);
                                    row2.setBackgroundColor(getResources().getColor(R.color.white));

                                    final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.LEFT);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setLines(1);
                                    txt_jawaban.setEnabled(false);
                                    txt_jawaban.setVisibility(View.GONE);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row2.addView(txt_jawaban, params3);

                                    Spinner spinner = new Spinner(edit_post_everything.this);
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(edit_post_everything.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.slc_ada_tidak_question));
                                    spinner.setAdapter(spinnerArrayAdapter);

                                    String[] spinner_jawaban = getResources().getStringArray(R.array.slc_ada_tidak_question);
                                    for(int v = 0; v < spinner_jawaban.length;v++){
                                        if(spinner_jawaban[v].equals(detPost.getPertanyaan_post_everything().get(i).getJawaban())){
                                            spinner.setSelection(v);
                                        }
                                    }

                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            txt_jawaban.setText(adapterView.getItemAtPosition(i).toString());
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    row2.addView(spinner, params3);
                                    table_pertanyaan.addView(row2);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                }
                                else if(obj.getString("keterangan_system").equals("n_p_y_na_question")){
                                    JSONArray jsonSub = obj.getJSONArray("sub_question");

                                    TableRow row2 = new TableRow(edit_post_everything.this);
                                    row2.setBackgroundColor(getResources().getColor(R.color.white));

                                    final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                    txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                    txt_jawaban.setTextSize(15);
                                    txt_jawaban.setGravity(Gravity.LEFT);
                                    txt_jawaban.setSingleLine(true);
                                    txt_jawaban.setLines(1);
                                    txt_jawaban.setEnabled(false);
                                    txt_jawaban.setVisibility(View.GONE);
                                    txt_jawaban.setText(detPost.getPertanyaan_post_everything().get(i).getJawaban());
                                    // add to row
                                    row2.addView(txt_jawaban, params3);

                                    Spinner spinner = new Spinner(edit_post_everything.this);
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(edit_post_everything.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.slc_n_p_y_na_question));
                                    spinner.setAdapter(spinnerArrayAdapter);

                                    String[] spinner_jawaban = getResources().getStringArray(R.array.slc_n_p_y_na_question);
                                    for(int v = 0; v < spinner_jawaban.length;v++){
                                        if(spinner_jawaban[v].equals(detPost.getPertanyaan_post_everything().get(i).getJawaban())){
                                            spinner.setSelection(v);
                                        }
                                    }

                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            txt_jawaban.setText(adapterView.getItemAtPosition(i).toString());

                                            double total_score = 0;
                                            for(int x = 0; x < list_edit_text_jawaban_score.size();x++){
                                                for(int y = 0; y < list_edit_text_jawaban_score.get(x).length(); y++){
                                                    try {
                                                        JSONObject objChild = list_edit_text_jawaban_score.get(x).getJSONObject(y);
                                                        if(list_edit_text_jawaban.get(x).getText().toString().equals(objChild.getString("jawaban"))){
                                                            total_score += Double.valueOf(objChild.getString("score"));
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            list_edit_text_jawaban.get(id_summary_score).setText(String.valueOf(total_score));
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    row2.addView(spinner, params3);
                                    table_pertanyaan.addView(row2);
                                    list_edit_text_jawaban.add(txt_jawaban);
                                    list_edit_text_jawaban_score.add(jsonSub);
                                }
                            }

                            nomor++;
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                } else {
                    Toast.makeText(edit_post_everything.this, "Pertanyaan Gagal Ditampilkan", Toast.LENGTH_SHORT).show();
                    btn_submit.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                btn_submit.setVisibility(View.GONE);
                progressDialog.dismiss();
                Toast.makeText(edit_post_everything.this, "Koneksi ke server error", Toast.LENGTH_SHORT).show();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    private void get_pertanyaan(String jenis_post) {
        if(first_load){
            first_load = false;
            get_pertanyaan_jawaban(jenis_post);
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(edit_post_everything.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Get Pertanyaan Post Everything.....");
            progressDialog.show();

            String url_server = url_get_pertanyaan_timeline + jenis_post;
            Log.d("url_question", url_server);

            // membuat request JSON
            JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("url_question_result", response.toString());
                    if (response.length() > 0) {
                        int nomor = 1;
                        list_edit_text_jawaban.clear();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            final int penomoran = nomor ;
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String tipe_pertanyaan = obj.getString("tipe_pertanyaan");
                                final HashMap<String,Object> dataPertanyaan = new HashMap<String,Object>();
                                dataPertanyaan.put("nomor",String.valueOf(penomoran));
                                dataPertanyaan.put("id_pertanyaan",obj.getInt("id_pertanyaan"));
                                dataPertanyaan.put("judul_pertanyaan",obj.getString("judul_pertanyaan"));
                                dataPertanyaan.put("pertanyaan",obj.getString("pertanyaan"));
                                dataPertanyaan.put("keterangan_system",obj.getString("keterangan_system"));

                                int nomor_pertanyaan = Integer.valueOf(obj.getString("nomor_pertanyaan"));
                                if (dataDaftarPertanyaan.size() < nomor_pertanyaan) {
                                    dataDaftarPertanyaan.add(dataPertanyaan);
                                }
                                else {
                                    dataDaftarPertanyaan.set(nomor_pertanyaan-1,dataPertanyaan);
                                }

                                TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                                params1.setMargins(3, 1, 3, 1);
                                params1.weight = 1;
                                params1.span = 3;

                                TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                                params2.setMargins(3, 1, 3, 1);
                                params2.weight = 1;
                                params2.span = 1;

                                TableRow.LayoutParams params3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                                params3.setMargins(3, 1, 3, 1);
                                params3.weight = 1;
                                params3.span = 2;

                                if(jenis_post.equals("ops_gwp") && !obj.getString("judul_pertanyaan").equals(judul_pertanyaan) && !obj.getString("judul_pertanyaan").equals("")){
                                    judul_pertanyaan = obj.getString("judul_pertanyaan");
                                    TableRow row_judul = new TableRow(edit_post_everything.this);
                                    row_judul.setBackgroundColor(getResources().getColor(R.color.white));

                                    TextView txt_judul_pertanyaan = new TextView(edit_post_everything.this);
                                    txt_judul_pertanyaan.setText(obj.getString("judul_pertanyaan"));
                                    txt_judul_pertanyaan.setBackgroundResource(R.color.orange_800);
                                    txt_judul_pertanyaan.setTextColor(getResources().getColor(R.color.white));
                                    txt_judul_pertanyaan.setTextSize(15);
                                    txt_judul_pertanyaan.setGravity(Gravity.LEFT);
                                    txt_judul_pertanyaan.setTypeface(null, Typeface.BOLD);
                                    row_judul.addView(txt_judul_pertanyaan, params3);
                                    table_pertanyaan.addView(row_judul);
                                }

                                TableRow row = new TableRow(edit_post_everything.this);
                                row.setBackgroundColor(getResources().getColor(R.color.white));

                                TextView txt_pertanyaan = new TextView(edit_post_everything.this);
                                txt_pertanyaan.setText(obj.getString("pertanyaan"));
                                if(obj.getString("keterangan_system").equals("sum_score_akhir")){
                                    txt_pertanyaan.setBackgroundResource(R.color.orange_800);
                                } else {
                                    txt_pertanyaan.setBackgroundResource(R.color.holo_blue_light);
                                }
                                txt_pertanyaan.setTextColor(getResources().getColor(R.color.white));
                                txt_pertanyaan.setTextSize(15);
                                txt_pertanyaan.setGravity(Gravity.LEFT);
                                txt_pertanyaan.setTypeface(null, Typeface.BOLD);
                                // add to row
                                if(obj.getString("keterangan_system").equals("sistem_lokasi")){
                                    row.addView(txt_pertanyaan, params3);
                                    table_pertanyaan.addView(row);
                                }
                                else if(obj.getString("keterangan_system").equals("no_yes_question")){
                                    row.addView(txt_pertanyaan, params3);
                                    table_pertanyaan.addView(row);
                                }
                                else if(obj.getString("keterangan_system").equals("ada_tidak_question")){
                                    row.addView(txt_pertanyaan, params3);
                                    table_pertanyaan.addView(row);
                                }
                                else if(obj.getString("keterangan_system").equals("n_p_y_na_question")){
                                    row.addView(txt_pertanyaan, params3);
                                    table_pertanyaan.addView(row);
                                }
                                else {
                                    row.addView(txt_pertanyaan, params2);
                                }

                                if (tipe_pertanyaan.equals("date")) {
                                    if(obj.getString("keterangan_system").equals("sistem_tanggal")){
                                        calendar = Calendar.getInstance();
                                        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        date = dateFormat.format(calendar.getTime());

                                        final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.LEFT);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setLines(1);
                                        txt_jawaban.setText(date);
                                        txt_jawaban.setEnabled(false);
                                        // add to row
                                        row.addView(txt_jawaban, params3);
                                        table_pertanyaan.addView(row);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                    }
                                    else {
                                        final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.LEFT);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setLines(1);
                                        txt_jawaban.setText("");
                                        // add to row
                                        row.addView(txt_jawaban, params3);
                                        table_pertanyaan.addView(row);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                    }
                                }
                                else if (tipe_pertanyaan.equals("text")) {
                                    if(obj.getString("keterangan_system").equals("sistem_lokasi")){
                                        TableRow row2 = new TableRow(edit_post_everything.this);
                                        row2.setBackgroundColor(getResources().getColor(R.color.white));

                                        final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.LEFT);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setLines(1);
                                        txt_jawaban.setEnabled(false);
                                        txt_jawaban.setText("");
                                        // add to row
                                        row2.addView(txt_jawaban, params3);

                                        Button button = new Button(edit_post_everything.this);
                                        button.setText("Ambil Titik Lokasi");
                                        button.setTextSize(10);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v){
                                                GPSTracker gps = new GPSTracker(edit_post_everything.this, edit_post_everything.this);
                                                if(gps.canGetLocation()) {
                                                    latitude = gps.getLatitude();
                                                    longitude = gps.getLongitude();
                                                }
                                                txt_jawaban.setText(latitude+" | "+longitude);
                                            }
                                        });
                                        row2.addView(button, params2);
                                        table_pertanyaan.addView(row2);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                    }
                                    else {
                                        final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.LEFT);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setLines(1);
                                        txt_jawaban.setText("");
                                        // add to row
                                        row.addView(txt_jawaban, params3);
                                        table_pertanyaan.addView(row);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                    }
                                }
                                else if (tipe_pertanyaan.equals("numeric")) {
                                    if(obj.getString("keterangan_system").equals("sum_score_akhir")){
                                        EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setBackgroundResource(R.color.orange_800);
                                        txt_jawaban.setFocusable(false);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.CENTER);
                                        txt_jawaban.setText("0");
                                        // add to row
                                        row.addView(txt_jawaban, params3);

                                        table_pertanyaan.addView(row);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                        id_summary_score = list_edit_text_jawaban.size()-1;
                                    }
                                    else {
                                        EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.CENTER);
                                        txt_jawaban.setText("0");
                                        // add to row
                                        row.addView(txt_jawaban, params3);

                                        table_pertanyaan.addView(row);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                    }
                                }
                                else if (tipe_pertanyaan.equals("spinner")){
                                    if(obj.getString("keterangan_system").equals("no_yes_question")){
                                        TableRow row2 = new TableRow(edit_post_everything.this);
                                        row2.setBackgroundColor(getResources().getColor(R.color.white));

                                        final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.LEFT);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setLines(1);
                                        txt_jawaban.setEnabled(false);
                                        txt_jawaban.setVisibility(View.GONE);
                                        txt_jawaban.setText("No");
                                        // add to row
                                        row2.addView(txt_jawaban, params3);

                                        Spinner spinner = new Spinner(edit_post_everything.this);
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(edit_post_everything.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.slc_no_yes_question));
                                        spinner.setAdapter(spinnerArrayAdapter);

                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                txt_jawaban.setText(adapterView.getItemAtPosition(i).toString());
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });

                                        row2.addView(spinner, params3);
                                        table_pertanyaan.addView(row2);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                    }
                                    else if(obj.getString("keterangan_system").equals("ada_tidak_question")){
                                        TableRow row2 = new TableRow(edit_post_everything.this);
                                        row2.setBackgroundColor(getResources().getColor(R.color.white));

                                        final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.LEFT);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setLines(1);
                                        txt_jawaban.setEnabled(false);
                                        txt_jawaban.setVisibility(View.GONE);
                                        txt_jawaban.setText("No");
                                        // add to row
                                        row2.addView(txt_jawaban, params3);

                                        Spinner spinner = new Spinner(edit_post_everything.this);
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(edit_post_everything.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.slc_ada_tidak_question));
                                        spinner.setAdapter(spinnerArrayAdapter);

                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                txt_jawaban.setText(adapterView.getItemAtPosition(i).toString());
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });

                                        row2.addView(spinner, params3);
                                        table_pertanyaan.addView(row2);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                    }
                                    else if(obj.getString("keterangan_system").equals("n_p_y_na_question")){
                                        JSONArray jsonSub = obj.getJSONArray("sub_question");

                                        TableRow row2 = new TableRow(edit_post_everything.this);
                                        row2.setBackgroundColor(getResources().getColor(R.color.white));

                                        final EditText txt_jawaban = new EditText(edit_post_everything.this);
                                        txt_jawaban.setBackgroundResource(R.drawable.edittext_border);
                                        txt_jawaban.setTextSize(15);
                                        txt_jawaban.setGravity(Gravity.LEFT);
                                        txt_jawaban.setSingleLine(true);
                                        txt_jawaban.setLines(1);
                                        txt_jawaban.setEnabled(false);
                                        txt_jawaban.setVisibility(View.GONE);
                                        txt_jawaban.setText("N/A");
                                        // add to row
                                        row2.addView(txt_jawaban, params3);

                                        Spinner spinner = new Spinner(edit_post_everything.this);
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(edit_post_everything.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.slc_n_p_y_na_question));
                                        spinner.setAdapter(spinnerArrayAdapter);

                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                txt_jawaban.setText(adapterView.getItemAtPosition(i).toString());

                                                double total_score = 0;
                                                for(int x = 0; x < list_edit_text_jawaban_score.size();x++){
                                                    for(int y = 0; y < list_edit_text_jawaban_score.get(x).length(); y++){
                                                        try {
                                                            JSONObject objChild = list_edit_text_jawaban_score.get(x).getJSONObject(y);
                                                            if(list_edit_text_jawaban.get(x).getText().toString().equals(objChild.getString("jawaban"))){
                                                                total_score += Double.valueOf(objChild.getString("score"));
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                list_edit_text_jawaban.get(id_summary_score).setText(String.valueOf(total_score));
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });

                                        row2.addView(spinner, params3);
                                        table_pertanyaan.addView(row2);
                                        list_edit_text_jawaban.add(txt_jawaban);
                                        list_edit_text_jawaban_score.add(jsonSub);
                                    }
                                }
                                nomor++;
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(edit_post_everything.this, "Pertanyaan Gagal Ditampilkan", Toast.LENGTH_SHORT).show();
                        btn_submit.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    btn_submit.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    Toast.makeText(edit_post_everything.this, "Koneksi ke server error", Toast.LENGTH_SHORT).show();
                }
            });

            jArr.setRetryPolicy(new DefaultRetryPolicy(
                    30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // menambah request ke request queue
            AppController.getInstance(this).addToRequestQueue(jArr);
        }
    }

    private void proc_save_pertanyaan(ProgressDialog progressDialog,String id_timeline_jawaban){
        int counter = 0;
        ArrayList<HashMap<String,Object>> list =(ArrayList<HashMap<String,Object>>)dataDaftarPertanyaan;
        for (HashMap<String, Object> row_pertanyaan : list) {
            String id_pertanyaan = row_pertanyaan.get("id_pertanyaan").toString();

            if(jawaban_pertanyaan[counter] != null){
                final String jawaban = jawaban_pertanyaan[counter];
                StringRequest strReq = new StringRequest(Request.Method.POST, url_post_timeline2_jawaban_detail, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "save survey Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            // Check for error node in json
                            if (success == 1) {
                                Log.e("save jawaban pertanyaan", jObj.toString());
                            } else {
                                Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "save Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Posting parameters to url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_timeline_jawaban", id_timeline_jawaban);
                        params.put("id_timeline_pertanyaan", id_pertanyaan);
                        params.put("jawaban", jawaban);

                        return params;
                    }

                };

                strReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                AppController.getInstance(this).addToRequestQueue(strReq);
            }
            else {
                continue;
            }
            counter++;
        }
        progressDialog.dismiss();
    }
}
