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

    FirebaseListAdapter mAdapter;

    ListView ListUserDesks;

    Activity activity;
    private List<String> desks = new ArrayList<String>();
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

    /*private  static class DeskViewHolder extends RecyclerView.ViewHolder{

        TextView mTitleDesk;

        public DeskViewHolder(View itemView) {
            super(itemView);
            mTitleDesk = (TextView) itemView.findViewById(R.id.tv_title_desk);
        }
    }*/


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

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            displayDesk();
        }
        /*RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_list_desks);

        FirebaseRecyclerAdapter adapter;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);*/
        /*adapter = new FirebaseRecyclerAdapter<String, DeskViewHolder>(
                String.class,
                R.layout.active_desk_list_row,
                DeskViewHolder.class,
                myRef.child(user.getUid()).child("Desks")
        ) {
            @Override
            protected void populateViewHolder(DeskViewHolder viewHolder, String title) {
                viewHolder.mTitleDesk.setText(title);
            }
        };*/


        /*ListUserDesks = (ListView) findViewById(R.id.deskView);

        myRef = FirebaseDatabase.getInstance().getReference();

        mAdapter = new FirebaseListAdapter <String>(
                this, String.class, android.R.layout.simple_list_item_1, myRef.child(user.getUid()).child("Desks"))
        {
            @Override
            protected void populateView(View v, String s, int position) {
                TextView text = (TextView) v.findViewById(android.R.id.text1);
                text.setText(s);
            }
        };
        ListUserDesks.setAdapter(mAdapter);*/


        /*ListUserDesks = (ListView) findViewById(R.id.deskView);

        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                desks = dataSnapshot.child("Desks").getValue(t);
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

      /*  FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference ref = bd.getReference();
        ref.child(user.getUid()).child("Desks").child().addListenerForSingleValueEvent(new ValueEventListener() {
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
                        intent1.putExtra("desk_name", temp);
                        startActivity(intent1);
                        break;
                    }
                }
            }
        });

*/



    }



    private void displayDesk() {
        Query query = FirebaseDatabase.getInstance().getReference().child(this.user.getUid()).child("Desks");
        ListView listDesks = (ListView)findViewById(R.id.deskView);
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
            }
        };
        listDesks.setAdapter(adapter);

        adapter.startListening();
    }



    /*private void updateUI(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1);

        ListUserDesks.setAdapter(adapter);
    }*/

    public void onAddActiveDeskClick(View view)
    {
        Intent i = new Intent(ActiveDesk.this, ActiveDeskAdd.class);
        startActivity(i);
    }
}
