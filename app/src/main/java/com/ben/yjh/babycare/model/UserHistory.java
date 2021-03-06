package com.ben.yjh.babycare.model;

import android.content.Context;

import com.ben.yjh.babycare.R;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;


public class UserHistory extends SugarRecord {

    @Unique
    @SerializedName("username") String username;
    @SerializedName("baby_name") String babyName;
    @SerializedName("profile") String profile;
    @SerializedName("gender") int gender;
    @SerializedName("type") int type;
    @SerializedName("is_email_activate") boolean isEmailActivate;
    @SerializedName("is_phone_activate") boolean isPhoneActive;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public static void saveUserHistory(User user) {
        UserHistory.deleteAll(UserHistory.class, "username = ?", user.getUsername());
        UserHistory userHistory = new UserHistory();
        userHistory.setUsername(user.getUsername());
        userHistory.setBabyName(user.getBabyName());
        userHistory.setProfile(user.getProfile());
        userHistory.setGender(user.getGender());
        userHistory.setEmailActivate(user.isEmailActivate());
        userHistory.setPhoneActive(user.isPhoneActive());
        userHistory.save();
    }
}
