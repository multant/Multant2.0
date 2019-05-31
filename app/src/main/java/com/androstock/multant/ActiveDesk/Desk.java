package com.androstock.multant.ActiveDesk;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.androstock.multant.ActiveDesk.Column;

public class Desk implements Parcelable {

    private String name_desk = "";
    private String autor = "";
    private String id = "";
    private long time_create_desk = 0;
    //final List<Column> columns = new ArrayList<Column>();


    public Desk(String name_desk, String autor) {
        this.name_desk = name_desk;
        this.autor = autor;
        this.time_create_desk = new Date().getTime();
    }


    public Desk(String name_desk, String autor, String id) {
        this.name_desk = name_desk;
        this.autor = autor;
        this.id = id;
        this.time_create_desk = new Date().getTime();
    }

    public Desk(String autor, String id, String name_desk, long time) {
        this.name_desk = name_desk;
        this.autor = autor;
        this.id = id;
        this.time_create_desk = time;
    }

    public static final Creator<Desk> CREATOR = new Creator<Desk>() {
        @Override
        public Desk createFromParcel(Parcel in) {
            return new Desk();
        }

        @Override
        public Desk[] newArray(int size) {
            return new Desk[size];
        }
    };

    public Desk() {
    }

    public String getNameDesk() {
        return name_desk;
    }

    public void setNameDesk(String name_desk) {
        this.name_desk = name_desk;
    }

    public String getAutor() { return autor; }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public long getTimeCreateDesk() {
        return time_create_desk;
    }

    public void setTimeCreateDesk(long time_create_desk) {
        this.time_create_desk = time_create_desk;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /*public void addToColumns(Column column){
        this.columns.add(column);
    }*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name_desk);
        dest.writeString(autor);
        dest.writeLong(time_create_desk);
    }
}