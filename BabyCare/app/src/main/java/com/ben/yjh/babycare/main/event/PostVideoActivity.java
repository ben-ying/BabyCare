package com.ben.yjh.babycare.main.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;

import mabeijianxi.camera.views.SurfaceVideoView;


public class PostVideoActivity extends BaseActivity implements View.OnClickListener,
        SurfaceVideoView.OnPlayStateListener, OnErrorListener,
        OnPreparedListener, OnCompletionListener, OnInfoListener {

    private String mVideoUrl;
    private TextView mSendTextView;
    private TextView mCancelTextView;
    private String mVideoThumbnailUrl;
    private EditText mContentEditText;
    private SurfaceVideoView mVideoView;
    private AlertDialog mDialog;
    private boolean mNeedResume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_text_edit_activity);

        mCancelTextView = (TextView) findViewById(R.id.tv_cancel);
        mSendTextView = (TextView) findViewById(R.id.tv_send);
        mContentEditText = (EditText) findViewById(R.id.et_send_content);
        mVideoView = (SurfaceVideoView) findViewById(R.id.videoView);
        mVideoView.requestLayout();
        Intent intent = getIntent();
        mVideoUrl = intent.getStringExtra(Constants.VIDEO_URL);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnPlayStateListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnClickListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setVideoPath(mVideoUrl);
        mVideoThumbnailUrl = intent.getStringExtra(Constants.VIDEO_THUMBNAIL);
        Bitmap bitmap = BitmapFactory.decodeFile(mVideoThumbnailUrl);
        mContentEditText.setHint(R.string.video_what_you_think);

        mCancelTextView.setOnClickListener(this);
        mSendTextView.setOnClickListener(this);
        mContentEditText.setOnClickListener(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
        mVideoView.setVolume(0);
        mVideoView.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // finish
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoView != null && mNeedResume) {
            mNeedResume = false;
            if (mVideoView.isRelease()) {
                mVideoView.reOpen();
            } else {
                mVideoView.start();
            }
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

    @Override
    public void onStateChanged(boolean isPlaying) {

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                cancelEvent();
                break;
            case R.id.tv_send:
                break;
            case R.id.videoView:
                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra(Constants.VIDEO_URL, mVideoUrl);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        cancelEvent();
    }

    private void cancelEvent() {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.alert)
                    .setMessage(R.string.exit_video_message)
                    .setPositiveButton(
                            R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            null).setCancelable(false).show();
        } else {
            mDialog.show();
        }
    }
}
