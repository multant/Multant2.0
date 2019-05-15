package com.androstock.multant.Notes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.androstock.multant.DB.Function;
import com.androstock.multant.R;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;
    String notecreatedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textViewCreate = (TextView) findViewById(R.id.textViewCreate);
        final TextView textViewChange = (TextView) findViewById(R.id.textViewChange);

        Date currentDate = new Date();
        DateFormat finalDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1)
        {
            Cursor res = Notes.mydb.getDataSpecific(Integer.toString(noteId),1);
            if (res!=null)
            {
                if (res.moveToFirst())
                {
                    editText.setText(res.getString(1));
                    notecreatedate = Function.Epoch2DateString(res.getString(2), "dd.MM.yyyy HH:mm:ss");
                    textViewCreate.setText(notecreatedate);
                    textViewChange.setText(Function.Epoch2DateString(res.getString(3), "dd.MM.yyyy HH:mm:ss"));
                }
                res.close();
            }
        }
        else
        {
            notecreatedate = finalDateAndTime.format(currentDate);
            textViewCreate.setText(notecreatedate);
            textViewChange.setText("");
            Notes.mydb.insert("",finalDateAndTime.format(currentDate),"");
            //Номер заметки устанавливается как следующий за последним ключом
            noteId = Integer.parseInt(Notes.keys.get(Notes.keys.size()-1))+1;
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
                textViewChange.setText(finalDateAndTime.format(currentDate));
                Notes.mydb.update(Integer.toString(noteId),String.valueOf(s),notecreatedate,finalDateAndTime.format(currentDate));
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
