package com.androstock.todotask.Notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.androstock.todotask.ActiveDesk.ActiveDesk;
import com.androstock.todotask.Home.HomeActivity;
import com.androstock.todotask.R;
import com.androstock.todotask.Task.TaskHome;
import com.androstock.todotask.chat.Chat_test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class Notes extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayList<String> creates = new ArrayList<>();
    static ArrayList<String> changes = new ArrayList<>();


    static ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        ListView listView = (ListView) findViewById(R.id.listView);
        Button buttonAdd = (Button) findViewById(R.id.buttonAddNote);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.androstock.todotask", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("com.androstock.todotask", Context.MODE_PRIVATE);
        HashSet<String> set1 = (HashSet<String>) sharedPreferences1.getStringSet("creates", null);
        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("com.androstock.todotask", Context.MODE_PRIVATE);
        HashSet<String> set2 = (HashSet<String>) sharedPreferences2.getStringSet("changes", null);

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        String finalDateAndTime = dateText + ", " + timeText;

        if (set == null || set1 == null || set2 == null)
        {
            notes.add("Example note");
            creates.add(finalDateAndTime);
            changes.add("");
        }
        else
        {
            notes = new ArrayList(set);
            creates = new ArrayList(set1);
            changes = new ArrayList(set2);
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent5 = new Intent(getApplicationContext(), NoteEditorActivity.class);
                startActivity(intent5);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent4 = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent4.putExtra("noteId", position);
                startActivity(intent4);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                new AlertDialog.Builder(Notes.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                notes.remove(position);
                                creates.remove(position);
                                changes.remove(position);

                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.androstock.todotask", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(Notes.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();

                                SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("com.androstock.todotask", Context.MODE_PRIVATE);
                                HashSet<String> set1 = new HashSet(Notes.creates);
                                sharedPreferences1.edit().putStringSet("creates", set1).apply();

                                SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("com.androstock.todotask", Context.MODE_PRIVATE);
                                HashSet<String> set2 = new HashSet(Notes.changes);
                                sharedPreferences2.edit().putStringSet("changes", set2).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
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
