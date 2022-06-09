package com.project45.ilovepadma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import androidx.appcompat.widget.Toolbar;

public class verification_registration extends AppCompatActivity {
    private String email_verifikasi ;
    Button btn_verifikasi;
    TextView label_email;
    EditText txt_verication1, txt_verication2,txt_verication3,txt_verication4,txt_verication5,txt_verication6;

    ProgressDialog pDialog;
    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "login/login_padma_ilv";
    //private String url = Server.URL + "login.php";

    private static final String TAG = verification_registration.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_ID_EMPLOYEE_DMS3 = "id_employee_dms3";
    public final static String TAG_DB_EMPLOYEE_DMS3 = "db_employee_dms3";
    public final static String TAG_NIK = "nik";
    public final static String TAG_JABATAN = "jabatan";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    public static final String my_shared_preferences = "45ilv_shared_preferences";
    public static final String session_status = "session_status";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_registration);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_verifikasi);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        btn_verifikasi = findViewById(R.id.btn_verifikasi);
        label_email = findViewById(R.id.label_email);
        txt_verication1 = findViewById(R.id.txt_verication1);
        txt_verication2 = findViewById(R.id.txt_verication2);
        txt_verication3 = findViewById(R.id.txt_verication3);
        txt_verication4 = findViewById(R.id.txt_verication4);
        txt_verication5 = findViewById(R.id.txt_verication5);
        txt_verication6 = findViewById(R.id.txt_verication6);

        disable_button();
        txt_verication1.requestFocus();

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

        Intent intentku = getIntent(); // gets the previously created intent
        email_verifikasi = intentku.getStringExtra("email");
        label_email.setText(email_verifikasi);

        btn_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = email_verifikasi;
                String password = txt_verication1.getText().toString()+txt_verication2.getText().toString()+txt_verication3.getText().toString()+
                        txt_verication4.getText().toString()+txt_verication5.getText().toString()+txt_verication6.getText().toString();
                //Toast.makeText(getApplicationContext(), username+" - "+password, Toast.LENGTH_LONG).show();
                //Intent intentku= new Intent(verification_registration.this, MainActivity.class);
                //startActivity(intentku);
                checkLogin(username,password);
            }

        });



        //for next verication number
        txt_verication1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // specify length of your editext here to move on next edittext

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txt_verication1.getText().toString().trim().length()>=1){
                    txt_verication2.requestFocus();
                }
            }
        });

        txt_verication2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // specify length of your editext here to move on next edittext

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txt_verication2.getText().toString().trim().length()>=1){
                    txt_verication3.requestFocus();
                }
            }
        });

        txt_verication3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // specify length of your editext here to move on next edittext

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txt_verication3.getText().toString().trim().length()>=1){
                    txt_verication4.requestFocus();
                }
            }
        });

        txt_verication4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // specify length of your editext here to move on next edittext

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txt_verication4.getText().toString().trim().length()>=1){
                    txt_verication5.requestFocus();
                }
            }
        });

        txt_verication5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // specify length of your editext here to move on next edittext

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txt_verication5.getText().toString().trim().length()>=1){
                    txt_verication6.requestFocus();
                }
            }
        });

        txt_verication6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // specify length of your editext here to move on next edittext

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txt_verication6.getText().toString().trim().length()>=1){
                    //txt_verication6.requestFocus();
                    Toast.makeText(getApplicationContext(), "Tekan Tombol Verifikasi", Toast.LENGTH_LONG).show();
                    enable_button();
                    btn_verifikasi.requestFocus();
                }
            }
        });
    }

    private void disable_button(){
        btn_verifikasi.setEnabled(false);
        btn_verifikasi.setBackgroundColor(getResources().getColor(R.color.iron));
        btn_verifikasi.setTextColor(getResources().getColor(R.color.monsoon));
    }

    private void enable_button(){
        btn_verifikasi.setEnabled(true);
        btn_verifikasi.setBackgroundColor(getResources().getColor(R.color.blue_color));
        btn_verifikasi.setTextColor(getResources().getColor(R.color.white));
    }

    private void checkLogin(final String username2, final String password2) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verification Process ...");
        showDialog();

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
                        String username = jObj.getString(TAG_USERNAME);
                        String id = jObj.getString(TAG_ID);
                        String email = jObj.getString(TAG_EMAIL);
                        String nik = jObj.getString("nik");
                        String jabatan = jObj.getString("jabatan");

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString(TAG_EMAIL, email);
                        editor.putString(TAG_NIK, nik);
                        editor.putString(TAG_JABATAN, jabatan);
                        editor.commit();

                        // Memanggil main activity
                        Intent intent = new Intent(verification_registration.this, MainActivity3.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_USERNAME, username);
                        intent.putExtra(TAG_EMAIL, email);
                        finish();
                        startActivity(intent);
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
                params.put("username", username2);
                params.put("password", password2);

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
