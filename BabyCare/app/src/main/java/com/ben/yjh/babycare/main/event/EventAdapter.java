package com.ben.yjh.babycare.main.event;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.event.video.holder.ImageViewHolder;
import com.ben.yjh.babycare.main.event.video.holder.TextViewHolder;
import com.ben.yjh.babycare.main.event.video.holder.VideoViewHolder;
import com.ben.yjh.babycare.main.left.UserInfoActivity;
import com.ben.yjh.babycare.main.user.UserDetailActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreRecyclerView;
import com.waynell.videolist.visibility.items.ListItem;
import com.waynell.videolist.visibility.scroll.ItemsProvider;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<TextViewHolder>
        implements ItemsProvider, View.OnClickListener, EventViewpagerAdapter.EventAdapterInterface {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;

    private Context mContext;
    private EventRecyclerViewInterface mInterface;
    private List<Event> mEvents;
    private User mUser;
    private LoadMoreRecyclerView mRecyclerView;
    private boolean mIsHomeEvent;

    @Override
    public void showImageDetail(int position) {
        mInterface.showImageDetail(position);
    }

    public interface EventRecyclerViewInterface {
        void showImageDetail(int position);

        void intent2CommentList(int eventId);

        void showShareSheet(Event event);
    }

    @Override
    public int getItemViewType(int position) {
        Event event = mEvents.get(position);
        if (event.getType() == Event.TYPE_VIDEO) {
            return TYPE_VIDEO;
        } else {
            if (!event.getImage1().isEmpty()) {
                return TYPE_IMAGE;
            } else {
                return TYPE_TEXT;
            }
        }
    }

    @Override
    public ListItem getListItem(int position) {
        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder instanceof ListItem) {
            return (ListItem) holder;
        }
        return null;
    }

    @Override
    public int listItemSize() {
        return getItemCount();
    }

    public EventRecyclerViewInterface getInterface() {
        return mInterface;
    }

    EventAdapter(Context context, LoadMoreRecyclerView recyclerView,
                 User user, List<Event> events, boolean isHomeEvent,
                 EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mUser = user;
        this.mEvents = events;
        this.mIsHomeEvent = isHomeEvent;
        this.mInterface = recyclerViewInterface;
    }

    public User getUser() {
        return mUser;
    }

    public void setData(List<Event> events) {
        this.mEvents = events;
        notifyDataSetChanged();
    }

    List<Event> getEvents() {
        return mEvents;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.media_layout);
        switch (viewType) {
            case TYPE_VIDEO:
                View videoView = LayoutInflater.from(
                        mContext).inflate(R.layout.item_card_video, parent, false);
                frameLayout.addView(videoView);
                return new VideoViewHolder(mContext, view, this);
            case TYPE_IMAGE:
                frameLayout.addView(LayoutInflater.from(
                        mContext).inflate(R.layout.item_card_image, parent, false));
                return new ImageViewHolder(mContext, view, this);
        }

        return new TextViewHolder(mContext, view, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.onBind(position, event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    private void intent2CommentList(int eventId) {
        mInterface.intent2CommentList(eventId);
    }

    private void showDeleteAlert(final Event event) {
        AlertUtils.showConfirmDialog(mContext, R.string.delete_event_alert,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(event);
                    }
                });
    }

    public void setLikeCount(RadioButton radioButton, Event event) {
        List<EventLike> likes = EventLike.find(EventLike.class,
                "event_id = ?", String.valueOf(event.getEventId()));
        radioButton.setChecked(false);
        radioButton.setEnabled(true);
        for (EventLike like : likes) {
            if (User.find(User.class, "user_id = ?",
                    String.valueOf(like.getLikeUserId())).size() > 0) {
                radioButton.setChecked(true);
                radioButton.setEnabled(false);
                break;
            }
        }
        if (likes.size() > 0) {
            radioButton.setText(String.valueOf(likes.size()));
        } else {
            radioButton.setText(R.string.empty);
        }
    }

    private void deleteTask(final Event event) {
        new EventTaskHandler(mContext, mUser.getToken()).deleteEvent(event.getEventId(),
                new HttpResponseInterface<Event>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Event classOfT) {
                        if (classOfT != null && event.getEventId() == classOfT.getEventId()) {
                            mEvents.remove(event);
                            notifyDataSetChanged();
                            Event.deleteAll(Event.class, "event_id = ?",
                                    String.valueOf(event.getEventId()));
                        }
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }

    private void likeTask(final RadioButton radioButton, final Event event) {
        new EventTaskHandler(mContext, mUser.getToken()).addLike(event, mUser.getUserId(),
                new HttpResponseInterface<EventLike>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(EventLike classOfT) {
                        classOfT.save();
                        setLikeCount(radioButton, event);
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Event event;
        switch (v.getId()) {
            case R.id.rb_comment:
                intent2CommentList((int) v.getTag());
                break;
            case R.id.rb_share:
                mInterface.showShareSheet((Event) v.getTag());
                break;
            case R.id.rb_common:
                event = (Event) v.getTag();
                if (event.getUserId() == mUser.getUserId()) {
                    showDeleteAlert(event);
                } else {
                    if (((RadioButton) v).isChecked()) {
                        likeTask(((RadioButton) v), event);
                    }
                }
                break;
            case R.id.ib_profile:
            case R.id.tv_name:
                if (mIsHomeEvent) {
                    int userId = (int) v.getTag();
                    if (userId == mUser.getUserId()) {
                        intent = new Intent(mContext, UserInfoActivity.class);
                        ((Activity) mContext).startActivityForResult(
                                intent, Constants.USER_INFO_REQUEST_CODE);
                    } else {
                        intent = new Intent(mContext, UserDetailActivity.class);
                        intent.putExtra(Constants.USER_ID, userId);
                        mContext.startActivity(intent);
                    }
                }
                break;
        }
    }
}
