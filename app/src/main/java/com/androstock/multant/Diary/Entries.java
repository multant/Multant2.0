package com.androstock.multant.Diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.*;

import com.androstock.multant.ActiveDesk.ActiveDesk;
import com.androstock.multant.DB.Function;
import com.androstock.multant.DB.MultantDBHelper;
import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Entries extends AppCompatActivity {

    static MultantDBHelper mydb;
    Activity activity;
    ProgressBar loader;
    ListView listView;
    DatePickerDialog dpd;

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
        loader = (ProgressBar) findViewById(R.id.loader);
        listView = (ListView) findViewById(R.id.listView);
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

        Entries.LoadTask loadTask = new Entries.LoadTask();
        loadTask.execute();
    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            datalist.clear();
        }

        protected String doInBackground(String... args) {
            loadDataList(mydb.getcreateddatesortedData(2));
            return "";
        }

        @Override
        protected void onPostExecute(String string) {

            loadListView(listView);
            loader.setVisibility(View.GONE);
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
                hashMap.put(CREATEDATE, Function.Epoch2DateString(cursor.getString(2),"dd.MM.yyyy HH:mm:ss"));
                hashMap.put(TITLE, cursor.getString(3));
                datalist.add(hashMap);
                cursor.moveToNext();
            }
        }
    }

    public void loadListView(ListView listView) {
        Date currentDate = new Date();
        DateFormat finalDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        if (datalist.size() == 0) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ENTRY, "Example entry");
            hashMap.put(CREATEDATE, finalDateAndTime.format(currentDate));
            hashMap.put(TITLE, "Example Title");
            hashMap.put(KEY,"1");
            datalist.add(hashMap);
            mydb.entryinsert(hashMap.get(ENTRY),hashMap.get(CREATEDATE),hashMap.get(TITLE));
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

    public void addNewEntry(View v)
    {
        Intent intent7 = new Intent(getApplicationContext(), EntryEditorActivity.class);
        startActivity(intent7);
    }

    public void showStartDatePicker(View v) {
        DialogFragment dialogFragment = new DatePickerDialogTheme3();
        Bundle bundle = new Bundle();
        bundle.putInt("whichDate", 1);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "Theme 3");
    }

    public void showFinishDatePicker(View v) {
        DialogFragment dialogFragment = new DatePickerDialogTheme3();
        Bundle bundle = new Bundle();
        bundle.putInt("whichDate", 2);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "Theme 3");
    }

    public static class DatePickerDialogTheme3 extends DialogFragment implements DatePickerDialog.OnDateSetListener{


        int whichDate;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK, this, year, month, day);

            Bundle args = getArguments();
            whichDate = args.getInt("whichDate");
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){

            if(whichDate == 1){
                EditText editText = (EditText) getActivity().findViewById(R.id.start_date_text);
                editText.setText(day + "/" + (month+1) + "/" + year);
            } else if(whichDate == 2){
                EditText editText = (EditText) getActivity().findViewById(R.id.end_date_text);
                editText.setText(day + "/" + (month+1) + "/" + year);
            }

        }
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
                    Intent intent1 = new Intent(Entries.this, Chat_test.class);
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
