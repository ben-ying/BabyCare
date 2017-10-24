package com.ben.yjh.babycare.main.event.video.target;

import android.view.View;
import android.widget.ProgressBar;

import java.io.File;


public class VideoProgressTarget extends ProgressTarget<String, File> {
    private final ProgressBar progress;

    public VideoProgressTarget(VideoLoadTarget target, ProgressBar progress) {
        super(target);
        this.progress = progress;
    }

//    @Override
//    public float getGranualityPercentage() {
//        return 0.1f; // this matches the format string for #text below
//    }

    @Override
    protected void onConnecting() {
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(0);
    }

    @Override
    protected void onDownloading(long bytesRead, long expectedLength) {
        progress.setProgress((int) (100 * bytesRead / expectedLength));
    }

    @Override
    protected void onDownloaded() {
    }

    @Override
    protected void onDelivered() {
        progress.setVisibility(View.GONE);
    }
}
