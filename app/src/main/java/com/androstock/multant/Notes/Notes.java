package com.androstock.multant.Notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androstock.multant.ActiveDesk.ActiveDesk;
import com.androstock.multant.DB.MultantDBHelper;
import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Notes extends AppCompatActivity {

    static MultantDBHelper mydb;
    Activity activity;
    ProgressBar loader;
    ListView listView;

    static ArrayList<String> notes = new ArrayList<>();
    //При удалении записи из таблицы ключи оставшихся не изменяются
    static ArrayList<String> keys = new ArrayList<>();

    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        activity = Notes.this;
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

        Notes.LoadTask loadTask = new Notes.LoadTask();
        loadTask.execute();
    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notes.clear();
            keys.clear();
        }

        protected String doInBackground(String... args) {
            loadDataList(mydb.getData(1));
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
        if(cursor!=null ) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                keys.add(cursor.getString(0));
                notes.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
    }

    public void loadListView(ListView listView) {
        Date currentDate = new Date();
        DateFormat finalDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        if (notes.size() == 0) {
            notes.add("Example note");
            mydb.insert(notes.get(0),finalDateAndTime.format(currentDate),"");
        }
        //TODO: Сделать собственный Адаптер к списку
        arrayAdapter = new ArrayAdapter(this, R.layout.notes_list_item, notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(activity, NoteEditorActivity.class);
                /*i.putExtra("isUpdate", true);*/
                i.putExtra("noteId", Integer.parseInt(keys.get(position)));
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(Notes.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Вы уверены?")
                        .setMessage("Вы хотите удалить эту заметку?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                mydb.deleteData(keys.get(position), 1);
                                keys.remove(position);
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();

                return true;
            }
        });
    }

    void addNewNote(View v)
    {
        Intent intent5 = new Intent(getApplicationContext(), NoteEditorActivity.class);
        startActivity(intent5);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    //открывает меню
                    Intent intent3 = new Intent(Notes.this, HomeActivity.class);
                    startActivity(intent3);
                    break;

                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(Notes.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    //открывает чат
                    Intent intent1 = new Intent(Notes.this, Chat_test.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    Intent intent2 = new Intent(Notes.this, ActiveDesk.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };
}
