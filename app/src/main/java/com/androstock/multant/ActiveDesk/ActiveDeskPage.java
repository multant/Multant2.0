package com.androstock.multant.ActiveDesk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.androstock.multant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class ActiveDeskPage extends FragmentActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    Desk desk = new Desk();



    //private Desk desk = new Desk("", "", "", 0);

    private String name_desk = "";
    private String id_desk = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_page);
        this.id_desk = getIntent().getExtras().getString("id_desk");
        desk.setId(id_desk);
        FirebaseUser user = mAuth.getInstance().getCurrentUser();

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child(user.getUid()).child("Desks").equalTo(id_desk).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot deskSnapshot: dataSnapshot.getChildren()) {
                     desk = deskSnapshot.getValue(Desk.class);
                }
                name_desk = desk.getNameDesk();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        TextView title = (TextView) findViewById(R.id.toolbar_active_desk_page_title);
        title.setText(this.name_desk);
    }

    public void closeActiveDeskPage(View v) {
        finish();
    }
}
