package com.project45.ilovepadma.notes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.data.Data_note;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.project.FileUtils;
import com.project45.ilovepadma.project.add_project;
import com.project45.ilovepadma.project.edit_project;
import com.project45.ilovepadma.project.project_file_pendukung;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class edit_note extends AppCompatActivity {

    String id_user, username,bm,email,jabatan;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_project.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static String url_save_note    = Server.URL + "note/post_update_note";
    private static String url_del_note    = Server.URL + "note/post_delete_note";
    private static String url_list_member_project    = Server.URL + "project/list_member_project/";
    private static String url_update_status_note    = Server.URL + "note/post_update_status_note";
    public static String url_file_upload = Server.URL2 +"file_pendukung/upload_file_note.php";
    private static String url_get_isi_note    = Server.URL + "note/get_isi_note/";

    EditText txt_tgl_note,txt_subject_note,txt_isi_note;
    Spinner spinner_jenis_note;
    MultiAutoCompleteTextView multi_member_note;
    Button btn_del,btn_change_status;
    TextView custom_title2;
    Button btn_file;
    TextView txt_file,txt_file_note;

    int randomNumber;
    String id_company="",nama_company="";
    String id_note = "",nmr_note="",timeStamp="",tanggal_note="",act_note="",memberNote="",id_member_project = "",status_note="",fileNoteName="";

    Data_note detNote;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private ArrayList<Data_work_report> list_member_project ;

    android.app.AlertDialog.Builder alertDialogBuilder;
    android.app.AlertDialog alertDialog;

    private int PICK_PDF_REQUEST = 1;
    private Uri filePathFile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        txt_tgl_note = findViewById(R.id.txt_tgl_note);
        txt_subject_note = findViewById(R.id.txt_subject_note);
        txt_isi_note = findViewById(R.id.txt_isi_note);
        spinner_jenis_note = findViewById(R.id.spinner_jenis_note);
        multi_member_note = findViewById(R.id.multi_member_note);
        btn_del = findViewById(R.id.btn_del);
        btn_change_status = findViewById(R.id.btn_change_status);
        custom_title2 = findViewById(R.id.custom_title2);
        btn_file = findViewById(R.id.btn_file);
        txt_file = findViewById(R.id.txt_file);
        txt_file_note = findViewById(R.id.txt_file_note);

        list_member_project = new ArrayList<Data_work_report>();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intentku = getIntent(); // gets the previously created intent
        act_note = intentku.getStringExtra("act");
        tanggal_note = intentku.getStringExtra("tanggal");
        detNote = (Data_note) intentku.getSerializableExtra(Api.PARAM_DATA);
        if(detNote != null)
        {
            id_note = detNote.getid();
            nmr_note = detNote.getnmr_note();
            memberNote = detNote.getparticipan();
            status_note = detNote.getstatus_note();
            fileNoteName = detNote.getfile_note();
            //txt_isi_note.setText(Html.fromHtml(detNote.getisi_note()));
            txt_subject_note.setText(detNote.getsubject_note());
            custom_title2.setText(status_note);
            txt_file_note.setText(fileNoteName);

            get_isi_note(nmr_note);

            ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(edit_note.this, R.array.slc_note_filter2, android.R.layout.simple_spinner_item);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            int spinnerPosition = adapter3.getPosition(detNote.getjenis_note());
            spinner_jenis_note.setSelection(spinnerPosition);

            getListMemberProject(id_company);
        }

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

        btn_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DeleteAlert(id_note);
            }
        });

        btn_change_status.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pilihAct(id_note);
            }
        });

        btn_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                showFileChooser1();

            }
        });

        txt_file_note.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                if(txt_file_note.getText().equals("no file"))
                {
                    Toast.makeText(getApplicationContext(), "Note tidak memiliki file", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(edit_note.this, note_download_file.class);
                    //Intent intent = new Intent(edit_note.this, project_file_pendukung.class);
                    intent.putExtra("act", "edit_note");
                    intent.putExtra("namaFileFlowMenu", fileNoteName);
                    startActivityForResult(intent, 1);
                }

            }
        });
        //get_id_note(timeStamp);

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

        GetNamaMember();
    }

    private void GetNamaMember(){
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < list_member_project.size(); i++) {
            list.add(list_member_project.get(i).getid());
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String nama_member = "";
        String id_member = memberNote.replace(",", "~");
        String memberID[] = id_member.split("~");
        for(String idM : memberID) {
            //Toast.makeText(this,"idM = "+idM,Toast.LENGTH_SHORT).show();
            int spinnerPosition2 = dataAdapter.getPosition(idM);
            if(spinnerPosition2 != -1) {
                nama_member += list_member_project.get(spinnerPosition2).getnama_user() + ", ";
            }
            //Log.d("idM",String.valueOf(spinnerPosition2));
        }
        multi_member_note.setText(nama_member);
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

            final ProgressDialog progressDialog = new ProgressDialog(edit_note.this,
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
                    params.put("id_note", id_note);
                    params.put("nmr_note", nmr_note);
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

    private void DeleteAlert(final String idNote){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Note");

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
                        proc_delete_note(idNote);
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

    private void proc_delete_note(final String idNote){

        final ProgressDialog progressDialog = new ProgressDialog(edit_note.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_del_note, new Response.Listener<String>() {

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
                Log.e(TAG, "Delete note Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_note", idNote);
                params.put("nmr_note", nmr_note);

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

    private void pilihAct(final String idNote) {
        alertDialogBuilder = new android.app.AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(edit_note.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(
                50, // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here
                0,
                50,
                0
        );

        final TableLayout tblRange = new TableLayout(edit_note.this);
        tblRange.setColumnStretchable(0, true);
        //tblRange.setColumnStretchable(1, true);
        //tblRange.setColumnStretchable(2, true);

        TableRow row = new TableRow(edit_note.this);
        row.setBackgroundColor(getResources().getColor(R.color.white));

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(3, 1, 3, 1);
        params.height = 100;



        final TableRow row2 = new TableRow(edit_note.this);
        row2.setBackgroundColor(getResources().getColor(R.color.white));

        TextView txt_open=new TextView(edit_note.this);
        txt_open.setText("OPEN");
        txt_open.setTextColor(getResources().getColor(R.color.white));
        txt_open.setBackgroundResource(R.color.page_tbl_total);
        txt_open.setTextSize(10);
        txt_open.setGravity(Gravity.CENTER);
        txt_open.setPadding(10,0,10,0);
        txt_open.setTypeface(null, Typeface.BOLD);
        txt_open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                cancelDialog(); //Implement method for canceling dialog

                proc_update_status_note(idNote,"OPEN");
            }
        });

        // add to row
        row2.addView(txt_open, params);
        tblRange.addView(row2);

        final TableRow row3 = new TableRow(edit_note.this);
        row3.setBackgroundColor(getResources().getColor(R.color.white));

        TextView txt_proses=new TextView(edit_note.this);
        txt_proses.setText("PROSES");
        txt_proses.setTextColor(getResources().getColor(R.color.white));
        txt_proses.setBackgroundResource(R.color.page_tbl_total);
        txt_proses.setTextSize(10);
        txt_proses.setGravity(Gravity.CENTER);
        txt_proses.setPadding(10,0,10,0);
        txt_proses.setTypeface(null, Typeface.BOLD);
        txt_proses.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                cancelDialog(); //Implement method for canceling dialog

                proc_update_status_note(idNote,"PROSES");
            }
        });

        // add to row
        row3.addView(txt_proses, params);
        tblRange.addView(row3);

        final TableRow row4 = new TableRow(edit_note.this);
        row4.setBackgroundColor(getResources().getColor(R.color.white));

        TextView txt_finish=new TextView(edit_note.this);
        txt_finish.setText("FINISH");
        txt_finish.setTextColor(getResources().getColor(R.color.white));
        txt_finish.setBackgroundResource(R.color.page_tbl_total);
        txt_finish.setTextSize(10);
        txt_finish.setGravity(Gravity.CENTER);
        txt_finish.setPadding(10,0,10,0);
        txt_finish.setTypeface(null, Typeface.BOLD);
        txt_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                cancelDialog(); //Implement method for canceling dialog

                proc_update_status_note(idNote,"FINISH");
            }
        });

        // add to row
        row4.addView(txt_finish, params);
        tblRange.addView(row4);

        layout.addView(tblRange);

        // set title dialog
        alertDialogBuilder.setTitle("Pilih Status ");
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

    private void proc_update_status_note(final String idNote,final String statusNote){

        final ProgressDialog progressDialog = new ProgressDialog(edit_note.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update_status_note, new Response.Listener<String>() {

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
                        custom_title2.setText(statusNote);
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
                Log.e(TAG, "update note Error: " + error.getMessage());
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_note", idNote);
                params.put("status_note", statusNote);
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

    private void get_isi_note(final String nmr_note){
        final ProgressDialog progressDialog = new ProgressDialog(edit_note.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Detail Note Loading ");
        progressDialog.show();

        String url_server       = url_get_isi_note+nmr_note;
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
                        String isi_note      = jObj.getString("isi_note");
                        /*
                        txt_isi_note.setText(Html.fromHtml(isi_note));
                        */
                        //isi_note_txt.setText(Html.fromHtml(isi_note,new URLImageParser(isi_note_txt, edit_note.this),null));
                        txt_isi_note.setText(Html.fromHtml(isi_note,new URLImageParser(txt_isi_note, edit_note.this),null));

                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(edit_note.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(edit_note.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

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
