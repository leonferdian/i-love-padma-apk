package com.project45.ilovepadma.absensi;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.adapter.GPSTracker;
import com.project45.ilovepadma.adapter.adapter_absensi;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.berita.detail_berita;
import com.project45.ilovepadma.complain.add_deskripsi_complain;
import com.project45.ilovepadma.complain.add_deskripsi_complain_multi_file;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.Util;
import com.project45.ilovepadma.notes.list_note;
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.util.Server;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_absen  extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, adapter_absensi.JVListener{

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    public static final String my_shared_preferences = "my_shared_preferences";

    private static final String TAG = list_absen.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    String id_user, username,bm,email,id_company="",nama_company="",timeStamp="",tglStamp="";
    RecyclerView card_view_list_absen;
    SwipeRefreshLayout swipe;
    FrameLayout mFrameLayout;
    View dialogView;
    TextView textNoData;

    private static String url_post_absen    = Server.URL + "absen/post_save_absen";
    private static String url_update_absen    = Server.URL + "absen/post_update_absen";
    private static String url_view_absen     = Server.URL + "absen/get_list_absen/";
    public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_absen.php";

    String tahun="",bulan="";

    GPSTracker gps;
    double latitude=0,longitude=0;
    String alamat="";

    adapter_absensi adapter;
    private ArrayList<ListItem> listItemArrayList;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    Spinner spinner_tahun,spinner_bulan;

    /* for upload image*/
    ImageView image_absen;
    private static final int CAMERA_REQUEST = 1888;
    private String image_name = "",url_foto_stok="",fileImagePath="",pathFoto="";
    private File destFile;
    Uri imageCaptureUri,mCropImageUri;
    private Bitmap bitmap;
    private Bitmap bitmapLast;
    private Matrix matrix;
    /*  end*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_absen);

        Intent i = getIntent();
        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_list_cust_service);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        changeStatusBarColor();

        //locationMangaer = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

        listItemArrayList = new ArrayList<>();

        swipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        card_view_list_absen    = (RecyclerView)findViewById(R.id.card_view_list_absen);
        mFrameLayout = findViewById(R.id.frame);
        textNoData = findViewById(R.id.text_no_data);

        mFrameLayout.setVisibility(View.GONE);
        textNoData.setVisibility(View.GONE);

        tglStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        timeStamp = new SimpleDateFormat("HH:mm",
                Locale.getDefault()).format(new Date());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if ( ContextCompat.checkSelfPermission( list_absen.this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( list_absen.this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                        11 );

            }
            else if ( ContextCompat.checkSelfPermission( list_absen.this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( list_absen.this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                        11 );

            }
            else {
                gps = new GPSTracker(list_absen.this, list_absen.this);
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if (gps.setAddressFromLatLng() != null) {
                        alamat = gps.setAddressFromLatLng();
                    } else {
                        alamat = "Address Undetected";
                    }
                }
            }

        // Create the grid layout manager with 2 columns.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        //card_view_list_photo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type=adapter.getItemViewType(position);
                if (type == 0)
                    return 1;
                else
                    return 1;
            }
        });

        // Set layout manager.
        card_view_list_absen.setLayoutManager(gridLayoutManager);

        adapter = new adapter_absensi(list_absen.this,listItemArrayList);
        card_view_list_absen.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(swipeRefreshDo);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           //callVolley(0);
                           //callVolley2(0,id_jv,id_customer);
                           callVolley(tahun,bulan,id_user,id_company);

                       }
                   }
        );


    }

    public boolean onSupportNavigateUp(){
        //onBackPressed();
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

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshDo = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            listItemArrayList.clear();
            callVolley(tahun,bulan,id_user,id_company);
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu ditoolbar
        getMenuInflater().inflate(R.menu.menu_list_cust_service, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_complain_menu) {

            gps = new GPSTracker(list_absen.this, list_absen.this);
            if (gps.canGetLocation()) {
                if (gps.setAddressFromLatLng() != null) {
                    alamat = gps.setAddressFromLatLng();
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    showCheckInDialog();
                } else {
                    alamat = "Address Undetected";
                    latitude = 0;
                    longitude = 0;
                    Toast.makeText(getApplicationContext(), "Kembali ke menu utama / aktifkan GPS anda", Toast.LENGTH_LONG).show();
                }
            }
            else {
                alamat = "Address Undetected";
                latitude = 0;
                longitude = 0;
                Toast.makeText(getApplicationContext(), "Kembali ke menu utama / aktifkan GPS anda", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        else if (id == R.id.filter_report_sales) {
            //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            DialogFilter("Filter");
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        listItemArrayList.clear();
        callVolley(tahun,bulan,id_user,id_company);
    }

    @Override
    public void onCheckOutClick(int position, ArrayList<ListItem> itemss) {
        //Toast.makeText(getApplicationContext(), "id "+itemss.get(position).getid(), Toast.LENGTH_LONG).show();
        showCheckOutDialog(itemss.get(position).getid());
    }

    @Override
    public void onImageClick(int position, ArrayList<ListItem> itemss) {
        if(itemss.get(position).getfoto_absen().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Photo not available ", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(list_absen.this, detail_foto.class);
            intent.putExtra("sku", "Image Absen");
            intent.putExtra("file_foto", itemss.get(position).getfoto_absen());
            intent.putExtra("tgl_buat", "");
            intent.putExtra("act", "image_absen");
            //startActivity(intent);
            startActivityForResult(intent, 1);
        }
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(list_absen.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_filter_wr, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Filter Note");

        spinner_tahun = dialogView.findViewById(R.id.spinner_tahun);
        spinner_bulan = dialogView.findViewById(R.id.spinner_bulan);


        addItemsOnSpinnerTahun(spinner_tahun);
        addItemsOnSpinnerBulan(spinner_bulan);

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tahun = String.valueOf(spinner_tahun.getSelectedItem());
                bulan = String.valueOf(spinner_bulan.getSelectedItem());

                getSupportActionBar().setTitle("List Note "+tahun+" - "+bulan);

                callVolley(tahun,bulan,id_user,id_company);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //kosong();
            }
        });

        dialog.show();

    }

    public void addItemsOnSpinnerTahun(final Spinner spinner_thn) {
        List<String> list = new ArrayList<String>();
        int tahun_lalu = Integer.valueOf(tahun)-1;
        for (int i = tahun_lalu; i <= Integer.valueOf(tahun); i++) {
            list.add(String.valueOf(i));
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_thn.setAdapter(dataAdapter);
        int spinnerPosition2 = dataAdapter.getPosition(tahun);
        spinner_thn.setSelection(spinnerPosition2);
    }

    public void addItemsOnSpinnerBulan(final Spinner spinner_bln) {
        List<String> list = new ArrayList<String>();

        for (int i = 1; i <=12; i++) {
            list.add(String.valueOf(i));
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bln.setAdapter(dataAdapter);
        int spinnerPosition2 = dataAdapter.getPosition(bulan);
        spinner_bln.setSelection(spinnerPosition2);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    image_absen.setImageBitmap(bitmap);
                }
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

    public Matrix getMatrix() {
        return matrix;
    }

    public void  setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void AddMark(String watermark, String imageName, String alamatt, String lat, String lng) {
        try{

            final int IMAGE_MAX_SIZE = 500000;
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
            paint.setColor(Color.WHITE);
            //paint.setAlpha(40);
            paint.setTextSize(12);
            paint.setAntiAlias(true);

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
            image_name = "absen_" + id_user + "_" + timeStamp2 + ".png";

            File outputPhoto = new File(file, image_name);
            url_foto_stok = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PADMA/"+image_name;
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

    public interface ListItem {
        boolean isHeader();
        String getid();
        String getid_user();
        String gethari_check_in();
        String gettime_check_in();
        String gettime_check_out();
        String getaddress_check_in();
        String getaddress_check_out();
        String getstatus_absensi();
        String getfoto_user();
        String getfoto_absen();
    }

    private void showCheckInDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_absen);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button spn_from_date = (Button) dialog.findViewById(R.id.spn_from_date);
        final Button spn_from_time = (Button) dialog.findViewById(R.id.spn_from_time);
        final EditText et_location = (EditText) dialog.findViewById(R.id.et_location);
        final Button bt_save = (Button) dialog.findViewById(R.id.bt_save);
        final ImageButton bt_close = dialog.findViewById(R.id.bt_close);
        image_absen = dialog.findViewById(R.id.image_absen);

        tglStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        timeStamp = new SimpleDateFormat("HH:mm",
                Locale.getDefault()).format(new Date());

        spn_from_date.setText(tglStamp);
        spn_from_time.setText(timeStamp);
        et_location.setText(alamat);

        image_absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if ( ContextCompat.checkSelfPermission( list_absen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( list_absen.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                            11 );

                }
                else if ( ContextCompat.checkSelfPermission( list_absen.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( list_absen.this, new String[] {  Manifest.permission.CAMERA  },
                            12 );

                }
                else {
                    pilihImageByCamera();
                }
            }

        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }

        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(alamat.equals("Address Undetected")){
                    Toast.makeText(getApplicationContext(), "Aktifkan GPS Anda ", Toast.LENGTH_LONG).show();
                }
                else if(et_location.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Aktifkan GPS Anda ", Toast.LENGTH_LONG).show();
                }
                else if(image_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Lengkapi Foto Check In", Toast.LENGTH_LONG).show();
                }
                else {
                    proc_save_absensi(tglStamp, timeStamp, dialog);
                }
            }

        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showCheckOutDialog(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_out_absen);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button spn_from_date = (Button) dialog.findViewById(R.id.spn_from_date);
        final Button spn_from_time = (Button) dialog.findViewById(R.id.spn_from_time);
        final EditText et_location = (EditText) dialog.findViewById(R.id.et_location);
        final Button bt_save = (Button) dialog.findViewById(R.id.bt_save);
        final ImageButton bt_close = dialog.findViewById(R.id.bt_close);

        tglStamp = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        timeStamp = new SimpleDateFormat("HH:mm",
                Locale.getDefault()).format(new Date());

        spn_from_date.setText(tglStamp);
        spn_from_time.setText(timeStamp);
        et_location.setText(alamat);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }

        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                proc_update_absensi(id,tglStamp,timeStamp,dialog);
            }

        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

        image_name="absen_" + timeStamp + ".png";
        destFile = new File(file, "absen_" + timeStamp + ".png");
        imageCaptureUri = Uri.fromFile(destFile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void proc_update_absensi(final String idAbsent,final String tanggal,final String waktu,final Dialog dialog){

        final String time_check_out = tanggal+" "+waktu;
        //Toast.makeText(getApplicationContext(), time_check_in, Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(list_absen.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_update_absen, new Response.Listener<String>() {

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
                        dialog.dismiss();

                        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Delete list work report", "Delete list work report on "+tgl_report,id_report);
                        progressDialog.dismiss();
                        callVolley(tahun,bulan,id_user,id_company);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "save absen Error: " + error.getMessage());
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idAbsent);
                params.put("time_check_out", time_check_out);
                params.put("address_check_out", alamat);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("update_by", id_user);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

        //end proses
        progressDialog.dismiss();


    }

    private void proc_save_absensi(final String tanggal,final String waktu,final Dialog dialog){

        final String time_check_in = tanggal+" "+waktu;
        //Toast.makeText(getApplicationContext(), time_check_in, Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(list_absen.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_absen, new Response.Listener<String>() {

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
                        dialog.dismiss();

                        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Delete list work report", "Delete list work report on "+tgl_report,id_report);

                        if(image_name.equals("")) {
                            progressDialog.dismiss();
                            callVolley(tahun,bulan,id_user,id_company);
                        }
                        else{
                            save_foto();
                        }


                    }
                    else{
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "save absen Error: " + error.getMessage());
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", id_user);
                params.put("time_check_in", time_check_in);
                params.put("address_check_in", alamat);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("id_company", id_company);
                params.put("create_by", id_user);
                params.put("absen_foto", image_name);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

        //end proses
        progressDialog.dismiss();


    }

    private void callVolley(final String thn,final String bln,final String idUser,final String idCompany) {

        final ProgressDialog progressDialog = new ProgressDialog(list_absen.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        listItemArrayList.clear();
        textNoData.setVisibility(View.GONE);

        String url_server = url_view_absen + thn + "/" + bln + "/" +idUser +"/" +idCompany ;
        Log.d("url_absen", url_server);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if(response.length()>0) {
                    //Log.d(TAG, "total "+response.length());
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            //header;
                            HeaderItemAbsen itemHeader = new HeaderItemAbsen();
                            itemHeader.settime_check_in(obj.getString("tanggal"));
                            itemHeader.set_hari_time_check_in(obj.getString("hari"));
                            listItemArrayList.add(itemHeader);

                            JSONArray jsonSKU = obj.getJSONArray("list_absent");
                            if (jsonSKU.length() > 0) {
                                for (int j = 0; j < jsonSKU.length(); j++) {
                                    ChildItemAbsen itemChild = new ChildItemAbsen();

                                    JSONObject objChild = jsonSKU.getJSONObject(j);

                                    itemChild.setid(objChild.getString("id"));
                                    itemChild.settime_check_in(objChild.getString("time_check_in"));
                                    itemChild.settime_check_out(objChild.getString("time_check_out"));
                                    itemChild.setaddress_check_in(objChild.getString("address_check_in"));
                                    itemChild.setaddress_check_out(objChild.getString("address_check_out"));
                                    itemChild.setstatus_absensi(objChild.getString("status_absensi"));
                                    itemChild.setid_user(objChild.getString("create_by"));
                                    itemChild.setfoto_user(objChild.getString("foto_user"));
                                    itemChild.setimage_absen(objChild.getString("image_absen"));
                                    itemChild.set_hari_time_check_in("");

                                    listItemArrayList.add(itemChild);

                                }
                            }



                            // menambah item ke array
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }
                else{
                    textNoData.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }


                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);

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

        private ProgressDialog pd = new ProgressDialog(list_absen.this);

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
            callVolley(tahun,bulan,id_user,id_company);


        }

    }

}
