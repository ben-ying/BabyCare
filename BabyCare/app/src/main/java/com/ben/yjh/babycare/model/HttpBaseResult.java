package com.ben.yjh.babycare.model;


public class HttpBaseResult {
    private String code;
    private String message;

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
}
