package com.androstock.multant.chat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androstock.multant.ActiveDesk.ActiveDesk;
import com.androstock.multant.Calendar.CalendarActivity;
import com.androstock.multant.Diary.Entries;
import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.Notes.Notes;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_Groups;
import com.androstock.multant.chat.Chat_test;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.androstock.multant.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Chat_Groups extends AppCompatActivity {

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>(){
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
    String[] groupArr = new String[3];
    final List<String> groups = new ArrayList<String>();
    Group[] Arr2 =new Group [10];
    Group gr1 = new Group("Default");
    Group gr2 = new Group("Room 1");
    Group gr3 = new Group("Room 2");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__groups);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference ref = bd.getReference();
        ref.child("Rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String gr = postSnapshot.getKey();
                    int n = 0;
                    for (int i = 0;i<groups.size();i++){
                        if (groups.get(i).equals(gr))
                            n++;
                    }
                    if (n==0)
                    groups.add(gr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Arr2[0] = gr1;
        Arr2[1] = gr2;
        Arr2[2] = gr3;
        int n = 2;
        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.shit, groups);
        ListView list = (ListView) findViewById(R.id.groupView);
        TextView text = (TextView) findViewById(R.id.group_name);
        list.setAdapter(adapter);
       /* for (int i = 0; i < 3; i++) {
            groupArr[i] = Arr2[i].getName();
            list.setAdapter(adapter);
        }*/
        ref.child("Rooms").child("Room 4").child("Password").setValue(" ");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp;
                for (int i = 0; i < groups.size(); i++) {
                    temp = (String) parent.getItemAtPosition(position);
                    if (temp.equals(groups.get(i))) {
                        Intent intent1 = new Intent(Chat_Groups.this, Chat_test.class);
                        intent1.putExtra("group", temp);
                        startActivity(intent1);
                        break;
                    }
                }
            }
        });
    }
        // Навигация которая должна быть в каждом из основных активити
     private   BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            // Навигация которая должна быть в каждом из основных активити
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_main:
                        Intent intent0 = new Intent(Chat_Groups.this, HomeActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.navigation_daily_log:
                        //открывает ежедневник
                        Intent intent = new Intent(Chat_Groups.this, TaskHome.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_chat:
                        return true;
                    case R.id.navigation_task_board:
                        Intent intent2 = new Intent(Chat_Groups.this, ActiveDesk.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        };

    public void addNew(View view) {
        Intent i = new Intent(Chat_Groups.this, Chat_add.class);
        startActivity(i);
    }
}
