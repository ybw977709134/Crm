package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.UpdateManager;
import com.onemeter.fragment.ClassFragment;
import com.onemeter.fragment.CourseFragment;
import com.onemeter.fragment.EncrollFragment;
import com.onemeter.fragment.StudentFragment;

/**
 * 主页面内容
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private long mExitTime;
    private ImageView  logo_img;//logo图标
    private Button bnt_student;//学员
    private Button bnt_coury;//课程
    private Button bnt_class;//班级
    private Button bnt_baoming;//报名
    private LinearLayout bnt_student_rel;//学员
    private LinearLayout bnt_coury_rel;//课程
    private LinearLayout bnt_class_rel;//班级
    private LinearLayout bnt_baoming_rel;//报名

    private Fragment main_fragment;//主布局容器
    /**
     * 三个fragment对象
     **/
    private StudentFragment fragment1;
    private CourseFragment fragment2;
    private ClassFragment fragment3;
    private EncrollFragment fragment4;
    // 定义FragmentManager
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fragmentManager = getSupportFragmentManager();
        // 默认打开第一个选项
        setTabSelection(R.id.bnt_student);

        //检测版本更新
        UpdateManager updateManager=new UpdateManager(this,MainActivity.this);
        updateManager.checkUpdateInfo();
    }

    /**
     * 当点击了tab时，切换选中的fragment
     *
     * @param i
     */
    private void setTabSelection(int i) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 创建FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (i) {
            case R.id.bnt_student:
                bnt_student_rel.setBackgroundColor(getResources().getColor(R.color.bule));
                bnt_class_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_coury_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_baoming_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                if (fragment1 == null) {
                    // 如果ChangJingFragment为空，则创建一个并添加到界面上
                    fragment1 = new StudentFragment();
                    fragmentTransaction.add(R.id.main_fragment, fragment1);
                } else {
                    // 如果ChangJingFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment1);

                }

                break;
            case R.id.bnt_coury:
                bnt_coury_rel.setBackgroundColor(getResources().getColor(R.color.bule));
                bnt_class_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_student_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_baoming_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                if (fragment2 == null) {
                    // 如果HomeFragment为空，则创建一个并添加到界面上
                    fragment2 = new CourseFragment();
                    fragmentTransaction.add(R.id.main_fragment, fragment2);
                } else {
                    // 如果HomeFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment2);
                }
                break;
            case R.id.bnt_class:
                bnt_class_rel.setBackgroundColor(getResources().getColor(R.color.bule));
                bnt_student_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_coury_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_baoming_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                if (fragment3 == null) {
                    // 如果MyFragment为空，则创建一个并添加到界面上
                    fragment3 = new ClassFragment();
                    fragmentTransaction.add(R.id.main_fragment, fragment3);
                } else {
                    // 如果MyFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment3);
                }

                break;
            case R.id.bnt_baoming:
                bnt_baoming_rel.setBackgroundColor(getResources().getColor(R.color.bule));
                bnt_class_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_student_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                bnt_coury_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
                if (fragment4 == null) {
                    // 如果MyFragment为空，则创建一个并添加到界面上
                    fragment4 = new EncrollFragment();
                    fragmentTransaction.add(R.id.main_fragment, fragment4);
                } else {
                    // 如果MyFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment4);

                }
                break;
        }
        fragmentTransaction.commit();
    }

    // 清除掉所有的选中状态。
    private void clearSelection() {
        bnt_student_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
        bnt_class_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
        bnt_coury_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));
        bnt_baoming_rel.setBackgroundColor(getResources().getColor(R.color.left_Black));


    }


    // 将所有的Fragment都置为隐藏状态。
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fragment1 != null) {
            fragmentTransaction.hide(fragment1);
        }
        if (fragment2 != null) {
            fragmentTransaction.hide(fragment2);
        }
        if (fragment3 != null) {
            fragmentTransaction.hide(fragment3);
        }
        if (fragment4 != null) {
            fragmentTransaction.hide(fragment4);
        }

    }

    /**
     * 初始化组件
     */
    private void initView() {
        logo_img= (ImageView) findViewById(R.id.logo_img);
        bnt_baoming_rel= (LinearLayout) findViewById(R.id.bnt_baoming_rel);
        bnt_student_rel= (LinearLayout) findViewById(R.id.bnt_student_rel);
        bnt_coury_rel= (LinearLayout) findViewById(R.id.bnt_coury_rel);
        bnt_class_rel= (LinearLayout) findViewById(R.id.bnt_class_rel);

        bnt_student = (Button) findViewById(R.id.bnt_student);
        bnt_coury = (Button) findViewById(R.id.bnt_coury);
        bnt_class = (Button) findViewById(R.id.bnt_class);
        bnt_baoming = (Button) findViewById(R.id.bnt_baoming);

        bnt_student.setOnClickListener(this);
        bnt_coury.setOnClickListener(this);
        bnt_class.setOnClickListener(this);
        bnt_baoming.setOnClickListener(this);
        logo_img.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logo_img:
            case R.id.bnt_student:
                //学员
                setTabSelection(R.id.bnt_student);
                break;
            case R.id.bnt_coury:
                //展示
                setTabSelection(R.id.bnt_coury);
                break;
            case R.id.bnt_class:
                //班级
                setTabSelection(R.id.bnt_class);
                break;
            case R.id.bnt_baoming:
                //报名缴费
                setTabSelection(R.id.bnt_baoming);
                break;
        }
    }


    // 监听手机上的BACK键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断两次点击的时间间隔（默认设置为2秒）
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
//                finish();
//                System.exit(0);
                Intent intent = new Intent();
                intent.setAction(BaseActivity.SYSTEM_EXIT);
                sendBroadcast(intent);
                super.onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
