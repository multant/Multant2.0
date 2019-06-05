package com.androstock.multant.ActiveDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.multant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PageFragment extends Fragment {

    String name_column;
    String id_desk;
    String id_page;
    Context context;
    int position;
    int max_position;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();


    public List<Card> cards = new ArrayList<>();



    public PageFragment(){
    }

    @SuppressLint("ValidFragment")
    public PageFragment(Context context, String id_desk, String id_page, int position, int max_position) {
        Bundle arguments = new Bundle();
        this.context = context;
        this.id_desk = id_desk;
        this.id_page = id_page;
        this.position = position;
        this.max_position = max_position;
        arguments.putString("context", context.toString());
        arguments.putString("id_desk", id_desk);
        arguments.putString("id_page", id_page);
        arguments.putString("position", String.valueOf(position));
        arguments.putString("max_position", String.valueOf(max_position));
        this.setArguments(arguments);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        myRef = FirebaseDatabase.getInstance().getReference();
        if (id_page == ""){
            View rootView = inflater.inflate(R.layout.active_desk_add_name_column, container, false);
            Button buttonInFragment = rootView.findViewById(R.id.button_add_column);
            buttonInFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.active_desk_prompt, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                    //Настраиваем prompt.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                            name_column = userInput.getText().toString();
                                            if (name_column.trim().length() < 1) {
                                                Toast.makeText(context, "Введите название Колонны!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show();
                                                String key = myRef.child(user.getUid()).child("Desks").child(id_desk).child("Columns").push().getKey();
                                                myRef.child(user.getUid()).child("Desks").child(id_desk).child("Columns").child(key).setValue(new Column(name_column, key));
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
            });

            return rootView;
        } else {
            View rootView = inflater.inflate(R.layout.active_desk_fragment_layout, container, false);
            Button buttonInFragment = rootView.findViewById(R.id.button_add_card);
            buttonInFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.active_desk_prompt, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                    //Настраиваем prompt.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                    userInput.setHint("Т" +
                            "екст карточки");
                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                            if (userInput.getText().toString().trim().length() < 1) {
                                                Toast.makeText(context, "Заполните Карточку!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show();
                                                String key = myRef.child(user.getUid()).child("Desks").child(id_desk).child("Columns").child(id_page).child("Cards").push().getKey();
                                                myRef.child(user.getUid()).child("Desks").child(id_desk).child("Columns").child(id_page).child("Cards").child(key).setValue(new Card(userInput.getText().toString(), key));
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
            });







            return rootView;
        }

    }

}
