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
import android.widget.ListView;
import android.widget.TextView;

import com.androstock.multant.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActiveDeskPage extends FragmentActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();

    private String id_desk = "";

    ViewPager pager;

    final List<String> columns = new ArrayList<String>();
    final List<String> names_of_columns = new ArrayList<String>();

    final Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_page);
        this.id_desk = getIntent().getExtras().getString("id_desk");


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


        myRef.child(user.getUid()).child("Desks").child(id_desk).child("Columns").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String cl = postSnapshot.getKey();
                    String nm = postSnapshot.child(cl).child("nameColumn").getValue(String.class);
                    int n = 0;
                    for (int i = 0;i<columns.size();i++){
                        if (columns.get(i).equals(cl))
                            n++;
                    }
                    if (n==0){
                        columns.add(cl);
                        names_of_columns.add(nm);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        pager = (ViewPager) findViewById(R.id.activeDesk_column);
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tab_layout_page);
        tabLayout.setupWithViewPager(pager);

    }



    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] childFragments = new Fragment[columns.size() + 1];



        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            for (int i=0; i < columns.size() + 1; i++){
                if (columns.size() == 0) {
                    childFragments[i] = new PageFragment(context, id_desk, "", i, columns.size());
                }else {
                    childFragments[i] = new PageFragment(context, id_desk, columns.get(i), i, columns.size());
                }

            }
        }

        @Override
        public Fragment getItem(int position) {
            if (columns.size() == 0) {
                return new PageFragment(context, id_desk, "", position, columns.size());
            }else {
                return new PageFragment(context, id_desk, columns.get(position), position, columns.size());
            }

        }

        @Override
        public int getCount() {
            return columns.size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {

                String title;
                if (position == columns.size()){
                    title = "Новый список";
                    return title.subSequence(title.lastIndexOf(".") + 1, title.length());
                } else {
                    /*myRef = FirebaseDatabase.getInstance().getReference();
                    myRef.child(user.getUid()).child("Desks").child(id_desk)
                            .child("Columns").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Column column = dataSnapshot.child(columns.get(position)).getValue(Column.class);
                            title_page = column.getNameColumn();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    title = title_page;*/
                    title = names_of_columns.get(position);
                    return title.subSequence(title.lastIndexOf(".") + 1, title.length());
                }
        }



    }

    public void closeActiveDeskPage(View v) {
        finish();
    }
}
