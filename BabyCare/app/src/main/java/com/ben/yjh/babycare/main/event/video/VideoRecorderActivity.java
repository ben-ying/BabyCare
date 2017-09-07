package com.ben.yjh.babycare.main.event.video;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.Utils;
import com.ben.yjh.babycare.widget.VideoProgressView;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.File;

import mabeijianxi.camera.MediaRecorderBase;
import mabeijianxi.camera.MediaRecorderNative;
import mabeijianxi.camera.MediaRecorderSystem;
import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.model.MediaObject;
import mabeijianxi.camera.model.MediaRecorderConfig;
import mabeijianxi.camera.util.DeviceUtils;
import mabeijianxi.camera.util.FileUtils;

import static mabeijianxi.camera.MediaRecorderBase.compressConfig;

public class VideoRecorderActivity extends BaseActivity implements
        MediaRecorderBase.OnErrorListener, OnClickListener, MediaRecorderBase.OnPreparedListener,
        MediaRecorderBase.OnEncodeListener {

    private static final int HANDLE_INVALIDATE_PROGRESS = 0;
    private static final int HANDLE_STOP_RECORD = 1;
    private CheckedTextView mRecordDelete;
    private Button mRecordController;
    private RelativeLayout mBottomLayout;
    private SurfaceView mSurfaceView;
    private VideoProgressView mProgressView;
    private MediaRecorderBase mMediaRecorder;
    private MediaObject mMediaObject;
    private volatile boolean mPressedStatus;
    private volatile boolean mReleased;
    private volatile boolean mIsFlashMode;
    private MediaRecorderConfig mConfig;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recorder);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Utils.changeStatusBarColor(this, R.color.black);
        initToolbar(0);

        Intent intent = getIntent();
        mConfig = intent.getParcelableExtra(Constants.VIDEO_CONFIG);
        if (mConfig == null) {
            finish();
        }

        MediaRecorderBase.SMALL_VIDEO_WIDTH = mConfig.getSmallVideoWidth();
        MediaRecorderBase.SMALL_VIDEO_HEIGHT = mConfig.getSmallVideoHeight();

        mSurfaceView = (SurfaceView) findViewById(R.id.recordPreview);
        mProgressView = (VideoProgressView) findViewById(R.id.recordProgress);
        mRecordDelete = (CheckedTextView) findViewById(R.id.tv_delete);
        mRecordController = (Button) findViewById(R.id.btn_controller);
        mBottomLayout = (RelativeLayout) findViewById(R.id.rl_bottom);

        mRecordDelete.setOnClickListener(this);
        mRecordController.setOnTouchListener(mOnVideoControllerTouchListener);

        mProgressView.setMaxDuration(mConfig.getRecordTimeMax());
        mProgressView.setMinTime(mConfig.getRecordTimeMin());

        initMediaRecorder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_recorder, menu);
        MenuItem itemFlash = menu.findItem(R.id.title_flash);
        MenuItem itemCameraSwitch = menu.findItem(R.id.title_camera_swith);
        MenuItem itemNextStep = menu.findItem(R.id.title_next_step);
        if (DeviceUtils.isSupportCameraLedFlash(getPackageManager())) {
            itemCameraSwitch.setVisible(true);
        } else {
            itemCameraSwitch.setVisible(false);
        }
        if (DeviceUtils.isSupportCameraLedFlash(getPackageManager())) {
            itemFlash.setVisible(true);
        } else {
            itemFlash.setVisible(false);
        }
        if (!isFinishing() && mMediaObject != null) {
            int duration = mMediaObject.getDuration();
            if (duration < mConfig.getRecordTimeMin()) {
                if (duration == 0) {
                    mRecordDelete.setVisibility(View.GONE);
                }
                itemNextStep.setVisible(false);
            } else {
                itemNextStep.setVisible(true);
            }
        }

        if (mPressedStatus) {
            itemFlash.setEnabled(false);
            itemCameraSwitch.setEnabled(false);
            if (mMediaRecorder != null && mMediaRecorder instanceof MediaRecorderSystem) {
                itemCameraSwitch.setVisible(false);
            }
        } else {
            if (mMediaRecorder != null && mMediaRecorder.isFrontCamera()) {
                itemFlash.setEnabled(false);
            } else {
                itemFlash.setEnabled(true);
            }
            itemCameraSwitch.setEnabled(true);
        }

        if (mIsFlashMode) {
            itemFlash.setIcon(R.drawable.record_camera_flash_on);
        } else {
            itemFlash.setIcon(R.drawable.record_camera_flash_off);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.title_camera_swith:
                if (mIsFlashMode) {
                    if (mMediaRecorder != null) {
                        mMediaRecorder.toggleFlashMode();
                    }
                    mIsFlashMode = false;
                }

                if (mMediaRecorder != null) {
                    mMediaRecorder.switchCamera();
                }
                invalidateOptionsMenu();
                break;
            case R.id.title_flash:
                if (mMediaRecorder != null) {
                    mMediaRecorder.toggleFlashMode();
                    mIsFlashMode = !mIsFlashMode;
                }
                invalidateOptionsMenu();
                break;
            case R.id.title_next_step:
                mMediaRecorder.startEncoding();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSurfaceView() {
        final int w = DeviceUtils.getScreenWidth(this);
        ((RelativeLayout.LayoutParams) mBottomLayout.getLayoutParams()).topMargin =
                (int) (w / (mConfig.getSmallVideoWidth() / (mConfig.getSmallVideoHeight() * 1.0f)));
        int height = (int) (w * ((MediaRecorderBase.mSupportedPreviewWidth * 1.0f)
                / mConfig.getSmallVideoWidth()));
        //
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView
                .getLayoutParams();
        lp.width = w;
        lp.height = height;
        mSurfaceView.setLayoutParams(lp);
    }

    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnEncodeListener(this);
        mMediaRecorder.setOnPreparedListener(this);

        File f = new File(VCamera.getVideoCachePath());
        if (!FileUtils.checkFile(f)) {
            f.mkdirs();
        }
        String key = String.valueOf(System.currentTimeMillis());
        mMediaObject = mMediaRecorder.setOutputDirectory(key,
                VCamera.getVideoCachePath() + key);
        mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
        mMediaRecorder.prepare();
    }

    private View.OnTouchListener mOnVideoControllerTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mMediaObject.getDuration() >= mConfig.getRecordTimeMax()) {
                        return true;
                    }
                    if (cancelDelete()) {
                        return true;
                    }
                    startRecord();
                    break;

                case MotionEvent.ACTION_UP:
                    if (mPressedStatus) {
                        stopRecord();
                        if (mMediaObject.getDuration() >= mConfig.getRecordTimeMax()) {
                            mMediaRecorder.startEncoding();
                        }
                    }
                    break;
            }
            return true;
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();

        mMediaRecorder.prepare();
        mProgressView.setData(mMediaObject);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRecord();
        UtilityAdapter.freeFilterParser();
        if (!mReleased) {
            mMediaRecorder.release();
        }
        mReleased = false;
    }


    private void startRecord() {
        MediaObject.MediaPart part = mMediaRecorder.startRecord();
        if (part == null) {
            return;
        }
        mProgressView.setData(mMediaObject);

        mPressedStatus = true;
        mRecordController.animate().scaleX(0.8f).scaleY(0.8f).setDuration(500).start();

        if (mHandler != null) {
            mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
            mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
            mHandler.removeMessages(HANDLE_STOP_RECORD);
            mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD,
                    mConfig.getRecordTimeMax() - mMediaObject.getDuration());
        }
        mRecordDelete.setVisibility(View.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (mRecordDelete != null && mRecordDelete.isChecked()) {
            cancelDelete();
            return;
        }

        if (mMediaObject != null && mMediaObject.getDuration() > 1) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.alert)
                    .setMessage(R.string.exit_video_message)
                    .setPositiveButton(
                            R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    mMediaObject.delete();
                                    finish();
                                }

                            })
                    .setNegativeButton(R.string.cancel,
                            null).setCancelable(false).show();
            return;
        }

        if (mMediaObject != null) {
            mMediaObject.delete();
        }
        finish();
    }

    private void stopRecord() {
        mPressedStatus = false;
        mRecordController.animate().scaleX(1).scaleY(1).setDuration(500).start();
        mMediaRecorder.stopRecord();
        mRecordDelete.setVisibility(View.VISIBLE);
        mHandler.removeMessages(HANDLE_STOP_RECORD);
        checkStatus();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (mHandler.hasMessages(HANDLE_STOP_RECORD)) {
            mHandler.removeMessages(HANDLE_STOP_RECORD);
        }

        switch (v.getId()) {
            case R.id.tv_delete:
                if (mMediaObject != null) {
                    MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                    if (part != null) {
                        if (part.remove) {
                            part.remove = false;
                            mMediaObject.removePart(part, true);
                            mRecordDelete.setChecked(false);
                        } else {
                            part.remove = true;
                            mRecordDelete.setChecked(true);
                        }
                    }
                    if (mProgressView != null)
                        mProgressView.invalidate();

                    checkStatus();
                }
                break;
            default:
                if (mMediaObject != null) {
                    MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                    if (part != null) {
                        if (part.remove) {
                            part.remove = false;
                            mRecordDelete.setChecked(false);
                            if (mProgressView != null)
                                mProgressView.invalidate();
                        }
                    }
                }
                break;
        }
    }

    private boolean cancelDelete() {
        if (mMediaObject != null) {
            MediaObject.MediaPart part = mMediaObject.getCurrentPart();
            if (part != null && part.remove) {
                part.remove = false;
                mRecordDelete.setChecked(false);

                if (mProgressView != null)
                    mProgressView.invalidate();

                return true;
            }
        }
        return false;
    }

    private int checkStatus() {
        invalidateOptionsMenu();

        if (!isFinishing() && mMediaObject != null) {
            return mMediaObject.getDuration();
        }
        return 0;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_INVALIDATE_PROGRESS:
                    if (!isFinishing()) {
                        if (mMediaObject != null && mMediaObject.getMedaParts() != null
                                && mMediaObject.getDuration() >= mConfig.getRecordTimeMax()) {
                            stopRecord();
                            mMediaRecorder.startEncoding();
                            return;
                        }
                        if (mProgressView != null) {
                            mProgressView.invalidate();
                        }
                        if (mPressedStatus) {
                            sendEmptyMessageDelayed(0, 30);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onEncodeStart() {
        showProgress(getString(R.string.video_preparing));
    }

    @Override
    public void onEncodeProgress(int progress) {
    }

    @Override
    public void onEncodeComplete() {
        hideProgress();
        Intent intent = new Intent(this, VideoPostActivity.class);
        intent.putExtra(Constants.VIDEO_TEMP_DIR, mMediaObject.getOutputDirectory());
        if (compressConfig != null) {
            intent.putExtra(Constants.VIDEO_URL, mMediaObject.getOutputTempTranscodingVideoPath());
        } else {
            intent.putExtra(Constants.VIDEO_URL, mMediaObject.getOutputTempVideoPath());
        }
        intent.putExtra(Constants.VIDEO_THUMBNAIL, mMediaObject.getOutputVideoThumbPath());
        startActivity(intent);
        finish();
    }

    @Override
    public void onEncodeError() {
        hideProgress();
        Toast.makeText(this, R.string.record_video_encode_failed, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onVideoError(int what, int extra) {

    }

    @Override
    public void onAudioError(int what, String message) {

    }

    @Override
    public void onPrepared() {
        initSurfaceView();
    }

    public ProgressDialog showProgress(String message) {
        return showProgress(message, -1);
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
}

