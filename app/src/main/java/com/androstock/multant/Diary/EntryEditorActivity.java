package com.androstock.multant.Diary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.multant.DB.Function;
import com.androstock.multant.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EntryEditorActivity extends AppCompatActivity {

    int noteId;
    String entrycreatedate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_editor);

        EditText editText = (EditText) findViewById(R.id.editText);
        TextView TitleeditText = (TextView) findViewById(R.id.TitleeditText);
        final TextView datetextView = (TextView) findViewById(R.id.datetextView);

        Date currentDate = new Date();
        DateFormat finalDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1)
        {
            Cursor res = Entries.mydb.getDataSpecific(Integer.toString(noteId),2);
            if (res!=null)
            {
                if (res.moveToFirst())
                {
                    editText.setText(res.getString(1));
                    entrycreatedate =Function.Epoch2DateString(res.getString(2), "dd/MM/yyyy HH:mm:ss");
                    datetextView.setText(entrycreatedate);
                    TitleeditText.setText(res.getString(3));
            }
                res.close();
            }
        }
        else
        {
            entrycreatedate =finalDateAndTime.format(currentDate);
            datetextView.setText(entrycreatedate);
            TitleeditText.setText("New title");
            Entries.mydb.entryinsert("",finalDateAndTime.format(currentDate),"New title");
            //Номер заметки устанавливается как следующий за последним ключом
            noteId = Integer.parseInt(Entries.datalist.get(Entries.datalist.size()-1).get(Entries.KEY))+1;
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
                Entries.mydb.entryupdate(Integer.toString(noteId), String.valueOf(s), entrycreatedate, String.valueOf(TitleeditText.getText()));
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        TitleeditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Entries.mydb.entryupdate(Integer.toString(noteId), String.valueOf(editText.getText()), entrycreatedate,String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    public void returnToEntries(View v)
    {
        EditText editText = findViewById(R.id.editText);
        String noteText = editText.getText().toString();
        if(noteText.trim().length() < 1){
            Toast.makeText(getApplicationContext(), "Запись в ежедневнике не может быть пустой", Toast.LENGTH_SHORT).show();
        } else{finish();}
    }
}
