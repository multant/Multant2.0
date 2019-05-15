package com.androstock.multant.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ferdousur Rahman Sarker on 3/19/2018.
 */

//TODO: Создать абстрактный интерфейс для БД
//TODO: Определить, почему записи добавляются не с нулевой позиции

public class MultantDBHelper extends SQLiteOpenHelper implements BaseColumns {

    // Одна БД и 3 таблицы:
    //Задачи: Номер, Задача, Дата создания
    //Заметки: Номер, Заметка, Дата создания, Дата изменения
    //Записи: Номер, Запись, Дата создания, Заголовок
    public static final String DATABASE_NAME = "MultantDBHelper.db";
    public static final String TASKS_TABLE_NAME = "todo";
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String ENTRIES_TABLE_NAME = "entries";
    public static final String TASKS_TABLE__ID = BaseColumns._ID;
    public static final String NOTES_TABLE__ID = BaseColumns._ID;
    public static final String ENTRIES_TABLE__ID = BaseColumns._ID;
    public static final String TASK = "task";
    public static final String NOTE = "note";
    public static final String ENTRY = "entry";
    public static final String ENTRY_TITLE = "entrytitle";
    public static final String TASK_CREATEDATESTR = "createdateStr";
    public static final String NOTE_CREATEDATESTR = "createdateStr";
    public static final String NOTE_CHANGEDATESTR = "changedateStr";
    public static final String ENTRY_CREATEDATESTR = "createdateStr";

