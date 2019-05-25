package com.androstock.multant.ActiveDesk;

import java.util.Date;
import java.util.Vector;
import com.androstock.multant.ActiveDesk.Column;

public class Desk {
    String name_desk;
    String autor;
    long time_create_desk;
    Vector columns = new Vector();

    public Desk(String name_desk, String autor) {
        this.name_desk = name_desk;
        this.autor = autor;
        time_create_desk = new Date().getTime();
    }

    public Desk() {
    }

    public String getNameDesk() {
        return name_desk;
    }

    public void setNameDesk(String name_desk) {
        this.name_desk = name_desk;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public long getTimeCreateDesk() {
        return time_create_desk;
    }

    public void setTimeCreateDesk(long time_create_desk) {
        this.time_create_desk = time_create_desk;
    }

    public void addToColumns(Column column){
        this.columns.addElement(column);
    }
}