package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.onemeter.R;
import com.onemeter.activity.AddStudentActivity;
import com.onemeter.activity.StudentDetailActivity;
import com.onemeter.adapter.StudentFragmentAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.StudentInfo;
import com.onemeter.net.httpUtils;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：学员列表页面
 * 项目名称：CrmWei
 * 作者：angelyin
 * 时间：2016/5/4 20:18
 * 备注：
 */
public class StudentFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, XListView.IXListViewListener {
    public List<StudentInfo> ssfo;
    private PopupWindow type_popupwindow;//类型选择弹窗
    private PopupWindow logout_popupwindow;//退出选择弹窗
    View view;
    private XListView student_list;
    private StudentFragmentAdapter mstudentFragmentAdapter;
    Intent intent;
    private Button  change_choose_type_text;//（全部、已报名、未报名）弹窗选择器
    private ImageButton add_student;//添加学员信息
    private Button  btn_user_logout;//退出登录弹窗选择
    private ImageButton  student_btn_sousuo;//搜索按钮
    private Button  btn_type_no_baoming;//未报名
    private Button  btn_type_yes_baoming;//已报名
    private Button  btn_type_all;//已报名
    private Button  btn_logout;//退出
    private EditText  student_soso_et;//收索框
    ProgressDialog prodialog;// 加载进度条对话框
    //学员列表分页字段
    private  int page=1;//第几页
    private  int pageSize=20;//单页数据条数
    //根据搜索列表分页字段
    private  int Spage=1;//第几页
    private  int SpageSize=20;//单页数据条数
    Handler  mHandler;
    private  static  String  chooseTypePush="正常列表";
    String  type=0+"";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_main, container, false);
        ssfo = new ArrayList<StudentInfo>();
        prodialog=new ProgressDialog(getActivity());
        mHandler=new Handler();
        initView();
        prodialog.setMessage("加载中");
        prodialog.show();
        getNetStudentlistData(page);
        registerPushResultReceiver();
        return view;
    }
    PushResultReceiver pushResultReceiver = new PushResultReceiver();

    private void registerPushResultReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("push");
        getActivity().registerReceiver(pushResultReceiver, intentFilter);
    }

    private void relaseRegisterPushResultReceiver() {
        getActivity().unregisterReceiver(pushResultReceiver);
    }

    private class PushResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int type = intent.getIntExtra("pushtype", 1);
                switch (type) {
                    case 1:
                        ssfo.clear();
                        getNetStudentlistData(1);
                        MyApplication.fragmentStudentPositon.clear();
                        mstudentFragmentAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        ssfo.clear();
                        getNetStudentlistData(1);
//                        Utils.showToast(getActivity(),"刷新");
                        MyApplication.fragmentStudentPositon.clear();
                        mstudentFragmentAdapter.notifyDataSetChanged();
                        break;

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        relaseRegisterPushResultReceiver();
    }



    /**
     * 发送请求，获取学员列表信息
     */
    private void getNetStudentlistData(int page) {
        mstudentFragmentAdapter.notifyDataSetChanged();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        //提交的数据请求部分

            final String apiurl = Constants.SERVER_UL+Constants.API_POST_STUDENT_LIST;
            final String userid  = MyApplication.userInfos.get(0).getUid()+"";
            final String privateKey =MyApplication.userInfos.get(0).getToken();
            final Map<String, String> data = new HashMap<String, String>();
            data.put("uid", MyApplication.userInfos.get(0).getUid()+"");
            data.put("page",page+"");
            data.put("type",0+"");
            data.put("keyword","");
            data.put("pageSize",pageSize+"");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, StudentFragment.this,"获取学员列表信息");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    if (prodialog != null && prodialog.isShowing()) {
                        prodialog.dismiss();
                        Log.i("Onemeter", "关闭prodialog");
                    }
                    mHandler.removeCallbacks(this);
                }
            }
        },500);

    }


    /**
     * 初始化组件
     */
    private void initView() {
        student_list = (XListView) view.findViewById(R.id.student_list);
        mstudentFragmentAdapter = new StudentFragmentAdapter(getActivity(), ssfo);
        student_list.setAdapter(mstudentFragmentAdapter);
        student_soso_et= (EditText) view.findViewById(R.id.student_soso_et);
        student_btn_sousuo= (ImageButton) view.findViewById(R.id.student_btn_sousuo);
        change_choose_type_text= (Button) view.findViewById(R.id.change_choose_type_text);
        change_choose_type_text.setText("全部");
        btn_user_logout= (Button) view.findViewById(R.id.btn_user_logout);
        btn_user_logout.setText(MyApplication.userInfos.get(0).getUsername());
        add_student= (ImageButton) view.findViewById(R.id.add_student);
        //设置可以刷新和加载
        student_list.setPullLoadEnable(true);
        student_list.setPullRefreshEnable(true);
        student_list.setXListViewListener(this);

        student_list.setOnItemClickListener(this);
        btn_user_logout.setOnClickListener(this);
        add_student.setOnClickListener(this);
        change_choose_type_text.setOnClickListener(this);
        student_btn_sousuo.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ssfo.clear();
                mstudentFragmentAdapter.notifyDataSetChanged();
                if (chooseTypePush.equals("正常列表")) {
                    page = 1;
                    getNetStudentlistData(page);
                    change_choose_type_text.setText("全部");

                } else if (chooseTypePush.equals("搜索列表")) {
                    Spage = 1;
                    getNetSOSOData(isChooseType().toString(), Spage);
                }

                student_list.stopRefresh();
//                Utils.showToast(getActivity(), "刷新成功");

            }
        }, 900);


    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            // 模拟加载数据，1s之后停止加载
            @Override
            public void run() {
                if (chooseTypePush.equals("正常列表")) {
                    page++;
                    getNetStudentlistData(page);

                } else if (chooseTypePush.equals("搜索列表")) {
                    Spage++;
                    getNetSOSOData(isChooseType().toString(), Spage);
                }
                student_list.stopLoadMore();
//                Utils.showToast(getActivity(), "加载成功");
            }
        }, 100);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         MyApplication.studentId.clear();
         MyApplication.studentName.clear();
        MyApplication.studentId.add(ssfo.get(position - 1).getStudent_id());
        MyApplication.studentName.add(ssfo.get(position - 1).getName());
        MyApplication.fragmentStudentPositon.clear();
        MyApplication.fragmentStudentPositon.add(position-1+"");
        mstudentFragmentAdapter.notifyDataSetChanged();
