package com.androstock.multant.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {
    String name;
    int isSelected;

    public Group(String name) {
        this.name = name;
        this.isSelected = 0;
    }
    public Group(){
        this.name = " ";
        this.isSelected = 0;
    }
    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getName() {
        return name;
    }
    public Group(Parcel in){
        name = in.readString();
        isSelected = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(isSelected);
    }

}
