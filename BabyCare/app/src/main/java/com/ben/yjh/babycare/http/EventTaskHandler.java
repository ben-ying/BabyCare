package com.ben.yjh.babycare.http;


import android.content.Context;

import com.android.volley.Request;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.HttpBaseResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EventTaskHandler extends BaseTaskHandler {

    private static final String URL_EVENTS = "events/";
    private static final String URL_LIKES = "event/likes";
    private static final String URL_LIKE = "event/like";
    private static final String URL_DELETE = "event/delete";
    private static final String URL_COMMENTS = "event/comments";
    private static final String URL_COMMENT = "event/comment";

    private String mToken;

    public EventTaskHandler(Context context, String token) {
        super(context);
        this.mToken = token;
    }

    public void getEvents(HttpResponseInterface<Event[]> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            new HttpPostTask(context).startTask(URL_EVENTS, Request.Method.GET,
                    bodyObject, Event[].class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEvent(int userId, String title, String content,
                         List<String> base64Images, HttpResponseInterface<Event> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("user_id", userId);
            bodyObject.put("title", title);
            bodyObject.put("content", content);
            bodyObject.put("token", mToken);
            if (base64Images.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (String base64 : base64Images) {
                    jsonArray.put(base64);
                }
                bodyObject.put("base64_images", jsonArray);
            }
            new HttpPostTask(context).startTask(URL_EVENTS, Request.Method.POST,
                    bodyObject, Event.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getEvent(int eventId, HttpResponseInterface<Event[]> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context).startTask(URL_EVENTS + eventId, Request.Method.GET,
                    bodyObject, Event[].class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int eventId,
                       HttpResponseInterface<Event> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            new HttpPostTask(context).startTask(URL_EVENTS + eventId, Request.Method.DELETE,
                    bodyObject, Event.class, false, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLikes(int eventId,
                     HttpResponseInterface<HttpBaseResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            bodyObject.put("event_id", eventId);
            new HttpPostTask(context).startTask(URL_LIKES, Request.Method.GET,
                    bodyObject, HttpBaseResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLike(int likeId, HttpResponseInterface<HttpBaseResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context).startTask(URL_LIKES + likeId, Request.Method.GET,
                    bodyObject, HttpBaseResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLike(Event event, int userId,
                        HttpResponseInterface<EventLike> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            bodyObject.put("event_id", event.getEventId());
            bodyObject.put("like_user_id", userId);
            new HttpPostTask(context).startTask(URL_LIKE, Request.Method.POST,
                    bodyObject, EventLike.class, false, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getComments(String token, String eventId,
                     HttpResponseInterface<EventComment[]> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", token);
            bodyObject.put("event_id", eventId);
            new HttpPostTask(context).startTask(URL_COMMENTS, Request.Method.GET,
                    bodyObject, EventComment[].class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addComment(int userId, Event event, String indirectUserId, String comment,
                        HttpResponseInterface<EventComment> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            bodyObject.put("event_id", event.getEventId());
            bodyObject.put("comment_user_id", userId);
            bodyObject.put("event_user_id", event.getUserId());
            bodyObject.put("indirect_user_id", indirectUserId);
            bodyObject.put("comment", comment);
            new HttpPostTask(context).startTask(URL_COMMENT, Request.Method.POST,
                    bodyObject, EventComment.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getComment(String commentId,
                           HttpResponseInterface<EventComment> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context).startTask(URL_COMMENTS + commentId, Request.Method.GET,
                    bodyObject, EventComment.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
