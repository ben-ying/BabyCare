package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;


public class EventComment extends SugarRecord implements Serializable {
    @Unique
    @SerializedName("comment_id") int commentId;
    @SerializedName("event_id") int eventId;
    // who comment user and commented by comment user
    @SerializedName("indirect_user_id") int indirectUserId;
    @SerializedName("indirect_user_name") int indirectUserName;
    @SerializedName("comment_user_id") int commentUserId;
    @SerializedName("comment") String comment;
    @SerializedName("datetime") String datetime;

    @SerializedName("username") String username;
    @SerializedName("user_profile") String userProfile;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getIndirectUserId() {
        return indirectUserId;
    }

    public void setIndirectUserId(int indirectUserId) {
        this.indirectUserId = indirectUserId;
    }

    public int getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(int commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getIndirectUserName() {
        return indirectUserName;
    }

    public void setIndirectUserName(int indirectUserName) {
        this.indirectUserName = indirectUserName;
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
}
