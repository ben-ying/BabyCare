package com.ben.yjh.babycare.main.event;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.main.left.UserInfoActivity;
import com.ben.yjh.babycare.main.user.UserDetailActivity;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface, View.OnClickListener {

    private Context mContext;
    private User mUser;
    private EventRecyclerViewInterface mInterface;
    private List<Event> mEvents;
    private boolean mIsHomeEvent;

    public void showImageDetail(int position) {
        mInterface.showImageDetail(position);
    }

    public void intent2CommentList() {
        mInterface.intent2CommentList();
    }

    public interface EventRecyclerViewInterface {
        void showImageDetail(int position);
        void intent2CommentList();
    }

    public EventAdapter(Context context, User user, List<Event> events, boolean isHomeEvent,
                 EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mUser = user;
        this.mEvents = events;
        this.mIsHomeEvent = isHomeEvent;
        this.mInterface = recyclerViewInterface;
    }

    public void setData(List<Event> events) {
        this.mEvents = events;
        notifyDataSetChanged();
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mEvents.get(position);

        holder.commonRadioButton.setOnClickListener(this);
        holder.commonRadioButton.setTag(event);
        holder.commentRadioButton.setOnClickListener(this);
        holder.shareRadioButton.setOnClickListener(this);
        holder.profileButton.setOnClickListener(this);
        holder.nameTextView.setOnClickListener(this);
        holder.profileButton.setTag(event.getUserId());
        holder.nameTextView.setTag(event.getUserId());

        if (event.getUserId() == mUser.getUserId()) {
            holder.commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    mContext.getResources().getDrawable(R.drawable.btn_delete), null, null, null);
            holder.commonRadioButton.setText("");
        } else {
            holder.commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    mContext.getResources().getDrawable(R.drawable.btn_like), null, null, null);
            setLikeCount(holder.commonRadioButton, event);
        }

        if (event.getImage1().isEmpty()) {
            holder.viewPager.setVisibility(View.GONE);
            holder.pageIndicator.setVisibility(View.GONE);
        } else {
            holder.viewPager.setVisibility(View.VISIBLE);
            holder.pageIndicator.setVisibility(View.GONE);
            List<String> images = new ArrayList<>();
            images.add(event.getImage1());
            holder.viewPager.setAdapter(new EventViewpagerAdapter(mContext, images, position, this));
            holder.pageIndicator.setViewPager(holder.viewPager);
            holder.pageIndicator.setSnap(true);
            holder.pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
            holder.pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));
        }

        if (event.getTitle().isEmpty()) {
            holder.titleTextView.setVisibility(View.GONE);
        } else {
            holder.titleTextView.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(event.getTitle());
        }

        if (event.getContent().isEmpty()) {
            holder.contentTextView.setVisibility(View.GONE);
        } else {
            holder.contentTextView.setVisibility(View.VISIBLE);
            holder.contentTextView.setText(event.getContent());
        }

        MyApplication.displayTinyImage(event.getUserProfile(),
                holder.profileButton, ImageUtils.getTinyProfileImageOptions(mContext));
        holder.nameTextView.setText(event.getUsername());
        holder.dateTextView.setText(event.getCreatedDate(mContext));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        CirclePageIndicator pageIndicator;
        RadioButton commonRadioButton;
        RadioButton commentRadioButton;
        RadioButton shareRadioButton;
        ImageButton profileButton;
        TextView nameTextView;
        TextView dateTextView;
        TextView titleTextView;
        TextView contentTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            this.viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            this.pageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
            this.commonRadioButton = (RadioButton) itemView.findViewById(R.id.rb_common);
            this.commentRadioButton = (RadioButton) itemView.findViewById(R.id.rb_comment);
            this.shareRadioButton = (RadioButton) itemView.findViewById(R.id.rb_share);
            this.profileButton = (ImageButton) itemView.findViewById(R.id.ib_profile);
            this.nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            this.dateTextView = (TextView) itemView.findViewById(R.id.tv_datetime);
            this.titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            this.contentTextView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_comment:
                intent2CommentList();
                break;
            case R.id.rb_share:
                break;
            case R.id.rb_common:
                Event event = (Event) v.getTag();
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
                    Intent intent;
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

    private void setLikeCount(RadioButton radioButton, Event event) {
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
            radioButton.setText("");
        }
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

    private void deleteTask(final Event event) {
        new EventTaskHandler(mContext, mUser.getToken()).delete(event.getEventId(),
                new HttpResponseInterface<Event>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Event classOfT) {
                        mEvents.remove(event);
                        notifyDataSetChanged();
                        if (classOfT != null) {
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
}
