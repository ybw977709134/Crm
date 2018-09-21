package com.onemeter.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.utils.Utils;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelBirthday;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 描述：报名页面
 * 项目名称：CrmWei
 * 作者：angelyin
 * 时间：2016/5/4 20:35
 * 备注：
 */
public class EncrollFragment extends Fragment implements View.OnClickListener {
    View view;
    private EditText  fg_baoming_student_name_text;//学员名称
    private EditText  fg_add_student_phone_text;//手机号码
    private TextView  fg_baoming_student_sex_text;//点击选择性别
    private EditText  fg_add_student_contacts_text;//联系人
    private TextView  fg_baoming_student_class_text;//选择班级
    private EditText  fg_add_student_Operator_text;//操作人员
    private TextView  fg_add_student_time_text;//报名时间
    private EditText  fg_add_student_code_text;//报名编号
    private EditText  fg_add_student_total_text;//报名总价
    private EditText  fg_add_student_card_text;//门禁卡号
    private EditText  add_student_beizhu_text;//备注
    private Button    btn_baoming;//报名
    private Button    btn_chongzhi;//重置

    private PopupWindow sex_popupwindow;//学员性别选择弹窗
    private Button btton1;
    private Button btton2;

    private PopupWindow time_popupwindow;//选择报名时间弹窗

    WheelBirthday wheelMain;
    /**
     * 时间格式选择
     **/
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_encroll_main, container, false);
        initView();
        return view;
    }

    /**
     * 初始化组件
     */
    private void initView() {
        fg_baoming_student_name_text= (EditText) view.findViewById(R.id.fg_baoming_student_name_text);
        fg_add_student_phone_text= (EditText) view.findViewById(R.id.fg_add_student_phone_text);
        fg_baoming_student_sex_text= (TextView) view.findViewById(R.id.fg_baoming_student_sex_text);
        fg_add_student_contacts_text= (EditText) view.findViewById(R.id.fg_add_student_contacts_text);
        fg_baoming_student_class_text= (TextView) view.findViewById(R.id.fg_baoming_student_class_text);
        fg_add_student_Operator_text= (EditText) view.findViewById(R.id.fg_add_student_Operator_text);
        fg_add_student_time_text= (TextView) view.findViewById(R.id.fg_add_student_time_text);
        fg_add_student_code_text= (EditText) view.findViewById(R.id.fg_add_student_code_text);
        fg_add_student_total_text= (EditText) view.findViewById(R.id.fg_add_student_total_text);
        fg_add_student_card_text= (EditText) view.findViewById(R.id.fg_add_student_card_text);
        add_student_beizhu_text= (EditText) view.findViewById(R.id.add_student_beizhu_text);
        btn_baoming= (Button) view.findViewById(R.id.btn_baoming);
        btn_chongzhi= (Button) view.findViewById(R.id.btn_chongzhi);

        btn_chongzhi.setOnClickListener(this);
        btn_baoming.setOnClickListener(this);
        fg_baoming_student_sex_text.setOnClickListener(this);
        fg_baoming_student_class_text.setOnClickListener(this);
        fg_add_student_time_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fg_baoming_student_sex_text:
                //性别
                if (sex_popupwindow != null && sex_popupwindow.isShowing()) {
                    sex_popupwindow.dismiss();
                    return;
                } else {
                    initbmPopupWindowView(fg_baoming_student_sex_text);
                }
                break;
            case R.id.fg_baoming_student_class_text:
               //班级
                break;
            case R.id.fg_add_student_time_text:
                //报名时间选择
                if (time_popupwindow != null && time_popupwindow.isShowing()) {
                    time_popupwindow.dismiss();
                    return;
                } else {
                    initTimePopupWindowView(fg_add_student_time_text);
                }
                break;
            case R.id.btn_chongzhi:
                //重置
                getClearEt();
                break;
            case R.id.btn_baoming:
                //报名
                Toast.makeText(getActivity(),"选择支付方式",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_student_nan_view:
                //男
                fg_baoming_student_sex_text.setText("男");
                sex_popupwindow.dismiss();
                break;
            case R.id.btn_student_nv_view:
                //女
                fg_baoming_student_sex_text.setText("女");
                sex_popupwindow.dismiss();
                break;
        }
    }

    /**
     *  初始化选择时间器弹窗
     */
    private void initTimePopupWindowView(View v) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layoutView = inflater.inflate(R.layout.timepicker, null);
        TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
        TextView ok = (TextView) layoutView.findViewById(R.id.ok);
        ScreenInfo screenInfo = new ScreenInfo(getActivity());
        wheelMain = new WheelBirthday(layoutView, null);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = fg_add_student_time_text.getText().toString();
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
                // 获取到日期
                fg_add_student_time_text.setText(Utils.parseDate(wheelMain.getTime()));
                time_popupwindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                time_popupwindow.dismiss();
            }
        });
        time_popupwindow = new PopupWindow(layoutView, RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT, true);
        time_popupwindow.setContentView(layoutView);
        time_popupwindow.setWidth(1100);
        time_popupwindow.setBackgroundDrawable(new BitmapDrawable());
        time_popupwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        time_popupwindow.setOutsideTouchable(true);
        time_popupwindow.update();
        time_popupwindow.setTouchable(true);
        time_popupwindow.setFocusable(true);
        time_popupwindow.showAtLocation(layoutView, Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 40, 0);


    }

    /**
     * 性别选择布局
     */
    private void initbmPopupWindowView(View v) {
        // // 获取自定义布局文件pop.xml的视图
        View customView = getActivity().getLayoutInflater().inflate(R.layout.sex_popview_item,
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
     * 清除数据
     */
    private void getClearEt() {
        fg_baoming_student_name_text.setText("");
        fg_add_student_phone_text.setText("");
        fg_baoming_student_sex_text.setText("");
        fg_add_student_contacts_text.setText("");
        fg_baoming_student_class_text.setText("");
        fg_add_student_Operator_text.setText("");
        fg_add_student_time_text.setText("");
        fg_add_student_code_text.setText("");
        fg_add_student_total_text.setText("");
        fg_add_student_card_text.setText("");
        add_student_beizhu_text.setText("");
    }
}
