package com.ben.yjh.babycare.main.event.video.holder;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.main.event.EventAdapter;
import com.ben.yjh.babycare.main.event.video.VideoListGlideModule;
import com.ben.yjh.babycare.main.event.video.VideoLoadMvpView;
import com.ben.yjh.babycare.main.event.video.VideoPlayerActivity;
import com.ben.yjh.babycare.main.event.video.target.VideoLoadTarget;
import com.ben.yjh.babycare.main.event.video.target.VideoProgressTarget;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.util.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.waynell.videolist.visibility.items.ListItem;
import com.waynell.videolist.widget.TextureVideoView;

import java.io.File;
import java.io.InputStream;

public class VideoViewHolder extends TextViewHolder implements
        ViewPropertyAnimatorListener, ListItem, View.OnClickListener, VideoLoadMvpView {
    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;

    private String mVideoLocalPath;
    private int mVideoState = STATE_IDLE;
    private TextureVideoView mVideoView;
    private ImageView mCoverImageView;
    private VideoProgressTarget mProgressTarget;
    private VideoLoadTarget mVideoTarget;
    private ProgressBar mProgressBar;

    public VideoViewHolder(Context context, View itemView, EventAdapter adapter) {
        super(context, itemView, adapter);
        this.mVideoView = (TextureVideoView) itemView.findViewById(R.id.videoView);
        this.mCoverImageView = (ImageView) itemView.findViewById(R.id.iv_cover);
        this.mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        this.mProgressBar.setVisibility(View.VISIBLE);
        this.mVideoView.setAlpha(0);
        this.mVideoTarget = new VideoLoadTarget(this);
        this.mProgressTarget = new VideoProgressTarget(mVideoTarget, mProgressBar);
    }

    @Override
    public void onBind(int position, Event event) {
        super.onBind(position, event);
        reset();
        mVideoView.setOnClickListener(this);
        mCoverImageView.setOnClickListener(this);
        Glide.with(context)
                .load(event.getVideoThumbnail()).centerCrop()
                .placeholder(new ColorDrawable(0xffdcdcdc))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        mCoverImageView.setBackgroundDrawable(resource);
                        mCoverImageView.setImageResource(R.mipmap.ic_play_circle_outline_white_48dp);
                        return true;
                    }
                }).into(mCoverImageView);
        Glide.with(context)
                .using(VideoListGlideModule.getOkHttpUrlLoader(), InputStream.class)
                .load(new GlideUrl(event.getVideoUrl()))
                .as(File.class)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mProgressTarget);
    }

    @Override
    public void onAnimationStart(View view) {

    }

    @Override
    public void onAnimationEnd(View view) {
        view.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationCancel(View view) {
    }

    @Override
    public void setActive(View view, int i) {
        mVideoState = STATE_ACTIVED;
        if (mVideoLocalPath != null) {
            mVideoView.setVideoPath(mVideoLocalPath);
            mVideoView.start();
        }
    }

    @Override
    public void deactivate(View view, int i) {
        mVideoState = STATE_DEACTIVED;
        mVideoView.stop();
        videoStopped();
    }

    @Override
    public TextureVideoView getVideoView() {
        return mVideoView;
    }

    @Override
    public void videoBeginning() {
        mVideoView.setAlpha(1.f);
        cancelAlphaAnimate(mCoverImageView);
        startAlphaAnimate(mCoverImageView);
    }

    @Override
    public void videoStopped() {
        cancelAlphaAnimate(mCoverImageView);
        mVideoView.setAlpha(0);
        mCoverImageView.setAlpha(1.f);
        mCoverImageView.setVisibility(View.VISIBLE);
    }

    private void cancelAlphaAnimate(View v) {
        ViewCompat.animate(v).cancel();
    }

    private void startAlphaAnimate(View v) {
        ViewCompat.animate(v).setListener(this).alpha(0f);
    }

    @Override
    public void videoPrepared(MediaPlayer player) {
        Log.d("VIDEO", "player: " + player);
        player.setVolume(0, 0);
    }

    @Override
    public void videoResourceReady(String videoPath) {
        mVideoLocalPath = videoPath;
        if (mVideoLocalPath != null) {
            mVideoView.setVideoPath(videoPath);
            if (mVideoState == STATE_ACTIVED) {
                mVideoView.start();
            }
        }
    }

    private void reset() {
        mVideoState = STATE_IDLE;
        mVideoView.stop();
        mVideoLocalPath = null;
        videoStopped();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoView:
                if (mVideoLocalPath != null) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra(Constants.VIDEO_URL, mVideoLocalPath);
                    intent.putExtra(Constants.VIDEO_TYPE, VideoPlayerActivity.TYPE_VIDEO_DETAIL);
                    context.startActivity(intent);
                    deactivate(v.getRootView(), getAdapterPosition());
                }
                break;
            case R.id.iv_cover:
                if (mVideoState == STATE_ACTIVED) {
                    deactivate(v.getRootView(), getAdapterPosition());
                } else {
                    setActive(v.getRootView(), getAdapterPosition());
                }
        }
    }
}
