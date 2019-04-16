package com.androstock.todotask.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.androstock.todotask.R;

public class Setting_Activity extends AppCompatActivity {

    private EditText receiver_Email;
    private EditText message_theme;
    private EditText message_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setTitle("E-mail");

        //Инициализируем переменные, привязываем их к нашим объектам:
        receiver_Email = (EditText) findViewById(R.id.receiver_Email);
        message_theme = (EditText) findViewById(R.id.message_theme);
        message_text = (EditText) findViewById(R.id.message_text);
    }

    //Стандартный метод для реализации меню приложения:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    //Описываем функционал кнопок меню:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //В случае кнопки "Очистить", для всех элементов EditText настраиваем пустые строки для ввода,
            //то есть очищаем все поля от введенного текста:
            case R.id.menu_clear:
                receiver_Email.setText("");
                message_text.setText("");
                message_theme.setText("");
                break;

            //Для кнопки "Отправить" создаем 3 строковых объекта для контакта, темы и текста сообщения
            case R.id.menu_send:
                String contact = receiver_Email.getText().toString();
                String subject = message_theme.getText().toString();
                String message = message_text.getText().toString();

                //С помощью намерения Intent вызываем стандартный пакет приложения для отправки e-mail,
                //передаем в него данные с полей "адрес", "тема" и "текст сообщения",
                //заполняя ими соответствующие поля стандартного e-mail приложения
                //и запускаем процесс перехода с нашего приложения в стандартную программу для обмена e-mail:
                Intent emailIntent = getPackageManager().getLaunchIntentForPackage("com.android.email");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { contact });
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(emailIntent);
                break;
        }
        return true;
    }

}
