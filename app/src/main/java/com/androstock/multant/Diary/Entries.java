package com.androstock.multant.Diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.multant.ActiveDesk.ActiveDesk;
import com.androstock.multant.DB.Function;
import com.androstock.multant.DB.MultantDBHelper;
import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_Groups;
import com.androstock.multant.chat.Chat_test;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.androstock.multant.DB.Function.Epoch2DateString;

public class Entries extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    static MultantDBHelper mydb;
    Activity activity;
    ProgressBar loader;
    ListView listView;
    EditText start_date_text,end_date_text;
    TextView starttextView, endtextView;
    DatePickerDialog dpd;
    String startdate, enddate;
    int edittextpick = -1,startYear = 0, startMonth = 0, startDay = 0;
    boolean setdate =false;

    public static final String KEY = "key";
    private static final String TITLE = "title";
    private static final String ENTRY = "entry";
    private static final String CREATEDATE = "createdate";

   static ArrayList<HashMap<String, String>> datalist = new ArrayList<>();

    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        activity = Entries.this;
        mydb = new MultantDBHelper(activity);
        loader = findViewById(R.id.loader);
        listView = findViewById(R.id.listView);
        start_date_text = findViewById(R.id.start_date_text);
        end_date_text = findViewById(R.id.end_date_text);
        starttextView= findViewById(R.id.starttextView);
        endtextView = findViewById(R.id.endtextView);
    }

    @Override
    public void onResume () {
        super.onResume();
        populateData();
    }

    public void populateData ()
    {
        mydb = new MultantDBHelper(activity);
        loader.setVisibility(View.VISIBLE);
        start_date_text.setVisibility(View.GONE);
        end_date_text.setVisibility(View.GONE);
        starttextView.setVisibility(View.GONE);
        endtextView.setVisibility(View.GONE);
        Entries.LoadTask loadTask = new Entries.LoadTask();
        loadTask.execute();
    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            datalist.clear();
            if (!setdate)
            {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                try {
                    Date firstdate = format.parse(Epoch2DateString(mydb.getminEntryDate(),"dd/MM/yyyy HH:mm:ss"));
                    Date lastdate = format.parse(Epoch2DateString(mydb.getmaxEntryDate(),"dd/MM/yyyy HH:mm:ss"));
                    format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    startdate = format.format(firstdate);
                    enddate = format.format(lastdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected String doInBackground(String... args) {
            loadDataList(mydb.getDatefilteredentries(startdate + " 00:00:00",enddate+ " 00:00:00"));
            return "";
        }

        @Override
        protected void onPostExecute(String string) {
            loadListView(listView);
            loader.setVisibility(View.GONE);
            start_date_text.setVisibility(View.VISIBLE);
            end_date_text.setVisibility(View.VISIBLE);
            starttextView.setVisibility(View.VISIBLE);
            endtextView.setVisibility(View.VISIBLE);
        }
    }

    public void loadDataList(Cursor cursor)
    {
        HashMap<String, String> hashMap;
        if(cursor!=null ) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                hashMap = new HashMap<>();
                hashMap.put(KEY, cursor.getString(0));
                hashMap.put(ENTRY, cursor.getString(1));
                hashMap.put(CREATEDATE, Epoch2DateString(cursor.getString(2),"dd/MM/yyyy HH:mm:ss"));
                hashMap.put(TITLE, cursor.getString(3));
                datalist.add(hashMap);
                cursor.moveToNext();
            }
        }

    }

    public void loadListView(ListView listView) {
        if (datalist.size() == 0) {
            Date currentDate = new Date();
            DateFormat finalDateAndTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            startdate = finalDateAndTime.format(currentDate);
            enddate = finalDateAndTime.format(currentDate);
            finalDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ENTRY, "Example entry");
            hashMap.put(CREATEDATE, finalDateAndTime.format(currentDate));
            hashMap.put(TITLE, "Example Title");
            hashMap.put(KEY, "1");
            datalist.add(hashMap);
            mydb.entryinsert(hashMap.get(ENTRY), hashMap.get(CREATEDATE), hashMap.get(TITLE));
        }
        adapter = new SimpleAdapter(this, datalist,
                R.layout.entries_list_item, new String[]{TITLE, ENTRY, CREATEDATE},
                new int[]{R.id.titletextView, R.id.entrytextView, R.id.datetextView});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(activity, EntryEditorActivity.class);
                i.putExtra("noteId", Integer.parseInt(datalist.get(position).get(KEY)));
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(Entries.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Вы уверены?")
                        .setMessage("Вы хотите удалить эту заметку?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mydb.deleteData(datalist.get(position).get(KEY), 2);
                                datalist.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();

                return true;
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        String date = dateinttoText(startYear, startMonth, startDay);
        if (edittextpick==0)
        {
            EditText task_date = findViewById(R.id.start_date_text);
            task_date.setText(date);
            startdate = date;
        }
        else if (edittextpick==1)
        {
            EditText task_date = findViewById(R.id.end_date_text);
            task_date.setText(date);
            enddate = date;
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Неизвестная ошибка",Toast.LENGTH_SHORT).show();
        }
        setdate = true;
        populateData();
        edittextpick = -1;
    }

    public String dateinttoText (int year, int monthOfYear, int dayOfMonth)
    {
        int monthAddOne = monthOfYear + 1;
        return (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" +
                (monthAddOne < 10 ? "0" + monthAddOne : monthAddOne) + "/" +
                year;
    }

    public void addNewEntry(View v)
    {
        Intent intent7 = new Intent(getApplicationContext(), EntryEditorActivity.class);
        startActivity(intent7);
    }

    public void showStartEntryDatePicker(View v) {
        edittextpick = 0;
        showDateDialog();
    }

    public void showEndEntryDatePicker(View v) {
        edittextpick = 1;
        showDateDialog();
    }

    public void showDateDialog()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);
        dpd = DatePickerDialog.newInstance(Entries.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.setAccentColor(getResources().getColor(R.color.entrydatepickerheadercolour));
        dpd.setMinDate(Function.Epoch2Calender(mydb.getminEntryDate()));
        dpd.setMaxDate(Function.Epoch2Calender(mydb.getmaxEntryDate()));
        dpd.show(getFragmentManager(), "startDatepickerdialog");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    //открывает меню
                    Intent intent3 = new Intent(Entries.this, HomeActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(Entries.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    //открывает чат
                    Intent intent1 = new Intent(Entries.this, Chat_Groups.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    Intent intent2 = new Intent(Entries.this, ActiveDesk.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };
}
