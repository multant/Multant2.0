package com.androstock.multant.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androstock.multant.ActiveDesk.ActiveDesk;
import com.androstock.multant.Calendar.CalendarActivity;
import com.androstock.multant.Diary.Entries;
import com.androstock.multant.Notes.Notes;
import com.androstock.multant.R;
import com.androstock.multant.Task.TaskHome;
import com.androstock.multant.chat.Chat_Groups;
import com.androstock.multant.chat.Chat_test;
import com.androstock.multant.chat.Group;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {
    RelativeLayout activity_main;
    private TextView mTextMessage;
    private static int SIGN_IN_REQUEST_CODE = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    //открывает меню
                    return true;
                case R.id.navigation_daily_log:
                    //открывает ежедневник
                    Intent intent = new Intent(HomeActivity.this, TaskHome.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chat:
                    //открывает чат
                    Intent intent1 = new Intent(HomeActivity.this, Chat_Groups.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_task_board:
                    //открывает доску задач
                    Intent intent2 = new Intent(HomeActivity.this, ActiveDesk.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),SIGN_IN_REQUEST_CODE
                    );
        }

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        TextView name = (TextView) findViewById(R.id.Name_Text);
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Snackbar.make(activity_main, "Вход выполнен", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(activity_main, "Вход не выполнен", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }*/
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
                Intent intent1 = new Intent(HomeActivity.this, Chat_Groups.class);
                startActivity(intent1);
                break;
            case R.id.linearLayout_Calendar:
                v.startAnimation(animAlpha);
                Intent intent5 = new Intent(HomeActivity.this, CalendarActivity.class);
                startActivity(intent5);
                break;
            case R.id.linearLayout_trello:
                v.startAnimation(animAlpha);
                Intent intent2 = new Intent(HomeActivity.this, ActiveDesk.class);
                startActivity(intent2);
                break;
            case R.id.linearLayout_notes:
                v.startAnimation(animAlpha);
                Intent intent = new Intent(HomeActivity.this, Notes.class);
                startActivity(intent);
                break;
            case R.id.linearLayout_planning:
                v.startAnimation(animAlpha);
                Intent intent6 = new Intent(HomeActivity.this, TaskHome.class);
                startActivity(intent6);
                break;
            case R.id.linearLayout_diary:
                v.startAnimation(animAlpha);
                Intent intent7 = new Intent(HomeActivity.this, Entries.class);
                startActivity(intent7);
                break;
            case R.id.setting_btn:
                v.startAnimation(animAlpha);
                Intent intent4 = new Intent(HomeActivity.this, Main_Setting_Activity.class);
                startActivity(intent4);
                break;
        }
    }
}
