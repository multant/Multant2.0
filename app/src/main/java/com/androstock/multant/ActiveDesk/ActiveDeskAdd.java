package com.androstock.multant.ActiveDesk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androstock.multant.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.androstock.multant.ActiveDesk.Desk;

public class ActiveDeskAdd extends AppCompatActivity{


    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private EditText name_desk;
    private Button btn_new_desk;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_add);

        myRef = FirebaseDatabase.getInstance().getReference();

        btn_new_desk = (Button)findViewById(R.id.addActiveDesk);
        name_desk = (EditText)findViewById(R.id.active_desk_name);

        btn_new_desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name_desk.getText().toString().trim().length() < 1) {
                    Toast.makeText(ActiveDeskAdd.this, "Введите название доски!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActiveDeskAdd.this, "Успешно!", Toast.LENGTH_SHORT).show();
                    String key = myRef.child(user.getUid()).child("Desks").push().getKey();
                    myRef.child(user.getUid()).child("Desks").child(key).setValue(new Desk(name_desk.getText().toString(), user.getUid(), key));

                    finish();
                }
            }
        });
    }

    public void closeAddActiveDesk(View v) {
        finish();
    }

}