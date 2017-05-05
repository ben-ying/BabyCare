package com.ben.yjh.babycare.model;

import com.ben.yjh.babycare.util.Constants;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;


public class VideoConfig extends SugarRecord implements Serializable {
    @SerializedName("username") String username;
    @SerializedName("device_id") String deviceId;
    @SerializedName("width") int width;
    @SerializedName("height") int height;
    // milliseconds
    @SerializedName("max_time") int maxTime;
    // milliseconds
    @SerializedName("min_time") int minTime;
    // 指定最大帧率，越大视频质量越好，体积也会越大，当在cbr模式下不再是动态帧率，而是固定帧率；
    @SerializedName("max_frame_rate") int maxFrameRate;
    // 指定剪切哪个时间的画面来作为封面图；
    // seconds
    @SerializedName("capture_thumbnails_time") int captureThumbnailsTime;
//     不传入值将不做进一步压缩，暂时可以传入三种模式AutoVBRMode、VBRMode、VBRMode；
    @SerializedName("do_h264_compress") int doH264Compress;
    // 视频录制时期的一些配置，暂时可以传入三种模式AutoVBRMode、VBRMode、VBRMode；
    @SerializedName("set_media_bitrate_config") int setMediaBitrateConfig;
    // 可以传入一个视频等级与转码速度，等级为0-51，越大质量越差，建议18~28之间即可。
    // 转码速度有ultrafast、superfast、veryfast、faster、fast、medium、slow、slower、veryslow、placebo。
    @SerializedName("auto_vbr_mode") int AutoVBRMode;
    // 此模式下可以传入一个最大码率与一个额定码率，当然同样可以设置转码速度。
    @SerializedName("vbr_mode") int VBRMode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getWidth() {
        return width == 0 ? Constants.VIDEO_DEFAULT_WIDTH : width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height == 0 ? Constants.VIDEO_DEFAULT_HEIGHT : height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxTime() {
        return maxTime == 0 ? Constants.VIDEO_MAX_DEFAULT_TIME : maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getMinTime() {
        return minTime == 0 ? Constants.VIDEO_MIN_DEFAULT_TIME : minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMaxFrameRate() {
        return maxFrameRate;
    }

    public void setMaxFrameRate(int maxFrameRate) {
        this.maxFrameRate = maxFrameRate;
    }

    public int getCaptureThumbnailsTime() {
        return captureThumbnailsTime;
    }

    public void setCaptureThumbnailsTime(int captureThumbnailsTime) {
        this.captureThumbnailsTime = captureThumbnailsTime;
    }

    public int getDoH264Compress() {
        return doH264Compress;
    }

    public void setDoH264Compress(int doH264Compress) {
        this.doH264Compress = doH264Compress;
    }

    public int getSetMediaBitrateConfig() {
        return setMediaBitrateConfig;
    }

    public void setSetMediaBitrateConfig(int setMediaBitrateConfig) {
        this.setMediaBitrateConfig = setMediaBitrateConfig;
    }

    public int getAutoVBRMode() {
        return AutoVBRMode;
    }

    public void setAutoVBRMode(int autoVBRMode) {
        AutoVBRMode = autoVBRMode;
    }

    public int getVBRMode() {
        return VBRMode;
    }

    public void setVBRMode(int VBRMode) {
        this.VBRMode = VBRMode;
    }
}
