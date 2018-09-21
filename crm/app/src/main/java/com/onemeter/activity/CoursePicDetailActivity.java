package com.onemeter.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import com.onemeter.R;
import com.onemeter.adapter.imageloadAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程图片展示页面（画廊模式）
 * Created by G510 on 2016/5/8.
 */
public class CoursePicDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private RelativeLayout course_pic_rel;
    private Gallery img_gallery;
    private imageloadAdapter mimageloadAdapter;
    public List<String> imgPath;
    public String[] url;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Transparent);
        setContentView(R.layout.class_course_pic_detail_layout);
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.NO_GRAVITY;//设置对话框右边显示
        win.setAttributes(lp);
        imgPath = new ArrayList<String>();
        initData();
        initView();
//        toast(MyApplication.imgurl.length+"");

    }

    /**
     * 初始化图片数据来源
     */
    private void initData() {
        url = new String[MyApplication.imgurl.length];
        for (int i = 0; i < MyApplication.imgurl.length; i++) {
            imgPath.add(MyApplication.imgurl[i]);
            url[i] =MyApplication.imgurl[i];
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView() {
        mimageloadAdapter = new imageloadAdapter(this, imgPath);
        course_pic_rel = (RelativeLayout) findViewById(R.id.course_pic_rel);
        img_gallery = (Gallery) findViewById(R.id.img_gallery);
        img_gallery.setAdapter(mimageloadAdapter);
        img_gallery.setOnItemClickListener(this);
        if (imgPath.size() > 2) {
            img_gallery.setSelection(1);
        } else {
            img_gallery.setSelection(0);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        imageBrower(position, url);
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(CoursePicDetailActivity.this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }


}
