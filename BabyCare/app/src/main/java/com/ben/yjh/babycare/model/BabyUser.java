package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;


public class BabyUser extends SugarRecord implements Serializable {
    @SerializedName("baby_id") int babyId;
    @SerializedName("user_id") int userId;
    @SerializedName("username") String username;
    @SerializedName("baby_name") String babyName;
    @SerializedName("profile") String profile;
    @SerializedName("email") String email;
    @SerializedName("phone") String phone;
    @SerializedName("gender") int gender;
    @SerializedName("region") String region;
    @SerializedName("whats_up") String whatsUp;
    @SerializedName("birth") String birth;
    @SerializedName("hobbies") String hobbies;
    @SerializedName("highlighted") String highlighted;
    @SerializedName("token") String token;
    @SerializedName("is_login") boolean isLogin;
    @SerializedName("is_email_activate") boolean isEmailActivate;
    @SerializedName("is_phone_activate") boolean isPhoneActive;

    @Override
    public long save() {
        BabyUser.deleteAll(BabyUser.class);
        isLogin = true;

        return super.save();
    }

    public int getBabyId() {
        return babyId;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void setBabyId(int babyId) {
        this.babyId = babyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getProfile() {
        return profile == null ? "" : profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getWhatsUp() {
        return whatsUp;
    }

    public void setWhatsUp(String whatsUp) {
        this.whatsUp = whatsUp;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(String highlighted) {
        this.highlighted = highlighted;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
