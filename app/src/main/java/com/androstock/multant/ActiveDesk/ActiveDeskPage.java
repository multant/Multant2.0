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
import android.support.v4.view.PagerTabStrip;
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

    public List<Column> columns = new ArrayList<>();
    final List<String> names_of_columns = new ArrayList<String>();

    final Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_page);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        this.id_desk = getIntent().getExtras().getString("id_desk");
        pager = (ViewPager) findViewById(R.id.activeDesk_column);
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child(user.getUid()).child("Desks").child(id_desk).child("Columns").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Column cl = postSnapshot.getValue(Column.class);
                    int n = 0;
                    for (int i = 0;i<columns.size();i++){
                        if (columns.get(i).getId().equals(cl.getId()))
                            n++;
                    }
                    if (n==0){
                        columns.add(cl);
                    }
                }
                List<Fragment> frags = new ArrayList<>();
                if(columns.size() == 0){
                    frags.add(new PageFragment(context, id_desk, "", 0, columns.size()));
                } else {
                    for (int i=0; i<columns.size(); i++){
                        frags.add(new PageFragment(context, id_desk, columns.get(i).getId(), i, columns.size()));
                    }
                    frags.add(new PageFragment(context, id_desk, "", columns.size(), columns.size()));
                }
                adapter.updateAdapter(frags);
                pager.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        /*PagerTabStrip tabLayout = findViewById(R.id.tab_layout_page);
        tabLayout.(pager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite));*/
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





    }



    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> childFragments = new ArrayList<>();


        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            if(columns.size() == 0){
                    childFragments.add(new PageFragment(context, id_desk, "", 0, columns.size()));
            } else {
                for (int i=0; i<columns.size(); i++){
                    childFragments.add(new PageFragment(context, id_desk, columns.get(i).getId(), i, columns.size()));
                }
                childFragments.add(new PageFragment(context, id_desk, "", columns.size(), columns.size()));
            }
        }


        @Override
        public Fragment getItem(int position) {
                return childFragments.get(position);
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
                    title = columns.get(position).getNameColumn();
                    return title.subSequence(title.lastIndexOf(".") + 1, title.length());
                }
        }

        public void updateAdapter(List<Fragment> frags){
                childFragments.clear();
                childFragments.addAll(frags);
                this.notifyDataSetChanged();
        }

    }


    public void closeActiveDeskPage(View v) {
        finish();
    }
}
