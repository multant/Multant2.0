package com.androstock.multant.ActiveDesk;

import java.util.ArrayList;
import java.util.List;
import com.androstock.multant.ActiveDesk.Card;

public class Column {
    private String name_column;
    final List<Card> cards = new ArrayList<Card>();

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
