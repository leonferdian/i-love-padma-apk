package com.project45.ilovepadma.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.Toast;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.global.Api;
import com.skyhope.eventcalenderlibrary.CalenderEvent;
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener;
import com.skyhope.eventcalenderlibrary.model.DayContainerModel;
import com.skyhope.eventcalenderlibrary.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class user_agenda extends AppCompatActivity {

    String id_user, username,bm,email,jabatan,id_company="",nama_company="";

    CalendarView simpleCalendarView;
    CalenderEvent calenderEvent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_agenda);

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

        simpleCalendarView = (CalendarView) findViewById(R.id.simpleCalendarView);
        calenderEvent = findViewById(R.id.calender_event);

        simpleCalendarView.setFocusedMonthDateColor(Color.BLACK); // set the red color for the dates of  focused month
        simpleCalendarView.setUnfocusedMonthDateColor(Color.YELLOW); // set the yellow color for the dates of an unfocused month
        //simpleCalendarView.setSelectedWeekBackgroundColor(Color.RED); // red color for the selected week's background
        simpleCalendarView.setWeekSeparatorLineColor(Color.GREEN); // green color for the week separator line

        // perform setOnDateChangeListener event on CalendarView
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }
        });



        //showEvent();
        //onAddEventClicked();
        //disp();
        displayEvent();
        calenderEvent.refreshDrawableState();
        calenderEvent.initCalderItemClickCallback(new CalenderDayClickListener() {
            @Override
            public void onGetDay(DayContainerModel dayContainerModel) {
                if(dayContainerModel.getEvent()!= null) {
                    Log.d("event", dayContainerModel.getDate() + " - " + dayContainerModel.getEvent().getEventText());
                }
                else{
                    Log.d("event", dayContainerModel.getDate());
                }
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

    public void displayEvent() {
        //https://github.com/mahimrocky/EventCalender
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2022-03-07 11:30:20"));

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2022-03-08 14:30:20"));

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 10);
            //Event event = new Event(calendar.getTimeInMillis(), "Test");
            //Event event = new Event(calendar.getTimeInMillis(), "Test");
            // to set desire day time milli second in first parameter
            //or you set color for each event
            Event event = new Event(cal.getTimeInMillis(), "Pengerjaan Agenda",Color.BLUE);
            calenderEvent.addEvent(event);

            Event event2 = new Event(cal2.getTimeInMillis(), "tes tiket",Color.RED);
            calenderEvent.addEvent(event2);
            //calenderEvent.removeEvent(event);


        }catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("upload_error", "info : "+exc.getMessage());
        }
    }

    public void showEvent() {
        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2022-03-06 10:30:20"));
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", false);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime",cal.getTimeInMillis() + 60 * 60 * 1000);
            intent.putExtra("title", " Test Title");
            intent.putExtra(CalendarContract.Events.DESCRIPTION, "This is a description");
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
            startActivity(intent);

        }catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("upload_error", "info : "+exc.getMessage());
        }
    }

    public void onAddEventClicked() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis() + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, "Siddharth Birthday");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "This is a description");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }

    public void disp() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(2013, 2, 13, 11, 35);
        Uri uri = Uri.parse("content://com.android.calendar/time/"
                + String.valueOf(startTime.getTimeInMillis()));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Use the Calendar app to view the time.
        startActivity(intent);
    }
}
