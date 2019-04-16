package com.androstock.todotask.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.androstock.todotask.R;
import com.androstock.todotask.Task.TaskHome;
import com.androstock.todotask.chat.Chat_test;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:

                    return true;
                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(HomeActivity.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    Intent intent1 = new Intent(HomeActivity.this, Chat_test.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    public void onClick(View v)
    {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        switch (v.getId())
        {
            case R.id.linearLayout_main:
                v.startAnimation(animAlpha);
                break;
            case R.id.linearLayout_chat:
                v.startAnimation(animAlpha);
                Intent intent1 = new Intent(HomeActivity.this, Chat_test.class);
                startActivity(intent1);
                break;
            case R.id.linearLayout_Calendar:
                v.startAnimation(animAlpha);
                break;
            case R.id.linearLayout_trello:
                v.startAnimation(animAlpha);
                break;
            case R.id.linearLayout_notes:
                Intent intent = new Intent(HomeActivity.this, TaskHome.class);
                startActivity(intent);
                v.startAnimation(animAlpha);
                break;
            case R.id.linearLayout_planning:
                v.startAnimation(animAlpha);
                break;
            case R.id.linearLayout_diary:
                v.startAnimation(animAlpha);
                break;
        }
    }
}