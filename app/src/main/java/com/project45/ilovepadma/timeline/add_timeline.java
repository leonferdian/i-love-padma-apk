package com.project45.ilovepadma.timeline;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.MainActivity2;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.add_deskripsi_complain;
import com.project45.ilovepadma.complain.add_respon_complain;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.global.Util;
import com.project45.ilovepadma.notes.detail_note;
import com.project45.ilovepadma.registration_photo_form;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class add_timeline extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,company_create="",id_company="",nama_company="",act="",content_timeline="",photo_timeline="",id_timeline="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_company.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static String url_post_timeline    = Server.URL + "timeline/post_timeline";
    public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_timeline.php";
    public static String url_get_isi_post_everything = Server.URL + "timeline/get_isi_post_everything/";
    int success;

    EditText txt_post;
    Button btn_post;
    ImageView image_outlet;

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

        Intent intentku = getIntent(); // gets the previously created intent
        act = intentku.getStringExtra("act");

        if(act.equals("detail_timeline")){
            btn_post.setVisibility(View.GONE);
            content_timeline = intentku.getStringExtra("isi_timeline");
            photo_timeline = intentku.getStringExtra("photo_timeline");
            id_timeline = intentku.getStringExtra("id_timeline");
            //txt_post.setText(content_timeline);
            txt_post.setEnabled(false);
            get_isi_post(id_timeline);

            String img = Server.URL2 + "image_upload/list_foto_timeline/"+photo_timeline;
            Picasso.with(image_outlet.getContext())
                    .load(img)
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    .fit().centerCrop().into(image_outlet);
        }

        gps = new GPSTracker(add_timeline.this, add_timeline.this);
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
                proc_company();

            }
        });

        image_outlet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!act.equals("detail_timeline")) {

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    if ( ContextCompat.checkSelfPermission( add_timeline.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( add_timeline.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                                11 );

                    }
                    else if ( ContextCompat.checkSelfPermission( add_timeline.this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( add_timeline.this, new String[] {  android.Manifest.permission.CAMERA  },
                                12 );

                    }
                    else {
                        onSelectImageClick();
                    }
                }
                else{
                    Intent intent = new Intent(add_timeline.this, detail_foto.class);
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

    private void proc_company(){

        final ProgressDialog progressDialog = new ProgressDialog(add_timeline.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Save...");
        progressDialog.show();


        final String isi_timeline = txt_post.getText().toString();
        final String kategori = "post_everything";

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

                        if(image_name.equals("")) {
                            //end proses
                            progressDialog.dismiss();
                            onBackPressedWithProcess();
                        }
                        else{
                            save_foto();
                        }

                    }
                    else{
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

        private ProgressDialog pd = new ProgressDialog(add_timeline.this);

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

    private void get_isi_post(final String id_timeline){
        final ProgressDialog progressDialog = new ProgressDialog(add_timeline.this,
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
                        txt_post.setText(Html.fromHtml(isi_timeline,new URLImageParser(txt_post, add_timeline.this),null));
                        //txt_isi_note.setText(Html.fromHtml(isi_note,new URLImageParser(txt_isi_note, detail_note.this),null));

                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(add_timeline.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(add_timeline.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }


}
