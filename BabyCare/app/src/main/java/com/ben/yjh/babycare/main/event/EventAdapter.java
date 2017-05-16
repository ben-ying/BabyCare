package com.ben.yjh.babycare.main.event;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface, View.OnClickListener, CacheListener {

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
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.d("VIDEO", "cache file: " + cacheFile.getAbsolutePath()
                + ", url: " + url + "percentage: " + percentsAvailable);
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
        holder.shareRadioButton.setTag(event);
        holder.profileButton.setOnClickListener(this);
        holder.nameTextView.setOnClickListener(this);
        holder.profileButton.setTag(event.getUserId());
        holder.nameTextView.setTag(event.getUserId());
        holder.commentRadioButton.setTag(event.getEventId());
        holder.videoView.getLayoutParams().height = (int)
                mContext.getResources().getDimension(R.dimen.timeline_image_height);

        if (event.getUserId() == mUser.getUserId()) {
            holder.commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    mContext.getResources().getDrawable(R.drawable.btn_delete), null, null, null);
            holder.commonRadioButton.setText(R.string.empty);
        } else {
            holder.commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    mContext.getResources().getDrawable(R.drawable.btn_like), null, null, null);
            setLikeCount(holder.commonRadioButton, event);
        }

        if (!event.getImage1().isEmpty()) {
            holder.viewPager.setVisibility(View.VISIBLE);
            holder.pageIndicator.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.pauseImageView.setVisibility(View.GONE);
            List<String> images = new ArrayList<>();
            images.add(event.getImage1());
            holder.viewPager.setAdapter(new EventViewpagerAdapter(mContext, images, position, this));
            holder.pageIndicator.setViewPager(holder.viewPager);
            holder.pageIndicator.setSnap(true);
            holder.pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
            holder.pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));
        } else if (event.getType() == Event.TYPE_VIDEO && event.getVideoUrl() != null) {
            holder.videoView.setVisibility(View.VISIBLE);
            holder.pauseImageView.setVisibility(View.VISIBLE);
            holder.pauseImageView.setOnClickListener(this);
            holder.pauseImageView.setTag(holder.videoView);
            MyApplication.getInstance(mContext).displayImage(event.getVideoThumbnail(),
                    holder.pauseImageView, ImageUtils.getGalleryOptions(), false, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            view.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
                            ((ImageView) view).setImageResource(R.mipmap.ic_play_circle_outline_white_48dp);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
            holder.viewPager.setVisibility(View.GONE);
            holder.pageIndicator.setVisibility(View.GONE);
//            HttpProxyCacheServer proxy = MyApplication.getProxy(mContext);
//            proxy.registerCacheListener(this, event.getVideoUrl());
//            String proxyUrl = proxy.getProxyUrl(event.getVideoUrl());
////            holder.videoView.setVideoPath(event.getVideo());
//            holder.videoView.setVideoPath(proxyUrl);
//            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setLooping(true);
//                    mp.setVolume(0, 0);
//                }
//            });
//            holder.videoView.seekTo(10);
//            if (proxy.isCached(event.getVideoUrl())) {
//                handleVideo(holder.videoView, holder.pauseImageView);
//            }
//            holder.videoView.getLayoutParams().height =  (int) (mScreenWidth
//                            - 2 * mContext.getResources().getDimension(R.dimen.recycler_view_padding)
//                            - 2 * mContext.getResources().getDimension(R.dimen.text_padding))
//                            * event.getVideoWidth() / event.getVideoHeight();
        } else {
            holder.videoView.setVisibility(View.GONE);
            holder.pauseImageView.setVisibility(View.GONE);
            holder.viewPager.setVisibility(View.GONE);
            holder.pageIndicator.setVisibility(View.GONE);
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

        MyApplication.getInstance(mContext).displayTinyImage(event.getUserProfile(),
                holder.profileButton, ImageUtils.getTinyProfileImageOptions(mContext));
        holder.nameTextView.setText(event.getUsername());
        holder.dateTextView.setText(Utils.getFormatDate(mContext, event.getCreated()));
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
        VideoView videoView;
        ImageView pauseImageView;

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
            this.videoView = (VideoView) itemView.findViewById(R.id.videoView);
            this.pauseImageView = (ImageView) itemView.findViewById(R.id.iv_pause);
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
                VideoView videoView = (VideoView) v.getTag();
                final ImageView imageView = (ImageView) v;
                handleVideo(videoView, imageView);
                break;
        }
    }

    public void handleVideo(VideoView videoView, final ImageView imageView) {
        if (videoView.isPlaying()) {
            videoView.pause();
            imageView.setImageResource(R.mipmap.ic_play_circle_outline_white_48dp);
        } else {
            videoView.pause();
            videoView.start();
            if (videoView.getBufferPercentage() > 0) {
                imageView.setBackgroundColor(
                        mContext.getResources().getColor(android.R.color.transparent));
                imageView.setImageResource(0);
            } else {
                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setBackgroundColor(
                                mContext.getResources().getColor(android.R.color.transparent));
                        imageView.setImageResource(0);
                    }
                }, 200);
            }
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
