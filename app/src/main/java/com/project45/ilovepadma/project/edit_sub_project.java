package com.project45.ilovepadma.project;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class edit_sub_project extends AppCompatActivity {

    String id_user, username,bm,email,jabatan;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = edit_sub_project.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    public static String url_file_upload = Server.URL2 +"file_pendukung/upload_file_sub_project.php";
    private static String url_list_divisi    = Server.URL + "project/list_divisi";
    private static String url_list_member_project    = Server.URL + "project/list_member_project/";
    private static String url_save_project    = Server.URL + "project/post_update_sub_project";
    private static String url_del_subproject    = Server.URL + "project/post_del_subproject";

    Spinner spinner_jenis_project,spinner_divisi;
    EditText txt_nama_project,txt_main_project,txt_sub_project;
    MultiAutoCompleteTextView multi_member_project;
    Button btn_flow_menu,btn_flow_design,btn_flow_output,btn_korelasi_table;
    TextView txt_flow_menu,txt_flow_design,txt_flow_output,txt_korelasi_table;
    LinearLayout layout_file,layout_file2;
    Button btn_del;
    TextView txt_file_flow_menu,txt_file_flow_design,txt_file_flow_output,txt_file_table_korelasi;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1,PICK_PDF_REQUEST2 = 2,PICK_PDF_REQUEST3 = 3,PICK_PDF_REQUEST4 = 4;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePathFlowMenu,filePathFlowDesign,filePathFlowOutput,filePathKorelasiTable;

    int randomNumber;
    String id_project = "",timeStamp="",id_divisi = "",nama_divisi = "",nmr_sub_project="";
    String id_company="",nama_company="",nmr_project = "",jenis_project="",divisi="",nama_project="",main_project="",createBy="",id_sub_project="",
            sub_project ="",member="",flow_menu="",flow_design="",flow_output="",table_korelasi="";

    private ArrayList<Data_work_report> list_divisi,list_member_project ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_sub_project);

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
        txt_flow_menu= findViewById(R.id.txt_flow_menu);
        txt_flow_design = findViewById(R.id.txt_flow_design);
        txt_flow_output = findViewById(R.id.txt_flow_output);
        txt_korelasi_table = findViewById(R.id.txt_korelasi_table);
        layout_file = findViewById(R.id.layout_file);
        layout_file2 = findViewById(R.id.layout_file2);
        btn_del = findViewById(R.id.btn_del);
        txt_file_flow_menu = findViewById(R.id.txt_file_flow_menu);
        txt_file_flow_design = findViewById(R.id.txt_file_flow_design);
        txt_file_flow_output = findViewById(R.id.txt_file_flow_output);
        txt_file_table_korelasi = findViewById(R.id.txt_file_table_korelasi);

        list_divisi = new ArrayList<Data_work_report>();
        list_member_project = new ArrayList<Data_work_report>();

        spinner_jenis_project.setEnabled(false);
        spinner_divisi.setEnabled(false);
        txt_nama_project.setEnabled(false);
        txt_main_project.setEnabled(false);

        Intent intentku = getIntent(); // gets the previously created intent
        nmr_project = intentku.getStringExtra("nmr_project");
        jenis_project=intentku.getStringExtra("jenis_project");
        divisi=intentku.getStringExtra("divisi");
        nama_project=intentku.getStringExtra("nama_project");
        main_project=intentku.getStringExtra("main_project");
        createBy=intentku.getStringExtra("createBy");
        id_sub_project=intentku.getStringExtra("id_sub_project");
        nmr_sub_project = intentku.getStringExtra("nmr_sub_project");
        sub_project =intentku.getStringExtra("sub_project");
        member = intentku.getStringExtra("member");
        flow_menu = intentku.getStringExtra("flow_menu");
        flow_design = intentku.getStringExtra("flow_design");
        flow_output = intentku.getStringExtra("flow_output");
        table_korelasi = intentku.getStringExtra("table_korelasi");

        id_project = nmr_sub_project;

        //Toast.makeText(getApplicationContext(), "id: "+nmr_sub_project, Toast.LENGTH_LONG).show();

        if(createBy.equals(id_user)){
            btn_del.setVisibility(View.VISIBLE);
        }

        txt_nama_project.setText(nama_project);
        txt_main_project.setText(main_project);
        txt_sub_project.setText(sub_project);
        multi_member_project.setText(member+", ");

        txt_file_flow_menu.setText(flow_menu);
        txt_file_flow_design.setText(flow_design);
        txt_file_flow_output.setText(flow_output);
        txt_file_table_korelasi.setText(table_korelasi);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(edit_sub_project.this, R.array.slc_jenis_project, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (jenis_project != null) {
            int spinnerPosition = adapter3.getPosition(jenis_project);
            spinner_jenis_project.setSelection(spinnerPosition);
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());
        //get_id_project(timeStamp);

        btn_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DeleteAlert(id_sub_project);
            }
        });

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
                    }
                    else if(item.equals("Digitalisasi")){
                        layout_file.setVisibility(View.VISIBLE);
                        layout_file2.setVisibility(View.VISIBLE);
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

        txt_file_flow_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(txt_file_flow_menu.getText().equals("no file"))
                {
                    Toast.makeText(getApplicationContext(), "Flow menu tidak memiliki file", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent intent = new Intent(edit_sub_project.this, project_file_pendukung2.class);
                    intent.putExtra("namaFileFlowMenu", flow_menu);
                    intent.putExtra("act", "file_subproject");
                    startActivityForResult(intent, 1);
                }
               /*
                String pdf_url = Server.URL2 + "file_pendukung/project/"+namaFileFlowMenu;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                startActivity(browserIntent);

                */

                //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
            }
        });

        txt_file_flow_design.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(txt_file_flow_design.getText().equals("no file"))
                {
                    Toast.makeText(getApplicationContext(), "Flow design tidak memiliki file", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent intent = new Intent(edit_sub_project.this, project_file_pendukung2.class);
                    intent.putExtra("namaFileFlowMenu", flow_design);
                    intent.putExtra("act", "file_subproject");
                    startActivityForResult(intent, 1);
                }

            }
        });

        txt_file_flow_output.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(txt_file_flow_output.getText().equals("no file"))
                {
                    Toast.makeText(getApplicationContext(), "Flow output tidak memiliki file", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent intent = new Intent(edit_sub_project.this, project_file_pendukung2.class);
                    intent.putExtra("namaFileFlowMenu", flow_output);
                    intent.putExtra("act", "file_subproject");
                    startActivityForResult(intent, 1);
                }

            }
        });

        txt_file_table_korelasi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(txt_file_table_korelasi.getText().equals("no file"))
                {
                    Toast.makeText(getApplicationContext(), "Korelasi tabel tidak memiliki file", Toast.LENGTH_LONG).show();
                }
                else {

                    Intent intent = new Intent(edit_sub_project.this, project_file_pendukung2.class);
                    intent.putExtra("namaFileFlowMenu", table_korelasi);
                    intent.putExtra("act", "file_subproject");
                    startActivityForResult(intent, 1);
                }

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

        final ProgressDialog progressDialog = new ProgressDialog(edit_sub_project.this,
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
        if(!divisi.equals(""))
        {
            int spinnerPosition2 = dataAdapter.getPosition(divisi);
            spinner_divisi.setSelection(spinnerPosition2);
        }
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

        if(nama_project.equals("")){
            Toast.makeText(getApplicationContext(), "Nama project harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(main_project.equals("")){
            Toast.makeText(getApplicationContext(), "Main project To harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(sub_project.equals("")){
            Toast.makeText(getApplicationContext(), "Sub project harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(member.equals("")){
            Toast.makeText(getApplicationContext(), "Member harus diisi ", Toast.LENGTH_LONG).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(edit_sub_project.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();

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
                    params.put("id_sub_project", id_sub_project);
                    params.put("nmr_project", nmr_project);
                    params.put("nmr_sub_project", id_project);
                    params.put("nama_project", nama_project);
                    params.put("jenis_project", jenis_project);
                    params.put("divisi", divisi);
                    params.put("main_project", main_project);
                    params.put("sub_project", sub_project);
                    params.put("member", member);
                    params.put("id_company", id_company);
                    params.put("create_by", id_user);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(strReq);
        }

    }

    private void DeleteAlert(final String idSubProject){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Sub Projek");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ? ")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                /*.setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something
                        dialogInterface.cancel();
                    }
                })*/
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        proc_delete_project(idSubProject);
                        dialog.cancel();

                    }
                })

                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });


        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void proc_delete_project(final String idSubProject){

        final ProgressDialog progressDialog = new ProgressDialog(edit_sub_project.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_del_subproject, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.d("Delete", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        onBackPressedWithProcess();
                        //end proses
                        progressDialog.dismiss();

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
                Log.e(TAG, "Delete company Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_subproject", idSubProject);


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

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


}
