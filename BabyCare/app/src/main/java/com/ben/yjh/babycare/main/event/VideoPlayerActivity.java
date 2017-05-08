package com.ben.yjh.babycare.main.event;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;

import mabeijianxi.camera.util.StringUtils;
import mabeijianxi.camera.views.SurfaceVideoView;


public class VideoPlayerActivity extends BaseActivity implements
        SurfaceVideoView.OnPlayStateListener, OnErrorListener,
        OnPreparedListener, OnClickListener, OnCompletionListener,
        OnInfoListener {

    private SurfaceVideoView mVideoView;
    private View mPlayerStatus;
    private View mLoading;
    private String mVideoUrl;
    private boolean mNeedResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mVideoUrl = getIntent().getStringExtra(Constants.VIDEO_URL);
        if (StringUtils.isEmpty(mVideoUrl)) {
            finish();
            return;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mVideoView = (SurfaceVideoView) findViewById(R.id.videoView);
        mVideoView.requestLayout();

        mPlayerStatus = findViewById(R.id.iv_pause);
        mLoading = findViewById(R.id.progressBar);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnPlayStateListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnClickListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnCompletionListener(this);
        findViewById(R.id.root).setOnClickListener(this);
        mVideoView.setVideoPath(mVideoUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.title_post:
                // TODO: 5/8/17
                setResult(RESULT_OK);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoView != null && mNeedResume) {
            mNeedResume = false;
            if (mVideoView.isRelease())
                mVideoView.reOpen();
            else
                mVideoView.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null) {
            if (mVideoView.isPlaying()) {
                mNeedResume = true;
                mVideoView.pause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null) {
            mVideoView.release();
            mVideoView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
        mVideoView.setVolume(SurfaceVideoView.getSystemVolumn(this));
        mVideoView.start();
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            // system volumn
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                mVideoView.dispatchKeyEvent(this, event);
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onStateChanged(boolean isPlaying) {
        mPlayerStatus.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (!isFinishing()) {
            // play failed
        }
        finish();
        return false;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pause:
            case R.id.root:
                finish();
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // finish
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                // video and sound error
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (!isFinishing())
                    mVideoView.pause();
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (!isFinishing())
                    mVideoView.start();
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                mVideoView.setBackground(null);
                break;
        }
        return false;
    }

}
