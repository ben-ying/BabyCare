package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mabeijianxi.camera.MediaRecorderBase;


public class Event extends SugarRecord implements Serializable {

    @Unique
    @SerializedName("event_id") int eventId;
    @SerializedName("user_id") int userId;
    @SerializedName("type") int type;
    @SerializedName("title") String title;
    @SerializedName("content") String content;
    @SerializedName("image1") String image1;
    @SerializedName("video_url") String videoUrl;
    @SerializedName("video_width") int videoWidth;
    @SerializedName("video_height") int videoHeight;
    @SerializedName("video_thumbnail") String videoThumbnail;
    @SerializedName("like_count") int likeCount;
    @SerializedName("comment_count") int commentCount;
    @SerializedName("created") String created;
    @SerializedName("modified") String modified;

    // not in event model
    @SerializedName("username") String username;
    @SerializedName("user_profile") String userProfile;
    @Ignore
    @SerializedName("likes") List<EventLike> eventLikes;
    @Ignore
    @SerializedName("comments") List<EventComment> eventComments;

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title == null ? "" : title.trim();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content == null ? "" : content.trim();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage1() {
        return image1 == null ? "" : image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreated() {
        return created == null ? "" : created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified == null ? "" : modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<EventLike> getEventLikes() {
        return eventLikes == null ? new ArrayList<EventLike>() : eventLikes;
    }

    public void setEventLikes(List<EventLike> eventLikes) {
        this.eventLikes = eventLikes;
    }

    public List<EventComment> getEventComments() {
        return eventComments == null ? new ArrayList<EventComment>() : eventComments;
    }

    public void setEventComments(List<EventComment> eventComments) {
        this.eventComments = eventComments;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getVideoWidth() {
        return videoWidth == 0 ? MediaRecorderBase.SMALL_VIDEO_HEIGHT : videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight == 0 ? MediaRecorderBase.SMALL_VIDEO_WIDTH : videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }
}
