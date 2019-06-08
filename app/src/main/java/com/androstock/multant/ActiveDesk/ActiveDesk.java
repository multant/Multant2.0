package com.androstock.multant.ActiveDesk;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleObserver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androstock.multant.Diary.Entries;
import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_Groups;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActiveDesk extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();

    private List<String> ids = new ArrayList<>();
    private List<String> desks = new ArrayList<>();
    private FirebaseListAdapter<Desk> adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    Intent intent0 = new Intent(ActiveDesk.this, HomeActivity.class);
                    startActivity(intent0);
                    break;
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

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        ListView listDesks = (ListView)findViewById(R.id.deskView);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            displayDesk(listDesks);
        }


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

        listDesks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(ActiveDesk.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Вы уверены?")
                        .setMessage("Вы хотите удалить эту доску?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef.child("Desks").child(ids.get(position)).removeValue();
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();

                return true;
            }
        });
    }



    private void displayDesk(ListView listDesks) {


        myRef.child("Desks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> delete = new ArrayList<>();
                //Query query = FirebaseDatabase.getInstance().getReference().child("Desks");
                List<String> all = new ArrayList<>();
                List<Desk> desks_query = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    int n = 0;
                    Desk d = postSnapshot.getValue(Desk.class);
                    all.clear();
                    all.addAll(d.getAllows());
                    for (int i = 0; i < all.size(); i++) {
                        if (all.get(i).equals(user.getEmail())) {
                            n++;
                        }
                    }
                    if (n != 0) {
                        desks_query.add(d);
                    }
                }
                List<View> views = new ArrayList<>();
                Query query = myRef.child("Desks");
                FirebaseListOptions<Desk> options = new FirebaseListOptions.Builder<Desk>()
                        .setLayout(R.layout.active_desk_list_row)
                        .setQuery(query, Desk.class)
                        .build();
                adapter = new FirebaseListAdapter<Desk>(options) {
                    @Override
                    protected void populateView(View v, Desk model, int position) {
                        for (int i = 0; i < desks_query.size(); i++){
                            if(model.getId().equals(desks_query.get(i).getId())){
                                TextView nameDesk;
                                nameDesk = (TextView) v.findViewById(R.id.text1);
                                nameDesk.setText(model.getNameDesk());
                                desks.add(model.getNameDesk());
                                ids.add(model.getId());
                            }else{
                                v.invalidate();

                            }
                        }
                    }
                };
                adapter.onDataChanged();
                listDesks.setAdapter(adapter);
                adapter.startListening();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onAddActiveDeskClick(View view)
    {
        Intent i = new Intent(ActiveDesk.this, ActiveDeskAdd.class);
        startActivity(i);
    }


}
