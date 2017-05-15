package com.ben.yjh.babycare.main.setting;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.io.IOException;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ShareViewHolder> {

    private Context mContext;
    private String[] mList;
    private ProgressBar mProgressBar;

    public SettingAdapter(Context context, ProgressBar progressBar) {
        this.mContext = context;
        this.mProgressBar = progressBar;
        this.mList = context.getResources().getStringArray(R.array.setting_choices);
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_dropdown, parent, false));
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, final int position) {
        holder.textView.setText(mList[position]);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        clearImageCacheAlert();
                        break;
                    case 1:
                        clearVideoCacheAlert();
                        break;
                }
            }
        });
    }

    private void clearImageCacheAlert() {
        AlertUtils.showConfirmDialog(mContext,
                R.string.clear_image_cache_alert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        MyApplication.getInstance(mContext).clearImageCache();
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext,
                                R.string.clear_image_cache_successfully, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearVideoCacheAlert() {
        AlertUtils.showConfirmDialog(mContext,
                R.string.clear_video_cache_alert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mProgressBar.setVisibility(View.VISIBLE);
                            Utils.cleanVideoCacheDir(mContext);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext,
                                R.string.clear_video_cache_successfully, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ShareViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
