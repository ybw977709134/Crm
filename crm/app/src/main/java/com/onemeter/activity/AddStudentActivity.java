package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.httpUtils;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelBirthday;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：添加学员页面
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/6 18:45
 * 备注：
 */
public class AddStudentActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton  add_student_img_return;//返回键
    private TextView     add_student_username;//学员用户名
    private Button       add_student_btn_baocun;//保存
    private EditText    fg_add_student_name_text;//名称
    private TextView    fg_add_student_sex_text;//性别
    private TextView    fg_add_student_birthday_text;//生日
    private EditText    fg_add_student_class_text;//班级
    private EditText    fg_add_student_weixin_text;//微信号
    private EditText    fg_add_student_phone_text;//手机号码
    private EditText    fg_add_student_contacts_text;//联系人
    private EditText    fg_add_student_other_contacts_text;//其他联系人
    private EditText    fg_add_student_adress_text;//地址
    private EditText    fg_add_student_qq_text;//qq号码
    private EditText    add_student_beizhu_text;//备注

    private PopupWindow sex_popupwindow;//学员性别选择弹窗
    private PopupWindow birthday_popupwindow;//学员生日选择弹窗
    private Button btton1;
    private Button btton2;

    WheelBirthday wheelMain;
    /**
     * 时间格式选择
     **/
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Handler  mHandler;
    ProgressDialog prodialog;// 加载进度条对话框
    Context  mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_student_main);
        prodialog=new ProgressDialog(this);
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.RIGHT;//设置对话框右边显示
        win.setAttributes(lp);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        mHandler=new Handler();
        add_student_username= (TextView) findViewById(R.id.add_student_username);
        add_student_username.setText(MyApplication.userInfos.get(0).getUsername().toString());
        add_student_img_return= (ImageButton) findViewById(R.id.add_student_img_return);
        add_student_username= (TextView) findViewById(R.id.add_student_username);
        add_student_btn_baocun= (Button) findViewById(R.id.add_student_btn_baocun);
        fg_add_student_name_text= (EditText) findViewById(R.id.fg_add_student_name_text);

        fg_add_student_sex_text= (TextView) findViewById(R.id.fg_add_student_sex_text);
        fg_add_student_birthday_text= (TextView) findViewById(R.id.fg_add_student_birthday_text);

        fg_add_student_class_text= (EditText) findViewById(R.id.fg_add_student_class_text);
        fg_add_student_weixin_text= (EditText) findViewById(R.id.fg_add_student_weixin_text);
        fg_add_student_phone_text= (EditText) findViewById(R.id.fg_add_student_phone_text);
        fg_add_student_contacts_text= (EditText) findViewById(R.id.fg_add_student_contacts_text);
        fg_add_student_other_contacts_text= (EditText) findViewById(R.id.fg_add_student_other_contacts_text);
        fg_add_student_adress_text= (EditText) findViewById(R.id.fg_add_student_adress_text);
        fg_add_student_qq_text= (EditText) findViewById(R.id.fg_add_student_qq_text);
        add_student_beizhu_text= (EditText) findViewById(R.id.add_student_beizhu_text);

        add_student_img_return.setOnClickListener(this);
        add_student_btn_baocun.setOnClickListener(this);
        fg_add_student_sex_text.setOnClickListener(this);
        fg_add_student_birthday_text.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_student_img_return:
               //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.add_student_btn_baocun:
                //保存按钮
                if(isEmpyt()){
                    prodialog.setMessage("加载中");
                    prodialog.show();
                    if (!MyApplication.isNetAvailable) {// 网络不可用
                        Utils.showToast(this, "网络不可用，请打开网络");
                        return;
                    }
                    getNetAddStudentData();
                }
                break;
            case R.id.fg_add_student_sex_text:
                //性别选择弹窗
                if (sex_popupwindow != null && sex_popupwindow.isShowing()) {
                    sex_popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView(fg_add_student_sex_text);
                }

                break;
            case R.id.fg_add_student_birthday_text:
                //生日选择弹窗
                if (birthday_popupwindow != null && birthday_popupwindow.isShowing()) {
                    birthday_popupwindow.dismiss();
                    return;
                } else {
                    initBirthdayPopupWindowView(fg_add_student_birthday_text);
                }
                break;
            case R.id.btn_student_nan_view:
                //男
                fg_add_student_sex_text.setText("男");
                sex_popupwindow.dismiss();
                break;
            case R.id.btn_student_nv_view:
                //女
                fg_add_student_sex_text.setText("女");
                sex_popupwindow.dismiss();
                break;

        }
    }

    /**
     * 初始化生日选择弹窗
     * @param v
     */
    private void initBirthdayPopupWindowView(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layoutView = inflater.inflate(R.layout.timepicker, null);
        TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
        TextView ok = (TextView) layoutView.findViewById(R.id.ok);
        ScreenInfo screenInfo = new ScreenInfo(this);
        wheelMain = new WheelBirthday(layoutView, null);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = fg_add_student_birthday_text.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 获取到生日
                fg_add_student_birthday_text.setText(Utils.parseDate(wheelMain.getTime()));
                birthday_popupwindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                birthday_popupwindow.dismiss();
            }
        });
        birthday_popupwindow = new PopupWindow(layoutView, RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT, true);
        birthday_popupwindow.setContentView(layoutView);
        birthday_popupwindow.setWidth(1100);
        birthday_popupwindow.setBackgroundDrawable(new BitmapDrawable());
        birthday_popupwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        birthday_popupwindow.setOutsideTouchable(true);
        birthday_popupwindow.update();
        birthday_popupwindow.setTouchable(true);
        birthday_popupwindow.setFocusable(true);
        birthday_popupwindow.showAtLocation(layoutView, Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 40, 0);

    }

    /**
     * 性别选择
     * @param v
     */
    private void initmPopupWindowView(View v) {
        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.sex_popview_item,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        sex_popupwindow = new PopupWindow(customView, 200, 190);
        // 自定义view添加触摸事件
        sex_popupwindow.setOutsideTouchable(true);
        sex_popupwindow.setTouchable(true);
        sex_popupwindow.setFocusable(true);
        sex_popupwindow.showAsDropDown(v);
        sex_popupwindow.update();
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sex_popupwindow != null && sex_popupwindow.isShowing()) {
                    sex_popupwindow.dismiss();
                    sex_popupwindow = null;
                }
                return false;
            }
        });

        /** 在这里可以实现自定义视图的功能 */
        btton1 = (Button) customView.findViewById(R.id.btn_student_nan_view);
        btton2 = (Button) customView.findViewById(R.id.btn_student_nv_view);
        btton2.setOnClickListener(this);
        btton1.setOnClickListener(this);

    }

    /**
     * 新增学员信息到服务器
     */
    private void getNetAddStudentData() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        //提交的数据请求部分
            final String apiurl = Constants.SERVER_UL+"api/editStudent";
            final String userid  = MyApplication.userInfos.get(0).getUid()+"";
            final String privateKey =MyApplication.userInfos.get(0).getToken();
            final Map<String, String> data = new HashMap<String, String>();
            data.put("id",0+"");
            data.put("name", fg_add_student_name_text.getText().toString());
            data.put("mobile",fg_add_student_phone_text.getText().toString());
            data.put("sex", fg_add_student_sex_text.getText().toString());
            data.put("contact", fg_add_student_contacts_text.getText().toString());
            if(!fg_add_student_birthday_text.getText().toString().equals("")){
                data.put("birthday", fg_add_student_birthday_text.getText().toString());
            }
            if(!fg_add_student_class_text.getText().toString().equals("")){
            data.put("grade", fg_add_student_class_text.getText().toString());
            }
            if(!fg_add_student_other_contacts_text.getText().toString().equals("")){
            data.put("otherContact", fg_add_student_other_contacts_text.getText().toString());
            }
            if(!fg_add_student_adress_text.getText().toString().equals("")){
            data.put("address", fg_add_student_adress_text.getText().toString());
            }
           if(!fg_add_student_weixin_text.getText().toString().equals("")){
            data.put("weixin", fg_add_student_weixin_text.getText().toString());
           }
           if(!fg_add_student_qq_text.getText().toString().equals("")){
            data.put("qq", fg_add_student_qq_text.getText().toString());
            }
           if(!add_student_beizhu_text.getText().toString().equals("")){
               data.put("comment", add_student_beizhu_text.getText().toString());
           }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        new httpUtils(mContext).getData(apiurl, userid, privateKey, data, AddStudentActivity.this, "添加学员详情信息");

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                       mHandler.removeCallbacks(this);
                        e.printStackTrace();
                    };
                }
            }, 800);

    }


    /**
     * 判断必填选项是否为空
     * @return
     */
    private boolean isEmpyt() {
        if("".equals(fg_add_student_name_text.getText().toString())){
            toast("学员姓名不能为空");
            return false;
        }
        if("".equals(fg_add_student_phone_text.getText().toString())){
            toast("手机号码不能为空");
            return false;
        }
        if(!Utils.isPhoneNum(fg_add_student_phone_text.getText().toString())){
            toast("手机号码格式不正确");
            return false;
        }
        if("".equals(fg_add_student_sex_text.getText().toString())){
            toast("性别不能为空");
            return false;
        }
        if("".equals(fg_add_student_contacts_text.getText().toString())){
            toast("联系人不能为空");
            return false;
        }
        return  true;
    }

    /**
     * 请求结束后此方法更新数据
     * @param result
     * @param action
     */
    public void onCompleted(String result, String action) {
        String message = null;
        String code;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
             code = jsonObject.getString("code");
//            Utils.showToast(getActivity(), code + "");
            /******************************************************************************/
              if(action.equals("添加学员详情信息")){
                  if (prodialog != null && prodialog.isShowing()) {
                      prodialog.dismiss();
                      Log.i("Onemeter", "关闭prodialog");
                  }
                  if(code.equals("10000")){
                    toast("添加成功");
                      Intent intent1 = new Intent();
                      intent1.setAction("push");
                      intent1.putExtra("pushtype", 1);
                     this.sendBroadcast(intent1);

                      finish();
                      this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);

                  }else {

                      if(code.equals("13008")){
                          toast("手机号已存在");
                      }else if(code.equals("13006")){
                          toast("新增学员失败");
                      }else {

                        toast(jsonObject.getString("err"));
                      }

                  }
              }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
