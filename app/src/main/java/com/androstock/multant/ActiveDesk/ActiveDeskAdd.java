package com.androstock.multant.ActiveDesk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.multant.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ActiveDeskAdd extends AppCompatActivity{

    String nameFinal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_add);
    }

    public void closeAddActiveDesk(View v) {
        finish();
    }


    public void doneAddActiveDesk(View v) {
        EditText active_desk_name = (EditText) findViewById(R.id.active_desk_name);
        nameFinal = active_desk_name.getText().toString();
        Desk desk = new Desk();
        desk.setNameDesk(nameFinal);

        /* Checking */
        if (nameFinal.trim().length() < 1) {
            active_desk_name.setError("Введите название доски.");
        } else {
            FirebaseDatabase.getInstance().getReference().child("Desks").child(nameFinal).setValue("");
            finish();
        }
    }
}
