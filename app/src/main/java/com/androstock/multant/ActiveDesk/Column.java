package com.androstock.multant.ActiveDesk;


import java.util.Date;

public class Column {
    private String name_column = "";
    private String id = "";
    private long time_create_column = 0;

    public Column(String name_column, String id) {
        this.name_column = name_column;
        this.id = id;
        this.time_create_column = new Date().getTime();
    }


    public Column(String name_column, String id, long time) {
        this.name_column = name_column;
        this.id = id;
        this.time_create_column = time;
    }
    public Column() {
    }

    public String getNameColumn() {
        return name_column;
    }

    public void setNameColumn(String name_column) {
        this.name_column = name_column;
    }


    public long getTime_create_column() {
        return time_create_column;
    }

    public void setTime_create_column(long time_create_column) {
        this.time_create_column = time_create_column;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
