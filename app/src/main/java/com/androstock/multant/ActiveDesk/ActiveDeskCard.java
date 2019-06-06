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
    //счетчик чисто декоративный для визуального отображения edittext'ov
    int count = 0;
    private FirebaseListAdapter<Card> adapter;
    private List<String> cards = new ArrayList<>();
    public EditText description;
    public List<Boolean> isChecked = new ArrayList<>(0);
    public List<String> subTasks = new ArrayList<>(0);

    private String id_desk = "";
    private String id_page = "";
    private String id_card = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        this.id_desk = getIntent().getExtras().getString("id_desk");
        this.id_page = getIntent().getExtras().getString("id_page");
        this.id_card = getIntent().getExtras().getString("id_card");

        description = (EditText) findViewById(R.id.description);

        ListView listCards = (ListView)findViewById(R.id.mylist);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            displayCards(listCards);

        }
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        /*myRef.child(user.getUid()).child("Desks").child(id_desk)
                .child("Columns").child(id_page).child("Cards").child(id_card).addValueEventListener(new ValueEventListener() */
        Button addButton = (Button) findViewById(R.id.button);
        //инициализировали наш массив с edittext
        allEds = new ArrayList<View>();
        ListView linear = (ListView) findViewById(R.id.mylist);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
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
                            linear.removeFooterView(view);
                        } catch(IndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                EditText text = (EditText) view.findViewById(R.id.editText);
                //добавляем все что создаем в массив
                allEds.add(view);
                //добавляем елементы в linearlayout
                linear.addFooterView(view);
            }
        });


    }

    public void onBackPressed(View v){
        finish();
    }

    public void onAddCardButtonPressed(View v){
        isChecked.clear();
        subTasks.clear();
        Log.i("size", Integer.toString(allEds.size()));
        for (int i=0; i<allEds.size(); i++){
            View view = allEds.get(i);
            CheckBox check = view.findViewById(R.id.checkbox);
            isChecked.add(check.isChecked());
            EditText text = view.findViewById(R.id.editText);
            subTasks.add(text.getText().toString());
        }
        myRef.child(user.getUid()).child("Desks").child("Cards").push().setValue(new Card("myCard", description.getText().toString(), isChecked, subTasks));
    }

    private void displayCards(ListView listCards) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Desks").child("Cards");

        FirebaseListOptions<Card> options = new FirebaseListOptions.Builder<Card>()
                .setLayout(R.layout.custom_edittext_layout)
                .setQuery(query, Card.class)
                .build();
        adapter = new FirebaseListAdapter<Card>(options) {
            @Override
            protected void populateView(View v, Card model, int position) {
                cards.add("myCard");
            }
        };

        listCards.setAdapter(adapter);
        adapter.startListening();
    }

}
