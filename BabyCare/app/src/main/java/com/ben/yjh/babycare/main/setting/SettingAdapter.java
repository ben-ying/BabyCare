package com.ben.yjh.babycare.main.setting;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.yjh.babycare.BuildConfig;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpPostTask;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.model.AppInfo;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Utils;

import java.io.File;
import java.io.IOException;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ShareViewHolder> {

    private Context mContext;
    private String[] mList;
    private ProgressBar mProgressBar;
    private User mUser;

    public SettingAdapter(Context context, ProgressBar progressBar, User user) {
        this.mContext = context;
        this.mProgressBar = progressBar;
        this.mUser = user;
        this.mList = context.getResources().getStringArray(R.array.setting_choices);
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_dropdown, parent, false));
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, int position) {
        final int index = position;
        holder.textView.setText(mList[position]);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (index) {
                    case 0:
                        checkUpdate();
                        break;
                    case 1:
                        clearImageCacheAlert();
                        break;
                    case 2:
                        clearVideoCacheAlert();
                        break;
                }
            }
        });
    }

    private void checkUpdate() {
        new UserTaskHandler(mContext).checkUpdate(mUser.getToken(),
                new HttpResponseInterface<AppInfo>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(final AppInfo classOfT) {
                        if (classOfT != null) {
                            if (classOfT.getVersionCode() > BuildConfig.VERSION_CODE) {
                                AlertUtils.showAlertDialog(mContext,
                                        String.format(mContext.getString(
                                                R.string.new_version_found), classOfT.getAppName()),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                downloadApk(classOfT);
                                            }
                                        });
                            } else {
                                Toast.makeText(mContext,
                                        R.string.latest_app_version, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mContext,
                                    R.string.latest_app_version, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }

    private void downloadApk(AppInfo appInfo) {
        String destination = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = mContext.getString(R.string.app_name);
        destination += appInfo.getAppName();
//        final Uri uri = Uri.parse("file://" + destination);

        File file = new File(destination);
        if (file.exists()) {
            for (int i = 0; i < 5; i++) {
                if (file.delete()) {
                    break;
                }
            }
        }

        final String url = HttpPostTask.DOMAIN + appInfo.getAppLink();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(String.format(mContext.getString(R.string.downloading), fileName));
        request.setTitle(fileName);
        request.setDestinationUri(Uri.parse(destination));
        final DownloadManager manager = (DownloadManager)
                mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(Uri.parse(url),
                        manager.getMimeTypeForDownloadedFile(downloadId));
                mContext.startActivity(install);
                mContext.unregisterReceiver(this);
                ((Activity) mContext).finish();
            }
        };

        mContext.registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void clearImageCacheAlert() {
        AlertUtils.showConfirmDialog(mContext,
                R.string.clear_image_cache_alert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ClearImageCache().execute();
                    }
                });
    }

    private void clearVideoCacheAlert() {
        AlertUtils.showConfirmDialog(mContext,
                R.string.clear_video_cache_alert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ClearVideoCache().execute();
                    }
                });
    }

    private class ClearImageCache extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            MyApplication.getInstance(mContext).clearImageCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext,
                    R.string.clear_image_cache_successfully, Toast.LENGTH_SHORT).show();
        }
    }

    private class ClearVideoCache extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Utils.cleanVideoCacheDir(mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Glide.get(mContext).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext,
                    R.string.clear_video_cache_successfully, Toast.LENGTH_SHORT).show();
        }
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
