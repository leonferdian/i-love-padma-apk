package com.project45.ilovepadma.timeline;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.detail_complain_for_you;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Util;
import com.project45.ilovepadma.setting.add_company;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class add_timeline2 extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,company_create="",id_company="",nama_company="",act="",content_timeline="",photo_timeline="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_company.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static String url_post_timeline    = Server.URL + "timeline/post_timeline";
    public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_timeline.php";
    int success;

    EditText txt_post;
    Button btn_post;
    ImageView image_outlet;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_timeline);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_aktifitas);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        txt_post = findViewById(R.id.txt_post);
        btn_post = findViewById(R.id.btn_post);
        image_outlet =  findViewById(R.id.image_outlet);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Toast.makeText(getApplicationContext(), "tl 2", Toast.LENGTH_LONG).show();

        Intent intentku = getIntent(); // gets the previously created intent
        act = intentku.getStringExtra("act");

        if(act.equals("detail_timeline")){
            btn_post.setVisibility(View.GONE);
            content_timeline = intentku.getStringExtra("isi_timeline");
            photo_timeline = intentku.getStringExtra("photo_timeline");
            txt_post.setText(content_timeline);

            String img = Server.URL2 + "image_upload/list_foto_timeline/"+photo_timeline;
            Picasso.with(image_outlet.getContext())
                    .load(img)
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    .fit().centerCrop().into(image_outlet);
        }

        gps = new GPSTracker(add_timeline2.this, add_timeline2.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            alamat = gps.setAddressFromLatLng();
        }

        btn_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                proc_save_posting();

            }
        });

        image_outlet.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!act.equals("detail_timeline")) {

                    /*
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    if ( ContextCompat.checkSelfPermission( add_timeline2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( add_timeline2.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                                11 );

                    }
                    else if ( ContextCompat.checkSelfPermission( add_timeline2.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( add_timeline2.this, new String[] {  Manifest.permission.CAMERA  },
                                12 );

                    }
                    else {
                        onSelectImageClick();
                    }
                    */

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    if ( ContextCompat.checkSelfPermission( add_timeline2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( add_timeline2.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                                11 );

                    }
                    else if ( ContextCompat.checkSelfPermission( add_timeline2.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( add_timeline2.this, new String[] {  Manifest.permission.CAMERA  },
                                12 );

                    }
                    else {

                        pilihAct();
                        /*
                        int picd = 0;
                        if (picd == 0) {
                            if (!checkCameraPermission()) {
                                requestCameraPermission();
                            } else {
                                pickFromGallery();
                                Log.e("crop2", "Response: tes");
                            }
                        } else if (picd == 1) {
                            if (!checkStoragePermission()) {
                                requestStoragePermission();
                            } else {
                                pickFromGallery();
                                Log.e("crop3", "Response: tes");
                            }
                        }

                         */
                    }

                }
                else{
                    Intent intent = new Intent(add_timeline2.this, detail_foto.class);
                    intent.putExtra("sku", "Foto Timeline");
                    intent.putExtra("file_foto", photo_timeline);
                    intent.putExtra("tgl_buat", "");
                    intent.putExtra("act", "foto_timeline");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                }

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

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
        Log.e("crop", "Response: tes" );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void proc_save_posting(){

        final ProgressDialog progressDialog = new ProgressDialog(add_timeline2.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Save...");
        progressDialog.show();

        final String isi_timeline = txt_post.getText().toString();
        final String kategori = "post_everything";

        // Get the device's current location and address
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(add_timeline2.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);

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
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("address", address);

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

    public void onSelectImageClick() {
        CropImage.startPickImageActivity(this);
    }

    private void startCropImageActivity(Uri imageUri) {

        //Toast.makeText(account_user.this,String.valueOf(imageUri), Toast.LENGTH_LONG).show();
        CropImage.activity(imageUri)
                .setRequestedSize(500, 500, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setInitialCropWindowPaddingRatio(0)
                .start(this);
        //CropImage.startPickImageActivity(this);
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
    private void pilihAct() {
        alertDialogBuilder = new android.app.AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(add_timeline2.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(
                50, // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here
                0,
                50,
                0
        );

        final TableLayout tblRange = new TableLayout(add_timeline2.this);
        tblRange.setColumnStretchable(0, true);
        //tblRange.setColumnStretchable(1, true);
        //tblRange.setColumnStretchable(2, true);

        TableRow row = new TableRow(add_timeline2.this);
        row.setBackgroundColor(getResources().getColor(R.color.white));

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(3, 1, 3, 1);
        params.height = 100;

        TextView txt_camera = new TextView(add_timeline2.this);
        txt_camera.setText("Camera");
        txt_camera.setTextColor(getResources().getColor(R.color.white));
        txt_camera.setBackgroundResource(R.color.page_tbl_total);
        txt_camera.setTextSize(10);
        txt_camera.setGravity(Gravity.CENTER);
        txt_camera.setPadding(10, 0, 10, 0);
        txt_camera.setTypeface(null, Typeface.BOLD);
        txt_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                cancelDialog(); //Implement method for canceling dialog

                pilihImageByCamera();
            }
        });

        // add to row
        row.addView(txt_camera, params);
        tblRange.addView(row);

        final TableRow row2 = new TableRow(add_timeline2.this);
        row2.setBackgroundColor(getResources().getColor(R.color.white));

        TextView txt_galery = new TextView(add_timeline2.this);
        txt_galery.setText("Gallery Image");
        txt_galery.setTextColor(getResources().getColor(R.color.white));
        txt_galery.setBackgroundResource(R.color.page_tbl_total);
        txt_galery.setTextSize(10);
        txt_galery.setGravity(Gravity.CENTER);
        txt_galery.setPadding(10, 0, 10, 0);
        txt_galery.setTypeface(null, Typeface.BOLD);
        txt_galery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                cancelDialog(); //Implement method for canceling dialog

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        // add to row
        row2.addView(txt_galery, params);
        tblRange.addView(row2);

        final TableRow row3 = new TableRow(add_timeline2.this);
        row3.setBackgroundColor(getResources().getColor(R.color.white));

        TextView txt_record_video = new TextView(add_timeline2.this);
        txt_record_video.setText("Record Video");
        txt_record_video.setTextColor(getResources().getColor(R.color.white));
        txt_record_video.setBackgroundResource(R.color.page_tbl_total);
        txt_record_video.setTextSize(10);
        txt_record_video.setGravity(Gravity.CENTER);
        txt_record_video.setPadding(10, 0, 10, 0);
        txt_record_video.setTypeface(null, Typeface.BOLD);
        txt_record_video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                final String isi_timeline = txt_post.getText().toString();
                if(isi_timeline.equals("")){
                    Toast.makeText(getApplicationContext(), "Caption harus diisi ", Toast.LENGTH_LONG).show();
                }
                else {
                    cancelDialog(); //Implement method for canceling dialog

                    recordVideo();
                }
            }
        });

        // add to row
        row3.addView(txt_record_video, params);
        tblRange.addView(row3);

        final TableRow row4 = new TableRow(add_timeline2.this);
        row4.setBackgroundColor(getResources().getColor(R.color.white));

        TextView txt_gallery_video = new TextView(add_timeline2.this);
        txt_gallery_video.setText("Gallery Video");
        txt_gallery_video.setTextColor(getResources().getColor(R.color.white));
        txt_gallery_video.setBackgroundResource(R.color.page_tbl_total);
        txt_gallery_video.setTextSize(10);
        txt_gallery_video.setGravity(Gravity.CENTER);
        txt_gallery_video.setPadding(10, 0, 10, 0);
        txt_gallery_video.setTypeface(null, Typeface.BOLD);
        txt_gallery_video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                final String isi_timeline = txt_post.getText().toString();
                if(isi_timeline.equals("")) {
                    Toast.makeText(getApplicationContext(), "Caption harus diisi ", Toast.LENGTH_LONG).show();
                }
                else {
                    cancelDialog(); //Implement method for canceling dialog
                    Intent videoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    videoPickerIntent.setType("video/*");
                    startActivityForResult(videoPickerIntent, GALLERY_VIDEO_REQUEST_CODE);
                }
            }
        });

        // add to row
        row4.addView(txt_gallery_video, params);
        tblRange.addView(row4);

        layout.addView(tblRange);

        // set title dialog
        alertDialogBuilder.setTitle("Pilih Gambar Dari ");
        // set pesan dari dialog
        alertDialogBuilder
                //.setMessage("ketikkan Nama Outlet")
                //.setIcon(R.mipmap.ic_launcher)
                .setView(layout)
                .setCancelable(false)
                .setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        //MainActivity2.this.finish();
                        cancelDialog();
                    }
                })
                /*.setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Simpan",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        //Toast.makeText(getApplicationContext(), "Audit finished", Toast.LENGTH_LONG).show();


                    }
                })*/

                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });


        // membuat alert dialog dari builder
        alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }

    private void cancelDialog()
    {
        //Now you can either use
        alertDialog.dismiss();
        //or dialog.dismiss();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getApplicationContext(), String.valueOf(requestCode)+"-"+String.valueOf(CAMERA_REQUEST), Toast.LENGTH_LONG).show();
        //Log.d("int", String.valueOf(RESULT_OK)+" ~ "+String.valueOf(resultCode));
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
                        image_outlet.setImageBitmap(selectedImage);
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(add_timeline2.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(add_timeline2.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == GALLERY_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                fileUriGalleryVideo = imageUri;
                launchUploadActivity2(false);
            }
            else {
                Toast.makeText(add_timeline2.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
        /*
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            //image_outlet.setImageURI(imageUri);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        */
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
                    Picasso.with(this).load(url_foto).into(image_outlet);
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        }
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
        /*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String resultUri = result.getUri().getPath();
                File filePhoto = new File(resultUri);

                if (filePhoto.exists()) {
                    fileImagePath = filePhoto.getAbsolutePath();
                    Uri url_foto = result.getUri();
                    url_foto_stok=filePhoto.getAbsolutePath();
                    Log.d("url_foto_stok1", url_foto_stok);
                    AddMark(username,url_foto_stok,alamat, String.valueOf(latitude), String.valueOf(longitude));
                    pathFoto="ok";
                    image_outlet.setImageBitmap(bitmap);
                }
            }
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted = grantResults[1] == (PackageManager.PERMISSION_GRANTED);
                    if (camera_accepted && storage_accepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if (storage_accepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
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
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

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
            paint.setColor(Color.RED);
            //paint.setAlpha(40);
            paint.setTextSize(24);
            paint.setAntiAlias(true);
            canvas.drawText(watermark+" "+timeStamp,10, 40, paint);
            canvas.drawText("Lat: "+lat+" Lng: "+lng,10, 80, paint);

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

        private ProgressDialog pd = new ProgressDialog(add_timeline2.this);

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

        Intent i = new Intent(add_timeline2.this, uploadVideo.class);
        i.putExtra("filePath", fileUriVideo.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("isi_timeline", isi_timeline);
        startActivityForResult(i, 77);

    }

    private void launchUploadActivity2(boolean isImage){

        final String isi_timeline = txt_post.getText().toString();

        Intent i = new Intent(add_timeline2.this, uploadVideo.class);
        i.putExtra("filePath", getPath(fileUriGalleryVideo));
        i.putExtra("isImage", isImage);
        i.putExtra("isi_timeline", isi_timeline);
        startActivityForResult(i, 77);

    }
}
