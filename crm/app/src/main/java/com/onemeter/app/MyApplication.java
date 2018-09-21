package com.onemeter.app;


import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.onemeter.R;
import com.onemeter.entity.StudentHetongInfo;
import com.onemeter.entity.userInfo;
import com.onemeter.service.NetChangeReceiver;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义app启动
 *
 * @author angelyin
 * @date 2015-10-9 下午7:50:54
 */
public class MyApplication extends Application {
	private static MyApplication app;
	/** 网络是否可用的标识 **/
	public static boolean isNetAvailable = false;
	/** 网络状态改变接收器 **/
	public NetChangeReceiver receiver;
	/**学员列表点击item的ID**/
	public static List<String>  studentId;
	/**学员列表点击item的姓名**/
	public static  List<String> studentName;
	public static List<userInfo>  userInfos;
	public  static List<String>  device_adress;
	public  static List<StudentHetongInfo>  hetongInfo;
	public  static  List<String>  hetongmun;
	public  static  List<String>  fragmentStudentPositon;

	/**根据课程名称收索班级，保存字段，用于刷新**/
	public static String  courser_id="";
	public static   String []  imgurl=null;
	@Override
	public void onCreate() {
		super.onCreate();
		studentId=new ArrayList<String>();
		userInfos=new ArrayList<userInfo>();
		studentName=new ArrayList<String>();
		device_adress=new ArrayList<String>();
		hetongmun=new ArrayList<String>();
		hetongInfo=new ArrayList<StudentHetongInfo>();
		fragmentStudentPositon=new ArrayList<String>();
		isNetAvailable = isNetworkConnected();
		receiver = new NetChangeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addCategory("android.intent.category.DEFAULT");
		registerReceiver(receiver, filter);// 注册广播接收器receiver
		//初始化图片缓存方法
		initPicLooke();

	}


	/**
	 * 图片全局初始化
	 */

	private void initPicLooke() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions
				.Builder()
				.showImageForEmptyUri(R.mipmap.ic_launcher)
				.showImageOnFail(R.mipmap.ic_launcher)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)//缓存一百张图片
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);



	}

	/**
	 * 判断网络状态是否可用
	 *
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		} else {
			return false;
		}
	}



	public static MyApplication getInstance() {
		if (app == null) {
			app = new MyApplication();
		}
		return app;
	}




}
