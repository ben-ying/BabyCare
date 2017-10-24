package com.ben.yjh.babycare.main.event.video;

import android.media.MediaPlayer;

import com.waynell.videolist.widget.TextureVideoView;

public interface VideoLoadMvpView {

    TextureVideoView getVideoView();

    void videoBeginning();

    void videoStopped();

    void videoPrepared(MediaPlayer player);

    void videoResourceReady(String videoPath);
}
