package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;


public class EventComment extends SugarRecord implements Serializable {
    @SerializedName("comment_id") int commentId;
    @SerializedName("event_id") int eventId;
    @SerializedName("event_user_id") int eventUserId;
    // who comment user and commented by comment user
    @SerializedName("indirect_user_id") int indirectUserId;
    @SerializedName("comment_user_id") int commentUserId;
    @SerializedName("comment") String comment;
    @SerializedName("datetime") String datetime;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventUserId() {
        return eventUserId;
    }

    public void setEventUserId(int eventUserId) {
        this.eventUserId = eventUserId;
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
}
