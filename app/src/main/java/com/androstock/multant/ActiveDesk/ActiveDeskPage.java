package com.androstock.multant.ActiveDesk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.androstock.multant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ActiveDeskPage extends FragmentActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();

    private String id_desk = "";
    private int id_page;

    ViewPager pager;

    public List<Column> columns = new ArrayList<>();

    final Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_desk_page);
        this.id_desk = getIntent().getExtras().getString("id_desk");
        this.id_page = getIntent().getExtras().getInt("id_page");
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child(user.getUid()).child("Desks").child(id_desk).child("Columns").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
                pager = (ViewPager) findViewById(R.id.activeDesk_column);
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
                pager.setCurrentItem(id_page);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        Intent i = new Intent(ActiveDeskPage.this, ActiveDesk.class);
        startActivity(i);
    }

}
