package com.project45.ilovepadma.setting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.MainActivity2;
import com.project45.ilovepadma.MainActivity3;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.registration_form;
import com.project45.ilovepadma.util.Server;
import com.project45.ilovepadma.verification_registration;
import com.project45.ilovepadma.verification_registration2;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class add_company extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,company_create="";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_company.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static String url_post_company    = Server.URL + "company/post_company";
    private static String url_delete_company    = Server.URL + "company/delete_company";
    public static String url_sent_invitation = Server.URL2 +"email_service/invitation_service.php";

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    int success,last_id;
    String act="",id_company="",code_company="",CompanyID = "" ;
    int randomNumber;

    EditText txt_user_invitation;
    EditText txt_id_company,txt_name_company,txt_tax,txt_pkp,txt_npwp,txt_siup,txt_alamat;
    Button btn_del,btn_invitation,btn_structure;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_company);

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

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        code_company = id_user+"-"+generateRandomNumbers(100001,999999);

        txt_id_company = findViewById(R.id.txt_id_company);
        txt_name_company= findViewById(R.id.txt_name_company);
        txt_alamat = findViewById(R.id.txt_alamat);

        btn_del = findViewById(R.id.btn_del);
        btn_invitation = findViewById(R.id.btn_invitation);
        btn_structure = findViewById(R.id.btn_structure);

        Intent intentku = getIntent(); // gets the previously created intent
        act = intentku.getStringExtra("act");
        if(act.equals("edit_company")) {
            Data_company detCom = (Data_company) intentku.getSerializableExtra(Api.PARAM_DATA);

            txt_id_company.setText(detCom.getszId());
            txt_id_company.setFocusable(false);
            txt_name_company.setText(detCom.getszName());
            txt_alamat.setText(detCom.getszAddress());

            id_company = detCom.getiInternalId();
            company_create = detCom.getszUserCreatedId();

            getSupportActionBar().setTitle("Update Company ");
            if(detCom.getszUserCreatedId().equals(id_user)) {
                btn_del.setVisibility(View.VISIBLE);
                btn_invitation.setVisibility(View.VISIBLE);
            }
            else{
                if(detCom.getuser_status().equals("administrator")){
                    btn_invitation.setVisibility(View.VISIBLE);
                }
            }

            btn_del.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // update login session ke FALSE dan mengosongkan nilai id dan username
                    DeleteAlert(id_company);

                }
            });

            btn_invitation.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // update login session ke FALSE dan mengosongkan nilai id dan username
                    DialogConfirmLocation("Invite");

                }
            });
        }
        else{
            CompanyID = id_user+"-"+generateRandomNumbers(101,999);
            //Toast.makeText(getApplicationContext(), "CompanyID "+CompanyID, Toast.LENGTH_LONG).show();
            txt_id_company.setText(CompanyID);
            txt_id_company.setFocusable(false);
        }

        if(act.equals("input_company_by_regist")) {
            btn_structure.setVisibility(View.GONE);
        }
        else if(act.equals("input_company")) {
            btn_structure.setVisibility(View.GONE);
        }

        btn_structure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                Intent intent = new Intent(getApplicationContext(), structure_company.class);
                startActivity(intent);

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
            window.setStatusBarColor(getResources().getColor(R.color.page_red));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(act.equals("edit_company")) {
            if (company_create.equals(id_user)) {
                getMenuInflater().inflate(R.menu.menu_save, menu);
            }
        }
        else{
            getMenuInflater().inflate(R.menu.menu_save, menu);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.action_save) {

            proc_company();
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
                /*else  if(absen.equals("filter2")){
                    tgl_akhir.setText(dateFormatter.format(newDate.getTime()));
                }
                */

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void proc_company(){

        final ProgressDialog progressDialog = new ProgressDialog(add_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Save...");
        progressDialog.show();


        final String szId = txt_id_company.getText().toString();
        final String szName = txt_name_company.getText().toString();
        final String szAddress = txt_alamat.getText().toString();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_company, new Response.Listener<String>() {

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

                        //end proses
                        progressDialog.dismiss();

                        if(act.equals("input_company_by_regist")) {
                            Intent intent = new Intent(add_company.this, MainActivity3.class);
                            intent.putExtra(TAG_ID, id_user);
                            intent.putExtra(TAG_USERNAME, username);
                            intent.putExtra(TAG_EMAIL, email);
                            finish();
                            startActivity(intent);
                        }
                        else{
                            onBackPressedWithProcess();
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
                params.put("szId", szId);
                params.put("szName", szName);
                params.put("szAddress", szAddress);
                params.put("code_company", code_company);
                params.put("szUserCreatedId", id_user);

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

    private void DeleteAlert(final String id_company){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Company");

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
                        proc_delete_company(id_company);
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

    private void proc_delete_company(final String iInternalId){

        final ProgressDialog progressDialog = new ProgressDialog(add_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete_company, new Response.Listener<String>() {

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
                params.put("iInternalId", iInternalId);


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

    private void DialogConfirmLocation(String button) {
        dialog = new AlertDialog.Builder(add_company.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.input_user_invitation, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("User Invitation");

        txt_user_invitation =  dialogView.findViewById(R.id.txt_user_invitation);

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save_confirm_outrange(txt_reason_outrange.getText().toString());
                sendInvitation(txt_user_invitation.getText().toString());
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

    private void sendInvitation(final String email_user){
        final ProgressDialog progressDialog = new ProgressDialog(add_company.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process ...");
        progressDialog.show();

        String code_invitation = id_user+"-"+generateRandomNumbers(100001,999999);
        String nama_company = txt_name_company.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_sent_invitation, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        //String email = jObj.getString(TAG_EMAIL);

                        Log.e("Successfully Sent!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }

                    //end proses
                    progressDialog.dismiss();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //end proses
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_user", email_user);
                params.put("code_invitation", code_invitation);
                params.put("id_company", id_company);
                params.put("create_by", id_user);
                params.put("nama_user", username);
                params.put("nama_company", nama_company);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

}
