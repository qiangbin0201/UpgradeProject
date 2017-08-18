package com.apply.update.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;


import com.apply.update.R;
import com.apply.update.utils.FileUtils;
import com.apply.update.utils.MD5Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by leonlee on 14-9-9.
 * To better product,to better world
 */
public class UpdateService extends Service {

    //文件存储
    private File updateFile = null;

    //通知栏
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder builder;
    //通知栏跳转Intent
    private PendingIntent mPendingIntent = null;
    private String url;

    private String mDownloadDir;
    private String download_file_name;
    private String tag_file_name;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取传值
        url = intent.getStringExtra("url");

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mDownloadDir = FileUtils.getAppDir(this) + "/update/";
        }

        this.mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(getString(R.string.app_name));
//        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, getTopActivity(this).getClass()), PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pi);
        builder.setProgress(100, 0, false);
        builder.setTicker("开始下载");
        builder.setDefaults(Notification.DEFAULT_SOUND);
        //发出通知
        mNotificationManager.notify(0, builder.build());
        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        new Thread(new updateRunnable()).start();//这个是下载的重点，是下载的过程

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class updateRunnable implements Runnable {
        Message message = updateHandler.obtainMessage();

        public void run() {
            message.what = DOWNLOAD_COMPLETE;
            try {
                long downloadSize = downloadUpdateFile(url, mDownloadDir);
                if (downloadSize > 0) {
                    //重命名
                    updateFile = FileUtils.chageFileName(mDownloadDir, download_file_name, mDownloadDir, tag_file_name);
                    //下载成功
                    updateHandler.sendMessage(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.what = DOWNLOAD_FAIL;
                //下载失败
                updateHandler.sendMessage(message);
            }
        }
    }

    public long downloadUpdateFile(String downloadUrl, String downloadDir) throws Exception {
        int downloadCount = 0;
        long currentSize = 0;
        long updateTotalSize = 0;

        InputStream is = null;
        HttpURLConnection httpConnection = null;
        RandomAccessFile raf = null;

        download_file_name = "apk." + MD5Utils.getPwd(downloadUrl);
        tag_file_name = MD5Utils.getPwd(downloadUrl) + ".apk";

        try {
            File folder = new File(downloadDir);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            if (!TextUtils.isEmpty(tag_file_name)) {
                File tagFile = new File(downloadDir, tag_file_name);
                if (tagFile.exists()) {
                    //已经下载完成, 备注:100没有任何实际意义，只是标志下载已经完成
                    return 100;
                }
            }

            File file = new File(downloadDir, download_file_name);

            if (file.exists()) {
                currentSize = (int) file.length();
            } else {
                currentSize = 0;
            }
            raf = new RandomAccessFile(file, "rwd");
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
//            httpConnection.setRequestProperty("Accept-Encoding", "identity");

            httpConnection.setRequestMethod("GET");
            httpConnection.setConnectTimeout(10000);

            updateTotalSize = getFileSize(downloadUrl);
            httpConnection.setRequestProperty("Range", "bytes=" + currentSize + "-" + updateTotalSize);

//            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            if (httpConnection.getResponseCode() == 200) {
                raf.seek(0);
                currentSize = 0;
            }
            if (httpConnection.getResponseCode() == 206) {
                raf.seek(currentSize);
            }
            is = httpConnection.getInputStream();
            byte[] bytes = new byte[1024 * 4];
            int length = -1;
            while ((length = is.read(bytes)) != -1) {
                raf.write(bytes, 0, length);
                currentSize += length;
                //为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if ((downloadCount == 0) || (int) (currentSize * 100 / updateTotalSize) - 1 > downloadCount) {
                    downloadCount += 1;
                    int progress = (int) ((int) currentSize * 100 / updateTotalSize);
                    builder.setContentText("下载进度：" + progress + "%");
                    builder.setProgress(100, progress, false);
                    builder.setDefaults(0);
                    Notification notification = builder.build();
                    notification.flags = Notification.FLAG_NO_CLEAR;
                    mNotificationManager.notify(0, notification);
                }

            }
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (raf != null) {
                raf.close();
            }
        }
        return currentSize;

    }

    //下载状态
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_COMPLETE: {
                    //点击安装PendingIntent --> 直接安装
                    Uri uri = Uri.fromFile(updateFile);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    mPendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);
                    builder.setContentIntent(mPendingIntent);
                    builder.setProgress(0, 0, true);
                    builder.setContentText("下载完成,点击安装");
                    Notification notification = builder.build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notification.defaults = Notification.DEFAULT_SOUND;
                    mNotificationManager.notify(0, notification);

                    startActivity(installIntent);
                    break;
                }
                case DOWNLOAD_FAIL:
                    //下载失败
                    builder.setProgress(0, 0, true);
                    builder.setContentText("下载失败");
                    mNotificationManager.notify(0, builder.build());
                    break;
                default:
                    break;
            }
            //停止服务
            stopSelf();
        }

    };

    private long getFileSize(String downloadUrl) {
        HttpURLConnection httpConnection = null;
        long totalSize = 0;
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
            totalSize = httpConnection.getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return totalSize;

    }

}
