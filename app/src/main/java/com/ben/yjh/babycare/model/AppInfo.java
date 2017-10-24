package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AppInfo implements Serializable {
    @SerializedName("version_name") String versionName;
    @SerializedName("version_code") int versionCode;
    @SerializedName("version_type") int versionType;
    @SerializedName("app_link") String appLink;
    @SerializedName("app_name") String appName;
    @SerializedName("update_info") String updateInfo;
    @SerializedName("datetime") String datetime;

    private static final int TYPE_RELEASE = 0;
    private static final int TYPE_DEBUG = 1;

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionType() {
        return versionType;
    }

    public void setVersionType(int versionType) {
        this.versionType = versionType;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
