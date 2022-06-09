package com.project45.ilovepadma.complain;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project45.ilovepadma.BuildConfig;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.util.Server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class respon_download_file extends AppCompatActivity {

    private static String url_file_project     = Server.URL2 + "image_upload/list_foto_complain_respon/";

    String id_user, username,bm,email;

    private WebView webview_user_gallon;
    private ProgressBar progress_user_gallon;
    TextView txt_header1;

    String namaFileFlowMenu = "",tipe_file="";

    Context context;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

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
        tipe_file = intentku.getStringExtra("tipe_file");
        txt_header1.setText(namaFileFlowMenu);

        context = respon_download_file.this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if ( ContextCompat.checkSelfPermission( respon_download_file.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( respon_download_file.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                    11 );

        }
        else {
            String File_DIRECTORY = "ilv_tiket";
            File file =  new File(Environment.getExternalStorageDirectory()
                    + "/" + File_DIRECTORY+"/"+namaFileFlowMenu);
            if (!file.exists()) {
                new DownloadFileFromURL().execute(url_file_project + namaFileFlowMenu);
            }
            else {
                viewFile(tipe_file);
            }
        }

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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }



    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {


                String FILE_DIRECTORY = "ilv_tiket";
                File file =  new File(Environment.getExternalStorageDirectory()
                        + "/" + FILE_DIRECTORY);
                if (!file.exists()) {
                    file.mkdirs();
                }

                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/ilv_tiket/" + namaFileFlowMenu);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            Toast.makeText(getApplicationContext(), "Download File Success, check folder ilv_tiket untuk melihat file.", Toast.LENGTH_LONG).show();
            OpenFileAlert();
        }



    }


    public void viewFile(final String tipeFile)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/ilv_tiket/" + namaFileFlowMenu);  // -> filename = maven.pdf

        /*
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(note_download_file.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }


         */


        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider" ,pdfFile);
        if(tipeFile.equals("image")) {
            intent.setDataAndType(data, "image/*");
        }
        else{
            intent.setDataAndType(data, "application/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);


        onBackPressed();
        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(Environment.getExternalStorageDirectory() + "/ilv_tiket/" + pdfFile);
        Uri data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".provider",file);
        //intent.setDataAndType(data,"application/vnd.android.package-archive");
        intent.setDataAndType(data,"/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
        */

    }

    private void OpenFileAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("File Deskripsi");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("File berhasil didownload di folder ilv_tiket. Apakah anda ingin membuka file ")
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
                        viewFile(tipe_file);
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
}
