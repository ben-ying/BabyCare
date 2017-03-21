package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class BabyUser extends UserHistory implements Serializable {
    @SerializedName("baby_id") int babyId;
    @SerializedName("user_id") int userId;
    @SerializedName("email") String email;
    @SerializedName("phone") String phone;
    @SerializedName("region") String region;
    @SerializedName("whats_up") String whatsUp;
    @SerializedName("birth") String birth;
    @SerializedName("zone") String zone;
    @SerializedName("hobbies") String hobbies;
    @SerializedName("highlighted") String highlighted;
    @SerializedName("token") String token;
    @SerializedName("is_login") boolean isLogin;

    @Override
    public long save() {
        BabyUser.deleteAll(BabyUser.class);
        isLogin = true;

        return super.save();
    }

    public static BabyUser getBabyUser() {
        List<BabyUser> babyUsers = BabyUser.find(BabyUser.class, "is_login = ?", "1");
        if (babyUsers.size() == 1 && !babyUsers.get(0).getToken().isEmpty()) {
            return babyUsers.get(0);
        } else {
            return null;
        }
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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

}
