package com.androstock.multant.User;

import java.util.List;

public class User {
    public String user_id;
    public List<String> desk_id;

    public User(){

    }

    public User(String user_id, List<String> desk_id){
        this.user_id = user_id;
        this.desk_id = desk_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getDesk_id() {
        return desk_id;
    }

    public void setDesk_id(List<String> desk_id) {
        this.desk_id = desk_id;
    }
}
