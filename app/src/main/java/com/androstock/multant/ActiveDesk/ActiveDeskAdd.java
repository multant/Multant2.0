package com.androstock.multant.ActiveDesk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.multant.R;

import java.util.Locale;

public static class ActiveDeskAdd extends AppCompatActivity {



    //TaskDBHelper mydb;
    String nameFinal;

    Intent intent;
    Boolean isUpdate;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        if (isUpdate) {
            init_update();
        }
    }

    public void init_update() {
        id = intent.getStringExtra("id");
        TextView toolbar_task_add_title = (TextView) findViewById(R.id.toolbar_task_add_title);
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        toolbar_task_add_title.setText("Update");
        if (task != null) {
            task.moveToFirst();

        }

    }

    public void closeAddTask(View v) {
        finish();
    }


    public void doneAddActiveDesk(View v) {
        int errorStep = 0;
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        nameFinal = task_name.getText().toString();
        dateFinal = task_date.getText().toString();


        /* Checking */
        if (nameFinal.trim().length() < 1) {
            errorStep++;
            task_name.setError("Provide a task name.");
        }

        if (dateFinal.trim().length() < 4) {
            errorStep++;
            task_date.setError("Provide a specific date");
        }



        if (errorStep == 0) {
            if (isUpdate) {
                mydb.updateContact(id, nameFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mydb.insertContact(nameFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }

            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
        }

    }

    public void onDeleteTask(View v) {
        mydb.deleteTask(id);
        finish();
    }
}
