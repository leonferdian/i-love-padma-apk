package com.project45.ilovepadma.project;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project45.ilovepadma.BuildConfig;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class project_file_pendukung extends AppCompatActivity implements DownloadFile.Listener{

    private static String url_file_project     = Server.URL2 + "inc/notes/file_notes/";

    String id_user, username,bm,email;

    private WebView webview_user_gallon;
    private ProgressBar progress_user_gallon;
    TextView txt_header1;

    String namaFileFlowMenu = "";
    String ShowOrHideWebViewReportUserGallon = "show";

    Context context;

    private  ProgressBar progressBar;
    private LinearLayout pdfLayout;

    private RemotePDFViewPager remotePDFViewPager;
    private PDFPagerAdapter pdfPagerAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_file_pendukung);

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

        webview_user_gallon = findViewById(R.id.webview_user_gallon);
        progress_user_gallon = findViewById(R.id.progress_user_gallon);
        txt_header1 = findViewById(R.id.txt_header1);



        Intent intentku = getIntent(); // gets the previously created intent
        namaFileFlowMenu = intentku.getStringExtra("namaFileFlowMenu");
        txt_header1.setText(namaFileFlowMenu);

        context = project_file_pendukung.this;

        //get_nama_file(namaFileFlowMenu);
        //Create a RemotePDFViewPager object
        //remotePDFViewPager = new RemotePDFViewPager(this, url_file_project+namaFileFlowMenu, this);

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
        getMenuInflater().inflate(R.menu.menu_download, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_download) {
            //Toast.makeText(getApplicationContext(), "tes: ", Toast.LENGTH_LONG).show();
            new DownloadFile().execute(url_file_project+namaFileFlowMenu, namaFileFlowMenu);
        }
        else if (id == R.id.action_view) {
            //Toast.makeText(getApplicationContext(), "tes: ", Toast.LENGTH_LONG).show();
            viewPDF();
        }

        return super.onOptionsItemSelected(item);
    }

    private void get_nama_file(final String nama_file){
        webview_user_gallon.setWebViewClient(new CustomWebViewReportUserGallon());

        webview_user_gallon.getSettings().setJavaScriptEnabled(true);
        webview_user_gallon.setVerticalScrollBarEnabled(true);
        webview_user_gallon.zoomIn();

        webview_user_gallon.loadUrl("https://docs.google.com/viewerng/viewer?embedded=true&url="+url_file_project+nama_file);
        Log.d("file_project", "https://docs.google.com/viewerng/viewer?embedded=true&url="+url_file_project+nama_file);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        // That's the positive case. PDF Download went fine
        pdfPagerAdapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(pdfPagerAdapter);
        updateLayout();
        progressBar.setVisibility(View.GONE);
    }

    private void updateLayout() {

        pdfLayout.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (pdfPagerAdapter != null) {
            pdfPagerAdapter.close();
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

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = "file5317_20220319_111941.pdf";  // -> maven.pdf
            Log.d("fileUrl", "fileUrl : " + fileUrl);
            //String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            //File folder = new File(extStorageDirectory, "ilv_note");
            //folder.mkdir();

            //File pdfFile = new File(folder, fileName);

            String File_DIRECTORY = "ilv_note";
            File file =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + "/" + File_DIRECTORY);
            if (!file.exists()) {
                file.mkdirs();
            }

            File pdfFile = new File(file, fileName);

            byte data[] = new byte[1024];
            try{
                pdfFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(pdfFile);
                fo.write(data);
                fo.flush();
                fo.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

    public void viewPDF()
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/ilv_pdf/" + namaFileFlowMenu);  // -> filename = maven.pdf
        /*
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(project_file_pendukung.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }



        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider" ,pdfFile);
        intent.setDataAndType(data, "/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);

         */

        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(Environment.getExternalStorageDirectory() + "/ilv_pdf/" + pdfFile);
        Uri data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".provider",file);
        //intent.setDataAndType(data,"application/vnd.android.package-archive");
        intent.setDataAndType(data,"/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }
}
