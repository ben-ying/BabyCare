package com.ben.yjh.babycare.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.ben.yjh.babycare.R;

import java.util.Iterator;

import mabeijianxi.camera.model.MediaObject;
import mabeijianxi.camera.util.DeviceUtils;

public class VideoProgressView extends View {

    private final static int HANDLER_INVALIDATE_ACTIVE = 0;
    private final static int HANDLER_INVALIDATE_RECORDING = 1;

    private Paint mProgressPaint;
    private Paint mActivePaint;
    private Paint mPausePaint;
    private Paint mRemovePaint;
    private Paint mThreePaint;
    private Paint mOverflowPaint;
    private boolean mStop;
    private boolean mProgressChanged;
    private boolean mActiveState;
    private MediaObject mMediaObject;
    private int mMaxDuration;
    private int mVLineWidth;
    private int mRecordTimeMin = 1500;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_INVALIDATE_ACTIVE:
                    invalidate();
                    mActiveState = !mActiveState;
                    if (!mStop)
                        sendEmptyMessageDelayed(0, 300);
                    break;
                case HANDLER_INVALIDATE_RECORDING:
                    invalidate();
                    if (mProgressChanged)
                        sendEmptyMessageDelayed(0, 50);
                    break;
            }
            super.dispatchMessage(msg);
        }
    };

    public VideoProgressView(Context paramContext) {
        super(paramContext);
        init();
    }

    public VideoProgressView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public VideoProgressView(Context paramContext, AttributeSet paramAttributeSet,
                             int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        mProgressPaint = new Paint();
        mActivePaint = new Paint();
        mPausePaint = new Paint();
        mRemovePaint = new Paint();
        mThreePaint = new Paint();
        mOverflowPaint = new Paint();

        mVLineWidth = DeviceUtils.dipToPX(getContext(), 1);

        setBackgroundColor(getResources().getColor(R.color.video_recorder_bg_color));
        mProgressPaint.setColor(0xFF45C01A);
        mProgressPaint.setStyle(Paint.Style.FILL);

        mActivePaint.setColor(getResources().getColor(R.color.white));
        mActivePaint.setStyle(Paint.Style.FILL);

        mPausePaint.setColor(getResources().getColor(R.color.video_progress_split));
        mPausePaint.setStyle(Paint.Style.FILL);

        mRemovePaint.setColor(getResources().getColor(R.color.video_progress_delete));
        mRemovePaint.setStyle(Paint.Style.FILL);

        mThreePaint.setColor(getResources().getColor(R.color.video_progress_three));
        mThreePaint.setStyle(Paint.Style.FILL);

        mOverflowPaint.setColor(getResources().getColor(R.color.video_progress_overflow));
        mOverflowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width = getMeasuredWidth(), height = getMeasuredHeight();
        int left = 0;
        int right = 0;
        int duration = 0;
        if (mMediaObject != null && mMediaObject.getMedaParts() != null) {
            Iterator<MediaObject.MediaPart> iterator = mMediaObject.getMedaParts().iterator();
            boolean hasNext = iterator.hasNext();
            int maxDuration = mMaxDuration;
            int currentDuration = mMediaObject.getDuration();
            boolean hasOutDuration = currentDuration > mMaxDuration;;
            if (hasOutDuration) {
                maxDuration = currentDuration;
            }

            while (hasNext) {
                MediaObject.MediaPart vp = iterator.next();
                final int partDuration = vp.getDuration();
                left = right;
                right = left + (int) (partDuration * 1.0F / maxDuration * width);

                if (vp.remove) {
                    canvas.drawRect(left, 0.0F, right, height, mRemovePaint);
                } else {
                    if (hasOutDuration) {
                        right = left + (int) ((mMaxDuration - duration) * 1.0F
                                / maxDuration * width);
                        canvas.drawRect(left, 0.0F, right, height, mProgressPaint);
                        left = right;
                        right = left + (int) ((partDuration - (mMaxDuration - duration))
                                * 1.0F / maxDuration * width);
                        canvas.drawRect(left, 0.0F, right, height, mOverflowPaint);
                    } else {
                        canvas.drawRect(left, 0.0F, right, height, mProgressPaint);
                    }
                }

                hasNext = iterator.hasNext();
                if (hasNext) {
                    canvas.drawRect(right - mVLineWidth, 0.0F, right, height, mPausePaint);
                }

                duration += partDuration;
            }
        }

        if (duration < mRecordTimeMin) {
            left = (int) ((mRecordTimeMin * 1.0f) / mMaxDuration * width);
            canvas.drawRect(left, 0.0F, left + mVLineWidth, height, mThreePaint);
        }

        if (mActiveState) {
            if (right + 8 >= width) {
                right = width - 8;
            }
            canvas.drawRect(right, 0.0F, right + 8, getMeasuredHeight(), mActivePaint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mStop = false;
        mHandler.sendEmptyMessage(HANDLER_INVALIDATE_ACTIVE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mStop = true;
        mHandler.removeMessages(HANDLER_INVALIDATE_ACTIVE);
    }

    public void setData(MediaObject mMediaObject) {
        this.mMediaObject = mMediaObject;
    }

    public void setMaxDuration(int duration) {
        this.mMaxDuration = duration;
    }

    public void start() {
        mProgressChanged = true;
    }

    public void stop() {
        mProgressChanged = false;
    }

    public void setMinTime(int recordTimeMin) {
        this.mRecordTimeMin = recordTimeMin;
    }
}
