package com.ben.yjh.babycare.http;


import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

public class HttpResult implements Serializable {
    @SerializedName("code") String code;
    @SerializedName("message") String message;
    @SerializedName("body") JSONObject body;

    public boolean isSuccess() {
        return code.equals("200");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}
