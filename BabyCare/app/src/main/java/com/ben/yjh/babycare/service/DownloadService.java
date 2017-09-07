package com.ben.yjh.babycare.service;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;

import com.ben.yjh.babycare.model.AppInfo;
import com.ben.yjh.babycare.util.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends IntentService {

    public static final String OSS_DOMAIN = "http://bensbabycare.oss-cn-hangzhou.aliyuncs.com/APKs/";

    public static boolean sRunning;

    public DownloadService() {
        super("DownloadService");
        sRunning = true;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppInfo appInfo = (AppInfo) intent.getSerializableExtra(Constants.APP_INFO);
        String urlToDownload = OSS_DOMAIN + new File(appInfo.getAppLink()).getName();
        ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/" +
                    new File(appInfo.getAppLink()).getName());
            if (file.exists()) {
                file.delete();
            }
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (sRunning) {
                    total += count;
                    // publishing the progress....
                    Bundle resultData = new Bundle();
                    resultData.putInt(Constants.PROGRESS, (int) (total * 100 / fileLength));
                    receiver.send(Constants.UPDATE_PROGRESS, resultData);
                    output.write(data, 0, count);
                } else {
                    Bundle resultData = new Bundle();
                    resultData.putInt(Constants.PROGRESS, 0);
                    receiver.send(Constants.UPDATE_PROGRESS, resultData);
                    file.delete();
                    stopSelf();
                    break;
                }
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt(Constants.PROGRESS, 100);
        receiver.send(Constants.UPDATE_PROGRESS, resultData);
    }
}
