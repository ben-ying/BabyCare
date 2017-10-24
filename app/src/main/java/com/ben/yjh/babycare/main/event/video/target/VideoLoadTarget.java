package com.ben.yjh.babycare.main.event.video.target;

import android.media.MediaPlayer;
import android.os.Build;

import com.ben.yjh.babycare.main.event.video.VideoLoadMvpView;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.waynell.videolist.widget.TextureVideoView;

import java.io.File;

public class VideoLoadTarget extends ViewTarget<TextureVideoView, File> implements TextureVideoView.MediaPlayerCallback {


    private final VideoLoadMvpView mLoadMvpView;

    public VideoLoadTarget(VideoLoadMvpView mvpView) {
        super(mvpView.getVideoView());
        view.setMediaPlayerCallback(this);
        mLoadMvpView = mvpView;
    }

    @Override
    public void onResourceReady(File resource, Transition<? super File> transition) {
        mLoadMvpView.videoResourceReady(resource.getAbsolutePath());
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mLoadMvpView.videoStopped();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mLoadMvpView.videoPrepared(mp);
        // it is better call when video rendering start, but this flag is added in API 17
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mLoadMvpView.videoBeginning();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // do nothing
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // do nothing
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        // do nothing
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            mLoadMvpView.videoBeginning();
            return true;
        }
        return false;
    }
}
