package com.onemeter.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.userInfo;
import com.onemeter.net.httpUtils;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：登陆页面
 * 项目名称：CrmWei
 * 作者：angelyin
 * 时间：2016/5/4 16:49
 * 备注：
 */
public class UserLoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText   edt_username;//用户名
    private EditText   edt_password;//密码
    private Button     btn_login;//登陆按钮
    Intent  intent;//跳转
    String username;
    String userpass;
    ProgressDialog prodialog;// 加载进度条对话框
    String result = null;//请求返回的结果
    /**
     * 配置账号信息
     */
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    Handler  mHandler;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_login_main);
        prodialog=new ProgressDialog(this);
        mHandler=new Handler();
        initView();

    }

    /**
     * 初始化组件
     */
    private void initView() {
        edt_username= (EditText) findViewById(R.id.edt_username);
        edt_password= (EditText) findViewById(R.id.edt_password);
        btn_login= (Button) findViewById(R.id.btn_login);

        //获取保存的用户名
        sp = getSharedPreferences("userInfo_config", Context.MODE_PRIVATE);
        editor = sp.edit();
        if (sp.getString("username", "") != null// 设置用户名
                && !sp.getString("username", "").equals("")) {
            edt_username.setText(sp.getString("username", ""));
            edt_username.setSelection(edt_username.getText().toString().length());
        }

//        if (sp.getString("password", "") != null// 设置用户名
//                && !sp.getString("password", "").equals("")) {
//            edt_password.setText(sp.getString("password", ""));
//            edt_password.setSelection(edt_password.getText().toString().length());
//        }

        btn_login.setOnClickListener(this);
        edt_username.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
                if (edt_username.getText().length() == 11) {
                    Utils.HideKeyboard(edt_username);
//                    toast(s+"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                prodialog.setMessage("加载中");
                prodialog.show();
                getNetLogin();
                break;
        }
    }

    /**
     * 登陆提交请求
     */
    private void getNetLogin() {
        username=edt_username.getText().toString();
        userpass=edt_password.getText().toString();
        if(isempty()){
          return;
        }else {

            if (!MyApplication.isNetAvailable) {// 网络不可用
                Utils.showToast(this, "网络不可用，请打开网络");
                return;
            }
            try {
               mHandler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       getPostLogin();
                   }
               },100);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    }

    /**
     * 使用原生方法提交登陆
     */

    private void getPostLogin() {

        //提交的数据请求部分
        try {
        String apiurl = Constants.SERVER_UL +"api/login";
        String userid  = "0";
        String privateKey ="32321";
            String app="app";
        Map<String, String> data = new HashMap<String, String>();
        data.put("userName", username);
        data.put("password", userpass);
            data.put("platform",app);
            new httpUtils(this).getData(apiurl, userid, privateKey, data, UserLoginActivity.this,"登入");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }



    /**
     * 输入框是否为空
     * 作者：$angelyin
     * 时间：2015/12/23 17:36
     */
    private boolean isempty() {
        if ("".equals(username)) {
            toast("用户名不能为空");
            return true;
        }
        if ("".equals(userpass)) {
            toast("请输入密码");
            return true;
        }
        if(username.length()>=32){
           toast("无效用户名");
            return true;
        }
        if(userpass.length()>=32){
            toast("无效密码");
            return true;
        }
        return false;
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
        Log.e("result",result);
       //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            code = jsonObject.getString("code");
//            toast(code+"");
        /***************************************************************************/
        if(action.equals("登入")){
            MyApplication.userInfos.clear();
            if (prodialog != null && prodialog.isShowing()) {
                prodialog.dismiss();
                Log.i("Onemeter", "关闭prodialog");
            }
           if(code.equals("10000")){
               userInfo  us=new userInfo();
               editor.putString("username", username);
//               editor.putString("password", username);
               editor.commit();
//             toast("登陆成功："+result);
               //保存登陆后的信息
               String  token=jsonObject.getString("token");
                       us.setToken(token);
               int     uid=jsonObject.getInt("id");
                       us.setUid(uid);
               if(jsonObject.isNull("appVer")){
                   us.setAppVerionCode("");
               }else{
                   us.setAppVerionCode(jsonObject.getString("appVer"));
               }
               if(jsonObject.isNull("appApk")){
                   us.setAppApkUrl("");
               }else {
                   us.setAppApkUrl(jsonObject.getString("appApk"));
               }
               if(jsonObject.isNull("desc")){
                  us.setDesc("");
               }else {
                   us.setDesc(jsonObject.getString("desc"));
               }
               if(jsonObject.isNull("nickname")){
                   us.setUsername("");
               }else {
                   us.setUsername(jsonObject.getString("nickname"));
               }

                 us.setToken(token);
               us.setUid(uid);
               MyApplication.userInfos.add(us);
              	intent=new Intent(this,MainActivity.class);
               startActivity(intent);
               this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

           }else {
               if(code.equals("11008")){
                   toast("账号不存在");
               }else if (code.equals("11014")){
                   toast("没有平台登录权限");
               }else if(code.equals("11009")){
                   toast("密码错误");
               }else {
                   message=jsonObject.getString("err");
                   toast(message);
               }
           }
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
