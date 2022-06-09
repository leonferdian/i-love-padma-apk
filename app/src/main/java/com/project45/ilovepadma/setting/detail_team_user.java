package com.project45.ilovepadma.setting;

import android.Manifest;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.login_form;
import com.project45.ilovepadma.page_choice;
import com.project45.ilovepadma.timeline.detail_foto;
import com.project45.ilovepadma.timeline.list_timeline_ilv;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class detail_team_user extends AppCompatActivity {

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_EMAIL = "email";

    private static final String TAG = detail_team_user.class.getSimpleName();


    private static String url_detail_user   = Server.URL + "get_detail_user/";
    private static String url_update_hak_akses_company    = Server.URL + "company/post_admin_company";
    private static String url_remove_hak_akses_company    = Server.URL + "company/post_remove_admin_company";

    String id_user, username,bm,email,jenis_crop="";
    int success,last_id;

    EditText txt_reg_nama, txt_reg_email,txt_nik,txt_phone_user,txt_atasan,txt_address,txt_ktp,txt_kode_user;
    CircleImageView mImageUser;
    TextView txt_det_pict;
    Button btn_invitation,btn_remove_admin;

    private String picture_pribadi_path = "",personal_foto = "",company_creator="",code_company="",status_hak_akses="";

    private Uri mCropImageUri;
    private Bitmap bitmap;
    String ba1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_team_user);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_account_user);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        changeStatusBarColor();

        //locationMangaer = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        //id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
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
        mImageUser = findViewById(R.id.image_user);
        txt_det_pict = findViewById(R.id.txt_det_pict);
        btn_invitation = findViewById(R.id.btn_invitation);
        btn_remove_admin = findViewById(R.id.btn_remove_admin);


        Intent intentku = getIntent(); // gets the previously created intent
        id_user = intentku.getStringExtra("id_user");
        company_creator = intentku.getStringExtra("company_creator");
        code_company = intentku.getStringExtra("code_company");
        status_hak_akses = intentku.getStringExtra("status_hak_akses");
        get_detail_user(id_user);
        if(company_creator!=null) {
            if (company_creator.equals(Api.sharedpreferences.getString(Api.TAG_ID, null))) {
                if(status_hak_akses.equals("administrator")){
                    btn_remove_admin.setVisibility(View.VISIBLE);
                }
                else {
                    btn_invitation.setVisibility(View.VISIBLE);
                }
            }
        }


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

        btn_invitation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                proc_hak_akses_company();
            }
        });

        btn_remove_admin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                proc_remove_akses_company();
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
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_save_user, menu);

       return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.action_save) {
            //post_update_user();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
        jenis_crop = "foto_pribadi";

    }



     private void get_detail_user(final String id_user){

         final ProgressDialog progressDialog = new ProgressDialog(detail_team_user.this,
                 R.style.AppTheme_Dark_Dialog);
         progressDialog.setIndeterminate(true);
         progressDialog.setMessage("Loading");
         progressDialog.show();

        String url_edit       = url_detail_user+id_user;
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
                        String email_user      = jObj.getString("email_user");
                        String nama_user    = jObj.getString("nama_user");
                        String alamat      = jObj.getString("alamat");
                        String phone    = jObj.getString("phone");
                        String ktp  = jObj.getString("ktp");
                        String kode_user  = jObj.getString("kode_user");
                        String foto_pribadi  = jObj.getString("foto_pribadi");
                        String nik  = jObj.getString("nik");
                        String email_atasan  = jObj.getString("email_atasan");
                        //Toast.makeText(account_user.this, jabatan, Toast.LENGTH_LONG).show();
                        txt_reg_nama.setText(nama_user);
                        txt_reg_email.setText(email_user);
                        txt_address.setText(alamat);
                        txt_phone_user.setText(phone);
                        txt_nik.setText(nik);
                        txt_ktp.setText(ktp);
                        txt_kode_user.setText(kode_user);
                        txt_atasan.setText(email_atasan);

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
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(detail_team_user.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(detail_team_user.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

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

    private void proc_hak_akses_company(){

        final ProgressDialog progressDialog = new ProgressDialog(detail_team_user.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update_hak_akses_company, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("update", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        //end proses
                        progressDialog.dismiss();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("hasBackPressed",true);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

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
                Log.e(TAG, "update company Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("code_company", code_company);
                params.put("id_user", id_user);

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

    private void proc_remove_akses_company(){

        final ProgressDialog progressDialog = new ProgressDialog(detail_team_user.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_remove_hak_akses_company, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("update", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        //end proses
                        progressDialog.dismiss();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("hasBackPressed",true);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

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
                Log.e(TAG, "update company Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("code_company", code_company);
                params.put("id_user", id_user);

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
