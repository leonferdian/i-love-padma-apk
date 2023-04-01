package com.project45.ilovepadma.outlet;

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
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.aktifitas.add_deskripsi;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.data.Data_customer;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Util;
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.timeline.list_timeline_post_everything2;
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
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class detail_outlet_semesta extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,employee_id_dms3="",id_outlet="",timeStamp="",act_form="",display_outlet="";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    int randomNumber;

    private static final String TAG = add_deskripsi.class.getSimpleName();

    private static String url_post_outlet     = Server.URL + "outlet/post_add_outlet_semesta";
    public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_outlet.php";

    ImageView img_outlet;
    public EditText txt_idcust,txt_namecust,txt_lat,txt_long,txt_address,txt_address2,txt_kelurahan,txt_kecamatan,txt_zip_code,
            txt_city,txt_province,txt_country,txt_jam_buka,txt_jam_tutup,txt_jam_istirahat_mulai,txt_jam_istirahat_tutup,txt_contact_person,
            txt_telp_pic,txt_phone_pic,txt_wa_pic,txt_id_dms;
    private Button btPlacesAPI,btn_camera_foto,btn_galery_foto,btn_save_outlet;
    Spinner spinner_type_outlet,spinner_saluran_dist,spinner_segment1;

    GPSTracker gps;
    double latitude,longitude;
    String alamat="";
    private Matrix matrix;
    private Bitmap bitmapLast;
    //for image
    private String fileImagePath,image_name = "",url_foto_stok="",pathFoto;
    private File destFile;
    Uri imageCaptureUri,mCropImageUri;
    public static final int CAMERA_REQUEST = 100;
    int RESULT_LOAD_IMG = 9;
    // last for image
    Data_customer detOutlet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_outlet_semesta);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_outlet);
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

        img_outlet = findViewById(R.id.img_outlet);
        btn_camera_foto = findViewById(R.id.btn_camera_foto);
        btn_galery_foto = findViewById(R.id.btn_galery_foto);
        btPlacesAPI = findViewById(R.id.bt_ppicker);
        txt_idcust = findViewById(R.id.txt_idcust);
        txt_namecust = findViewById(R.id.txt_namecust);
        txt_lat = findViewById(R.id.txt_lat);
        txt_long = findViewById(R.id.txt_long);
        txt_address = findViewById(R.id.txt_address);
        txt_address2 = findViewById(R.id.txt_address2);
        txt_kelurahan = findViewById(R.id.txt_kelurahan);
        txt_kecamatan = findViewById(R.id.txt_kecamatan);
        txt_zip_code = findViewById(R.id.txt_zip_code);
        txt_city = findViewById(R.id.txt_city);
        txt_province = findViewById(R.id.txt_province);
        txt_country = findViewById(R.id.txt_country);
        spinner_type_outlet = findViewById(R.id.spinner_type_outlet);
        spinner_saluran_dist = findViewById(R.id.spinner_saluran_dist);
        spinner_segment1 = findViewById(R.id.spinner_segment1);
        btn_save_outlet = findViewById(R.id.btn_save_outlet);

        timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());
        /*
        txt_lat.setVisibility(View.GONE);
        txt_long.setVisibility(View.GONE);
        txt_kelurahan.setVisibility(View.GONE);
        txt_kecamatan.setVisibility(View.GONE);
        txt_zip_code.setVisibility(View.GONE);
        txt_city.setVisibility(View.GONE);
        txt_province.setVisibility(View.GONE);
        txt_country.setVisibility(View.GONE);
        txt_kelurahan.setVisibility(View.GONE);*/

        //get_id_outlet(timeStamp);
        Intent intentku = getIntent(); // gets the previously created intent
        act_form = intentku.getStringExtra("act");
        detOutlet = (Data_customer) intentku.getSerializableExtra(Api.PARAM_DATA);
        if (detOutlet != null) {

            display_outlet = detOutlet.getimage_customer();
            image_name = detOutlet.getimage_customer();
            String bm = Server.URL2 +"image_upload/list_foto_outlet/"+detOutlet.getimage_customer();
            Picasso.with(img_outlet.getContext())
                    .load(bm)
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    .fit().centerCrop().into(img_outlet);

            id_outlet = detOutlet.getcode_customer();
            txt_idcust.setText(id_outlet);
            txt_namecust.setText(detOutlet.getName());
            txt_lat.setText(String.valueOf(detOutlet.getLat()));
            txt_long.setText(String.valueOf(detOutlet.getLng()));
            txt_address.setText(detOutlet.getAddress());
            txt_kelurahan.setText(detOutlet.getKelurahan());
            txt_kecamatan.setText(detOutlet.getKecamatan());
            txt_zip_code.setText(detOutlet.getZip_code());
            txt_city.setText(detOutlet.getCity());
            txt_province.setText(detOutlet.getProvince());
            txt_address.setText(detOutlet.getAddress());
            txt_country.setText(detOutlet.getCountry());
            txt_address2.setText(detOutlet.getkoreksi_alamat());

            ArrayAdapter<CharSequence> adapterTypeOutlet = ArrayAdapter.createFromResource(getApplicationContext(), R.array.slc_type_outlet, android.R.layout.simple_spinner_item);
            adapterTypeOutlet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (detOutlet.getOutlet_type() != null) {
                int spinnerPosition2 = adapterTypeOutlet.getPosition(detOutlet.getOutlet_type());
                spinner_type_outlet.setSelection(spinnerPosition2);
            }

            ArrayAdapter<CharSequence> adapterSaluranDist = ArrayAdapter.createFromResource(getApplicationContext(), R.array.slc_saluran_disc, android.R.layout.simple_spinner_item);
            adapterSaluranDist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (detOutlet.getSaluran_distribusi() != null) {
                int spinnerPosition2 = adapterSaluranDist.getPosition(detOutlet.getSaluran_distribusi());
                spinner_saluran_dist.setSelection(spinnerPosition2);
            }

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.slc_segment1, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (detOutlet.getSegment_level_1() != null) {
                int spinnerPosition2 = adapter.getPosition(detOutlet.getSegment_level_1());
                spinner_segment1.setSelection(spinnerPosition2);
            }

            /*
            if(!detOutlet.getcreate_by().equals(id_user)){

                btn_camera_foto.setVisibility(View.GONE);
                btn_galery_foto.setVisibility(View.GONE);
                btPlacesAPI.setVisibility(View.GONE);
                btn_save_outlet.setVisibility(View.GONE);
            }*/

            img_outlet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // update login session ke FALSE dan mengosongkan nilai id dan username
                    Intent intent = new Intent(detail_outlet_semesta.this, detail_foto.class);
                    intent.putExtra("sku", "Foto Outlet");
                    intent.putExtra("file_foto", detOutlet.getimage_customer());
                    intent.putExtra("tgl_buat", "");
                    intent.putExtra("act", "foto_outlet");
                    //startActivity(intent);
                    startActivityForResult(intent, 1);

                }
            });
        }

        gps = new GPSTracker(detail_outlet_semesta.this, detail_outlet_semesta.this);
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

        btn_camera_foto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( detail_outlet_semesta.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( detail_outlet_semesta.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( detail_outlet_semesta.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( detail_outlet_semesta.this, new String[] {  Manifest.permission.CAMERA  },
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

                if ( ContextCompat.checkSelfPermission( detail_outlet_semesta.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( detail_outlet_semesta.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( detail_outlet_semesta.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( detail_outlet_semesta.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }

            }
        });

        btn_save_outlet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                postOutlet();

            }
        });

        btPlacesAPI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), get_address.class);
                startActivityForResult(intent, 212);

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

    private void get_id_outlet(final String tanggal){
        id_outlet =  "ILP-"+id_user+"-"+tanggal+"-"+ String.valueOf(generateRandomNumbers(1001,9999));
        txt_idcust.setText(id_outlet);
    }

    private int generateRandomNumbers(int min, int max) {
        // min & max will be changed as per your requirement. In my case, I've taken min = 2 & max = 32

        //Random r = new Random();
        //int randomNumber = r.nextInt(100);
        int randomNumberCount = 10;

        int dif = max - min;
        if (dif < (randomNumberCount * 3)) {
            dif = (randomNumberCount * 3);
        }

        int margin = (int) Math.ceil((float) dif / randomNumberCount);
        List<Integer> randomNumberList = new ArrayList<>();



        Random random = new Random();

        for (int i = 0; i < randomNumberCount; i++) {
            int range = (margin * i) + min; // 2, 5, 8

            int randomNum = random.nextInt(margin);
            if (randomNum == 0) {
                randomNum = 1;
            }

            int number = (randomNum + range);
            randomNumber = number;
            //randomNumberList.add(number);
        }
        //Toast.makeText(getApplicationContext(), " random "+String.valueOf(randomNumber), Toast.LENGTH_LONG).show();
        return randomNumber;
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
                        img_outlet.setImageBitmap(selectedImage);
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(detail_outlet_semesta.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(detail_outlet_semesta.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
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
                    Picasso.with(this).load(url_foto).into(img_outlet);
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        }
        /* get address*/
        if (requestCode == 212) {
            if (resultCode == 22) {
                String address = data.getStringExtra("address");
                String lat_address = data.getStringExtra("lat_address");
                String lng_address = data.getStringExtra("lng_address");

                setAddressFromLatLng(Double.valueOf(lat_address),Double.valueOf(lng_address));
                set_address(String.valueOf(address),String.valueOf(lat_address),String.valueOf(lng_address));
            }
        }

    }

    public void setAddressFromLatLng(double lat, double lng) {
        Geocoder geocoder = new Geocoder(detail_outlet_semesta.this, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                //StringBuilder sb = new StringBuilder();
                //for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                //sb.append(address.getAddressLine(i)).append("\n");//address
                //}
                txt_address.setText(address.getAddressLine(0));
                txt_kecamatan.setText(address.getLocality());//village
                txt_kelurahan.setText(address.getSubLocality());
                txt_zip_code.setText(address.getPostalCode());
                txt_country.setText(address.getCountryName());
                txt_province.setText(address.getAdminArea()); //state
                txt_city.setText(address.getSubAdminArea());//district
                //sb.append(address.getSubLocality()).append("\n");
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable connect to Geocoder", e);
        }
    }

    public void set_address(String address,String Lat,String Long) {
        //txt_address.setText(address);
        txt_lat.setText(String.valueOf(Lat));
        txt_long.setText(String.valueOf(Long));
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
            Util.drawTextAndBreakLine(canvas, paint2, 7,
                    textHeight, 280, alamatt);

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
            image_name = "oT_" + id_user + "_" + timeStamp2 + ".png";

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

    private void postOutlet() {

        final String id_outlet = txt_idcust.getText().toString();
        final String nama_outlet = txt_namecust.getText().toString();
        final String latitude = txt_lat.getText().toString();
        final String longitude = txt_long.getText().toString();
        final String alamat = txt_address.getText().toString();
        final String kelurahan = txt_kelurahan.getText().toString();
        final String kecamatan = txt_kecamatan.getText().toString();
        final String zipcode = txt_zip_code.getText().toString();
        final String city = txt_city.getText().toString();
        final String province = txt_province.getText().toString();
        final String country = txt_country.getText().toString();
        final String koreksi_alamat = txt_address2.getText().toString();
        final String channel_outlet = String.valueOf(spinner_type_outlet.getSelectedItem());
        final String segment = String.valueOf(spinner_saluran_dist.getSelectedItem());
        final String sub_segment = String.valueOf(spinner_segment1.getSelectedItem());
        final String foto_outlet = image_name;

        if(foto_outlet.equals("")){
            Toast.makeText(getApplicationContext(), "Foto harus dilengkapi ", Toast.LENGTH_LONG).show();
        }
        else if(nama_outlet.equals("")){
            Toast.makeText(getApplicationContext(), "Nama Outlet From harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(alamat.equals("")){
            Toast.makeText(getApplicationContext(), "Alamat harus diisi ", Toast.LENGTH_LONG).show();
        }
        else{

            final ProgressDialog progressDialog = new ProgressDialog(detail_outlet_semesta.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();

            //proses save to database using api
            StringRequest strReq = new StringRequest(Request.Method.POST, url_post_outlet, new Response.Listener<String>() {

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

                            if(image_name.equals(display_outlet)) {
                                //end proses
                                progressDialog.dismiss();
                                onBackPressedWithProcess();
                            }
                            else{
                                save_foto();
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
                    params.put("id_outlet", id_outlet);
                    params.put("nama_outlet", nama_outlet);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    params.put("alamat", alamat);
                    params.put("kelurahan", kelurahan);
                    params.put("kecamatan", kecamatan);
                    params.put("zipcode", zipcode);
                    params.put("city", city);
                    params.put("province", province);
                    params.put("country", country);
                    params.put("koreksi_alamat", koreksi_alamat);
                    params.put("channel_outlet", channel_outlet);
                    params.put("segment", segment);
                    params.put("sub_segment", sub_segment);
                    params.put("foto_outlet", foto_outlet);
                    params.put("create_by", id_user);
                    params.put("update_by", id_user);

                    return params;
                }

            };

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

        private ProgressDialog pd = new ProgressDialog(detail_outlet_semesta.this);

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
}
