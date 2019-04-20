package com.androstock.todotask.Calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.todotask.ActiveDesk.ActiveDesk;
import com.androstock.todotask.Home.HomeActivity;
import com.androstock.todotask.R;
import com.androstock.todotask.Task.TaskDBHelper;
import com.androstock.todotask.Task.TaskHome;
import com.androstock.todotask.chat.Chat_test;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
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


    private com.applandeo.materialcalendarview.CalendarView calendarview;
    private List<EventDay> mEventDays = new ArrayList<>();
    TaskDBHelper database;
    TextView dateDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);

        calendarview = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();

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
