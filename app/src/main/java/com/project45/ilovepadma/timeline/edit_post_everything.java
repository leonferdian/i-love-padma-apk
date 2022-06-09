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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class edit_post_everything extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,company_create="",id_company="",nama_company="",act="",content_timeline="",photo_timeline="",act_form="",id_timeline="",
           imagePost = "";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = edit_post_everything.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static String url_post_timeline    = Server.URL + "timeline/post_edit_timeline";
    public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_timeline.php";
    int success;

    EditText txt_post;
    TextView txt_nama_user;
    CircularImageView image_user_post;
    AppCompatButton btn_submit;
    ImageView img_post;
    Button btn_camera_foto,btn_galery_foto,btn_camera_video,btn_galery_video;

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
        }

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
                // update login session ke FALSE dan mengosongkan nilai id dan username
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

    private void pilihImageByCamera()
    {
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

    public String getPath(Uri uri)
    {
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

        if(isi_timeline.equals("")){
            Toast.makeText(getApplicationContext(), "Caption harus diisi ", Toast.LENGTH_LONG).show();
        }
        else {
            StringRequest strReq = new StringRequest(Request.Method.POST, url_post_timeline, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        // Check for error node in json
                        if (success == 1) {

                            Log.d("Save", jObj.toString());
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
}
