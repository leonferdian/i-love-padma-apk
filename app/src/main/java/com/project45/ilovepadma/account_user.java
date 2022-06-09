package com.project45.ilovepadma;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.add_deskripsi_complain;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.setting.main_setting;
import com.project45.ilovepadma.timeline.detail_foto;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class account_user extends AppCompatActivity {

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_EMAIL = "email";

    private static final String TAG = account_user.class.getSimpleName();

    private String url_post_edit = Server.URL + "update_detail_user";
    public static String url_image_upload = Server.URL2 +"image_upload/upload_foto_pribadi.php";
    private static String url_post_logout_user   = Server.URL + "survey_45/post_logout_user";

    String id_user, username,bm,email,jenis_crop="";
    int success,last_id;

    EditText txt_reg_nama, txt_reg_email,txt_nik,txt_phone_user,txt_atasan,txt_address,txt_ktp,txt_kode_user,txt_tgl_lahir;
    CircleImageView mImageUser;
    Button btn_logout;
    TextView txt_det_pict;

    private String picture_pribadi_path = "",personal_foto = "";

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private Uri mCropImageUri;
    private Bitmap bitmap;
    String ba1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_user);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_account_user);
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

        txt_reg_nama = findViewById(R.id.txt_reg_nama);
        txt_reg_email = findViewById(R.id.txt_reg_email);
        txt_nik = findViewById(R.id.txt_nik);
        txt_phone_user = findViewById(R.id.txt_phone_user);
        txt_atasan = findViewById(R.id.txt_atasan);
        txt_address = findViewById(R.id.txt_address);
        txt_ktp = findViewById(R.id.txt_ktp);
        txt_kode_user = findViewById(R.id.txt_kode_user);
        txt_tgl_lahir = findViewById(R.id.txt_tgl_lahir);
        mImageUser = findViewById(R.id.image_user);
        btn_logout = findViewById(R.id.btn_logout);
        txt_det_pict = findViewById(R.id.txt_det_pict);

        txt_reg_nama.setText(username);
        txt_reg_email.setText(email);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        get_detail_user(id_user);

        txt_tgl_lahir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_lahir = "filter1";
                showDateDialog(tgl_lahir,txt_tgl_lahir);

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                logout_user(id_user);
                // update login session ke FALSE dan mengosongkan nilai id dan username

                SharedPreferences.Editor editor = Api.sharedpreferences.edit();
                editor.putBoolean(login_form.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.putString(TAG_EMAIL, null);
                editor.putString(Api.TAG_COMPANY_USER_ID, null);
                editor.putString(Api.TAG_COMPANY_USER_NAME, null);
                editor.commit();

                Intent intent = new Intent(account_user.this, page_choice.class);
                finish();
                startActivity(intent);
            }
        });

        txt_det_pict.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), detail_foto.class);
                intent.putExtra("sku", "Photo Profil");
                intent.putExtra("file_foto", personal_foto);
                intent.putExtra("tgl_buat", "");
                intent.putExtra("act", "photo_profil");
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
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
            window.setStatusBarColor(getResources().getColor(R.color.blue_color));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_user, menu);

       return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.action_save) {
            post_update_user();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDateDialog(final String absen, final EditText tgl_awal){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                if(absen.equals("filter1"))
                {
                    tgl_awal.setText(dateFormatter.format(newDate.getTime()));
                }


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
        jenis_crop = "foto_pribadi";

    }

    public void post_update_user() {
        final String nik = txt_nik.getText().toString();
        final String phone = txt_phone_user.getText().toString();
        final String email_atasan = txt_atasan.getText().toString();
        final String ktp = txt_ktp.getText().toString();
        final String alamat = txt_address.getText().toString();
        final String tgl_lahir = txt_tgl_lahir.getText().toString();
        final String email_user = email;

        final ProgressDialog progressDialog = new ProgressDialog(account_user.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Prosess Update...");
        progressDialog.show();

        //proses save to database using api
        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_edit, new Response.Listener<String>() {

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
                        if(!picture_pribadi_path.equals("")){
                            upload_foto_pribadi();
                            //Toast.makeText(account_user.this,"success 2: "+String.valueOf(last_id), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(account_user.this,"Update success ", Toast.LENGTH_LONG).show();

                        }



                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("phone", phone);
                params.put("email_atasan", email_atasan);
                params.put("ktp", ktp);
                params.put("alamat", alamat);
                params.put("email_user", email_user);
                params.put("tgl_lahir", tgl_lahir);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
        //end proses
        progressDialog.dismiss();


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
                        String tgl_lahir  = jObj.getString("tgl_lahir");
                        //Toast.makeText(account_user.this, jabatan, Toast.LENGTH_LONG).show();
                        txt_address.setText(alamat);
                        txt_phone_user.setText(phone);
                        txt_nik.setText(nik);
                        txt_ktp.setText(ktp);
                        txt_kode_user.setText(kode_user);
                        txt_atasan.setText(email_atasan);
                        txt_tgl_lahir.setText(tgl_lahir);

                        if(!foto_pribadi.equals("")){
                            personal_foto = foto_pribadi;
                            bm =Server.URL2 +"image_upload/list_foto_pribadi/"+foto_pribadi;
                            Picasso.with(getApplicationContext())
                                    .load(bm)
                                    //.fit()
                                    .resize(100, 100)
                                    .onlyScaleDown()
                                    .centerCrop()
                                    .into(mImageUser);
                        }
                        else{
                            Picasso.with(getApplicationContext())
                                    .load(R.mipmap.ic_add_photo_black)
                                    .fit()
                                    .centerCrop()
                                    .into(mImageUser);
                        }
                        //adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(account_user.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(account_user.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private void logout_user(final String userId) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_logout_user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();



            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", userId);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
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
//                mTextImage.setText(result.getUri().getPath());

                //String jenis_crop = data.getStringExtra("jenis_crop");
                //Toast.makeText(account_user.this,"jeniscrop: " +String.valueOf(jenis_crop), Toast.LENGTH_LONG).show();


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (jenis_crop == "foto_pribadi") {
                    uploadPhoto(result.getUri().getPath());
                    //Setting the Bitmap to ImageView
                    mImageUser.setImageBitmap(bitmap);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }


    }

    private void uploadPhoto(final String path) {
        picture_pribadi_path = path;
        //Toast.makeText(account_user.this,"Test Upload: " +String.valueOf(path), Toast.LENGTH_LONG).show();
    }

    private void startCropImageActivity(Uri imageUri) {
        /*String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = account_user.this.getContentResolver().query(imageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
                */
        //Toast.makeText(account_user.this,String.valueOf(imageUri), Toast.LENGTH_LONG).show();
        CropImage.activity(imageUri)
                .setRequestedSize(500, 500, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setInitialCropWindowPaddingRatio(0)
                .start(this);
    }

    private void upload_foto_pribadi() {
        Log.e("path", "----------------" + picture_pribadi_path);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picture_pribadi_path);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        //ba1 = Base64.encodeBytes(ba);
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new uploadFoto_PribadiToServer().execute();

    }

    public class uploadFoto_PribadiToServer extends AsyncTask<Void, Void, String> {

        /* jika menggunakan parameter
        private int param1;
        public uploadFoto_PribadiToServer(int param1) {
            this.param1 = param1;
        }
        */

        private ProgressDialog pd = new ProgressDialog(account_user.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image  uploading! ");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("id_user", id_user));
            nameValuePairs.add(new BasicNameValuePair("ImageName", "img_"+System.currentTimeMillis() + ".jpg"));
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


        }

    }
}
