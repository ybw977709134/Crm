package com.onemeter.app;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.onemeter.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 初始化检测版本更新工具类
 */
public class UpdateManager {
    private Context mContext;
    // 提示语
    private String updateMsg = "";
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/updatecrmwei/";
    private static final String saveFileName = savePath + "微系统.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private String appVersion;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    Activity act;
    String m_appNameStr = "微系统.apk"; //下载到本地要给这个APP命的名字

    public UpdateManager(Context context, Activity act) {
        this.mContext = context;
        this.act = act;
    }

    // 检测版本是否需要更新
    public void checkUpdateInfo() {
        String new_appversion = MyApplication.userInfos.get(0).getAppVerionCode();
        try {
            getVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("".equals("new_appversion")) {
        } else {
            //		 int appVer=Integer.valueOf(appVersion).intValue();
            String appVer = appVersion;
//		int  new_appVer=Integer.valueOf(new_appversion).intValue();
            String new_appVer = new_appversion;

            if (appVer.compareTo(new_appVer) < 0) {
                showNoticeDialog();
            } else {
//			Utils.showToast(mContext,"已经是最新的版本了");
            }

        }


    }

    /**
     * 获取版本号
     *
     * @return
     * @throws Exception
     */
    public String getVersion() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(
                mContext.getPackageName(), 0);
        String versionName = packInfo.versionName;
        int versionCode = packInfo.versionCode;
        appVersion = versionCode + "";
//		Utils.showToast(mContext, appVersion);
        return appVersion;
    }

    private void showNoticeDialog() {
        updateMsg =
// "当前版本：" + appVersion + "\n发现新版本：" + MyApplication.userInfos.get(0).getAppVerionCode() +
                "最新更新："+ MyApplication.userInfos.get(0).getDesc();
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                });

        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }


    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("正在下载");
        builder.setMessage("请稍候...");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        downloadApk();
    }

    /**
     * 下载apk的操作
     */

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                // 返回的安装包url下载地址
                String apkDownLoadUrl = MyApplication.userInfos.get(0).getAppApkUrl().toString();
                URL url = new URL(apkDownLoadUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = null;
                if (is != null) {
                    File file = new File(
                            Environment.getExternalStorageDirectory(),
                            m_appNameStr);
                    fos = new FileOutputStream(file);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            // 下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!interceptFlag);// 点击取消就停止下载.

                }
                fos.flush();
                if (fos != null) {
                    fos.close();
                }

                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), m_appNameStr)),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }
}