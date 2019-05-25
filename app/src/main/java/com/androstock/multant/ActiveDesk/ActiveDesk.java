package com.androstock.multant.ActiveDesk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
//import com.androstock.multant.ActiveDesk.ListActiveDeskAdapter;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_test;
import com.androstock.multant.ActiveDesk.ActiveDeskAdd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActiveDesk extends AppCompatActivity {

    Activity activity;
    final List<String> desks = new ArrayList<String>();


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
                    Intent intent1 = new Intent(ActiveDesk.this, Chat_test.class);
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

        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference ref = bd.getReference();
        ref.child("Desks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String desk_key = postSnapshot.getKey();
                    int n = 0;
                    for (int i = 0;i<desks.size();i++){
                        if (desks.get(i).equals(desk_key))
                            n++;
                    }
                    if (n==0)
                        desks.add(desk_key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.active_desk_list_row, desks);
        ListView list = (ListView) findViewById(R.id.deskView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp;
                for (int i = 0; i < desks.size(); i++) {
                    temp = (String) parent.getItemAtPosition(position);
                    if (temp.equals(desks.get(i))) {
                        Intent intent1 = new Intent(ActiveDesk.this, ActiveDeskPage.class);
                        intent1.putExtra("desk", temp);
                        startActivity(intent1);
                        break;
                    }
                }
            }
        });
    }

    public void onAddActiveDeskClick(View view)
    {
        Intent i = new Intent(ActiveDesk.this, ActiveDeskAdd.class);
        startActivity(i);
    }
}
