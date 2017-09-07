package com.ben.yjh.babycare.main.left;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
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
import com.ben.yjh.babycare.glide.GlideApp;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.model.AppInfo;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.service.DownloadService;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.Utils;

import java.io.File;
import java.io.IOException;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ShareViewHolder> {

    private Context mContext;
    private String[] mList;
    private ProgressBar mProgressBar;
    private User mUser;
    private ProgressDialog mProgressDialog;
    private AppInfo mAppInfo;

    public SettingAdapter(Context context, ProgressBar progressBar, User user) {
        this.mContext = context;
        this.mProgressBar = progressBar;
        this.mUser = user;
        this.mList = context.getResources().getStringArray(R.array.about_us_choices);
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
                        mAppInfo = classOfT;
                        if (classOfT != null) {
                            if (classOfT.getVersionCode() > BuildConfig.VERSION_CODE) {
                                if (!((Activity) mContext).isFinishing()) {
                                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                                            .setTitle(String.format(mContext.getString(
                                                    R.string.new_version_found), classOfT.getAppName()))
                                            .setMessage(classOfT.getUpdateInfo())
                                            .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    String destination = Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_DOWNLOADS) + "/";
                                                    destination += new File(mAppInfo.getAppLink()).getName();
                                                    if (new File(destination).exists()) {
                                                        new File(destination).delete();
                                                    }
                                                    mProgressDialog = new ProgressDialog(mContext);
                                                    mProgressDialog.setIndeterminate(false);
                                                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                    mProgressDialog.setCancelable(true);
                                                    mProgressDialog.setMessage(String.format(
                                                            mContext.getString(R.string.downloading),
                                                            classOfT.getAppName()));
                                                    mProgressDialog.show();
                                                    Intent intent = new Intent(mContext,
                                                            DownloadService.class);
                                                    intent.putExtra(Constants.APP_INFO, classOfT);
                                                    intent.putExtra(Constants.RECEIVER,
                                                            new DownloadReceiver(new Handler()));
                                                    mContext.startService(intent);
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .create();
                                    dialog.setCancelable(true);
                                    dialog.show();
                                }
                            } else {
                                AlertUtils.showAlertDialog(mContext, R.string.latest_app_version);
                            }
                        } else {
                            AlertUtils.showAlertDialog(mContext, R.string.latest_app_version);
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

    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.UPDATE_PROGRESS) {
                int progress = resultData.getInt(Constants.PROGRESS);
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                    if (mAppInfo != null) {
                        openAPK(mAppInfo);
                    }
                } else if (progress > 100) {
                    Intent intent = new Intent(mContext, DownloadService.class);
                    mContext.stopService(intent);
                }
            }
        }
    }

    private void openAPK(AppInfo appInfo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String destination = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/";
        destination += new File(appInfo.getAppLink()).getName();
        intent.setDataAndType(Uri.fromFile(
                new File(destination)), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        new File(destination).delete();
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
            GlideApp.get(mContext).clearDiskCache();
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
//        return mList.length;
        return 0;
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ShareViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
