package com.androstock.multant.chat;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androstock.multant.Home.HomeActivity;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Chat_test extends AppCompatActivity {

    Activity activity;
    NestedScrollView scrollView;
    Button button;
    private FirebaseListAdapter<Message> adapter;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Подсвечивание выбранного пункта
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.editText);
                FirebaseDatabase.getInstance().getReference().push()
                        .setValue(new Message(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                input.setText("");
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            displayChat();
        }
    }
    private void displayChat() {
        Query query = FirebaseDatabase.getInstance().getReference();
        ListView listMessages = (ListView)findViewById(R.id.listView);
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setLayout(R.layout.item)
                .setQuery(query, Message.class)
                .build();
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView textMessage, autor, timeMessage;
                textMessage = (TextView)v.findViewById(R.id.tvMessage);
                autor = (TextView)v.findViewById(R.id.tvUser);
                timeMessage = (TextView)v.findViewById(R.id.tvTime);
                textMessage.setText(model.getTextMessage());
                autor.setText(model.getAutor());
                timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessages.setAdapter(adapter);
        adapter.startListening();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        // Навигация которая должна быть в каждом из основных активити
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    Intent intent0 = new Intent(Chat_test.this, HomeActivity.class);
                    startActivity(intent0);
                    break;
                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(Chat_test.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    Intent intent1 = new Intent(Chat_test.this, Chat_test.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    return true;
            }
            return false;
        }
    };
}