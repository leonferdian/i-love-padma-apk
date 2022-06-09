package com.project45.ilovepadma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project45.ilovepadma.app.AppController;
import com.project45.ilovepadma.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class registration_form extends AppCompatActivity {

    Button btn_signup;
    EditText txt_name, txt_email, txt_email_atasan, txt_kode_atasan,txt_kode_dms3,txt_ktp,txt_telp,txt_alamat,txt_tgl_lahir;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL2 + "email_service/registration_service.php";
    private static final String TAG = registration_form.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_EMAIL = "email";

    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form);


        btn_signup = findViewById(R.id.btn_signup);
        txt_name =  findViewById(R.id.txt_name);
        txt_email =  findViewById(R.id.txt_email);
        txt_email_atasan = findViewById(R.id.txt_email_atasan);
        txt_kode_atasan = findViewById(R.id.txt_kode_atasan);
        txt_kode_dms3 = findViewById(R.id.txt_kode_dms3);
        txt_ktp = findViewById(R.id.txt_ktp);
        txt_telp = findViewById(R.id.txt_telp);
        txt_alamat = findViewById(R.id.txt_alamat);
        txt_tgl_lahir = findViewById(R.id.txt_tgl_lahir);

        //String nama = txt_name.getText().toString();
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String nama_user = txt_name.getText().toString();
                String email_atasan = txt_email_atasan.getText().toString();
                String kode_atasan = txt_kode_atasan.getText().toString();
                String kode_dms3 = txt_kode_dms3.getText().toString();
                String ktp = txt_ktp.getText().toString();
                String telp = txt_telp.getText().toString();

                /*Intent intentku= new Intent(registration_form.this, verification_registration.class);
                intentku.putExtra("email", email);
                startActivity(intentku);
                */

                if (email.trim().length() > 0 && nama_user.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(getApplicationContext(), "Email tidak sesuai", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(ktp.equals("")){
                                Toast.makeText(getApplicationContext(), "KTP harus diisi ", Toast.LENGTH_LONG).show();
                            }
                            else if(telp.equals("")){
                                Toast.makeText(getApplicationContext(), "Telp harus diisi ", Toast.LENGTH_LONG).show();
                            }
                            /*
                            else if(email_atasan.equals("")){
                                Toast.makeText(getApplicationContext(), "Email atasan harus diisi ", Toast.LENGTH_LONG).show();
                            }
                            */
                            else {
                                checkLogin(email, nama_user, email_atasan, kode_atasan, kode_dms3);
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Email & nama tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void checkLogin(final String email, final String nama_user, final String email_atasan, final String kode_atasan, final String kode_dms3){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Waiting For Code ...");
        showDialog();

        String ktp = txt_ktp.getText().toString();
        String telp = txt_telp.getText().toString();
        String alamat = txt_alamat.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        //String email = jObj.getString(TAG_EMAIL);

                        Log.e("Successfully Get Code!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // Memanggil main activity
                        Intent intent = new Intent(registration_form.this, verification_registration2.class);
                        intent.putExtra(TAG_EMAIL, email);
                        startActivity(intent);
                        /*Intent intentku= new Intent(login_form.this, verification_registration.class);
                        intentku.putExtra("email", email);
                        startActivity(intentku);
                        */
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", email);
                params.put("nama_user", nama_user);
                params.put("email_atasan", email_atasan);
                params.put("kode_atasan", kode_atasan);
                params.put("kode_dms3", kode_dms3);
                params.put("ktp", ktp);
                params.put("telp", telp);
                params.put("alamat", alamat);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
