package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BaseResult implements Serializable {

    @SerializedName("code") int code;
    @SerializedName("message") String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return code == 200;
    }
}
