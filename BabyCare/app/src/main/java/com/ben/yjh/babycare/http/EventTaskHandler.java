package com.ben.yjh.babycare.http;


import android.content.Context;

import com.android.volley.Request;
import com.ben.yjh.babycare.model.CommentsResult;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.EventsResult;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class EventTaskHandler extends BaseTaskHandler {

    private static final String URL_EVENTS = "events/";
    private static final String URL_LIKES = "event/likes";
    private static final String URL_LIKE = "event/like";
    private static final String URL_DELETE = "event/delete";
    private static final String URL_COMMENTS = "event/comments/";
    private static final String URL_COMMENT = "event/comment";

    private String mToken;

    public EventTaskHandler(Context context, String token) {
        super(context);
        this.mToken = token;
    }

    public void getEvents(int userId, HttpResponseInterface<EventsResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context, false).startTask(URL_EVENTS + "?token=" + mToken + "&user_id=" + userId,
                    Request.Method.GET, bodyObject, EventsResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMoreEvents(String url, HttpResponseInterface<EventsResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context, false).startTask(url, Request.Method.GET, bodyObject,
                    EventsResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addImageEvent(int userId, String title, String content,
                              List<String> base64s, HttpResponseInterface<Event> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("user_id", userId);
            bodyObject.put("title", title);
            bodyObject.put("content", content);
            bodyObject.put("token", mToken);
            bodyObject.put("type", Event.TYPE_IMAGE);
            if (base64s.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (String base64 : base64s) {
                    jsonArray.put(base64);
                }
                bodyObject.put("base64s", jsonArray);
            }
            new HttpPostTask(context, true).startTask(URL_EVENTS, Request.Method.POST,
                    bodyObject, Event.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addVideoEvent(int userId, String content, String videoThumbnail, int width, int height,
                              List<String> base64s, HttpResponseInterface<Event> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("user_id", userId);
            bodyObject.put("content", content);
            bodyObject.put("video_width", width);
            bodyObject.put("video_height", height);
            bodyObject.put("video_thumbnail", videoThumbnail);
            bodyObject.put("token", mToken);
            bodyObject.put("type", Event.TYPE_VIDEO);
            if (base64s.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (String base64 : base64s) {
                    jsonArray.put(base64);
                }
                bodyObject.put("base64s", jsonArray);
            }
            new HttpPostTask(context, false).startTask(URL_EVENTS, Request.Method.POST,
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

    public void deleteEvent(int eventId,
                       HttpResponseInterface<Event> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            new HttpPostTask(context).startTask(URL_EVENTS + eventId, Request.Method.DELETE,
                    bodyObject, Event.class, true, httpResponseInterface);
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

    public void getComments(int eventId,
                     HttpResponseInterface<CommentsResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context, false).startTask(URL_COMMENTS + "?token=" + mToken + "&event_id=" + eventId,
                    Request.Method.GET, bodyObject, CommentsResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMoreComments(String url, HttpResponseInterface<CommentsResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context, false).startTask(url, Request.Method.GET, bodyObject,
                    CommentsResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addComment(String text, int eventId, int userId, int replyCommentId,
                        HttpResponseInterface<EventComment> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            bodyObject.put("text", text);
            bodyObject.put("event_id", eventId);
            bodyObject.put("user_id", userId);
            bodyObject.put("source_comment_id", replyCommentId);
            new HttpPostTask(context).startTask(URL_COMMENTS, Request.Method.POST,
                    bodyObject, EventComment.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteComment(int commentId,
                           HttpResponseInterface<EventComment> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            new HttpPostTask(context).startTask(URL_COMMENTS + commentId, Request.Method.DELETE,
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
