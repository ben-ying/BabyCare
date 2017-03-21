package com.ben.yjh.babycare.model;

import android.content.Context;

import com.ben.yjh.babycare.R;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;


public class UserHistory extends SugarRecord {

    @SerializedName("username") String username;
    @SerializedName("baby_name") String babyName;
    @SerializedName("profile") String profile;
    @SerializedName("gender") int gender;
    @SerializedName("is_email_activate") boolean isEmailActivate;
    @SerializedName("is_phone_activate") boolean isPhoneActive;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getGender() {
        return gender;
    }

    public String getGenderValue(Context context) {
        return gender == 1 ? context.getString(R.string.female) : context.getString(R.string.male);
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setGenderStr(Context context, String gender) {
        if (gender.equals(context.getString(R.string.male))) {
            this.gender = 0;
        } else if (gender.equals(context.getString(R.string.female))) {
            this.gender = 1;
        }
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public boolean isEmailActivate() {
        return isEmailActivate;
    }

    public void setEmailActivate(boolean emailActivate) {
        isEmailActivate = emailActivate;
    }

    public boolean isPhoneActive() {
        return isPhoneActive;
    }

    public void setPhoneActive(boolean phoneActive) {
        isPhoneActive = phoneActive;
    }

    public static void saveUserHistory(String username, BabyUser babyUser) {
        UserHistory.deleteAll(UserHistory.class, "username = ?", username);
        UserHistory userHistory = new UserHistory();
        userHistory.setUsername(username);
        userHistory.setBabyName(babyUser.getBabyName());
        userHistory.setProfile(babyUser.getProfile());
        userHistory.setGender(babyUser.getGender());
        userHistory.setEmailActivate(babyUser.isEmailActivate());
        userHistory.setPhoneActive(babyUser.isPhoneActive());
        userHistory.save();
    }
}