//        Utils.showToast(getActivity(),MyApplication.studentId.get(0).toString());
        intent = new Intent(getActivity(), StudentDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_choose_type_text:
                //全部选择筛选类型
//                Utils.showToast(getActivity(),"全部");
                if (type_popupwindow != null&&type_popupwindow.isShowing()) {
                    type_popupwindow.dismiss();
                    return;
                } else {
                    initTypePopupWindowView(change_choose_type_text);
                }

                break;
            case R.id.add_student:
                //添加学员信息操作
                intent=new Intent(getActivity(),AddStudentActivity.class);
                startActivity(intent);
                break;
            case R.id.student_btn_sousuo:
                //收索按钮
                chooseTypePush="搜索列表";
                    ssfo.clear();
                    mstudentFragmentAdapter.notifyDataSetChanged();
                  prodialog.setMessage("加载中");
                  prodialog.show();
                    getNetSOSOData(isChooseType().toString(), Spage);
                Utils.HideKeyboard(view);

                break;
            case R.id.btn_user_logout:
                //登出用户操作
//                Utils.showToast(getActivity(),"退出登录");
                if (logout_popupwindow != null&&logout_popupwindow.isShowing()) {
                    logout_popupwindow.dismiss();
                    return;
                } else {
                    initLogoutPopupWindowView(btn_user_logout);
                }

                break;
            case R.id.btn_type_no_baoming:
                //未报名
                change_choose_type_text.setText("未报名");
                type_popupwindow.dismiss();
                break;
            case R.id.btn_type_yes_baoming:
                //已报名
                change_choose_type_text.setText("已报名");
                type_popupwindow.dismiss();
                break;
            case R.id.btn_type_all:
                //全部
                change_choose_type_text.setText("全部");
                type_popupwindow.dismiss();
                break;
            case R.id.btn_logout:
                //退出登录
                 getLogout();
                logout_popupwindow.dismiss();
                break;
        }
    }

    /**
     * 判断选择的类型（全部：0、未报名：2、已报名：1）
     * @return
     */
    private String isChooseType() {

      if(change_choose_type_text.getText().toString().equals("已报名")){
            type=1+"";
        }else if(change_choose_type_text.getText().toString().equals("未报名")){
            type=2+"";
        }else {
            type=0+"";
        }
        return type;
    }


    /**
     * 向服务器提交收索信息
     */
    private void getNetSOSOData(String type,int Spage) {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }

        //提交的数据请求部分
        final String apiurl = Constants.SERVER_UL+Constants.API_POST_STUDENT_LIST;
        final String userid  = MyApplication.userInfos.get(0).getUid()+"";
        final String privateKey =MyApplication.userInfos.get(0).getToken();
        final Map<String, String> data = new HashMap<String, String>();
        data.put("uid", MyApplication.userInfos.get(0).getUid()+"");
        data.put("page",Spage+"");
            data.put("type",type);
        data.put("keyword",student_soso_et.getText().toString());
        data.put("pageSize", SpageSize + "");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, StudentFragment.this, "获取学员列表信息");
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
        }, 200);

    }


    /**
     * 退出当前账号，初始化所有数据
     */
    private void getLogout() {
        Intent intent = new Intent();
        intent.setAction(BaseActivity.SYSTEM_EXIT);
        getActivity().sendBroadcast(intent);

    }


    /**
     * 初始化退出登录弹窗
     * @param btn_user_logout
     */

    private void initLogoutPopupWindowView(Button btn_user_logout) {
        // // 获取自定义布局文件pop.xml的视图
        View logout_itmeView =getActivity().getLayoutInflater().inflate(R.layout.logout_popview_item,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        logout_popupwindow = new PopupWindow(logout_itmeView, 200,  120);
        // 自定义view添加触摸事件
        logout_popupwindow.setOutsideTouchable(true);
        logout_popupwindow.setTouchable(true);
        logout_popupwindow.setFocusable(true);
        logout_popupwindow.showAsDropDown(btn_user_logout);
        logout_popupwindow.update();
        logout_itmeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (logout_popupwindow != null && logout_popupwindow.isShowing()) {
                    logout_popupwindow.dismiss();
                    logout_popupwindow = null;
                }
                return false;
            }
        });

        /** 在这里可以实现自定义视图的功能 */
        btn_logout= (Button) logout_itmeView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);


    }


    /**
     * 初始化全部类型选择下拉框
     * @param change_choose_type_text
     */
    private void initTypePopupWindowView(Button change_choose_type_text) {
        // // 获取自定义布局文件pop.xml的视图
        View type_itmeView =getActivity().getLayoutInflater().inflate(R.layout.type_popview_item,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
       type_popupwindow = new PopupWindow(type_itmeView, 200,  300);
        // 自定义view添加触摸事件
        type_popupwindow.setOutsideTouchable(true);
        type_popupwindow.setTouchable(true);
        type_popupwindow.setFocusable(true);
        type_popupwindow.showAsDropDown(change_choose_type_text);
        type_popupwindow.update();
        type_itmeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (type_popupwindow != null && type_popupwindow.isShowing()) {
                    type_popupwindow.dismiss();
                    type_popupwindow = null;
                }
                return false;
            }
        });

        /** 在这里可以实现自定义视图的功能 */
        btn_type_no_baoming = (Button) type_itmeView.findViewById(R.id.btn_type_no_baoming);
        btn_type_yes_baoming = (Button) type_itmeView.findViewById(R.id.btn_type_yes_baoming);
        btn_type_all = (Button) type_itmeView.findViewById(R.id.btn_type_all);
        btn_type_no_baoming.setOnClickListener(this);
        btn_type_yes_baoming.setOnClickListener(this);
        btn_type_all.setOnClickListener(this);


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
//            Utils.showToast(getActivity(),code+"");
          /***************************************************************************/
           if(action.equals("获取学员列表信息")){
               if (prodialog != null && prodialog.isShowing()) {
                   prodialog.dismiss();
                   Log.i("Onemeter", "关闭prodialog");
               }
               if(code.equals("10000")){
                   JSONArray  jsonArray=jsonObject.getJSONArray("datas");
                   if(jsonArray.length()==0){
                       return;
                   }else {
                       for (int i=0;i<jsonArray.length();i++){
                           StudentInfo sdfo = new StudentInfo();
                           JSONObject  jsonObject1=jsonArray.getJSONObject(i);
                           if(jsonObject1.isNull("id")){
                               sdfo.setStudent_id("");
                           }else{
                               sdfo.setStudent_id(jsonObject1.getString("id"));
                           }
                           if(jsonObject1.isNull("name")){
                           }else{
                               sdfo.setName(jsonObject1.getString("name"));
                           }
                           if(jsonObject1.isNull("cardNumber")){
                               sdfo.setCard("");
                           }else {
                               sdfo.setCard(jsonObject1.getString("cardNumber"));
                           }
                           if(jsonObject1.isNull("mobile")){
                               sdfo.setPhone("");
                           }else {
                               sdfo.setPhone(jsonObject1.getString("mobile"));
                           }
                           if(jsonObject1.isNull("grade")){
                               sdfo.setNclass("");
                           }else {
                               sdfo.setNclass(jsonObject1.getString("grade"));
                           }
                           if(jsonObject1.isNull("contact")){
                               sdfo.setContacts("");
                           }else {
                               sdfo.setContacts(jsonObject1.getString("contact"));
                           }
                           if(jsonObject1.isNull("school")){
                               sdfo.setSchool("");
                           }else {
                               sdfo.setSchool(jsonObject1.getString("school"));
                           }
                           ssfo.add(sdfo);
                       }


                   }
                   mstudentFragmentAdapter.notifyDataSetChanged();

               }else {
                   message=jsonObject.getString("err");
              Utils.showToast(getActivity(),message);
               }
               //初始化收索框
//               change_choose_type_text.setText("全部");
           }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
