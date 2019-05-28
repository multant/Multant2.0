package com.androstock.multant.ActiveDesk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.androstock.multant.R;

public class ActiveDeskPage extends AppCompatActivity {

    private String name_desk = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_page);
        this.name_desk = getIntent().getExtras().getString("desk_name");
        TextView title = (TextView) findViewById(R.id.toolbar_active_desk_page_title);
        title.setText(this.name_desk);
    }

    public void closeActiveDeskPage(View v) {
        finish();
    }
}
