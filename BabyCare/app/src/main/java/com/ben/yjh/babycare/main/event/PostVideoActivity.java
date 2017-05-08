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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private String mVideoThumbnailUrl;
    private EditText mContentEditText;
    private SurfaceVideoView mVideoView;
    private AlertDialog mDialog;
    private boolean mNeedResume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_text_edit_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

        mContentEditText.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cancelEvent();
                break;
            case R.id.title_post:
                // TODO: 5/8/17
                setResult(RESULT_OK);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
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
