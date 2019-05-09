package com.androstock.todotask.Notes;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.androstock.todotask.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textViewCreate = (TextView) findViewById(R.id.textViewCreate);
        final TextView textViewChange = (TextView) findViewById(R.id.textViewChange);

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        final String finalDateAndTime = dateText + ", " + timeText;

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1)
        {
            editText.setText(Notes.notes.get(noteId));
            textViewCreate.setText(Notes.creates.get(noteId));
            textViewChange.setText(Notes.changes.get(noteId));
        }
        else
        {
            Notes.notes.add("");
            Notes.creates.add(finalDateAndTime);
            Notes.changes.add("");
            noteId = Notes.notes.size() - 1;
            textViewCreate.setText(finalDateAndTime);
            textViewChange.setText("");
            Notes.arrayAdapter.notifyDataSetChanged();
        }


        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Notes.notes.set(noteId, String.valueOf(s));
                Notes.changes.set(noteId, finalDateAndTime);
                textViewChange.setText(finalDateAndTime);
                Notes.arrayAdapter.notifyDataSetChanged();

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

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    void returnToNotes(View v)
    {
        finish();
    }
}
