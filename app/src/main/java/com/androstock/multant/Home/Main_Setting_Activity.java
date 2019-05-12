package com.androstock.multant.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.androstock.multant.ActiveDesk.ActiveDesk;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_test;

public class Main_Setting_Activity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    Intent intent0 = new Intent(Main_Setting_Activity.this, HomeActivity.class);
                    startActivity(intent0);
                    return true;
                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(Main_Setting_Activity.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    Intent intent1 = new Intent(Main_Setting_Activity.this, Chat_test.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    Intent intent2 = new Intent(Main_Setting_Activity.this, ActiveDesk.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__setting_);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void onClickS(View v)
    {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        switch (v.getId())
        {
            case R.id.about_app_btn:
                v.startAnimation(animAlpha);
                Intent intent0 = new Intent(Main_Setting_Activity.this, About_app_scrolling_activity.class);
                startActivity(intent0);
                break;
            case R.id.developers_btn:
                v.startAnimation(animAlpha);
                Intent intent1 = new Intent(Main_Setting_Activity.this, Developers_Activity.class);
                startActivity(intent1);
                break;
            case R.id.questions_btn:
                v.startAnimation(animAlpha);
                Intent intent2 = new Intent(Main_Setting_Activity.this, Setting_Activity.class);
                startActivity(intent2);
                break;
        }
    }

}
