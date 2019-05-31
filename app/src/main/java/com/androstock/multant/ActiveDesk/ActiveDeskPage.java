package com.androstock.multant.ActiveDesk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androstock.multant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActiveDeskPage extends FragmentActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private String id_desk = "";

    ViewPager pager;
    static final int PAGE_COUNT = 1;

    final List<Column> columns = new ArrayList<Column>();

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_page);
        this.id_desk = getIntent().getExtras().getString("id_desk");
        FirebaseUser user = mAuth.getInstance().getCurrentUser();

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child(user.getUid()).child("Desks").child(id_desk).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Desk desk = dataSnapshot.getValue(Desk.class);
                TextView title = (TextView) findViewById(R.id.toolbar_active_desk_page_title);
                title.setText(desk.getNameDesk());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        pager = (ViewPager) findViewById(R.id.activeDesk_column);
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tab_layout_page);
        tabLayout.setupWithViewPager(pager);

    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] childFragments;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            childFragments = new Fragment[] {
                    new PageFragment(context, id_desk)
            };
        }

        @Override
        public Fragment getItem(int position) {
            return childFragments[position];
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Новый список";
            return title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }

    }


    public void closeActiveDeskPage(View v) {
        finish();
    }
}
