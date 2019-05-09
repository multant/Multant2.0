package com.androstock.todotask.DB;

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

public class MultantDBHelper extends SQLiteOpenHelper implements BaseColumns {

    // Одна БД и 3 таблицы
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
                        "( " + ENTRIES_TABLE__ID + " INTEGER PRIMARY KEY, " +  ENTRY + " TEXT, " + ENTRY_CREATEDATESTR + " INTEGER)"
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

    public boolean insert(String data, String dateStr, Integer tableindex)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (tableindex)
        {
            case 0:
                contentValues.put(TASK, data);
                contentValues.put(TASK_CREATEDATESTR, getDate(dateStr));
                db.insert(TASKS_TABLE_NAME, null, contentValues);
                break;
            case 1:
                contentValues.put(ENTRY, data);
                contentValues.put(ENTRY_CREATEDATESTR, getDate(dateStr));
                db.insert(ENTRIES_TABLE_NAME, null, contentValues);
                break;
        }
        return true;
    }

    public boolean insert(String note, String createdateStr, String changedatestr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE, note);
        contentValues.put(NOTE_CREATEDATESTR, getDate(createdateStr));
        contentValues.put(NOTE_CHANGEDATESTR, getDate(changedatestr));
        db.insert(NOTES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean update(String id, String data, String dateStr, Integer tableindex)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (tableindex)
        {
            case 0:
                contentValues.put(TASK, data);
                contentValues.put(TASK_CREATEDATESTR, getDate(dateStr));
                db.update(TASKS_TABLE_NAME, contentValues, TASKS_TABLE__ID + " = ? ", new String[]{id});
                break;
            case 1:
                contentValues.put(ENTRY, data);
                contentValues.put(ENTRY_CREATEDATESTR, getDate(dateStr));
                db.update(ENTRIES_TABLE_NAME,contentValues, ENTRIES_TABLE__ID+" = ? ", new String[]{id});
                break;
        }
        return true;
    }

    public boolean update(String id, String note, String createdateStr, String changedatestr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE, note);
        contentValues.put(NOTE_CREATEDATESTR, getDate(createdateStr));
        contentValues.put(NOTE_CHANGEDATESTR, getDate(changedatestr));
        db.update(NOTES_TABLE_NAME, contentValues, NOTES_TABLE__ID+" = ? ", new String[]{id});
        return true;
    }

    //Для изменения порядка заменить asc на desc

    public Cursor getData(Integer tableindex){
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
}
