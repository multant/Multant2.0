package com.androstock.multant.ActiveDesk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_Groups;
import com.androstock.multant.ActiveDesk.ActiveDeskAdd;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.androstock.multant.ActiveDesk.Desk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public void testFunc(View v){
        Intent i2 = new Intent(ActiveDesk.this, addCardActivity.class);
        startActivity(i2);
    }

}
