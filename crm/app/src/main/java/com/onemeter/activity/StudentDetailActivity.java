package com.onemeter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.fragment.DongTaiFragment;
import com.onemeter.fragment.HeTongFragment;
import com.onemeter.fragment.StudentDetailFragment;

/**
 * 描述：学员详情页面
 * 项目名称：CrmWei
 * 作者：angelyin
 * 时间：2016/5/5 10:10
 * 备注：
 */
public class StudentDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_return;
    private Button btn_change_model;
    private TextView fragment_student_detail_username;
    private PopupWindow popupwindow;
    private FrameLayout change_fragment;//主布局容器
    private Button btton1;
    private Button btton2;
    private Button btton3;
    private Button  dayin_setting;//打印设置
    /**
     * 三个fragment对象
     **/
    private StudentDetailFragment fragment1;
    private HeTongFragment fragment2;
    private DongTaiFragment fragment3;
    // 定义FragmentManager
    FragmentManager fragmentManager;
   //检查蓝牙
    Intent  intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail_main);
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.RIGHT;//设置对话框右边显示
        win.setAttributes(lp);
        fragmentManager = getSupportFragmentManager();
        // 默认打开第一个选项
        setTabSelection(R.id.btn_student_detail_view);
        initView();
        registerClearResultReceiver();
    }

    ClearResultReceiver clearResultReceiver = new ClearResultReceiver();

    private void registerClearResultReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("bianji");
        this.registerReceiver(clearResultReceiver, intentFilter);
    }

    private void relaseRegisterClearResultReceiver() {
        this.unregisterReceiver(clearResultReceiver);
    }

    private class ClearResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int type = intent.getIntExtra("type", 11);
                switch (type) {
                    case 11:
//                        toast("打开广播");
                        btn_change_model.setEnabled(false);
                        btn_change_model.setVisibility(View.INVISIBLE);
                        break;
                    case 22:
                        btn_change_model.setEnabled(true);
                        btn_change_model.setVisibility(View.VISIBLE);
//                        toast("关闭广播");
                        break;
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        relaseRegisterClearResultReceiver();
    }

    /**
     * @param i
     */
    private void setTabSelection(int i) {
        // 每次选中之前先清楚掉上次的选中状态
        if (popupwindow != null && popupwindow.isShowing()) {
            clearSelection();
        }
        // 创建FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (i) {
            case R.id.btn_student_detail_view:
                if (popupwindow != null && popupwindow.isShowing()) {
                    btton1.setBackgroundColor(getResources().getColor(R.color.bule));
                    btton2.setBackgroundColor(getResources().getColor(R.color.bg_choose));
                    btton3.setBackgroundColor(getResources().getColor(R.color.bg_choose));
                }

                if (fragment1 == null) {
                    // 如果ChangJingFragment为空，则创建一个并添加到界面上
                    fragment1 = new StudentDetailFragment();
                    fragmentTransaction.add(R.id.change_fragment, fragment1);
                } else {
                    // 如果ChangJingFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment1);
                }
                break;
            case R.id.btn_hetong_view:
                if (popupwindow != null && popupwindow.isShowing()) {
                    btton2.setBackgroundColor(getResources().getColor(R.color.bule));
                    btton1.setBackgroundColor(getResources().getColor(R.color.bg_choose));
                    btton3.setBackgroundColor(getResources().getColor(R.color.bg_choose));
                }

                if (fragment2 == null) {
                    // 如果HomeFragment为空，则创建一个并添加到界面上
                    fragment2 = new HeTongFragment();
                    fragmentTransaction.add(R.id.change_fragment, fragment2);
                } else {
                    // 如果HomeFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment2);
                }
                break;
            case R.id.btn_dongtai_view:
                if (popupwindow != null && popupwindow.isShowing()) {
                    btton3.setBackgroundColor(getResources().getColor(R.color.bule));
                    btton1.setBackgroundColor(getResources().getColor(R.color.bg_choose));
                    btton2.setBackgroundColor(getResources().getColor(R.color.bg_choose));
                }

                if (fragment3 == null) {
                    // 如果HomeFragment为空，则创建一个并添加到界面上
                    fragment3 = new DongTaiFragment();
                    fragmentTransaction.add(R.id.change_fragment, fragment3);
                } else {
                    // 如果HomeFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment3);
                }
        }
        fragmentTransaction.commit();
    }

    // 清除掉所有的选中状态。
    private void clearSelection() {
        btton1.setBackgroundColor(getResources().getColor(R.color.bg_choose));
        btton2.setBackgroundColor(getResources().getColor(R.color.bg_choose));
        btton3.setBackgroundColor(getResources().getColor(R.color.bg_choose));
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

    }

    /**
     * 初始化组件
     */
    private void initView() {
        dayin_setting= (Button) findViewById(R.id.dayin_setting);
        fragment_student_detail_username = (TextView) findViewById(R.id.fragment_student_detail_username);
        fragment_student_detail_username.setText(MyApplication.studentName.get(0).toString());
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_change_model = (Button) findViewById(R.id.btn_change_model);
        img_return.setOnClickListener(this);
        btn_change_model.setOnClickListener(this);
        dayin_setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                Intent intent1 = new Intent();
                intent1.setAction("push");
                intent1.putExtra("pushtype", 2);
                sendBroadcast(intent1);
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.btn_change_model:
                //弹出选项对话框
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView(btn_change_model);
                }
                break;
            case R.id.btn_student_detail_view:
                //学员详情
                btn_change_model.setText("学员详情");
                setTabSelection(R.id.btn_student_detail_view);
                popupwindow.dismiss();
                dayin_setting.setVisibility(View.GONE);
                break;
            case R.id.btn_hetong_view:
                //合同
                btn_change_model.setText("合同");
                setTabSelection(R.id.btn_hetong_view);
                popupwindow.dismiss();
                dayin_setting.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_dongtai_view:
                //联系动态
                dayin_setting.setVisibility(View.GONE);
                btn_change_model.setText("联系动态");
                setTabSelection(R.id.btn_dongtai_view);
                popupwindow.dismiss();
                break;
            case R.id.dayin_setting:
                //打印设置
                getNetLanYan();
                break;
        }
    }

    /**
     * 打印预览
     */
    private void getNetLanYan() {
         intent=new Intent(this,BluetoothActivity.class);
            startActivity(intent);
//            toast("请先设置打印设备");
    }

    /**
     * 初始化弹窗
     */
    public void initmPopupWindowView(View v) {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popview_item,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 200, 300);
        // 自定义view添加触摸事件
        popupwindow.setOutsideTouchable(true);
        popupwindow.setTouchable(true);
        popupwindow.setFocusable(true);
//        popupwindow.showAsDropDown(v);
        popupwindow.showAtLocation(customView, Gravity.RIGHT | Gravity.TOP, 30, 220);
        popupwindow.update();
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });

        /** 在这里可以实现自定义视图的功能 */
        btton1 = (Button) customView.findViewById(R.id.btn_student_detail_view);
        btton2 = (Button) customView.findViewById(R.id.btn_hetong_view);
        btton3 = (Button) customView.findViewById(R.id.btn_dongtai_view);
        btton2.setOnClickListener(this);
        btton1.setOnClickListener(this);
        btton3.setOnClickListener(this);

    }
}
