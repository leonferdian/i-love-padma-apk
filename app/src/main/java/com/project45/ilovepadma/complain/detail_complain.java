package com.project45.ilovepadma.complain;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.project45.ilovepadma.data.Data_jenis_complain;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.project.edit_project;
import com.project45.ilovepadma.project.edit_project2;
import com.project45.ilovepadma.project.edit_sub_project2;
import com.project45.ilovepadma.project.list_project;
import com.project45.ilovepadma.util.Server;

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

public class detail_complain extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,employee_id_dms3="",id_complain="",timeStamp="",act_form="",tanggal_act="",complain_purpose="",complain_status = "",
            jenis_complain = "",nmr_project="",nmr_sub_project="",complain_to="";
    int randomNumber;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    private static final String TAG = detail_complain.class.getSimpleName();

    private static String url_list_complain_deskripsi     = Server.URL + "cust_service/get_list_complain_deskripsi/";
    private static String url_post_delete_deskripsi_padma_complain     = Server.URL + "cust_service/post_delete_deskripsi_padma_complain";
    private static String url_post_edit_status_padma_complain     = Server.URL + "cust_service/post_edit_status_padma_complain";

    EditText txt_subject,txt_complain_purpose,txt_complain_from,txt_due_date,txt_periode_awal,txt_periode_akhir,txt_complain_to,txt_jenis_complain,
            txt_l1,txt_l2,txt_l3,txt_pic2;
    TextView txt_nmr_project,txt_nmr_subproject;

    TableLayout tabel_1a,tabel_1b;
    Button btn_deskripsi,btn_respon;

    TextView txt_durasi;
    LinearLayout layout_durasi,layout_l,layout_pic2;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private ArrayList<Data_jenis_complain> jenis_complainList ;
    List<Data_work_report> itemList = new ArrayList<Data_work_report>();

    Data_cust_service detComplain;

    android.app.AlertDialog.Builder alertDialogBuilder;
    android.app.AlertDialog alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_complain);

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

        jenis_complainList = new ArrayList<Data_jenis_complain>();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intentku = getIntent(); // gets the previously created intent
        act_form = intentku.getStringExtra("act");
        detComplain = (Data_cust_service) intentku.getSerializableExtra(Api.PARAM_DATA);

        timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());

        txt_subject = findViewById(R.id.txt_subject);
        txt_complain_from = findViewById(R.id.txt_complain_from);
        txt_due_date = findViewById(R.id.txt_due_date);
        txt_periode_awal = findViewById(R.id.txt_periode_awal);
        txt_periode_akhir = findViewById(R.id.txt_periode_akhir);
        txt_complain_to = findViewById(R.id.txt_complain_to);
        txt_complain_purpose = findViewById(R.id.txt_complain_purpose);
        btn_deskripsi = findViewById(R.id.btn_deskripsi);
        btn_respon = findViewById(R.id.btn_respon);
        tabel_1a = findViewById(R.id.tabel_1a);
        tabel_1b = findViewById(R.id.tabel_1b);
        txt_jenis_complain = findViewById(R.id.txt_jenis_complain);
        txt_nmr_project = findViewById(R.id.txt_nmr_project);
        txt_nmr_subproject = findViewById(R.id.txt_nmr_subproject);
        txt_durasi = findViewById(R.id.txt_durasi);
        layout_durasi = findViewById(R.id.layout_durasi);
        layout_l = findViewById(R.id.layout_l);
        txt_l1 = findViewById(R.id.txt_l1);
        txt_l2 = findViewById(R.id.txt_l2);
        txt_l3 = findViewById(R.id.txt_l3);
        layout_pic2 = findViewById(R.id.layout_pic2);
        txt_pic2 = findViewById(R.id.txt_pic2);

        if (detComplain != null) {
            id_complain = detComplain.getid_pelanggan();
            complain_status = detComplain.getstatus_complain();
            jenis_complain = detComplain.getkat_complain();
            nmr_project = detComplain.getnmr_project();
            nmr_sub_project = detComplain.getnmr_sub_project();
            complain_to = detComplain.getsign_to();
            txt_complain_purpose.setText(detComplain.getjenis_complain());
            txt_subject.setText(detComplain.getsubject_complain());
            txt_complain_from.setText(detComplain.getnama_pelanggan());
            txt_complain_to.setText(detComplain.getsign_to());
            txt_due_date.setText(detComplain.getdue_date());
            txt_periode_awal.setText(detComplain.getperiode_awal());
            txt_periode_akhir.setText(detComplain.getperiode_akhir());
            txt_jenis_complain.setText(jenis_complain);
            txt_l1.setText(detComplain.getl1());
            txt_l2.setText(detComplain.getl2());
            txt_l3.setText(detComplain.getl3());
            txt_pic2.setText(detComplain.getpic2());

            if(jenis_complain.equals("Proak")){
                txt_durasi.setVisibility(View.VISIBLE);
                layout_durasi.setVisibility(View.VISIBLE);
                txt_nmr_project.setVisibility(View.GONE);
                txt_nmr_subproject.setVisibility(View.GONE);
                layout_l.setVisibility(View.VISIBLE);
                layout_pic2.setVisibility(View.GONE);
            }
            else if(jenis_complain.equals("Project")){
                txt_durasi.setVisibility(View.GONE);
                layout_durasi.setVisibility(View.GONE);
                txt_nmr_project.setVisibility(View.VISIBLE);
                txt_nmr_subproject.setVisibility(View.VISIBLE);
                layout_l.setVisibility(View.VISIBLE);
                layout_pic2.setVisibility(View.VISIBLE);

                txt_nmr_project.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        Intent intent = new Intent(detail_complain.this, edit_project2.class);
                        //intent.putExtra("act", "edit_outlet");
                        intent.putExtra("act", "edit_project");
                        intent.putExtra("id_project", nmr_project);
                        intent.putExtra("nmr_project", nmr_project);
                        intent.putExtra("create_by", "");
                        startActivityForResult(intent,1);

                        //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                    }
                });

                txt_nmr_subproject.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        Intent intent = new Intent(detail_complain.this, edit_sub_project2.class);
                        //intent.putExtra("act", "edit_outlet");
                        intent.putExtra("act", "edit_project");
                        intent.putExtra("nmr_sub_project", nmr_sub_project);
                        startActivityForResult(intent,1);

                        //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                txt_durasi.setVisibility(View.GONE);
                layout_durasi.setVisibility(View.GONE);
                txt_nmr_project.setVisibility(View.GONE);
                txt_nmr_subproject.setVisibility(View.GONE);
                layout_l.setVisibility(View.VISIBLE);
                layout_pic2.setVisibility(View.GONE);
            }

            getDeskripsi(id_complain);
        }



        btn_deskripsi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getApplicationContext(), add_deskripsi_complain_multi_file.class);
                intent.putExtra("act_form", "add_deskripsi");
                intent.putExtra("id_complain", id_complain);
                intent.putExtra("complain_status", complain_status);
                startActivityForResult(intent, 111);

                //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
            }
        });

        btn_respon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(detail_complain.this, list_respon.class);
                //intent.putExtra("act", "edit_outlet");
                intent.putExtra("id_complain", id_complain);
                intent.putExtra("complain_status", complain_status);
                intent.putExtra("complain_to", complain_to);
                startActivityForResult(intent,1);



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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(complain_status.equals("FINISH")) {
            getMenuInflater().inflate(R.menu.menu_action_user, menu);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            //Toast.makeText(getApplicationContext(), "Test ", Toast.LENGTH_LONG).show();
            pilihAct();
        }

        return super.onOptionsItemSelected(item);
    }

    private void pilihAct() {
        alertDialogBuilder = new android.app.AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(detail_complain.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(
                50, // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here
                0,
                50,
                0
        );

        final TableLayout tblRange = new TableLayout(detail_complain.this);
        tblRange.setColumnStretchable(0, true);
        //tblRange.setColumnStretchable(1, true);
        //tblRange.setColumnStretchable(2, true);

        TableRow row = new TableRow(detail_complain.this);
        row.setBackgroundColor(getResources().getColor(R.color.white));

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(3, 1, 3, 1);
        params.height = 100;

        TextView txt_accepted = new TextView(detail_complain.this);
        txt_accepted.setText("Close");
        txt_accepted.setTextColor(getResources().getColor(R.color.white));
        txt_accepted.setBackgroundResource(R.color.page_tbl_total);
        txt_accepted.setTextSize(10);
        txt_accepted.setGravity(Gravity.CENTER);
        txt_accepted.setPadding(10, 0, 10, 0);
        txt_accepted.setTypeface(null, Typeface.BOLD);
        txt_accepted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                cancelDialog(); //Implement method for canceling dialog

                proc_edit_deskripsi("close");
            }
        });

        // add to row
        row.addView(txt_accepted, params);
        tblRange.addView(row);

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



    private void get_id_id_complain(final String tanggal){
        id_complain =  id_user+"-"+tanggal+"-"+ String.valueOf(generateRandomNumbers(100001,999999));
        getDeskripsi(id_complain);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == 11) {
                Toast.makeText(this, "Deskripsi added ", Toast.LENGTH_LONG).show();
                cleanTable(tabel_1a);
                cleanTable(tabel_1b);
                getDeskripsi(id_complain);
            }
        }
    }


    private void getDeskripsi(final String id_complainn) {
        itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(detail_complain.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url_server = url_list_complain_deskripsi + id_complainn;
        Log.d("list_complain_deskripsi", url_server);
        JsonArrayRequest jArr = new JsonArrayRequest(url_server, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if(response.length()>0) {

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            Data_work_report jv = new Data_work_report();

                            jv.setid(obj.getString("id_complain"));
                            jv.setid_job_list(obj.getString("complain_nomor"));
                            jv.setketerangan(obj.getString("deskripsi"));
                            jv.setcomplain_foto(obj.getString("complain_foto"));

                            itemList.add(jv);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Klik deskripsi untuk melihat detail ", Toast.LENGTH_LONG).show();
                    callList1();
                    callList2();
                }
                else{
                    Toast.makeText(detail_complain.this, "Tidak bisa mendapatkan data ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                // notifikasi adanya perubahan data pada adapter
                //photoAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_complain.this, "Error Connection ", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        jArr.setRetryPolicy(new DefaultRetryPolicy(
                30000,//DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,//30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // menambah request ke request queue
        AppController.getInstance(this).addToRequestQueue(jArr);
        // progressDialog.dismiss();
    }

    private void callList1() {
        int nmr = 1;
        for (int i = 0; i < itemList.size(); i++) {
            final Data_work_report sel = itemList.get(i);

            TableRow row = new TableRow(detail_complain.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;
            params.width = 310;

            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params2.setMargins(3, 1, 3, 1);
            params2.height = 120;

            TextView txt_nmr = new TextView(detail_complain.this);
            txt_nmr.setText("" + nmr);
            txt_nmr.setBackgroundResource(R.color.page_join_visit);
            txt_nmr.setTextColor(getResources().getColor(R.color.white));
            txt_nmr.setTextSize(10);
            txt_nmr.setGravity(Gravity.CENTER);
            txt_nmr.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_nmr, params2);

            tabel_1a.addView(row);

            nmr++;
        }
    }

    private void callList2() {

        for (int i = 0; i < itemList.size(); i++) {
            final Data_work_report sel = itemList.get(i);

            TableRow row = new TableRow(detail_complain.this);
            row.setBackgroundColor(getResources().getColor(R.color.white));

            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 1, 3, 1);
            params.height = 90;

            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params2.setMargins(3, 1, 3, 1);
            params2.height = 120;

            TableRow.LayoutParams paramsImgTag = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsImgTag.setMargins(3, 1, 3, 1);
            paramsImgTag.height = 120;

            TextView txt_keterangan = new TextView(detail_complain.this);
            txt_keterangan.setText("" + Html.fromHtml(sel.getketerangan()));
            txt_keterangan.setBackgroundResource(R.color.page_join_visit);
            txt_keterangan.setTextColor(getResources().getColor(R.color.white));
            txt_keterangan.setTextSize(10);
            txt_keterangan.setGravity(Gravity.CENTER);
            txt_keterangan.setPadding(3, 0, 3, 0);
            txt_keterangan.setTypeface(null, Typeface.BOLD);
            txt_keterangan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(getApplicationContext(), add_deskripsi_complain.class);
                    Intent intent = new Intent(getApplicationContext(), detail_deskripsi_complain_multi_file.class);
                    intent.putExtra("act_form", "view_deskripsi");
                    intent.putExtra("keterangan", sel.getketerangan());
                    intent.putExtra("complain_foto", sel.getcomplain_foto());
                    intent.putExtra("id_complain", id_complain);
                    intent.putExtra("id_det_complain", sel.getid());
                    intent.putExtra("complain_from", "from_you");
                    startActivityForResult(intent, 111);
                }
            });
            // add to row
            row.addView(txt_keterangan, params2);

            ImageView img_coaching = new ImageView(detail_complain.this);
            img_coaching.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_clear_white_48));
            img_coaching.setBackgroundResource(R.color.page_join_visit);
            img_coaching.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getApplicationContext(), "tes hr "+hari, Toast.LENGTH_LONG).show();
                    proc_del_deskripsi(sel.getid());
                }
            });
            // add to row
            row.addView(img_coaching, paramsImgTag);

            tabel_1b.addView(row);

        }
    }

    private void proc_del_deskripsi(final String id_report){

        Log.e(TAG, "id_report: " + id_report);

        final ProgressDialog progressDialog = new ProgressDialog(detail_complain.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Delete...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_delete_deskripsi_padma_complain, new Response.Listener<String>() {

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

                        cleanTable(tabel_1a);
                        cleanTable(tabel_1b);
                        getDeskripsi(id_complain);
                        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Delete list work report", "Delete list work report on "+tgl_report,id_report);


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Del Work report Error: " + error.getMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_report", id_report);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

        //end proses
        progressDialog.dismiss();
    }

    private void proc_edit_deskripsi(final String status_complain){


        final ProgressDialog progressDialog = new ProgressDialog(detail_complain.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Process Update...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url_post_edit_status_padma_complain, new Response.Listener<String>() {

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

                        onBackPressedWithProcess();
                        //TrackingActivity.startTrackActivity(aktifitas_perdate.this, aktifitas_perdate.this, "list work report per date", "Delete list work report", "Delete list work report on "+tgl_report,id_report);


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Del Work report Error: " + error.getMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("complain_nomor", id_complain);
                params.put("complain_status", status_complain);
                params.put("create_by", id_user);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq);

        //end proses
        progressDialog.dismiss();
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }


    public void onBackPressedWithProcess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
