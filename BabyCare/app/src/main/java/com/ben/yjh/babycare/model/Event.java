package com.ben.yjh.babycare.model;

import android.content.Context;

import com.ben.yjh.babycare.util.Utils;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Event extends SugarRecord implements Serializable {

    @SerializedName("event_id") int eventId;
    @SerializedName("user_id") int userId;
    @SerializedName("title") String title;
    @SerializedName("content") String content;
    @SerializedName("image1") String image1;
    @SerializedName("like_count") int likeCount;
    @SerializedName("comment_count") int commentCount;
    @SerializedName("created") String created;
    @SerializedName("modified") String modified;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
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

    public String getCreatedDate(Context context) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = format.parse(created);
            return Utils.getDateStr(context, date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
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
}
