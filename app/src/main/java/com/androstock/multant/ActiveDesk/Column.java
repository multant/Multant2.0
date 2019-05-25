package com.androstock.multant.ActiveDesk;

import java.util.Vector;
import com.androstock.multant.ActiveDesk.Card;

public class Column {
    private String name_column;
    private Vector cards = new Vector();

    public Column(String name_column) {
        this.name_column = name_column;

    }
    public Column() {
    }

    public String getNameColumn() {
        return name_column;
    }

    public void setNameColumn(String name_column) {
        this.name_column = name_column;
    }



}