    public MultantDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TASKS_TABLE_NAME +
                        "( " + TASKS_TABLE__ID + " INTEGER PRIMARY KEY, " +  TASK + " TEXT, " + TASK_CREATEDATESTR + " INTEGER)"
        );
        db.execSQL(
                "CREATE TABLE " + NOTES_TABLE_NAME +
                        "( " + NOTES_TABLE__ID + " INTEGER PRIMARY KEY, " +  NOTE + " TEXT, " + NOTE_CREATEDATESTR + " INTEGER, " + NOTE_CHANGEDATESTR+ " INTEGER)"
        );
        db.execSQL(
                "CREATE TABLE " + ENTRIES_TABLE_NAME +
                        "( " + ENTRIES_TABLE__ID + " INTEGER PRIMARY KEY, " +  ENTRY + " TEXT, " + ENTRY_CREATEDATESTR + " INTEGER, "+ENTRY_TITLE+" TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ NOTES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ENTRIES_TABLE_NAME);
        onCreate(db);
    }

    private long getDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
        date = dateFormat.parse(day);
        } catch (ParseException e) {}
        return date.getTime();
    }

    private long getTextcreateDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(day);
        } catch (ParseException e) {}
        return date.getTime();
    }

    public boolean insert(String data, String createdateStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, data);
        contentValues.put(TASK_CREATEDATESTR, getDate(createdateStr));
        db.insert(TASKS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean entryinsert(String entry, String createdateStr, String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTRY, entry);
        contentValues.put(ENTRY_CREATEDATESTR, getTextcreateDate(createdateStr));
        contentValues.put(ENTRY_TITLE, title);
        db.insert(ENTRIES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insert(String note, String createdateStr, String changedatestr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE, note);
        contentValues.put(NOTE_CREATEDATESTR, getTextcreateDate(createdateStr));
        contentValues.put(NOTE_CHANGEDATESTR, getTextcreateDate(changedatestr));
        db.insert(NOTES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean update(String id, String data, String createdateStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, data);
        contentValues.put(TASK_CREATEDATESTR, getDate(createdateStr));
        db.update(TASKS_TABLE_NAME, contentValues, TASKS_TABLE__ID + " = ? ", new String[]{id});
        return true;
    }

    public boolean entryupdate(String id, String entry, String createdateStr, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTRY, entry);
        contentValues.put(ENTRY_CREATEDATESTR, getTextcreateDate(createdateStr));
        contentValues.put(ENTRY_TITLE, title);
        db.update(ENTRIES_TABLE_NAME, contentValues, ENTRIES_TABLE__ID + " = ? ", new String[]{id});
        return true;
    }

    public boolean update(String id, String note, String createdateStr, String changedatestr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE, note);
        contentValues.put(NOTE_CREATEDATESTR, getTextcreateDate(createdateStr));
        contentValues.put(NOTE_CHANGEDATESTR, getTextcreateDate(changedatestr));
        db.update(NOTES_TABLE_NAME, contentValues, NOTES_TABLE__ID + " = ? ", new String[]{id});
        return true;
    }

    public Cursor getcreateddatesortedData(Integer tableindex) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        switch (tableindex)
        {
            case 1:
                res = db.rawQuery("select * from " + NOTES_TABLE_NAME + " order by " + NOTE_CREATEDATESTR + " asc", null);
                break;
            case 2:
                res = db.rawQuery("select * from " + ENTRIES_TABLE_NAME + " order by " + ENTRY_CREATEDATESTR + " asc", null);
                break;
            default:
                res = db.rawQuery("select * from " + TASKS_TABLE_NAME + " order by " + TASK_CREATEDATESTR + " asc", null);
                break;
        }
        return res;
    }

    public Cursor getData(Integer tableindex) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        switch (tableindex)
        {
            case 1:
                res = db.rawQuery("select * from " + NOTES_TABLE_NAME, null);
                break;
            case 2:
                res = db.rawQuery("select * from " + ENTRIES_TABLE_NAME, null);
                break;
            default:
                res = db.rawQuery("select * from " + TASKS_TABLE_NAME, null);
                break;
        }
        return res;
    }


    public Cursor getDataSpecific(String id, Integer tableindex){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        switch (tableindex) {
            case 1:
                res = db.rawQuery("select * from " + NOTES_TABLE_NAME + " WHERE "+NOTES_TABLE__ID+" = '" + id + "' order by " + NOTE_CREATEDATESTR + " asc", null);
                break;
            case 2:
                res = db.rawQuery("select * from " + ENTRIES_TABLE_NAME + " WHERE "+ENTRIES_TABLE__ID+" = '" + id + "' order by " + ENTRY_CREATEDATESTR + " asc", null);
                break;
            default:
                res = db.rawQuery("select * from " + TASKS_TABLE_NAME + " WHERE "+TASKS_TABLE__ID+" = '" + id + "' order by " + TASK_CREATEDATESTR + " asc", null);
                break;
        }
        return res;
    }

    public Cursor getDataToday(Integer tableindex) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        switch (tableindex) {
            case 1:
                res = db.rawQuery("select * from " + NOTES_TABLE_NAME +
                        " WHERE date(datetime("+NOTE_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by " + NOTE_CREATEDATESTR + " asc", null);
                break;
            case 2:
                res = db.rawQuery("select * from " + ENTRIES_TABLE_NAME +
                        " WHERE date(datetime("+ENTRY_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by " + ENTRY_CREATEDATESTR + " asc", null);
                break;
            default:
                res = db.rawQuery("select * from " + TASKS_TABLE_NAME +
                        " WHERE date(datetime("+TASK_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by " + TASK_CREATEDATESTR + " asc", null);
                break;
        }
      return res;
    }

    public Cursor getDataTomorrow(Integer tableindex){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        switch (tableindex) {
            case 1:
                res =  db.rawQuery("select * from "+ NOTES_TABLE_NAME +
                        " WHERE date(datetime("+NOTE_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by " + NOTE_CREATEDATESTR + " asc", null);
                break;
            case 2:
                res =  db.rawQuery("select * from "+ ENTRIES_TABLE_NAME +
                        " WHERE date(datetime("+ENTRY_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by " + ENTRY_CREATEDATESTR + " asc", null);
                break;
            default:
                res =  db.rawQuery("select * from "+ TASKS_TABLE_NAME +
                        " WHERE date(datetime("+TASK_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by " + TASK_CREATEDATESTR + " asc", null);
                break;
        }
        return res;
    }


    public Cursor getDataUpcoming(Integer tableindex){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        switch (tableindex) {
            case 1:
                res =  db.rawQuery("select * from "+ NOTES_TABLE_NAME +
                        " WHERE date(datetime("+NOTE_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by " + NOTE_CREATEDATESTR + " asc", null);
                break;
            case 2:
                res =  db.rawQuery("select * from "+ ENTRIES_TABLE_NAME +
                        " WHERE date(datetime("+ENTRY_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by " + ENTRY_CREATEDATESTR + " asc", null);
                break;
            default:
                res =  db.rawQuery("select * from "+ TASKS_TABLE_NAME +
                        " WHERE date(datetime("+TASK_CREATEDATESTR+" / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by " + TASK_CREATEDATESTR + " asc", null);
                break;
        }
        return res;
    }

    public void deleteData(String id, Integer tableindex){
        SQLiteDatabase db = this.getWritableDatabase();
        switch (tableindex) {
            case 1:
                db.delete(NOTES_TABLE_NAME, NOTES_TABLE__ID+" = ?", new String[] {id});
                break;
            case 2:
                db.delete(ENTRIES_TABLE_NAME, ENTRIES_TABLE__ID+" = ?", new String[] {id});
                break;
            default:
                db.delete(TASKS_TABLE_NAME, TASKS_TABLE__ID+" = ?", new String[] {id});
                break;
        }
    }

    public long getNumberOfStrings(Integer tableindex){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        switch (tableindex) {
            case 1:
                c =  db.rawQuery("select * from "+ NOTES_TABLE_NAME +" order by "+ NOTE_CREATEDATESTR +" asc", null);
                break;
            case 2:
                c =  db.rawQuery("select * from "+ ENTRIES_TABLE_NAME +" order by "+ENTRY_CREATEDATESTR+" asc", null);
                break;
            default:
                c =  db.rawQuery("select * from "+ TASKS_TABLE_NAME +" order by "+TASK_CREATEDATESTR+" asc", null);
                break;
        }
        return c.getCount();
    }

    public long getMillis(int i, Integer tableindex){
        SQLiteDatabase db = this.getReadableDatabase();
        long date; Cursor cur; String query;
        switch (tableindex) {
            case 1:
                query = "SELECT " + NOTE_CREATEDATESTR + " FROM " + NOTES_TABLE_NAME;
                cur = db.rawQuery(query, null);
                cur.moveToPosition(i);
                date = cur.getLong(cur.getColumnIndex(NOTE_CREATEDATESTR));
                break;
            case 2:
                query = "SELECT " + ENTRY_CREATEDATESTR + " FROM " + ENTRIES_TABLE_NAME;
                cur = db.rawQuery(query, null);
                cur.moveToPosition(i);
                date = cur.getLong(cur.getColumnIndex(ENTRY_CREATEDATESTR));
                break;
            default:
                query = "SELECT " + TASK_CREATEDATESTR + " FROM " + TASKS_TABLE_NAME;
                cur = db.rawQuery(query, null);
                cur.moveToPosition(i);
                date = cur.getLong(cur.getColumnIndex(TASK_CREATEDATESTR));
                break;
        }
        return date;
    }

    private long yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis();
    }

    public void updateTable(Integer tableindex){
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        String query; Cursor cur;
        switch (tableindex)
        {
            case 1:
                query = "SELECT " + NOTE_CREATEDATESTR + " FROM " + NOTES_TABLE_NAME;
                cur = db.rawQuery(query, null);
                while(cur.moveToNext()){
                    long time = cur.getLong(cur.getColumnIndex(NOTE_CREATEDATESTR));
                    date.setTime(time);
                    if(date.getTime()<yesterday())
                        db.delete(NOTES_TABLE_NAME, NOTE_CREATEDATESTR + "=" + cur.getLong(cur.getColumnIndex(NOTE_CREATEDATESTR)), null);
                }
                break;
            case 2:
                query = "SELECT " + ENTRY_CREATEDATESTR + " FROM " + ENTRIES_TABLE_NAME;
                cur = db.rawQuery(query, null);
                while(cur.moveToNext()){
                    long time = cur.getLong(cur.getColumnIndex(ENTRY_CREATEDATESTR));
                    date.setTime(time);
                    if(date.getTime()<yesterday())
                        db.delete(ENTRIES_TABLE_NAME, ENTRY_CREATEDATESTR + "=" + cur.getLong(cur.getColumnIndex(ENTRY_CREATEDATESTR)), null);
                }
                break;
            default:
                query = "SELECT " + TASK_CREATEDATESTR + " FROM " + TASKS_TABLE_NAME;
                cur = db.rawQuery(query, null);
                while(cur.moveToNext()){
                    long time = cur.getLong(cur.getColumnIndex(TASK_CREATEDATESTR));
                    date.setTime(time);
                    if(date.getTime()<yesterday())
                        db.delete(TASKS_TABLE_NAME, TASK_CREATEDATESTR + "=" + cur.getLong(cur.getColumnIndex(TASK_CREATEDATESTR)), null);
                }
                break;
        }
    }

    public void deleteEmptyRows(Integer tableindex)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        switch (tableindex) {
            case 1:
                db.execSQL("DELETE FROM "+NOTES_TABLE_NAME+" WHERE "+NOTE+" IS NULL OR trim("+NOTE+") = '';");
                break;
            case 2:
                db.execSQL("DELETE FROM "+ENTRIES_TABLE_NAME+" WHERE "+ENTRY+" IS NULL OR trim("+ENTRY+") = '';");
                break;
            default:
                db.execSQL("DELETE FROM "+TASKS_TABLE_NAME+" WHERE "+TASK+" IS NULL OR trim("+TASK+") = '';");
                break;
        }
    }

    public void deleteAllRows(Integer tableindex)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        switch (tableindex) {
            case 1:
                db.execSQL("DELETE FROM "+NOTES_TABLE_NAME+";");
                break;
            case 2:
                db.execSQL("DELETE FROM "+ENTRIES_TABLE_NAME+";");
                break;
            default:
                db.execSQL("DELETE FROM "+TASKS_TABLE_NAME+";");
                break;
        }
    }
}
