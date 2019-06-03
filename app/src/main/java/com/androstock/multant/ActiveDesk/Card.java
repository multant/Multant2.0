package com.androstock.multant.ActiveDesk;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Card implements Parcelable {
    String name;
    String desc;
    List<Boolean> isChecked;
    List<String> subTasks;

    public Card() {
    }

    public Card(String name, String desc, List<Boolean> isChecked, List<String> subTasks){
        this.name = name;
        this.desc = desc;
        this.isChecked = isChecked;
        this.subTasks = subTasks;
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

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
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
        dest.writeString(name);
        dest.writeString(desc);
    }

}
