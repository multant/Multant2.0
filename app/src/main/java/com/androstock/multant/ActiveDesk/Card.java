package com.androstock.multant.ActiveDesk;

import android.os.Parcel;

import java.util.Date;

public class Card{
    String desc;
    private String text_card = "";
    private String id = "";
    private long time_create_card = 0;

    public Card() {
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

    public Card(String text_card, String desc, String id) {
        this.text_card = text_card;
        this.desc = desc;
        this.id = id;
        this.time_create_card = new Date().getTime();
    }


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


    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }


}
