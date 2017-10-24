package com.ben.yjh.babycare.main.event.video;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.Utils;

import java.util.ArrayList;
import java.util.List;

import mabeijianxi.camera.views.SurfaceVideoView;


public class VideoPostActivity extends BaseActivity implements View.OnClickListener,
        SurfaceVideoView.OnPlayStateListener, OnErrorListener,
        OnPreparedListener, OnCompletionListener, OnInfoListener {

    private String mVideoUrl;
    private Bitmap mVideoThumbnailBitmap;
    private EditText mContentEditText;
    private SurfaceVideoView mVideoView;
    private AlertDialog mDialog;
    private boolean mNeedResume;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_text_edit_activity);

        Utils.changeStatusBarColor(this, R.color.black);
        initToolbar(0);
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
//        mVideoThumbnailBitmap = intent.getStringExtra(Constants.VIDEO_THUMBNAIL);
//        Bitmap bitmap = BitmapFactory.decodeFile(mVideoThumbnailBitmap);
        mVideoThumbnailBitmap = ThumbnailUtils.createVideoThumbnail(
                mVideoUrl, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        mContentEditText.setHint(R.string.video_what_you_think);
        mContentEditText.requestFocus();
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
                postEventTask();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postEventTask() {
        List<String> base64Images = new ArrayList<>();
        String thumbnail = ImageUtils.getBase64FromBitmap(mVideoThumbnailBitmap);
        base64Images.add(ImageUtils.getBase64FromFile(mVideoUrl));
        new EventTaskHandler(this, user.getToken()).addVideoEvent(user.getUserId(),
                mContentEditText.getText().toString(), thumbnail, mVideoView.getVideoWidth(),
                mVideoView.getVideoHeight(), base64Images, new HttpResponseInterface<Event>() {
                    @Override
                    public void onStart() {
                        showProgress(getString(R.string.posting), -1);
                    }

                    @Override
                    public void onSuccess(Event classOfT) {
                        Log.d("", "");
                        hideProgress();
                        classOfT.save();
                        Intent intent = getIntent();
                        intent.putExtra(Constants.EVENT, classOfT);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                        hideProgress();
                    }

                    @Override
                    public void onHttpError(String error) {
                        hideProgress();
                    }
                });
    }

    public ProgressDialog showProgress(String message, int theme) {
        if (mProgressDialog == null) {
            if (theme > 0)
                mProgressDialog = new ProgressDialog(this, theme);
            else
                mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
        mProgressDialog = null;
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
                intent.putExtra(Constants.VIDEO_TYPE, VideoPlayerActivity.TYPE_VIDEO_PREVIEW);
                intent.putExtra(Constants.VIDEO_URL, mVideoUrl);
                intent.putExtra(Constants.VIDEO_CONTENT, mContentEditText.getText().toString());
                startActivityForResult(intent, Constants.SHOW_VIDEO_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SHOW_VIDEO_REQUEST_CODE) {
                Intent intent = getIntent();
                intent.putExtra(Constants.EVENT, data.getSerializableExtra(Constants.EVENT));
                setResult(RESULT_OK, intent);
                finish();
            }
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
