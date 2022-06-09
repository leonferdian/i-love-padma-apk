package com.project45.ilovepadma.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.complain.list_complain_for_you2;
import com.project45.ilovepadma.complain.report_complain;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.project.add_project;
import com.project45.ilovepadma.util.Server;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class note_editor1 extends AppCompatActivity {

    String id_user, username,bm,email,jabatan;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_EMAIL = "email";
    private static final String TAG = add_project.class.getSimpleName();

    private static String url_monitoring_gallon     = Server.URL2 + "web_view/notes/notes_text.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String id_company="",nama_company="";

    private WebView webview_user_gallon;
    private ProgressBar progress_user_gallon;

    String ShowOrHideWebViewReportUserGallon = "show";

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor1);

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


        get_perf_kunjungan("","","");

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

    private void get_perf_kunjungan(final String id_emp,final String tahun,final String bulan){
        webview_user_gallon.setWebViewClient(new CustomWebViewReportUserGallon());

        webview_user_gallon.getSettings().setJavaScriptEnabled(true);
        webview_user_gallon.getSettings().setDomStorageEnabled(true);
        webview_user_gallon.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview_user_gallon.addJavascriptInterface(new PerfInterface(tahun,bulan,id_user), "interface");
        //+"&id_emp="+id_emp
        webview_user_gallon.loadUrl(url_monitoring_gallon+"?jabatan="+jabatan+"&id_user="+id_user+"&tahun="+tahun+"&bulan="+bulan+"&id_company="+id_company);
        Log.d("complain_url", url_monitoring_gallon+"?jabatan="+jabatan+"&id_user="+id_user+"&tahun="+tahun+"&bulan="+bulan+"&id_company="+id_company);
        webview_user_gallon.setWebChromeClient(new WebChromeClient()
        {
            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                note_editor1.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                note_editor1.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                note_editor1.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), note_editor1.FILECHOOSER_RESULTCODE );

            }

        });
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
        public void get_detail(final String complain_from,final String complain_status) {
            //Toast.makeText(getApplicationContext(), complain_from+" - "+complain_status, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), list_complain_for_you2.class);
            intent.putExtra("tahun", tahun);
            intent.putExtra("bulan", bulan);
            intent.putExtra("idUser", idUser);
            intent.putExtra("complain_from", complain_from);
            intent.putExtra("complain_status", complain_status);
            startActivity(intent);


        }

        @JavascriptInterface
        public void get_detail2(final String complain_from,final String complain_status,final String user_id) {
            //Toast.makeText(getApplicationContext(), complain_from+" - "+complain_status, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), list_complain_for_you2.class);
            intent.putExtra("tahun", tahun);
            intent.putExtra("bulan", bulan);
            intent.putExtra("idUser", user_id);
            intent.putExtra("complain_from", complain_from);
            intent.putExtra("complain_status", complain_status);
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
