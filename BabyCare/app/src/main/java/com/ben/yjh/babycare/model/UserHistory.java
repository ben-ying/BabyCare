package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;


public class UserHistory extends SugarRecord {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void saveUserHistory(String username) {
        UserHistory.deleteAll(UserHistory.class, "username = ?", username);
        UserHistory userHistory = new UserHistory();
        userHistory.setUsername(username);
        userHistory.save();
    }
}
