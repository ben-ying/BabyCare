package com.ben.yjh.babycare.main.event;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;
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
import mabeijianxi.camera.util.StringUtils;
import mabeijianxi.camera.views.ProgressView;

import static mabeijianxi.camera.MediaRecorderBase.SMALL_VIDEO_WIDTH;
import static mabeijianxi.camera.MediaRecorderBase.compressConfig;

public class MediaRecorderActivity extends BaseActivity implements
        MediaRecorderBase.OnErrorListener, OnClickListener, MediaRecorderBase.OnPreparedListener,
        MediaRecorderBase.OnEncodeListener {

    private static final int HANDLE_INVALIDATE_PROGRESS = 0;
    private static final int HANDLE_STOP_RECORD = 1;
    private ImageView mTitleNext;
    private CheckBox mCameraSwitch;
    private CheckedTextView mRecordDelete;
    private CheckBox mRecordLed;
    private Button mRecordController;
    private RelativeLayout mBottomLayout;
    private SurfaceView mSurfaceView;
    private ProgressView mProgressView;
    private MediaRecorderBase mMediaRecorder;
    private MediaObject mMediaObject;
    private volatile boolean mPressedStatus;
    private volatile boolean mReleased;
    private MediaRecorderConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        mConfig = intent.getParcelableExtra(Constants.VIDEO_CONFIG);
        if (mConfig == null) {
            finish();
        }
        mSurfaceView = (SurfaceView) findViewById(R.id.recordPreview);
        mCameraSwitch = (CheckBox) findViewById(R.id.record_camera_switcher);
        mTitleNext = (ImageView) findViewById(R.id.title_next);
        mProgressView = (ProgressView) findViewById(R.id.recordProgress);
        mRecordDelete = (CheckedTextView) findViewById(R.id.tv_delete);
        mRecordController = (Button) findViewById(R.id.btn_controller);
        mBottomLayout = (RelativeLayout) findViewById(R.id.rl_bottom);
        mRecordLed = (CheckBox) findViewById(R.id.record_camera_led);

        mTitleNext.setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        mRecordDelete.setOnClickListener(this);
        mRecordController.setOnTouchListener(mOnVideoControllerTouchListener);

        if (MediaRecorderBase.isSupportFrontCamera()) {
            mCameraSwitch.setOnClickListener(this);
        } else {
            mCameraSwitch.setVisibility(View.GONE);
        }
        if (DeviceUtils.isSupportCameraLedFlash(getPackageManager())) {
            mRecordLed.setOnClickListener(this);
        } else {
            mRecordLed.setVisibility(View.GONE);
        }

        mProgressView.setMaxDuration(mConfig.getRecordTimeMax());
        mProgressView.setMinTime(mConfig.getRecordTimeMin());
    }

    private void initSurfaceView() {
        final int w = DeviceUtils.getScreenWidth(this);
        ((RelativeLayout.LayoutParams) mBottomLayout.getLayoutParams()).topMargin =
                (int) (w / (SMALL_VIDEO_WIDTH / (MediaRecorderBase.SMALL_VIDEO_HEIGHT * 1.0f)));
        int height = (int) (w * ((MediaRecorderBase.mSupportedPreviewWidth * 1.0f)
                / MediaRecorderBase.SMALL_VIDEO_WIDTH));
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
            if (mMediaRecorder == null) {
                return false;
            }

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
                            mTitleNext.performClick();
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

        if (mMediaRecorder == null) {
            initMediaRecorder();
        } else {
            mRecordLed.setChecked(false);
            mMediaRecorder.prepare();
            mProgressView.setData(mMediaObject);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRecord();
        UtilityAdapter.freeFilterParser();
        if (!mReleased) {
            if (mMediaRecorder != null)
                mMediaRecorder.release();
        }
        mReleased = false;
    }


    private void startRecord() {
        if (mMediaRecorder != null) {
            MediaObject.MediaPart part = mMediaRecorder.startRecord();
            if (part == null) {
                return;
            }
            if (mMediaRecorder instanceof MediaRecorderSystem) {
                mCameraSwitch.setVisibility(View.GONE);
            }
            mProgressView.setData(mMediaObject);
        }

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
        mCameraSwitch.setEnabled(false);
        mRecordLed.setEnabled(false);
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
                    .setNegativeButton(
                            R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    mMediaObject.delete();
                                    finish();
                                }

                            })
                    .setPositiveButton(R.string.cancel,
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

        if (mMediaRecorder != null) {
            mMediaRecorder.stopRecord();
        }

        mRecordDelete.setVisibility(View.VISIBLE);
        mCameraSwitch.setEnabled(true);
        mRecordLed.setEnabled(true);

        mHandler.removeMessages(HANDLE_STOP_RECORD);
        checkStatus();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (mHandler.hasMessages(HANDLE_STOP_RECORD)) {
            mHandler.removeMessages(HANDLE_STOP_RECORD);
        }

        if (id != R.id.tv_delete) {
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
        }

        if (id == R.id.title_back) {
            onBackPressed();
        } else if (id == R.id.record_camera_switcher) {
            if (mRecordLed.isChecked()) {
                if (mMediaRecorder != null) {
                    mMediaRecorder.toggleFlashMode();
                }
                mRecordLed.setChecked(false);
            }

            if (mMediaRecorder != null) {
                mMediaRecorder.switchCamera();
            }

            if (mMediaRecorder.isFrontCamera()) {
                mRecordLed.setEnabled(false);
            } else {
                mRecordLed.setEnabled(true);
            }
        } else if (id == R.id.record_camera_led) {
            if (mMediaRecorder != null) {
                if (mMediaRecorder.isFrontCamera()) {
                    return;
                }
            }

            if (mMediaRecorder != null) {
                mMediaRecorder.toggleFlashMode();
            }
        } else if (id == R.id.title_next) {
            mMediaRecorder.startEncoding();
        } else if (id == R.id.tv_delete) {
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
        int duration = 0;
        if (!isFinishing() && mMediaObject != null) {
            duration = mMediaObject.getDuration();
            if (duration < mConfig.getRecordTimeMin()) {
                if (duration == 0) {
                    mCameraSwitch.setVisibility(View.VISIBLE);
                    mRecordDelete.setVisibility(View.GONE);
                }
                if (mTitleNext.getVisibility() != View.INVISIBLE)
                    mTitleNext.setVisibility(View.INVISIBLE);
            } else {
                if (mTitleNext.getVisibility() != View.VISIBLE) {
                    mTitleNext.setVisibility(View.VISIBLE);
                }
            }
        }
        return duration;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_INVALIDATE_PROGRESS:
                    if (mMediaRecorder != null && !isFinishing()) {
                        if (mMediaObject != null && mMediaObject.getMedaParts() != null
                                && mMediaObject.getDuration() >= mConfig.getRecordTimeMax()) {
                            stopRecord();
                            mTitleNext.performClick();
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
        showProgress("", getString(R.string.video_preparing));
    }

    @Override
    public void onEncodeProgress(int progress) {
    }

    @Override
    public void onEncodeComplete() {
        hideProgress();
        Intent intent = new Intent(this, PostVideoActivity.class);
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

    protected ProgressDialog mProgressDialog;

    public ProgressDialog showProgress(String title, String message) {
        return showProgress(title, message, -1);
    }

    public ProgressDialog showProgress(String title, String message, int theme) {
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

        if (!StringUtils.isEmpty(title)) {
            mProgressDialog.setTitle(title);
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

