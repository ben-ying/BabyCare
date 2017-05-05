package com.ben.yjh.babycare.util;


import android.content.Context;
import android.hardware.Camera;
import android.provider.Settings;

import com.ben.yjh.babycare.model.VideoConfig;

import java.util.List;

public class VideoUtils {

    public static VideoConfig getVideoConfig(Context context, String username) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (androidId != null) {
            List<VideoConfig> videoConfigs = VideoConfig.find(VideoConfig.class,
                    "username=? and device_id = ?", username, androidId);
            if (videoConfigs.size() > 0) {
                return videoConfigs.get(0);
            } else {
                return generateVideoConfig(androidId, username);
            }
        } else {
            return generateVideoConfig(String.valueOf(System.currentTimeMillis()), username);
        }
    }

    private static VideoConfig generateVideoConfig(String androidId, String username) {
        VideoConfig videoConfig = new VideoConfig();
        videoConfig.setDeviceId(androidId);
        videoConfig.setUsername(username);
        videoConfig.setMaxTime(Constants.VIDEO_MAX_DEFAULT_TIME);
        videoConfig.setMinTime(Constants.VIDEO_MIN_DEFAULT_TIME);
        videoConfig.setUsername(username);
        videoConfig.setMaxFrameRate(Constants.VIDEO_MAX_FRAME_RATE);
        videoConfig.setCaptureThumbnailsTime(Constants.VIDEO_THUMBNAILS_TIME);
        Camera back = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        List<Camera.Size> backSizeList = back.getParameters().getSupportedPreviewSizes();
        for (Camera.Size bSize : backSizeList) {
            int decimal = Constants.VIDEO_DEFAULT_WIDTH - bSize.width;
            if (decimal == 0) {
                videoConfig.setWidth(bSize.width);
                break;
            } else {
                if (decimal < Constants.VIDEO_DEFAULT_WIDTH - videoConfig.getWidth()) {
                    videoConfig.setWidth(bSize.width);
                }
            }
        }
        for (Camera.Size bSize : backSizeList) {
            int decimal = Constants.VIDEO_DEFAULT_HEIGHT - bSize.height;
            if (decimal == 0) {
                videoConfig.setHeight(bSize.height);
                break;
            } else {
                if (decimal < Constants.VIDEO_DEFAULT_HEIGHT - videoConfig.getHeight()) {
                    videoConfig.setHeight(bSize.height);
                }
            }
        }
        back.release();
        Camera front = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        List<Camera.Size> frontSizeList = front.getParameters().getSupportedPreviewSizes();
        for (Camera.Size fSize : frontSizeList) {
            int decimal = Constants.VIDEO_DEFAULT_WIDTH - fSize.width;
            if (decimal == 0) {
                videoConfig.setWidth(fSize.width);
                break;
            } else {
                if (decimal < Constants.VIDEO_DEFAULT_WIDTH - videoConfig.getWidth()) {
                    videoConfig.setWidth(fSize.width);
                }
            }
        }
        for (Camera.Size fSize : frontSizeList) {
            int decimal = Constants.VIDEO_DEFAULT_HEIGHT - fSize.height;
            if (decimal == 0) {
                videoConfig.setHeight(fSize.height);
                break;
            } else {
                if (decimal < Constants.VIDEO_DEFAULT_HEIGHT - videoConfig.getHeight()) {
                    videoConfig.setHeight(fSize.height);
                }
            }
        }
        front.release();

        videoConfig.save();
        return videoConfig;
    }
}
