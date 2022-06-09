package com.project45.ilovepadma.complain;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.view.LayoutInflater;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Util;
import com.project45.ilovepadma.project.FileUtils;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
import java.util.UUID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class add_respon_complain_multi_file extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,employee_id_dms3="",id_complain="",timeStamp="",act_form="",tanggal_act="",complain_to="";
    int randomNumber;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static final String TAG = add_respon_complain_multi_file.class.getSimpleName();

    private static String url_post_deskripsi_complain     = Server.URL + "cust_service/post_save_respon_padma_complain";
    //public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_complain_respon.php";
    public static String url_image_upload2 = Server.URL2 +"image_upload/upload_foto_complain_respon2.php";
    public static String url_image_upload3 = Server.URL2 +"image_upload/upload_foto_complain_respon3.php";

    EditText txt_deskripsi;
    Button btn_save_deskripsi,btn_camera_foto,btn_galery_foto,btn_other;
    ImageView image_outlet;
    LinearLayout lay_list_file,lay_info_file;

    GPSTracker gps;
    double latitude,longitude;
    String alamat="";
    private Matrix matrix;
    private Bitmap bitmapLast;

    private static final int CAMERA_REQUEST = 1888;
    private Bitmap bitmap;
    private String fileImagePath,image_name = "",url_foto_stok="",pathFoto;
    private File destFile;
    Uri imageCaptureUri,mCropImageUri;

    int RESULT_LOAD_IMG = 9;

    android.app.AlertDialog.Builder alertDialogBuilder;
    android.app.AlertDialog alertDialog;

    private int PICK_PDF_REQUEST = 1;
    private Uri filePathFile;

    List<Data_cust_service> itemListFile = new ArrayList<Data_cust_service>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_respon_complain_multi_file);

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
        employee_id_dms3 = Api.sharedpreferences.getString(Api.TAG_ID_EMPLOYEE_DMS3, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);

        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        btn_save_deskripsi = findViewById(R.id.btn_save_deskripsi);
        image_outlet =  findViewById(R.id.image_outlet);
        btn_camera_foto =  findViewById(R.id.btn_camera_foto);
        btn_galery_foto =  findViewById(R.id.btn_galery_foto);
        btn_other =  findViewById(R.id.btn_other);
        lay_list_file =  findViewById(R.id.lay_list_file);
        lay_info_file =  findViewById(R.id.lay_info_file);

        lay_info_file.setVisibility(View.GONE);

        Intent intentku = getIntent(); // gets the previously created intent
        id_complain = intentku.getStringExtra("id_complain");
        complain_to = intentku.getStringExtra("complain_to");

        //Toast.makeText(getApplicationContext(), "id_complain "+id_complain, Toast.LENGTH_LONG).show();
        btn_camera_foto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {
                    pilihImageByCamera();
                }
                //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
            }
        });

        btn_galery_foto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }

            }
        });

        btn_other.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {
                    showFileChooser1();
                }
            }
        });

        btn_save_deskripsi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                postDeskripsi();

                //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
            }
        });

        gps = new GPSTracker(add_respon_complain_multi_file.this, add_respon_complain_multi_file.this);
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

        image_outlet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( add_respon_complain_multi_file.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( add_respon_complain_multi_file.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {
                    //onSelectImageClick();
                    pilihAct();
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
            window.setStatusBarColor(getResources().getColor(R.color.page_join_visit));
        }
    }

    private void postDeskripsi(){
        final String detail_aktifitas = txt_deskripsi.getText().toString().replace("\n", "<br>").trim();


        if(detail_aktifitas.equals("")){
            Toast.makeText(getApplicationContext(), "Respon harus diisi ", Toast.LENGTH_LONG).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(add_respon_complain_multi_file.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();

            //proses save to database using api
            StringRequest strReq = new StringRequest(Request.Method.POST, url_post_deskripsi_complain, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        // Check for error node in json
                        if (success == 1) {

                            Log.d("Add/update", jObj.toString());
                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            /*
                            if(image_name.equals("")) {
                                //end proses
                                progressDialog.dismiss();
                                onBackPressedWithProcess();
                            }
                            else{
                                save_foto();
                            }

                            */
                            if(itemListFile.size() !=0){
                                //Log.e(TAG, "last_id: " + jObj.getInt("last_id"));
                                saveFileComplain(String.valueOf(jObj.getInt("last_id")));
                            }
                            else{
                                //end proses
                                progressDialog.dismiss();
                                onBackPressedWithProcess();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "error " + jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Update Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "error " + error.getMessage(), Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("complain_nomor", id_complain);
                    params.put("isi_respon", detail_aktifitas);
                    params.put("create_by", id_user);
                    params.put("respon_foto", image_name);
                    params.put("complain_to", complain_to);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(strReq);
        }
    }

    private void saveFileComplain(final String idDetail) {
        ProgressDialog pd = new ProgressDialog(add_respon_complain_multi_file.this);
        pd.show();

        for (int i = 0; i < itemListFile.size(); i++) {
            Data_cust_service sel = itemListFile.get(i);

            if(!sel.getnama_file().equals("")) {
                Log.d(TAG,"fileName : "+sel.getnama_file()+" - "+i );
                if (sel.gettipe_file().equals("non_image")) {
                    uploadFileNote(sel.getnama_file(), idDetail, sel.getPathFile());
                } else if (sel.gettipe_file().equals("image")) {
                    save_foto(i, sel.getlokasi_file(), sel.getnama_file(), idDetail,pd);
                }

            }
            if(i == itemListFile.size()-1){
                onBackPressedWithProcess();
            }
        }
    }

    public void uploadFileNote(final String nama_file,final String idDetail, final Uri pathFile) {
        //getting name for the image
        String name = nama_file;

        if (name.equals("Nama File")) {

            Toast.makeText(this, "Pilih file non image", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code

            try {
                //getting the actual path of the image
                //String pathFlowMenu = FilePath.getPath(this, filePathFlowMenu);

                String pathFlowMenu = FileUtils.getReadablePathFromUri(this, pathFile);
                String uploadId = UUID.randomUUID().toString();


                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_image_upload3)
                        .addFileToUpload(pathFlowMenu, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("id_detail", idDetail)
                        .addParameter("tipe_upload", "file_complain")
                        .setMaxRetries(5);

                // For Android > 8, we need to set an Channel to the UploadNotificationConfig.
                // So, here, we create the channel and set it to the MultipartUploadRequest
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    NotificationChannel channel = new NotificationChannel("Upload", "Upload service", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);

                    UploadNotificationConfig notificationConfig = new UploadNotificationConfig();
                    notificationConfig.setTitle("Upload");

                    //uploadRequest.setNotificationConfig(notificationConfig);
                } else {
                    // If android < Oreo, just set a simple notification (or remove if you don't wanna any notification
                    // Notification is mandatory for Android > 8
                    uploadRequest.setNotificationConfig(new UploadNotificationConfig());
                }

                uploadRequest.startUpload();

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("upload_error", "info : "+exc.getMessage());
            }


        }
    }

    public void onSelectImageClick(View view) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    Toast.makeText(add_respon_complain_multi_file.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(add_respon_complain_multi_file.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAMERA_REQUEST){
            if(resultCode == Activity.RESULT_OK) {
                AddMark(username,image_name,alamat, String.valueOf(latitude), String.valueOf(longitude));
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
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFile = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "file" + id_user + "_" + timeStamp2;
            lay_list_file.removeAllViews();
            Data_cust_service itemFile = new Data_cust_service();
            itemFile.settipe_file("non_image");
            itemFile.setnama_file(filename);
            itemFile.setlokasi_file(filePathFile.getPath());
            itemFile.setPathFile(filePathFile);
            itemListFile.add(itemFile);

            callListFile();
            Log.d("file_log", filePathFile.toString());
        }
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

    private void callListFile() {
        if (getApplicationContext() != null) {
            //lay_list_file.removeAllViews();
            //lp.removeViewAt(0);
            for (int i = 0; i < itemListFile.size(); i++) {
                Data_cust_service sel = itemListFile.get(i);

                final int nmr = i;
                LayoutInflater inflater = LayoutInflater.from(add_respon_complain_multi_file.this);
                View v = inflater.inflate(R.layout.item_deskripsi_file, null, false);
                ImageView image_file = v.findViewById(R.id.image_file);
                TextView txt_type_file = v.findViewById(R.id.txt_type_file);
                TextView txt_nama_file = v.findViewById(R.id.txt_nama_file);
                TextView bt_delete = v.findViewById(R.id.bt_delete);
                CardView lyt_image = v.findViewById(R.id.lyt_image);

                txt_type_file.setText(sel.gettipe_file());
                txt_nama_file.setText(sel.getnama_file());

                if(sel.gettipe_file().equals("image")) {
                    lyt_image.setVisibility(View.VISIBLE);
                    String img = "file://" + sel.getlokasi_file();
                    Picasso.with(image_file.getContext())
                            .load(img)
                            .fit()
                            .placeholder(R.drawable.default_photo)
                            //.resize(300, 100)
                            //.onlyScaleDown()
                            .centerCrop()
                            .into(image_file);
                }
                else{
                    lyt_image.setVisibility(View.GONE);
                }

                bt_delete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        itemListFile.remove(nmr);
                        //lay_list_file.removeViewAt(nmr);
                        lay_list_file.removeAllViews();
                        if(sel.gettipe_file().equals("image")) {
                            DelImageFile(sel.getnama_file());
                        }
                        callListFile();
                    }
                });

                lay_list_file.addView(v);

                Log.d(TAG, "File : "+sel.gettipe_file());
            }
        }

    }

    private void DelImageFile(String file_img) {
        String file_dj_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PADMA/"+file_img;
        File fdelete = new File(file_dj_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.d(TAG,"file Deleted :" );
            } else {
                Log.d(TAG,"file not Deleted :");
            }
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
            paint.setColor(Color.RED);
            //paint.setAlpha(40);
            paint.setTextSize(12);
            paint.setAntiAlias(true);

            Paint paint2 = new Paint();
            paint2.setColor(Color.RED);
            //paint.setAlpha(40);
            paint2.setTextSize(10);
            paint2.setAntiAlias(true);
            //float textHeight = paint2.descent() - paint2.ascent();
            float textHeight = 60;
            Util.drawTextAndBreakLine(canvas, paint2, 7,
                    textHeight, 280, alamatt);

            canvas.drawText(watermark+" "+timeStamp,7, 30, paint);
            canvas.drawText("Lat: "+lat+" Lng: "+lng,7, 45, paint);

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
            image_name = "responC_" + id_user + "_" + timeStamp2 + ".png";

            File outputPhoto = new File(file, image_name);
            url_foto_stok = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PADMA/"+image_name;
            Log.d("url_foto_stok2", url_foto_stok);
            Log.d("compressPhoto", "output : " + outputPhoto.getName());

            //add file to array list

            lay_list_file.removeAllViews();
            Data_cust_service itemFile = new Data_cust_service();
            itemFile.settipe_file("image");
            itemFile.setnama_file(image_name);
            itemFile.setlokasi_file(url_foto_stok);
            itemListFile.add(itemFile);

            callListFile();
            //

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

    private void save_foto(final int nmr,final String lokasiImage,final String imageName,final String idDetail,ProgressDialog pd){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bm = BitmapFactory.decodeFile(lokasiImage);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        if(bm !=null) {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            String ba3 = Base64.encodeToString(ba, Base64.DEFAULT);

            // Upload image to server
            new uploadToServer(nmr, ba3,imageName,idDetail,pd).execute();
        }

    }

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private int param1;
        private String ba3,imageName,idDetail;
        ProgressDialog progessImage;

        public uploadToServer(int param1, String ba3, String imageNamee, String idDetaill,ProgressDialog pd) {
            this.param1 = param1;
            this.ba3 = ba3;
            this.imageName = imageNamee;
            this.idDetail = idDetaill;
            this.progessImage = pd;
        }

        //private ProgressDialog pd = new ProgressDialog(add_respon_complain_multi_file.this);

        protected void onPreExecute() {
            super.onPreExecute();
            progessImage.setMessage("Wait image " + param1 + " uploading! ");
            //pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba3));
            nameValuePairs.add(new BasicNameValuePair("ImageName", image_name));
            nameValuePairs.add(new BasicNameValuePair("cek_gambar", "ok"));
            nameValuePairs.add(new BasicNameValuePair("id_detail", idDetail));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url_image_upload2);
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

            //pd.hide();
            //progessImage.dismiss();
            int hitung = param1+1;

            // Memanggil main activity
            Toast.makeText(getApplicationContext(),"Upload Image "+hitung+" Selesai", Toast.LENGTH_LONG).show();
            onBackPressedWithProcess();


        }

    }

    private void showFileChooser1() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    private void pilihAct() {
        alertDialogBuilder = new android.app.AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(add_respon_complain_multi_file.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(
                50, // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here
                0,
                50,
                0
        );

        final TableLayout tblRange = new TableLayout(add_respon_complain_multi_file.this);
        tblRange.setColumnStretchable(0, true);
        //tblRange.setColumnStretchable(1, true);
        //tblRange.setColumnStretchable(2, true);

        TableRow row = new TableRow(add_respon_complain_multi_file.this);
        row.setBackgroundColor(getResources().getColor(R.color.white));

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(3, 1, 3, 1);
        params.height = 100;

        TextView txt_camera = new TextView(add_respon_complain_multi_file.this);
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

        final TableRow row2 = new TableRow(add_respon_complain_multi_file.this);
        row2.setBackgroundColor(getResources().getColor(R.color.white));

        TextView txt_galery = new TextView(add_respon_complain_multi_file.this);
        txt_galery.setText("Gallery");
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

        image_name="responC_" + timeStamp + ".png";
        destFile = new File(file, "responC_" + timeStamp + ".png");
        imageCaptureUri = Uri.fromFile(destFile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void cancelDialog()
    {
        //Now you can either use
        alertDialog.dismiss();
        //or dialog.dismiss();
    }

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(11,returnIntent);
        finish();
    }
}
