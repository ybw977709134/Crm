package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.onemeter.R;
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
 * 描述：学员详情页面
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/6 13:44
 * 备注：
 */
public class StudentDetailFragment extends Fragment implements View.OnClickListener {
    View view;
    private EditText fg_sd_student_name_text;//学员姓名
    private TextView fg_sd_student_sex_text;//学员性别
    private TextView fg_sd_student_birthday_text;//生日
    private EditText fg_sd_student_class_text;//三年级
    private EditText fg_sd_student_weixin_text;//微信号
    private EditText fg_sd_student_phone_text;//手机号
    private EditText fg_sd_student_contacts_text;//联系人
    private EditText fg_sd_student_other_contacts_text;//紧急联系人
    private EditText fg_sd_student_adress_text;//地址
    private EditText fg_sd_student_qq_text;//QQ地址
    private EditText beizhu_text;//备注
    private Button btn_bianji;//编辑按钮
    ProgressDialog prodialog;// 加载进度条对话框
    String studentId;//学员的ID

    private PopupWindow sex_popupwindow;//学员性别选择弹窗
    private PopupWindow birthday_popupwindow;//学员生日选择弹窗
    private Button btton1;
    private Button btton2;

    WheelBirthday wheelMain;
    Handler mHandler;
    /**
     * 时间格式选择
     **/
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_detail_main, container, false);
        prodialog = new ProgressDialog(getActivity());
        mHandler=new Handler();
        initView();
        initStudentDeyailData();
        return view;
    }

    /**
     * 初始化组件
     */
    private void initView() {
        fg_sd_student_name_text = (EditText) view.findViewById(R.id.fg_sd_student_name_text);
        fg_sd_student_sex_text = (TextView) view.findViewById(R.id.fg_sd_student_sex_text);
        fg_sd_student_birthday_text = (TextView) view.findViewById(R.id.fg_sd_student_birthday_text);
        fg_sd_student_class_text = (EditText) view.findViewById(R.id.fg_sd_student_class_text);
        fg_sd_student_weixin_text = (EditText) view.findViewById(R.id.fg_sd_student_weixin_text);
        fg_sd_student_phone_text = (EditText) view.findViewById(R.id.fg_sd_student_phone_text);
        fg_sd_student_contacts_text = (EditText) view.findViewById(R.id.fg_sd_student_contacts_text);
        fg_sd_student_other_contacts_text = (EditText) view.findViewById(R.id.fg_sd_student_other_contacts_text);
        fg_sd_student_adress_text = (EditText) view.findViewById(R.id.fg_sd_student_adress_text);
        fg_sd_student_qq_text = (EditText) view.findViewById(R.id.fg_sd_student_qq_text);
        beizhu_text = (EditText) view.findViewById(R.id.beizhu_text);

        fg_sd_student_sex_text.setOnClickListener(this);
        fg_sd_student_birthday_text.setOnClickListener(this);

        fg_sd_student_adress_text.setEnabled(false);
        fg_sd_student_name_text.setEnabled(false);
        fg_sd_student_sex_text.setEnabled(false);
        fg_sd_student_birthday_text.setEnabled(false);
        fg_sd_student_class_text.setEnabled(false);
        fg_sd_student_weixin_text.setEnabled(false);
        fg_sd_student_phone_text.setEnabled(false);
        fg_sd_student_contacts_text.setEnabled(false);
        fg_sd_student_other_contacts_text.setEnabled(false);
        fg_sd_student_qq_text.setEnabled(false);
        beizhu_text.setEnabled(false);
        btn_bianji = (Button) view.findViewById(R.id.btn_bianji);
        btn_bianji.setOnClickListener(this);
    }

    /**
     * 发送请求获取指定学生详情信息
     */
    private void initStudentDeyailData() {
        prodialog.setMessage("加载中");
        prodialog.show();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        //提交的数据请求部分

            final String apiurl = Constants.SERVER_UL+ "api/studentDetail";
            final String userid = MyApplication.userInfos.get(0).getUid() + "";
            final String privateKey = MyApplication.userInfos.get(0).getToken();
            final Map<String, String> data = new HashMap<String, String>();
            data.put("studentId", MyApplication.studentId.get(0).toString());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, StudentDetailFragment.this, "获取指定学员详细信息");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    mHandler.removeCallbacks(this);
                    if (prodialog != null && prodialog.isShowing()) {
                        prodialog.dismiss();
                        Log.i("Onemeter", "关闭prodialog");
                    }
                }
            }
        },500);



    }


    /**
     * 请求结束后此方法更新数据
     *
     * @param result
     * @param action
     */
    public void onCompleted(String result, String action) {
        String message = null;
        String code;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            Log.e("result",result);
            code = jsonObject.getString("code");
//            Utils.showToast(getActivity(), code + "");
            /******************************************************************************/
            if (action.equals("获取指定学员详细信息")) {
                if (prodialog != null && prodialog.isShowing()) {
                    prodialog.dismiss();
                    Log.i("Onemeter", "关闭prodialog");
                }

                if (code.equals("10000")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("student");
                    if (jsonObject1.isNull("name")) {
                        fg_sd_student_name_text.setText("");
                    } else {
                        String name = jsonObject1.getString("name");
                        fg_sd_student_name_text.setText(name);
                    }
                    if (jsonObject1.isNull("id")) {
                    } else {
                        studentId = jsonObject1.getString("id");
                    }
                    if (jsonObject1.isNull("birthday")) {
                        fg_sd_student_birthday_text.setText("");
                    } else {
                        String birthday = jsonObject1.getString("birthday");
                        fg_sd_student_birthday_text.setText(birthday);
                    }
                    if (jsonObject1.isNull("grade")) {
                        fg_sd_student_class_text.setText("");
                    } else {
                        String grade = jsonObject1.getString("grade");
                        fg_sd_student_class_text.setText(grade);
                    }
                    if (jsonObject1.isNull("mobile")) {
                        fg_sd_student_phone_text.setText("");
                    } else {
                        String mobile = jsonObject1.getString("mobile");
                        fg_sd_student_phone_text.setText(mobile);
                    }
                    if (jsonObject1.isNull("contact")) {
                        fg_sd_student_contacts_text.setText("");
                    } else {
                        String contact = jsonObject1.getString("contact");
                        fg_sd_student_contacts_text.setText(contact);
                    }
                    if (jsonObject1.isNull("otherContact")) {
                        fg_sd_student_other_contacts_text.setText("");
                    } else {
                        String otherContact = jsonObject1.getString("otherContact");
                        fg_sd_student_other_contacts_text.setText(otherContact);
                    }
                    if (jsonObject1.isNull("weixin")) {
                        fg_sd_student_weixin_text.setText("");
                    } else {
                        String weixin = jsonObject1.getString("weixin");
                        fg_sd_student_weixin_text.setText(weixin);
                    }
                    if (jsonObject1.isNull("qq")) {
                        fg_sd_student_qq_text.setText("");
                    } else {
                        String qq = jsonObject1.getString("qq");
                        fg_sd_student_qq_text.setText(qq);
                    }
                    if (jsonObject1.isNull("sex")) {
                        fg_sd_student_sex_text.setText("");
                    } else {
                        String sex = jsonObject1.getString("sex");
                        fg_sd_student_sex_text.setText(sex);
                    }
                    if (jsonObject1.isNull("address")) {
                        fg_sd_student_adress_text.setText("");
                    } else {
                        String address = jsonObject1.getString("address");
                        fg_sd_student_adress_text.setText(address);
                    }

                    if (jsonObject1.isNull("comment")) {
                        beizhu_text.setText("");
                    } else {
                        String beizhu = jsonObject1.getString("comment");
                        beizhu_text.setText(beizhu);
                    }

                } else {
                    message=jsonObject.getString("err");
                    Utils.showToast(getActivity(), message);
                }
            }

            /************************************************************************/
            if (action.equals("修改学员详情信息")) {
                if (prodialog != null && prodialog.isShowing()) {
                    prodialog.dismiss();
                    Log.i("Onemeter", "关闭prodialog");
                }
                if (code.equals("10000")) {
                    Utils.showToast(getActivity(), "修改成功");
                    btn_bianji.setText("编辑");
                    Intent intent1 = new Intent();
                    intent1.setAction("push");
                    intent1.putExtra("pushtype", 1);
                    getActivity().sendBroadcast(intent1);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                } else {
                    if(code.equals("13007")){
                        Utils.showToast(getActivity(), "更新学员信息失败");
                    }else if(code.equals("13008")) {
                        Utils.showToast(getActivity(), "手机号已存在");
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bianji:
                //编辑学员信息
                if (btn_bianji.getText().toString().equals("编辑")) {
                    setNetBianJi();
                    Intent intent1 = new Intent();
                    intent1.setAction("bianji");
                    intent1.putExtra("type", 11);
                    getActivity().sendBroadcast(intent1);
                } else if (btn_bianji.getText().toString().equals("保存")) {
                    if(Utils.isPhoneNum(fg_sd_student_phone_text.getText().toString())){
                        Intent intent2 = new Intent();
                        intent2.setAction("bianji");
                        intent2.putExtra("type", 22);
                        getActivity().sendBroadcast(intent2);
                        getNetPostBianJi();
                    }else{
                        Utils.showToast(getActivity(),"手机号码格式不正确");
                    }


                }

                break;
            case R.id.fg_sd_student_sex_text:
                //选择学员性别
                //弹出选项对话框
                if (sex_popupwindow != null && sex_popupwindow.isShowing()) {
                    sex_popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView(fg_sd_student_sex_text);
                }

                break;
            case R.id.fg_sd_student_birthday_text:
                //选择生日
//                Utils.showToast(getActivity(),"生日");
                if (birthday_popupwindow != null && birthday_popupwindow.isShowing()) {
                    birthday_popupwindow.dismiss();
                    return;
                } else {
                    initBirthdayPopupWindowView(fg_sd_student_birthday_text);
                }

                break;
            case R.id.btn_student_nan_view:
                //男
                fg_sd_student_sex_text.setText("男");
                sex_popupwindow.dismiss();
                break;
            case R.id.btn_student_nv_view:
                //女
                fg_sd_student_sex_text.setText("女");
                sex_popupwindow.dismiss();
                break;

        }
    }

    /**
     * 初始化生日选择弹窗
     *
     * @param v
     */
    private void initBirthdayPopupWindowView(View v) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layoutView = inflater.inflate(R.layout.timepicker, null);
        TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
        TextView ok = (TextView) layoutView.findViewById(R.id.ok);
        ScreenInfo screenInfo = new ScreenInfo(getActivity());
        wheelMain = new WheelBirthday(layoutView, null);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = fg_sd_student_birthday_text.getText().toString();
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
                fg_sd_student_birthday_text.setText(Utils.parseDate(wheelMain.getTime()));
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
     * 初始化性别选择弹窗
     *
     * @param v
     */
    private void initmPopupWindowView(View v) {
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
     * 发送请求提交服务器
     */
    private void getNetPostBianJi() {
        if (isEmypt()) {
            prodialog.setMessage("加载中");
            prodialog.show();
            if (!MyApplication.isNetAvailable) {// 网络不可用
                Utils.showToast(getActivity(), "网络不可用，请打开网络");
                return;
            }
            //提交的数据请求部分

                final String apiurl = Constants.SERVER_UL+"api/editStudent";
                final String userid = MyApplication.userInfos.get(0).getUid() + "";
                final String privateKey = MyApplication.userInfos.get(0).getToken();
                final Map<String, String> data = new HashMap<String, String>();
                data.put("id", studentId);
                data.put("name", fg_sd_student_name_text.getText().toString());
                data.put("sex", fg_sd_student_sex_text.getText().toString());
                data.put("birthday", fg_sd_student_birthday_text.getText().toString());
                data.put("grade", fg_sd_student_class_text.getText().toString());
                data.put("mobile", fg_sd_student_phone_text.getText().toString());
                data.put("contact", fg_sd_student_contacts_text.getText().toString());
                data.put("otherContact", fg_sd_student_other_contacts_text.getText().toString());
                data.put("address", fg_sd_student_adress_text.getText().toString());
                data.put("weixin", fg_sd_student_weixin_text.getText().toString());
                data.put("qq", fg_sd_student_qq_text.getText().toString());
                data.put("comment", beizhu_text.getText().toString());

//            Utils.showToast(getActivity(),fg_sd_student_class_text.getText().toString());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, StudentDetailFragment.this, "修改学员详情信息");
                    }catch (Exception e){
                        mHandler.removeCallbacks(this);
                        if (prodialog != null && prodialog.isShowing()) {
                            prodialog.dismiss();
                        }
                    }


                }
            },500);

        }

    }

    /**
     * 判断必填选项不能为空
     *
     * @return
     */
    private boolean isEmypt() {
        if ("".equals(studentId)) {
            Utils.showToast(getActivity(), "ID不能为空");
            return false;
        }
        if ("".equals(fg_sd_student_name_text.getText().toString())) {
            Utils.showToast(getActivity(), "姓名不能为空");
            return false;
        }
        return true;
    }

    /**
     * 控制编辑框可编辑状态
     */
    private void setNetBianJi() {

        fg_sd_student_adress_text.setEnabled(true);
        fg_sd_student_adress_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_name_text.setEnabled(true);
        fg_sd_student_name_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_sex_text.setEnabled(true);
        fg_sd_student_sex_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_birthday_text.setEnabled(true);
        fg_sd_student_birthday_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_class_text.setEnabled(true);
        fg_sd_student_class_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_weixin_text.setEnabled(true);
        fg_sd_student_weixin_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_phone_text.setEnabled(true);
        fg_sd_student_phone_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_contacts_text.setEnabled(true);
        fg_sd_student_contacts_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_other_contacts_text.setEnabled(true);
        fg_sd_student_other_contacts_text.setBackgroundResource(R.drawable.edt_style);
        fg_sd_student_qq_text.setEnabled(true);
        fg_sd_student_qq_text.setBackgroundResource(R.drawable.edt_style);
        beizhu_text.setEnabled(true);
        beizhu_text.setBackgroundResource(R.drawable.edt_style);
        btn_bianji.setText("保存");
        btn_bianji.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.mipmap.save), null, null, null);
    }
}
