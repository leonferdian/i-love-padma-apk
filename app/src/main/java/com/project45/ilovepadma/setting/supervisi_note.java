package com.project45.ilovepadma.setting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_join_visit;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.notes.list_note_by_supervisi;
import com.project45.ilovepadma.util.Server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class supervisi_note extends AppCompatActivity {
    private static String url_monitoring_gallon     = Server.URL2 + "web_view/supervisi/supervisi_note.php";

    private static final String TAG = supervisi_note.class.getSimpleName();

    String id_user, username,bm,email,jabatan,list_emp="",list_rute="",timeStamp="",employee_id_dms3="",tahun="",bulan="",timeStamp1 = "",id_filter = "";

    private WebView webview_user_gallon,webview_user_sps;
    private ProgressBar progress_user_gallon,progress_user_sps;

    String ShowOrHideWebViewReportUserGallon = "show";

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    EditText txt_tgl_awal,txt_tgl_akhir;
    Spinner spinner_tahun,spinner_bulan,spinner_depo;
    String id_depo="";

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    TextView txt_header1,txt_header2;
    LinearLayout ll1b,container_user_sps;

    private ArrayList<Data_join_visit> list_depo,list_sm,list_hos,list_hot;

    Spinner spinner_filter,spinner_sm,spinner_hos,spinner_hot;
    LinearLayout rl_sm,rl_hos,rl_hot;

    String filte_jns="All";
    String id_sm="",id_hos="",id_hot="",id_company="",nama_company="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reporting_dashboard);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtasaccount_user = (Toolbar) findViewById(R.id.toolbar_main_ms);
        setSupportActionBar(ToolBarAtasaccount_user);
        // ToolBarAtas.setLogo(R.mipmap.ic_launcher);
        ToolBarAtasaccount_user.setLogoDescription(getResources().getString(R.string.app_name));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor();

        Api.sharedpreferences = getSharedPreferences(Api.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);
        username = Api.sharedpreferences.getString(Api.TAG_USERNAME, null);
        email = Api.sharedpreferences.getString(Api.TAG_EMAIL, null);
        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);
        id_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_ID, null);
        nama_company = Api.sharedpreferences.getString(Api.TAG_COMPANY_USER_NAME, null);

        webview_user_gallon = findViewById(R.id.webview_user_gallon);

        progress_user_gallon = findViewById(R.id.progress_user_gallon);

        txt_header1 = findViewById(R.id.txt_header1);

        txt_header1.setText("Note");


        tahun= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        bulan= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //Add or minus day to current date.
        //cal.add(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -1);
        //Log.d(TAG,dateFormat.format(cal.getTime()));
        timeStamp1 = dateFormat.format(cal.getTime());
        //timeStamp1 = new SimpleDateFormat("yyyy-MM-01", Locale.getDefault()).format(new Date());
        timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        getSupportActionBar().setTitle("Supervisi Note ");
        webview_user_gallon = findViewById(R.id.webview_user_gallon);

        get_perf_kunjungan(username,tahun,bulan);

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
        // Add menu ditoolbar
        //getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filter_list) {

            //DialogFilter("Filter");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void DialogFilter(String button) {
        dialog = new AlertDialog.Builder(supervisi_note.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_filter_wr, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_web_asset_black_24);
        dialog.setTitle("Complain");

        jabatan = Api.sharedpreferences.getString(Api.TAG_JABATAN, null);

        spinner_tahun =  dialogView.findViewById(R.id.spinner_tahun);
        spinner_bulan =  dialogView.findViewById(R.id.spinner_bulan);

        addItemsOnSpinnerTahun(spinner_tahun);
        addItemsOnSpinnerBulan(spinner_bulan);


        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //callVolley(txt_tgl_awal.getText().toString(),txt_tgl_akhir.getText().toString());
                tahun = String.valueOf(spinner_tahun.getSelectedItem());
                bulan = String.valueOf(spinner_bulan.getSelectedItem());
                getSupportActionBar().setTitle("Complain " + tahun + " - " + bulan);

                ShowOrHideWebViewReportUserGallon = "show";
                progress_user_gallon.setVisibility(View.VISIBLE);
                webview_user_gallon.setVisibility(View.GONE);


                get_perf_kunjungan(list_emp,tahun,bulan);



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

    public void addItemsOnSpinnerTahun(final Spinner spinner_thn) {
        List<String> list = new ArrayList<String>();
        int tahun_lalu = Integer.valueOf(tahun)-1;
        for (int i = tahun_lalu; i <=Integer.valueOf(tahun); i++) {
            list.add(String.valueOf(i));
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_thn.setAdapter(dataAdapter);
        int spinnerPosition2 = dataAdapter.getPosition(tahun);
        spinner_thn.setSelection(spinnerPosition2);
    }

    public void addItemsOnSpinnerBulan(final Spinner spinner_bln) {
        List<String> list = new ArrayList<String>();

        for (int i = 1; i <=12; i++) {
            list.add(String.valueOf(i));
            //Toast.makeText(add_complain.this, "name "+jenis_complainList.get(i).getName(), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bln.setAdapter(dataAdapter);
        int spinnerPosition2 = dataAdapter.getPosition(bulan);
        spinner_bln.setSelection(spinnerPosition2);
    }

    private void get_perf_kunjungan(final String usernamee,final String tahun,final String bulan){
        webview_user_gallon.setWebViewClient(new CustomWebViewReportUserGallon());

        String usernamee2 = usernamee.replace(" ", "+");

        webview_user_gallon.getSettings().setJavaScriptEnabled(true);
        webview_user_gallon.getSettings().setDomStorageEnabled(true);
        webview_user_gallon.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview_user_gallon.addJavascriptInterface(new PerfInterface(tahun,bulan,id_user), "interface");
        //+"&id_emp="+id_emp
        webview_user_gallon.loadUrl(url_monitoring_gallon+"?nama_user="+usernamee2+"&id_company="+id_company+"&username="+email);
        Log.d("supervisi_url", url_monitoring_gallon+"?nama_user="+usernamee2+"&id_company="+id_company+"&username="+email);
    }

    private class PerfInterface {
        String tahun="";
        String bulan = "";
        String idUser = "";
        public PerfInterface(final String thn,final String bln,final String userID) {
            this.tahun = thn;
            this.bulan = bln;
            this.idUser = userID;
        }

        @JavascriptInterface
        public void get_detail(final String id_bawanan,final String nama_bawahan) {
            //Toast.makeText(getApplicationContext(), id_bawanan+" - "+nama_bawahan, Toast.LENGTH_LONG).show();


            Intent intent = new Intent(getApplicationContext(), list_note_by_supervisi.class);
            intent.putExtra("id_bawanan", id_bawanan);
            intent.putExtra("nama_bawahan", nama_bawahan);
            startActivity(intent);


        }

    }

    private class CustomWebViewReportUserGallon extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {

            // only make it invisible the FIRST time the app is run
            if (ShowOrHideWebViewReportUserGallon.equals("show")) {
                webview.setVisibility(webview.INVISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            ShowOrHideWebViewReportUserGallon = "hide";
            progress_user_gallon.setVisibility(View.GONE);

            view.setVisibility(webview_user_gallon.VISIBLE);
            super.onPageFinished(view, url);

        }
    }
}
