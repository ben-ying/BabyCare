package com.ben.yjh.babycare.util;


import android.content.Context;
import android.hardware.Camera;

import com.ben.yjh.babycare.model.VideoConfig;

import java.util.List;

public class VideoUtils {

    public static VideoConfig getVideoConfig(Context context, String username) {
//        String androidId = Settings.Secure.getString(
//                context.getContentResolver(), Settings.Secure.ANDROID_ID);

//        if (androidId != null) {
//            List<VideoConfig> videoConfigs = VideoConfig.find(VideoConfig.class,
//                    "username=? and device_id = ?", username, androidId);
//            if (videoConfigs.size() > 0) {
//                return videoConfigs.get(0);
//            } else {
//                return generateVideoConfig(androidId, username);
//            }
//        } else {
            return generateVideoConfig(String.valueOf(System.currentTimeMillis()), username);
//        }
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
            int difference = Math.abs(Constants.VIDEO_DEFAULT_WIDTH - bSize.height);
            if (difference == 0) {
                videoConfig.setWidth(bSize.height);
                break;
            } else {
                if (difference < Constants.VIDEO_DEFAULT_WIDTH - videoConfig.getWidth()) {
                    videoConfig.setWidth(bSize.height);
                }
            }
        }
        for (Camera.Size bSize : backSizeList) {
            int difference = Math.abs(Constants.VIDEO_DEFAULT_HEIGHT - bSize.width);
            if (difference == 0) {
                videoConfig.setHeight(bSize.width);
                break;
            } else {
                if (difference < Constants.VIDEO_DEFAULT_HEIGHT
                        - videoConfig.getHeight() && bSize.width < videoConfig.getWidth()) {
                    videoConfig.setHeight(bSize.width);
                }
            }
        }
        back.release();
        Camera front = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        List<Camera.Size> frontSizeList = front.getParameters().getSupportedPreviewSizes();
        for (Camera.Size fSize : frontSizeList) {
            int difference = Math.abs(Constants.VIDEO_DEFAULT_WIDTH - fSize.height);
            if (difference == 0) {
                videoConfig.setWidth(fSize.height);
                break;
            } else {
                if (difference < Constants.VIDEO_DEFAULT_WIDTH - videoConfig.getWidth()) {
                    videoConfig.setWidth(fSize.height);
                }
            }
        }
        for (Camera.Size fSize : frontSizeList) {
            int difference = Math.abs(Constants.VIDEO_DEFAULT_HEIGHT - fSize.width);
            if (difference == 0) {
                videoConfig.setHeight(fSize.width);
                break;
            } else {
                if (difference < Constants.VIDEO_DEFAULT_HEIGHT
                        - videoConfig.getHeight() && fSize.width < videoConfig.getWidth()) {
                    videoConfig.setHeight(fSize.width);
                }
            }
        }
        front.release();

        VideoConfig.deleteAll(VideoConfig.class);
        videoConfig.save();
        return videoConfig;
    }
}
