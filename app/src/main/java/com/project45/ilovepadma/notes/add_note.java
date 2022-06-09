package com.project45.ilovepadma.notes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.project.FileUtils;
import com.project45.ilovepadma.project.add_project;
import com.project45.ilovepadma.util.Server;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class add_note extends AppCompatActivity {

    String id_user, username,bm,email,jabatan;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_project.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static String url_save_note    = Server.URL + "note/post_save_note";
    private static String url_list_member_project    = Server.URL + "project/list_member_project/";
    public static String url_file_upload = Server.URL2 +"file_pendukung/upload_file_note.php";

    EditText txt_tgl_note,txt_subject_note,txt_isi_note;
    Spinner spinner_jenis_note;
    MultiAutoCompleteTextView multi_member_note;
    Button btn_file;
    TextView txt_file;

    int randomNumber;
    String id_company="",nama_company="";
    String id_note = "",timeStamp="",tanggal_note="",act_note="",id_member_project="";

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private ArrayList<Data_work_report> list_member_project ;

    private int PICK_PDF_REQUEST = 1;
    private Uri filePathFile;

    String[] mimeTypes =
            {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                    "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                    "text/plain",
                    "application/pdf",
                    "application/zip"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

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

        txt_tgl_note = findViewById(R.id.txt_tgl_note);
        txt_subject_note = findViewById(R.id.txt_subject_note);
        multi_member_note = findViewById(R.id.multi_member_note);
        txt_isi_note = findViewById(R.id.txt_isi_note);
        spinner_jenis_note = findViewById(R.id.spinner_jenis_note);
        btn_file = findViewById(R.id.btn_file);
        txt_file = findViewById(R.id.txt_file);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        list_member_project = new ArrayList<Data_work_report>();

        Intent intentku = getIntent(); // gets the previously created intent
        act_note = intentku.getStringExtra("act");
        tanggal_note = intentku.getStringExtra("tanggal");

        timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());

        txt_tgl_note.setText(tanggal_note);

        txt_tgl_note.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                String tgl_absen = "filter1";
                showDateDialog(tgl_absen,txt_tgl_note);

            }
        });

        btn_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                showFileChooser1();
                /*
                Intent intent = new Intent(add_note.this, note_editor1.class);
                intent.putExtra("act", "edit_note");
                startActivityForResult(intent, 1);

                */

            }
        });

        get_id_note(timeStamp);
        getListMemberProject(id_company);
        /*
        for save note member
        @Override
            public void onClick(View view) {
                String text = multiAutoCompleteTextView.getText().toString();

                if(text != null && text.length() >0) {
                    text = text.substring(0, text.length() - 1);
                String countries[] = text.split(",");  // countries array will have all the countries entered in multiAutoCompleteTextView

                for(String s : countries) {
                    Toast.makeText(this,"Countries are = "+s,Toast.LENGTH_SHORT).show();
                }
                }
                else {
                   Toast.makeText(this,"Please enter text",Toast.LENGTH_SHORT).show();
               }

            }
        */

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

    private void get_id_note(final String tanggal){
        id_note =  id_user+"-"+tanggal+"-"+ String.valueOf(generateRandomNumbers(100001,999999));
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
        multi_member_note.setAdapter(dataAdapter);
        multi_member_note.setThreshold(2);
        multi_member_note.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_user, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            //Toast.makeText(getApplicationContext(), "tes: "+id_note, Toast.LENGTH_LONG).show();
            postNote();

        }

        return super.onOptionsItemSelected(item);
    }

    private void showFileChooser1() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
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

    private void postNote() {

        final String tgl_note = txt_tgl_note.getText().toString();
        final String jenis_note = String.valueOf(spinner_jenis_note.getSelectedItem());
        final String subject_note = txt_subject_note.getText().toString();
        final String isi_note = txt_isi_note.getText().toString().replace("\n", "<br>").trim();
        final String member = multi_member_note.getText().toString();

        String file_note = txt_file.getText().toString().trim();

        if(subject_note.equals("")){
            Toast.makeText(getApplicationContext(), "Subject note harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(isi_note.equals("")){
            Toast.makeText(getApplicationContext(), "Isi note harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(member.equals("")){
            Toast.makeText(getApplicationContext(), "Member harus diisi ", Toast.LENGTH_LONG).show();
        }
        else{

            final ProgressDialog progressDialog = new ProgressDialog(add_note.this,
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

            String id_member = multi_member_note.getText().toString().replace(", ", "~");
            String memberID[] = id_member.split("~");
            for(String idM : memberID) {
                //Toast.makeText(this,"idM = "+idM,Toast.LENGTH_SHORT).show();
                int spinnerPosition2 = dataAdapter.getPosition(idM);
                if(spinnerPosition2 != -1) {
                    id_member_project += list_member_project.get(spinnerPosition2).getid() + ",";
                }
                //Log.d("idM",String.valueOf(spinnerPosition2));
            }
            //id_member_project = id_member_project+" ";
            /* -------- */

            //proses save to database using api
            StringRequest strReq = new StringRequest(Request.Method.POST, url_save_note, new Response.Listener<String>() {

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

                            if(!file_note.equals("Nama File")){
                                uploadFileNote();
                                Log.d("file_note", file_note);
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
                    params.put("nmr_note", id_note);
                    params.put("tgl_note", tgl_note);
                    params.put("subject_note", subject_note);
                    params.put("jenis_note", jenis_note);
                    params.put("isi_note", isi_note);
                    params.put("member", id_member_project);
                    params.put("id_company", id_company);
                    params.put("create_by", id_user);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(strReq);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathFile = data.getData();
            String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String filename = "file" + id_user + "_" + timeStamp2;
            txt_file.setText(filename);
            Log.d("upload_error", filePathFile.toString());
        }
    }

    public void uploadFileNote() {
        //getting name for the image
        String name = txt_file.getText().toString().trim();

        if (name.equals("Nama File")) {

            Toast.makeText(this, "Pilih file", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code

            try {
                //getting the actual path of the image
                //String pathFlowMenu = FilePath.getPath(this, filePathFlowMenu);

                String pathFlowMenu = FileUtils.getReadablePathFromUri(this, filePathFile);
                String uploadId = UUID.randomUUID().toString();


                MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url_file_upload)
                        .addFileToUpload(pathFlowMenu, "pdf")
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("nmr_note", id_note)
                        .addParameter("create_by", id_user)
                        .addParameter("tipe_upload", "file_note")
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

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
