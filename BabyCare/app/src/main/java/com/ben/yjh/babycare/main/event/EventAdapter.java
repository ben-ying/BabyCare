package com.ben.yjh.babycare.main.event;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.left.UserInfoActivity;
import com.ben.yjh.babycare.main.user.UserDetailActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.Utils;
import com.viewpagerindicator.CirclePageIndicator;
import com.waynell.videolist.widget.TextureVideoView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.BaseViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface, View.OnClickListener {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;

    private Context mContext;
    private User mUser;
    private EventRecyclerViewInterface mInterface;
    private List<Event> mEvents;
    private boolean mIsHomeEvent;
    private int mScreenWidth;

    public void showImageDetail(int position) {
        mInterface.showImageDetail(position);
    }

    private void intent2CommentList(int eventId) {
        mInterface.intent2CommentList(eventId);
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

    interface EventRecyclerViewInterface {
        void showImageDetail(int position);

        void intent2CommentList(int eventId);

        void showShareSheet(Event event);
    }

    EventAdapter(Context context, User user, List<Event> events, boolean isHomeEvent,
                 EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mUser = user;
        this.mEvents = events;
        this.mIsHomeEvent = isHomeEvent;
        this.mInterface = recyclerViewInterface;
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
    }

    public void setData(List<Event> events) {
        this.mEvents = events;
        notifyDataSetChanged();
    }

    List<Event> getEvents() {
        return mEvents;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.media_layout);
        switch (viewType) {
            case TYPE_VIDEO:
                frameLayout.addView(LayoutInflater.from(
                        mContext).inflate(R.layout.item_card_video, parent, false));
                return new VideoViewHolder(view);
            case TYPE_IMAGE:
                frameLayout.addView(LayoutInflater.from(
                        mContext).inflate(R.layout.item_card_image, parent, false));
                return new ImageViewHolder(view);
        }

        return new BaseViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.onBind(position, event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        RadioButton commonRadioButton;
        RadioButton commentRadioButton;
        RadioButton shareRadioButton;
        ImageButton profileButton;
        TextView nameTextView;
        TextView dateTextView;
        TextView titleTextView;
        TextView contentTextView;

        BaseViewHolder(View itemView) {
            super(itemView);
            this.commonRadioButton = (RadioButton) itemView.findViewById(R.id.rb_common);
            this.commentRadioButton = (RadioButton) itemView.findViewById(R.id.rb_comment);
            this.shareRadioButton = (RadioButton) itemView.findViewById(R.id.rb_share);
            this.profileButton = (ImageButton) itemView.findViewById(R.id.ib_profile);
            this.nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            this.dateTextView = (TextView) itemView.findViewById(R.id.tv_datetime);
            this.titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            this.contentTextView = (TextView) itemView.findViewById(R.id.tv_content);
        }

        public void onBind(int position, Event event) {
            commonRadioButton.setOnClickListener(EventAdapter.this);
            commonRadioButton.setTag(event);
            commentRadioButton.setOnClickListener(EventAdapter.this);
            shareRadioButton.setOnClickListener(EventAdapter.this);
            shareRadioButton.setTag(event);
            profileButton.setOnClickListener(EventAdapter.this);
            nameTextView.setOnClickListener(EventAdapter.this);
            profileButton.setTag(event.getUserId());
            nameTextView.setTag(event.getUserId());
            commentRadioButton.setTag(event.getEventId());
            if (event.getUserId() == mUser.getUserId()) {
                commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.btn_delete), null, null, null);
                commonRadioButton.setText(R.string.empty);
            } else {
                commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.btn_like), null, null, null);
                setLikeCount(commonRadioButton, event);
            }

            if (event.getTitle().isEmpty()) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setVisibility(View.VISIBLE);
                titleTextView.setText(event.getTitle());
            }

            if (event.getContent().isEmpty()) {
                contentTextView.setVisibility(View.GONE);
            } else {
                contentTextView.setVisibility(View.VISIBLE);
                contentTextView.setText(event.getContent());
            }

            MyApplication.getInstance(mContext).displayTinyImage(event.getUserProfile(),
                    profileButton, ImageUtils.getTinyProfileImageOptions(mContext));
            nameTextView.setText(event.getUsername());
            dateTextView.setText(Utils.getFormatDate(mContext, event.getCreated()));
        }
    }

    private class ImageViewHolder extends BaseViewHolder {
        ViewPager viewPager;
        CirclePageIndicator pageIndicator;

        ImageViewHolder(View itemView) {
            super(itemView);
            this.viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            this.pageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
        }

        @Override
        public void onBind(int position, Event event) {
            List<String> images = new ArrayList<>();
            images.add(event.getImage1());
            viewPager.setAdapter(new EventViewpagerAdapter(
                    mContext, images, position, EventAdapter.this));
            pageIndicator.setViewPager(viewPager);
            pageIndicator.setSnap(true);
            pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
            pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
            pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));
        }
    }

    private class VideoViewHolder extends BaseViewHolder {
        TextureVideoView videoView;
        ImageView pauseImageView;

        VideoViewHolder(View itemView) {
            super(itemView);
            this.videoView = (TextureVideoView) itemView.findViewById(R.id.videoView);
            this.pauseImageView = (ImageView) itemView.findViewById(R.id.iv_pause);
        }

        @Override
        public void onBind(int position, Event event) {

        }
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
            case R.id.iv_pause:
//                VideoView videoView = (VideoView) v.getTag();
//                final ImageView imageView = (ImageView) v;
//                handleVideo(videoView, imageView);
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
            radioButton.setText(R.string.empty);
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
}
