package com.androstock.multant.ActiveDesk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActiveDeskCard extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    //Создаем список вьюх которые будут создаваться
    private List<View> allEds;

    private FirebaseListAdapter<Card> adapter;
    private List<String> id_checkbox = new ArrayList<>();
    public EditText description;
    public List<Boolean> isChecked = new ArrayList<>(0);
    public List<String> subTasks = new ArrayList<>(0);
    public List<ActiveDeskCheckBox> checkBoxes = new ArrayList<>();

    private String id_desk = "";
    private String id_page = "";
    private String id_card = "";
    private int position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        this.id_desk = getIntent().getExtras().getString("id_desk");
        this.id_page = getIntent().getExtras().getString("id_page");
        this.id_card = getIntent().getExtras().getString("id_card");
        this.position = getIntent().getExtras().getInt("position");

        description = (EditText) findViewById(R.id.description);

        LinearLayout linear = (LinearLayout) findViewById(R.id.mylist);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        myRef.child(user.getUid()).child("Desks").child(id_desk)
                .child("Columns").child(id_page).child("Cards").child(id_card).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Card card = dataSnapshot.getValue(Card.class);
                if(card.getText_card() != ""){
                    EditText txtCard = (EditText) findViewById(R.id.textCard);
                    txtCard.setText(card.getText_card());
                }
                if(card.getDesc() != ""){
                    EditText desc = (EditText) findViewById(R.id.description);
                    desc.setText(card.getDesc());
                }
                allEds = new ArrayList<View>();
                for (DataSnapshot postSnapshot: dataSnapshot.child("CheckBoxes").getChildren()){
                    LinearLayout mLin = (LinearLayout) findViewById(R.id.mylist);
                    final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
                    Button deleteBut = (Button) view.findViewById(R.id.button2);
                    deleteBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                //получаем родительский view и удаляем его
                                //((ListView) view.getParent()).removeView(view);
                                //удаляем эту же запись из массива что бы не оставалось мертвых записей
                                allEds.remove(view);
                                linear.removeView(view);
                            } catch(IndexOutOfBoundsException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    ActiveDeskCheckBox chB = postSnapshot.getValue(ActiveDeskCheckBox.class);
                    EditText eT = (EditText) view.findViewById(R.id.editText);
                    CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox);
                    int n = 0;
                    for (int i = 0;i<checkBoxes.size();i++){
                        if (checkBoxes.get(i).getId().equals(chB.getId()))
                            n++;
                    }
                    if (n==0){
                        checkBoxes.add(chB);
                        eT.setText(chB.getText_checkbox());
                        checkBox.setChecked(Boolean.valueOf(chB.isChecked()));
                        allEds.add(view);
                        mLin.addView(view);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        Button addButton = (Button) findViewById(R.id.button);
        //инициализировали наш массив с edittext
        allEds = new ArrayList<View>();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
                final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
                Button deleteField = (Button) view.findViewById(R.id.button2);
                deleteField.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //получаем родительский view и удаляем его
                            //((ListView) view.getParent()).removeView(view);
                            //удаляем эту же запись из массива что бы не оставалось мертвых записей
                            allEds.remove(view);
                            linear.removeView(view);
                        } catch(IndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                //добавляем все что создаем в массив
                allEds.add(view);
                //добавляем елементы в linearlayout
                linear.addView(view);
            }
        });


    }

    public void onBackPressed(View v){
        Intent intent = new Intent(ActiveDeskCard.this, ActiveDeskPage.class);
        intent.putExtra("id_desk", id_desk);
        intent.putExtra("page_pos", position);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();
        checkBoxes.clear();
        EditText txtCard = (EditText) findViewById(R.id.textCard);
        EditText desc = (EditText) findViewById(R.id.description);
        myRef.child(user.getUid()).child("Desks").child(id_desk)
                .child("Columns").child(id_page).child("Cards").child(id_card)
                .setValue(new Card(txtCard.getText().toString(), desc.getText().toString(), id_card));
        for (int i=0; i<allEds.size(); i++){
            View view = allEds.get(i);
            CheckBox check = view.findViewById(R.id.checkbox);
            EditText text = view.findViewById(R.id.editText);
            String key = myRef.child(user.getUid()).child("Desks").child(id_desk)
                    .child("Columns").child(id_page).child("Cards").child(id_card).child("CheckBoxes").push().getKey();
            myRef.child(user.getUid()).child("Desks").child(id_desk)
                    .child("Columns").child(id_page).child("Cards").child(id_card).child("CheckBoxes").child(key)
                    .setValue(new ActiveDeskCheckBox(text.getText().toString(), key, check.isChecked()));
        }
    }
}
