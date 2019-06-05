package com.androstock.multant.ActiveDesk;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Card implements Parcelable {
    String desc;
    private String text_card = "";
    private String id = "";
    private long time_create_card = 0;

    List<Boolean> isChecked;
    List<String> subTasks;

    public Card() {
    }

    public Card(String text_card, String desc, List<Boolean> isChecked, List<String> subTasks){
        this.text_card = text_card;
        this.desc = desc;
        this.isChecked = isChecked;
        this.subTasks = subTasks;
    }

    public Card(String text_card, String id) {

        this.text_card = text_card;
        this.id = id;
        this.time_create_card = new Date().getTime();
    }


    public Card(String text_card, String id, long time) {
        this.text_card = text_card;
        this.id = id;
        this.time_create_card = time;
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card();
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public void setText_card(String name){
        this.text_card = name;
    }

    public String getText_card(){
        return text_card;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTime_create_card(long time_create_card) {
        this.time_create_card = time_create_card;
    }

    public long getTime_create_card() {
        return time_create_card;
    }

    public List<Boolean> getIsChecked(){
        return isChecked;
    }

    public void setIsChecked(ArrayList<Boolean> isChecked) {
        this.isChecked = isChecked;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }

    public void setSubTasks(ArrayList<String> subTasks) {
        this.subTasks = subTasks;
    }

    public List<String> getSubTasks() {
        return subTasks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text_card);
        dest.writeString(desc);
    }

}
