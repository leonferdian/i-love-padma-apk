package com.project45.ilovepadma.project;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.complain.add_complain;
import com.project45.ilovepadma.complain.add_respon_complain;
import com.project45.ilovepadma.data.Data_jenis_complain;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.setting.list_master_company;
import com.project45.ilovepadma.util.Server;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class add_project extends AppCompatActivity {

    String id_user, username,bm,email,jabatan;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_project.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    public static String url_file_upload = Server.URL2 +"file_pendukung/upload_file_project.php";
    private static String url_list_divisi    = Server.URL + "project/list_divisi";
    private static String url_list_member_project    = Server.URL + "project/list_member_project/";
    private static String url_save_project    = Server.URL + "project/post_save_project";

    Spinner spinner_jenis_project,spinner_divisi;
    EditText txt_nama_project,txt_main_project,txt_sub_project;
    MultiAutoCompleteTextView multi_member_project;
    Button btn_flow_menu,btn_flow_design,btn_flow_output,btn_korelasi_table;
    Button btn_tujuan,btn_fiat,btn_flow,btn_presentasi;
    TextView txt_flow_menu,txt_flow_design,txt_flow_output,txt_korelasi_table;
    TextView txt_tujuan,txt_fiat,txt_flow,txt_presentasi;
    LinearLayout layout_file,layout_file2,layout_file3,layout_file4;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1,PICK_PDF_REQUEST2 = 2,PICK_PDF_REQUEST3 = 3,PICK_PDF_REQUEST4 = 4;
    private int PICK_PDF_REQUEST5 = 5,PICK_PDF_REQUEST6 = 6,PICK_PDF_REQUEST7 = 7,PICK_PDF_REQUEST8 = 8;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePathFlowMenu,filePathFlowDesign,filePathFlowOutput,filePathKorelasiTable;
    private Uri filePathTujuan,filePathFiat,filePathFlow,filePathPresentasi;

    int randomNumber;
    String id_project = "",timeStamp="",id_divisi = "",nama_divisi = "";
    String id_company="",nama_company="",id_member_project="";

    private ArrayList<Data_work_report> list_divisi,list_member_project ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_setting);
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

        spinner_jenis_project = findViewById(R.id.spinner_jenis_project);
        spinner_divisi = findViewById(R.id.spinner_divisi);
        txt_nama_project = findViewById(R.id.txt_nama_project);
        txt_main_project = findViewById(R.id.txt_main_project);
        txt_sub_project = findViewById(R.id.txt_sub_project);
        multi_member_project = findViewById(R.id.multi_member_project);
        btn_flow_menu = findViewById(R.id.btn_flow_menu);
        btn_flow_design = findViewById(R.id.btn_flow_design);
        btn_flow_output = findViewById(R.id.btn_flow_output);
        btn_korelasi_table = findViewById(R.id.btn_korelasi_table);
        btn_tujuan = findViewById(R.id.btn_tujuan);
        btn_fiat = findViewById(R.id.btn_fiat);
        btn_flow = findViewById(R.id.btn_flow);
        btn_presentasi = findViewById(R.id.btn_presentasi);
        txt_flow_menu= findViewById(R.id.txt_flow_menu);
        txt_flow_design = findViewById(R.id.txt_flow_design);
        txt_flow_output = findViewById(R.id.txt_flow_output);
        txt_korelasi_table = findViewById(R.id.txt_korelasi_table);
        txt_tujuan = findViewById(R.id.txt_tujuan);
        txt_fiat = findViewById(R.id.txt_fiat);
        txt_flow = findViewById(R.id.txt_flow);
        txt_presentasi = findViewById(R.id.txt_presentasi);
        layout_file = findViewById(R.id.layout_file);
        layout_file2 = findViewById(R.id.layout_file2);
        layout_file3 = findViewById(R.id.layout_file3);
        layout_file4 = findViewById(R.id.layout_file4);

        list_divisi = new ArrayList<Data_work_report>();
        list_member_project = new ArrayList<Data_work_report>();

        timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());
        get_id_project(timeStamp);

        btn_flow_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser1();
            }
        });

        btn_flow_design.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser2();
            }
        });

        btn_flow_output.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser3();
            }
        });

        btn_korelasi_table.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser4();
            }
        });

        btn_tujuan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser5();
            }
        });

        btn_fiat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser6();
            }
        });

        btn_flow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser7();
            }
        });

        btn_presentasi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFileChooser8();
            }
        });

        spinner_jenis_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    //Toast.makeText(getApplicationContext(), "jenis: "+String.valueOf(item.toString()), Toast.LENGTH_LONG).show();
                    if(item.equals("Reguler")){
                        layout_file.setVisibility(View.GONE);
                        layout_file2.setVisibility(View.GONE);
                        layout_file3.setVisibility(View.VISIBLE);
                        layout_file4.setVisibility(View.VISIBLE);
                    }
                    else if(item.equals("Digitalisasi")){
                        layout_file.setVisibility(View.VISIBLE);
                        layout_file2.setVisibility(View.VISIBLE);
                        layout_file3.setVisibility(View.GONE);
                        layout_file4.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinner_divisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Data_work_report sel = getDivisi().get(position);
                    id_divisi = String.valueOf(sel.getid());
                    nama_divisi = String.valueOf(sel.getdivisi());
                    //Toast.makeText(getApplicationContext(), "rute "+String.valueOf(rute), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        getListDivisi();
        getListMemberProject(id_company);
        requestStoragePermission();

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
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_user, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            //Toast.makeText(getApplicationContext(), "file: "+filePathFlowMenu, Toast.LENGTH_LONG).show();
            //uploadFlowDesign();
            postComplain();
            //testGetIDMember();
        }

        return super.onOptionsItemSelected(item);
    }

    //method to show file chooser
    private void showFileChooser1() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    private void showFileChooser2() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST2);
    }

    private void showFileChooser3() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST3);
    }

    private void showFileChooser4() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST4);
    }

    private void showFileChooser5() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST5);
    }

    private void showFileChooser6() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST6);
    }

    private void showFileChooser7() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST7);
    }

    private void showFileChooser8() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST8);
    }

    public void uploadFlowMenu() {
        //getting name for the image
        String name = txt_flow_menu.getText().toString().trim();

        if (name.equals("Flow Menu")) {

            Toast.makeText(this, "Pilih file flow menu", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code

            try {
                //getting the actual path of the image
                //String pathFlowMenu = FilePath.getPath(this, filePathFlowMenu);

                String pathFlowMenu = FileUtils.getReadablePathFromUri(this, filePathFlowMenu);
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                /*
                new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .setMethod("POST")
                        .addFileToUpload(pathFlowMenu, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "flow_menu")
                        //.setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

                 */
                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathFlowMenu, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "flow_menu")
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

    public void uploadFlowDesign() {
        //getting name for the image
        String name = txt_flow_design.getText().toString().trim();

        if (name.equals("Flow Design")) {

            Toast.makeText(this, "Pilih file flow design", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                //getting the actual path of the image
                String pathFlowDesign = FileUtils.getReadablePathFromUri(this, filePathFlowDesign);
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathFlowDesign, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "flow_design")
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
                Log.d("upload_error", exc.getMessage());
            }
        }
    }

    public void uploadFlowOutput() {
        //getting name for the image
        String name = txt_flow_output.getText().toString().trim();

        if (name.equals("Flow Output")) {

            Toast.makeText(this, "Pilih file flow output", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                //getting the actual path of the image
                String pathFlowOutput = FileUtils.getReadablePathFromUri(this, filePathFlowOutput);
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathFlowOutput, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "flow_output")
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
            }
        }
    }

    public void uploadKorTable() {
        //getting name for the image
        String name = txt_korelasi_table.getText().toString().trim();

        if (name.equals("Korelasi Table")) {

            Toast.makeText(this, "Pilih file korelasi table", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                //getting the actual path of the image
                String pathKorelasiTable = FileUtils.getReadablePathFromUri(this, filePathKorelasiTable);
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathKorelasiTable, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "table_korelasi")
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
            }
        }
    }

    public void uploadTujuan() {
        //getting name for the image
        String name = txt_tujuan.getText().toString().trim();

        if (name.equals("Tujuan")) {

            Toast.makeText(this, "Pilih file Tujuan", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {

                String pathTujuan = FileUtils.getReadablePathFromUri(this, filePathTujuan);
                String uploadId = UUID.randomUUID().toString();

                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathTujuan, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "flow_menu")
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

    public void uploadFiat() {
        //getting name for the image
        String name = txt_fiat.getText().toString().trim();

        if (name.equals("Fiat")) {

            Toast.makeText(this, "Pilih file Fiat", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {

                String pathFiat = FileUtils.getReadablePathFromUri(this, filePathFiat);
                String uploadId = UUID.randomUUID().toString();

                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathFiat, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "flow_design")
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

    public void uploadFlow() {
        //getting name for the image
        String name = txt_flow.getText().toString().trim();

        if (name.equals("Flow")) {

            Toast.makeText(this, "Pilih file Flow", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {

                String pathFlow = FileUtils.getReadablePathFromUri(this, filePathFlow);
                String uploadId = UUID.randomUUID().toString();

                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathFlow, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "flow_output")
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

    public void uploadPresentasi() {
        //getting name for the image
        String name = txt_presentasi.getText().toString().trim();

        if (name.equals("Presentasi")) {

            Toast.makeText(this, "Pilih file Presentasi", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {

                String pathPresentasi = FileUtils.getReadablePathFromUri(this, filePathPresentasi);
                String uploadId = UUID.randomUUID().toString();

                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathPresentasi, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_project", id_project)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "table_korelasi")
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFlowMenu = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "flowMenu" + id_user + "_" + timeStamp2;
            txt_flow_menu.setText(filename);
            Log.d("upload_error", filePathFlowMenu.toString());
        }
        else if (requestCode == PICK_PDF_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFlowDesign = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "flowDesign" + id_user + "_" + timeStamp2;
            txt_flow_design.setText(filename);
        }
        else if (requestCode == PICK_PDF_REQUEST3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFlowOutput = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "flowOutput" + id_user + "_" + timeStamp2;
            txt_flow_output.setText(filename);
        }
        else if (requestCode == PICK_PDF_REQUEST4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathKorelasiTable = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "korTbl" + id_user + "_" + timeStamp2;
            txt_korelasi_table.setText(filename);
        }
        else if (requestCode == PICK_PDF_REQUEST5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathTujuan = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "tujuan" + id_user + "_" + timeStamp2;
            txt_tujuan.setText(filename);
        }
        else if (requestCode == PICK_PDF_REQUEST6 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFiat = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "fiat" + id_user + "_" + timeStamp2;
            txt_fiat.setText(filename);
        }
        else if (requestCode == PICK_PDF_REQUEST7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFlow = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "flow" + id_user + "_" + timeStamp2;
            txt_flow.setText(filename);
        }
        else if (requestCode == PICK_PDF_REQUEST8 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathPresentasi = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "presentasi" + id_user + "_" + timeStamp2;
            txt_presentasi.setText(filename);
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void get_id_project(final String tanggal){
        id_project =  id_user+"-"+tanggal+"-"+ String.valueOf(generateRandomNumbers(100001,999999));
        //getDeskripsi(id_complain);
        //Toast.makeText(getApplicationContext(), " id_survey "+id_survey, Toast.LENGTH_LONG).show();
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

    private void getListDivisi() {

        final ProgressDialog progressDialog = new ProgressDialog(add_project.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Get Divisi...");
        progressDialog.show();

        String url_server = url_list_divisi;
        Log.d("url_divisi", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Data_work_report rute = new Data_work_report();
                            rute.setid(obj.getString("id"));
                            rute.setdivisi(obj.getString("nama_divisi"));
                            list_divisi.add(rute);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();
                    addItemsOnSpinnerDivisi();
                    getDivisi();
                    //end proses
                    progressDialog.dismiss();
                }
                else{
                    //end proses
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();
            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
    }

    public void addItemsOnSpinnerDivisi() {
        List<String> list = new ArrayList<String>();
        /*list.add("list 1");
        list.add("list 2");
        list.add("list 3");*/
        //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();
        for (int i = 0; i < list_divisi.size(); i++) {
            list.add(list_divisi.get(i).getdivisi());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_divisi.setAdapter(dataAdapter);
    }

    public List<Data_work_report> getDivisi(){
        return list_divisi;
    }

    private void getListMemberProject(final String idCompany) {
        String url_server = url_list_member_project+idCompany;
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Data_work_report jenis = new Data_work_report();
                            jenis.setid(obj.getString("id"));
                            jenis.setnama_user(obj.getString("nama_user"));
                            list_member_project.add(jenis);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    addItemsOnSpinnerMemberProject();
                    //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();

                }
                else{

                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);


    }

    public void addItemsOnSpinnerMemberProject() {
        List<String> list = new ArrayList<String>();
        /*list.add("list 1");
        list.add("list 2");
        list.add("list 3");*/
        //Toast.makeText(add_complain.this, "size "+String.valueOf(jenis_complainList.size()), Toast.LENGTH_LONG).show();
        for (int i = 0; i < list_member_project.size(); i++) {
            list.add(list_member_project.get(i).getnama_user());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multi_member_project.setAdapter(dataAdapter);
        multi_member_project.setThreshold(2);
        multi_member_project.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        /*
        multi_member_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    Data_work_report sel = list_member_project.get(position);
                    id_member_project += sel.getid()+", ";
                    Toast.makeText(getApplicationContext(), "id_member_project "+sel.getid().replace(" ", ""), Toast.LENGTH_LONG).show();
                    Log.d("id_member_project",id_member_project.replace(" ", ""));
                }
            }
        });

         */
    }

    private void testGetIDMember(){
        List<String> list = new ArrayList<String>();
        id_member_project = "";
        for (int i = 0; i < list_member_project.size(); i++) {
            list.add(list_member_project.get(i).getnama_user());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String id_member = multi_member_project.getText().toString().replace(", ", "~");
        String memberID[] = id_member.split("~");
        for(String idM : memberID) {
            //Toast.makeText(this,"idM = "+idM,Toast.LENGTH_SHORT).show();
            int spinnerPosition2 = dataAdapter.getPosition(idM);
            if(spinnerPosition2 != -1) {
                id_member_project += list_member_project.get(spinnerPosition2).getid() + ",";
            }
            //Log.d("idM",String.valueOf(spinnerPosition2));
        }
        id_member_project = id_member_project+" ";
        //Toast.makeText(getApplicationContext(), "id_member_project "+id_member_project, Toast.LENGTH_LONG).show();
        Log.d("id_member_project",id_member_project);
    }
    private void postComplain(){

        final String nama_project = txt_nama_project.getText().toString();
        final String jenis_project = String.valueOf(spinner_jenis_project.getSelectedItem());
        final String divisi = nama_divisi;
        final String main_project = txt_main_project.getText().toString();
        final String sub_project = txt_sub_project.getText().toString();
        final String member = multi_member_project.getText().toString();

        String flow_menu = txt_flow_menu.getText().toString().trim();
        String flow_design = txt_flow_design.getText().toString().trim();
        String flow_output = txt_flow_output.getText().toString().trim();
        String korelasi_table = txt_korelasi_table.getText().toString().trim();

        String tujuan = txt_tujuan.getText().toString().trim();
        String fiat = txt_fiat.getText().toString().trim();
        String flow = txt_flow.getText().toString().trim();
        String presentasi = txt_presentasi.getText().toString().trim();

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

        Geocoder geocoder = new Geocoder(add_project.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);

        if(nama_project.equals("")){
            Toast.makeText(getApplicationContext(), "Nama project harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(main_project.equals("")){
            Toast.makeText(getApplicationContext(), "Main project harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(member.equals("")){
            Toast.makeText(getApplicationContext(), "Member harus diisi ", Toast.LENGTH_LONG).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(add_project.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();

            /* mendapatkan id member*/
            List<String> list = new ArrayList<String>();
            id_member_project = "";
            for (int i = 0; i < list_member_project.size(); i++) {
                list.add(list_member_project.get(i).getnama_user());
                //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            String id_member = multi_member_project.getText().toString().replace(", ", "~");
            String memberID[] = id_member.split("~");
            for(String idM : memberID) {
                //Toast.makeText(this,"idM = "+idM,Toast.LENGTH_SHORT).show();
                int spinnerPosition2 = dataAdapter.getPosition(idM);
                if(spinnerPosition2 != -1) {
                    id_member_project += list_member_project.get(spinnerPosition2).getid() + ",";
                }
                //Log.d("idM",String.valueOf(spinnerPosition2));
            }
            id_member_project = id_member_project+" ";
            /* -------- */

            //proses save to database using api
            StringRequest strReq = new StringRequest(Request.Method.POST, url_save_project, new Response.Listener<String>() {

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
                            // Digitalisasi
                            if(!flow_menu.equals("Flow Menu")){
                                uploadFlowMenu();
                                Log.d("flow", flow_menu);
                            }
                            if(!flow_design.equals("Flow Design")){
                                uploadFlowDesign();
                                Log.d("flow", flow_design);
                            }
                            if(!flow_output.equals("Flow Output")){
                                uploadFlowOutput();
                                Log.d("flow", flow_output);
                            }
                            if(!korelasi_table.equals("Korelasi Table")){
                                uploadKorTable();
                                Log.d("flow", korelasi_table);
                            }

                            // Reguler
                            if(!tujuan.equals("Tujuan")){
                                uploadTujuan();
                                Log.d("tujuan", tujuan);
                            }
                            if(!fiat.equals("Fiat")){
                                uploadFiat();
                                Log.d("Fiat", fiat);
                            }
                            if(!flow.equals("Flow")){
                                uploadFlow();
                                Log.d("Flow", flow);
                            }
                            if(!presentasi.equals("Presentasi")){
                                uploadPresentasi();
                                Log.d("presentasi", presentasi);
                            }

                            //end proses
                            progressDialog.dismiss();
                            onBackPressedWithProcess();


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
                    params.put("nmr_project", id_project);
                    params.put("nama_project", nama_project);
                    params.put("jenis_project", jenis_project);
                    params.put("divisi", divisi);
                    params.put("main_project", main_project);
                    params.put("sub_project", sub_project);
                    params.put("member", id_member_project);
                    params.put("id_company", id_company);
                    params.put("create_by", id_user);
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("address", address);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(strReq);
        }

    }

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


}
