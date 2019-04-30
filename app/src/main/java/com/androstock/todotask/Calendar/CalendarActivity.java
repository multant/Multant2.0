package com.androstock.todotask.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.androstock.todotask.ActiveDesk.ActiveDesk;
import com.androstock.todotask.Home.HomeActivity;
import com.androstock.todotask.R;
import com.androstock.todotask.Task.TaskDBHelper;
import com.androstock.todotask.Task.TaskHome;
import com.androstock.todotask.chat.Chat_test;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity
{

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;


    private CalendarView calendarview;
    private List<EventDay> mEventDays = new ArrayList<>();
    TaskDBHelper database;
    TextView dateDisplay;
    Activity activity;
    ArrayList<Calendar> cal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);
        activity = CalendarActivity.this;
        database = new TaskDBHelper(activity);
        Calendar calen = Calendar.getInstance();
        for (int i=0; i<database.getNumberOfStrings(); i++){
            calen.setTimeInMillis(database.getMillis(i));
            mEventDays.add(new EventDay(calen, DrawableUtils.getThreeDots(this)));
            cal.add(calen);
            calen = (Calendar) calen.clone();
        }
        Calendar calendar = Calendar.getInstance();
        calendarview = findViewById(R.id.calendarView);
        calendarview.setEvents(mEventDays);

        try {
            calendarview.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    //открывает меню
                    Intent intent3 = new Intent(CalendarActivity.this, HomeActivity.class);
                    startActivity(intent3);
                    break;

                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(CalendarActivity.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    //открывает чат
                    Intent intent1 = new Intent(CalendarActivity.this, Chat_test.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    Intent intent2 = new Intent(CalendarActivity.this, ActiveDesk.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

}
