package com.project45.ilovepadma.aktifitas;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.project.edit_project;
import com.project45.ilovepadma.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class edit_deskripsi extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,employee_id_dms3="",id_aktifitas="",timeStamp="",act_form="",tanggal_aktifitas="",deskripsi = "",jam_awal="",
            jam_akhir = "",status_agenda="";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static final String TAG = add_aktifitas.class.getSimpleName();

    private static String url_post_deskripsi     = Server.URL + "aktifitas/post_edit_aktifitas_detail";
    EditText txt_deskripsi,txt_jam_awal,txt_jam_akhir;
    Button btn_save_deskripsi;
    Spinner spinner_status_agenda;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_deskripsi);

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
        employee_id_dms3 = Api.sharedpreferences.getString(Api.TAG_ID_EMPLOYEE_DMS3, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);

        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        txt_jam_awal = findViewById(R.id.txt_jam_awal);
        txt_jam_akhir = findViewById(R.id.txt_jam_akhir);
        spinner_status_agenda = findViewById(R.id.spinner_status_agenda);
        btn_save_deskripsi = findViewById(R.id.btn_save_deskripsi);

        Intent intentku = getIntent(); // gets the previously created intent
        act_form = intentku.getStringExtra("act");
        id_aktifitas = intentku.getStringExtra("id_aktifitas");
        deskripsi = intentku.getStringExtra("deskripsi");
        jam_awal = intentku.getStringExtra("jam_awal");
        jam_akhir = intentku.getStringExtra("jam_akhir");
        status_agenda = intentku.getStringExtra("status_agenda");

        txt_deskripsi.setText(deskripsi);
        txt_jam_awal.setText(jam_awal);
        txt_jam_akhir.setText(jam_akhir);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(edit_deskripsi.this, R.array.slc_status_agenda, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (status_agenda != null) {
            int spinnerPosition = adapter3.getPosition(status_agenda);
            spinner_status_agenda.setSelection(spinnerPosition);
        }

        txt_jam_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(edit_deskripsi.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedMinutee,selectedHourr;

                        if(selectedMinute<10){
                            selectedMinutee = "0"+selectedMinute;
                        }
                        else{
                            selectedMinutee = String.valueOf(selectedMinute);
                        }

                        if(selectedHour<10){
                            selectedHourr = "0"+selectedHour;
                        }
                        else{
                            selectedHourr = String.valueOf(selectedHour);
                        }
                        txt_jam_awal.setText(selectedHourr + ":" + selectedMinutee);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Pilih Jam Awal");
                mTimePicker.show();
            }
        });

        txt_jam_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(edit_deskripsi.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedMinutee,selectedHourr;

                        if(selectedMinute<10){
                            selectedMinutee = "0"+selectedMinute;
                        }
                        else{
                            selectedMinutee = String.valueOf(selectedMinute);
                        }

                        if(selectedHour<10){
                            selectedHourr = "0"+selectedHour;
                        }
                        else{
                            selectedHourr = String.valueOf(selectedHour);
                        }
                        txt_jam_akhir.setText(selectedHourr + ":" + selectedMinutee);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Pilih Jam Akhir");
                mTimePicker.show();
            }
        });

        //Toast.makeText(getApplicationContext(), "id_aktifitas "+id_aktifitas, Toast.LENGTH_LONG).show();
        btn_save_deskripsi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                postDeskripsi();

                //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
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
            window.setStatusBarColor(getResources().getColor(R.color.page_join_visit));
        }
    }

    private void postDeskripsi(){
        final String detail_aktifitas = txt_deskripsi.getText().toString().replace("\n", "<br>").trim();
        final String jam_awal = txt_jam_awal.getText().toString();
        final String jam_akhir = txt_jam_akhir.getText().toString();
        final String status_agenda = String.valueOf(spinner_status_agenda.getSelectedItem());

        if(detail_aktifitas.equals("")){
            Toast.makeText(getApplicationContext(), "Deskripsi harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(jam_awal.equals("")){
            Toast.makeText(getApplicationContext(), "Jam Awal harus diisi ", Toast.LENGTH_LONG).show();
        }
        else if(jam_akhir.equals("")){
            Toast.makeText(getApplicationContext(), "Jam Akhir harus diisi ", Toast.LENGTH_LONG).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(edit_deskripsi.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();

            //proses save to database using api
            StringRequest strReq = new StringRequest(Request.Method.POST, url_post_deskripsi, new Response.Listener<String>() {

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
                    params.put("id_report", id_aktifitas);
                    params.put("detail_aktifitas", detail_aktifitas);
                    params.put("jam_awal", jam_awal);
                    params.put("jam_akhir", jam_akhir);
                    params.put("status_agenda", status_agenda);
                    params.put("create_by", id_user);

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
        setResult(11,returnIntent);
        finish();
    }
}
