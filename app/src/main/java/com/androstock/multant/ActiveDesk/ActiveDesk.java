package com.androstock.multant.ActiveDesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_Groups;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ActiveDesk extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();


    private List<String> desks = new ArrayList<>();
    private FirebaseListAdapter<Desk> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    Intent intent0 = new Intent(ActiveDesk.this, HomeActivity.class);
                    startActivity(intent0);
                    return true;
                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(ActiveDesk.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    Intent intent1 = new Intent(ActiveDesk.this, Chat_Groups.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    Intent intent2 = new Intent(ActiveDesk.this, ActiveDesk.class);
                    startActivity(intent2);
                    break;
                    default: return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        ListView listDesks = (ListView)findViewById(R.id.deskView);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            displayDesk(listDesks);
        }

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        listDesks.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < desks.size(); i++) {
                        Desk d = (Desk) (listDesks.getItemAtPosition(position));
                        if (d.getNameDesk().equals(desks.get(i))) {
                            Intent intent1 = new Intent(ActiveDesk.this, ActiveDeskPage.class);
                            intent1.putExtra("id_desk", d.getId());
                            intent1.putExtra("page_pos", 0);
                            startActivity(intent1);
                            break;
                        }
                    }
            }
        });


    }



    private void displayDesk(ListView listDesks) {
        Query query = FirebaseDatabase.getInstance().getReference().child(this.user.getUid()).child("Desks");
        FirebaseListOptions<Desk> options = new FirebaseListOptions.Builder<Desk>()
                .setLayout(R.layout.active_desk_list_row)
                .setQuery(query, Desk.class)
                .build();
        adapter = new FirebaseListAdapter<Desk>(options) {
            @Override
            protected void populateView(View v, Desk model, int position) {
                TextView nameDesk;
                nameDesk = (TextView)v.findViewById(R.id.text1);
                nameDesk.setText(model.getNameDesk());
                desks.add(model.getNameDesk());
            }
        };

        listDesks.setAdapter(adapter);
        adapter.startListening();
    }


    public void onAddActiveDeskClick(View view)
    {
        Intent i = new Intent(ActiveDesk.this, ActiveDeskAdd.class);
        startActivity(i);
    }


}
