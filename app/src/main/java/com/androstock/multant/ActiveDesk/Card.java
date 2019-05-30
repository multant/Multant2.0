package com.androstock.multant.ActiveDesk;

import android.content.Context;
import android.widget.EditText;

public class Card {
    String name;
    boolean[] isChecked;

    public Card(String name){
        this.name = name;
    }

    public void setNameCard(String name){
        this.name = name;
    }

    public String getNameCard(){
        return name;
    }

    public boolean[] getIsChecked(){
        return isChecked;
    }

    public void setIsChecked(boolean[] isChecked) {
        this.isChecked = isChecked;
    }
}
