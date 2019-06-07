package com.androstock.multant.ActiveDesk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.multant.R;
import com.google.android.gms.appinvite.AppInviteInvitation;
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
        this.id_page = getIntent().getExtras().getInt("page_pos");
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

        Button btn_more = (Button)findViewById(R.id.onMoreActiveDeskPageClick);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.active_desk_popupmenu);
        myRef = FirebaseDatabase.getInstance().getReference();
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_add_user:
                            {
                                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.active_desk_prompt, null);

                                //Создаем AlertDialog
                                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                                //Настраиваем prompt.xml для нашего AlertDialog:
                                mDialogBuilder.setView(promptsView);

                                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                                userInput.setHint("E-mail пользователя");
                                //Настраиваем сообщение в диалоговом окне:
                                mDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        //Вводим текст и отображаем в строке ввода на основном экране:
                                                        if (userInput.getText().toString().trim().length() < 1) {
                                                            Toast.makeText(context, "Введите почту пользователя!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show();
                                                            String key = myRef.child(user.getUid()).child("Desks").child(id_desk).child("AllowedToUsers").push().getKey();
                                                            //myRef.child(user.getUid()).child("Desks").child(id_desk).child("AllowedToUsers").child(key).setValue(new User(userInput.getText().toString()));
                                                            myRef.child("Desk").child(id_desk).child("AllowedToUsers").setValue(new User(userInput.getText().toString()));
                                                        }
                                                    }
                                                })
                                        .setNegativeButton("Отмена",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                //Создаем AlertDialog:
                                AlertDialog alertDialog = mDialogBuilder.create();

                                //и отображаем его:
                                alertDialog.show();
                            }
                                return true;
                            case R.id.menu_del_desk:
                                Toast.makeText(getApplicationContext(),
                                        "На этой кнопке не будет удаление доски ",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
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
