package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;


public class EventLike extends SugarRecord implements Serializable {
    @SerializedName("like_id") int likeId;
    @SerializedName("event_id") int eventId;
    @SerializedName("event_user_id") int eventUserId;
    @SerializedName("like_user_id") int likeUserId;
    @SerializedName("datetime") String datetime;

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

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

    public int getLikeUserId() {
        return likeUserId;
    }

    public void setLikeUserId(int likeUserId) {
        this.likeUserId = likeUserId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
