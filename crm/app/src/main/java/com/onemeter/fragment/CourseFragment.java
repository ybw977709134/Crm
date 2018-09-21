package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.onemeter.R;
import com.onemeter.activity.CoursePicDetailActivity;
import com.onemeter.adapter.CourseListAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.new_courseInfo;
import com.onemeter.net.httpUtils;
import com.onemeter.utils.Constants;
import com.onemeter.utils.GsonUtil;
import com.onemeter.utils.Utils;

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
 * 描述：课程页面
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/4 20:29
 * 备注：
 */
public class CourseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    View view;
    private PopupWindow logout_popupwindow;//退出选择弹窗
    private EditText  course_soso_et;//搜索框
    private ImageButton course_btn_sousuo;//搜索按钮
    private Button  btn_user_logout;//退出登录
    private GridView  course_list;//课程列表

    private CourseListAdapter  mCourseListAdapter;
    public List<new_courseInfo>  new_courseList;
    new_courseInfo   fo;//实体类
    private Button  btn_logout;//退出
    Intent  intent;
    ProgressDialog prodialog;// 加载进度条对话框
    //分页字段
    private  int page=1;//第几页
    private  int pageSize=30;//单页数据条数
    Handler mHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_main, container, false);
        new_courseList=new ArrayList<new_courseInfo>();
        prodialog=new ProgressDialog(getActivity());
        mHandler=new Handler();
        initView();
        prodialog.setMessage("加载中");
        prodialog.show();
        getNetCourseListData();
        return view;
    }


    /**
     * 发送请求，获取课程列表信息
     */
    private void getNetCourseListData() {
          new_courseList.clear();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }

        //提交的数据请求部分

            final String apiurl = Constants.SERVER_UL+"api/queryCourses";
            final String userid  = MyApplication.userInfos.get(0).getUid()+"";
            final String privateKey =MyApplication.userInfos.get(0).getToken();
            final Map<String, String> data = new HashMap<String, String>();
            data.put("uid", MyApplication.userInfos.get(0).getUid()+"");
            data.put("page", page + "");
            data.put("keyword", "");
            data.put("pageSize", pageSize + "");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, CourseFragment.this, "获取课程列表信息");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    mHandler.removeCallbacks(this);
                    if (prodialog != null && prodialog.isShowing()) {
                        prodialog.dismiss();
                        Log.i("Onemeter", "关闭prodialog");
                    }
                }

            }
        }, 500);

    }


    /**
     * 初始化组件
     */
    private void initView() {
        course_list= (GridView) view.findViewById(R.id.course_list);
        course_soso_et= (EditText) view.findViewById(R.id.course_soso_et);
        course_btn_sousuo= (ImageButton) view.findViewById(R.id.course_btn_sousuo);
        btn_user_logout= (Button) view.findViewById(R.id.btn_user_logout);
        btn_user_logout.setText(MyApplication.userInfos.get(0).getUsername());
        course_btn_sousuo.setOnClickListener(this);
        btn_user_logout.setOnClickListener(this);
        course_list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.course_btn_sousuo:
                //搜索
                prodialog.setMessage("加载中");
                prodialog.show();
                getNetSOSOCourseData();
                Utils.HideKeyboard(view);
                break;
            case R.id.btn_user_logout:
                //退出登录
                if (logout_popupwindow != null&&logout_popupwindow.isShowing()) {
                    logout_popupwindow.dismiss();
                    return;
                } else {
                    initLogoutPopupWindowView(btn_user_logout);
                }
                break;
            case R.id.btn_logout:
                //退出登录
                getLogout();
                logout_popupwindow.dismiss();
                break;
        }
    }

    /**
     * 根据搜索内容提交请求课程信息
     */
    private void getNetSOSOCourseData() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        //提交的数据请求部分

            final String apiurl = Constants.SERVER_UL+"api/queryCourses";
            final String userid  = MyApplication.userInfos.get(0).getUid()+"";
            final String privateKey =MyApplication.userInfos.get(0).getToken();
            final Map<String, String> data = new HashMap<String, String>();
            data.put("uid", MyApplication.userInfos.get(0).getUid()+"");
            data.put("page",page+"");
            data.put("type",1+"");
            data.put("keyword",course_soso_et.getText().toString());
            data.put("pageSize",pageSize+"");

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new_courseList.clear();
                    mCourseListAdapter.notifyDataSetChanged();
                    new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, CourseFragment.this,"获取课程列表信息");

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
        },100);

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int  num=0;
             num=new_courseList.get(0).getDatas().get(position).getImgs().size();
        if(num>0) {
             MyApplication.imgurl = new String[num];
            for (int i = 0; i < num; i++) {
                MyApplication.imgurl [i] =new_courseList.get(0).getDatas().get(position).getImgs().get(i).getSrc();
            }
            intent = new Intent(getActivity(), CoursePicDetailActivity.class);
            startActivity(intent);
        }else{
            Utils.showToast(getActivity(),"没有图片");
        }

    }


    /**
     * 提交请求此方法更新数据
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
            /***************************************************************************/
           if(action.equals("获取课程列表信息")){
               if (prodialog != null && prodialog.isShowing()) {
                   prodialog.dismiss();
                   Log.i("Onemeter", "关闭prodialog");
               }

              if(code.equals("10000")){
                  fo= (new_courseInfo) GsonUtil.convertJson2Object(result, new_courseInfo.class, GsonUtil.JSON_JAVABEAN);
                  if(fo!=null){
                      new_courseList.add(fo);
                      mCourseListAdapter=new CourseListAdapter(getActivity(),new_courseList.get(0).getDatas());
                      course_list.setAdapter(mCourseListAdapter);
                      mCourseListAdapter.notifyDataSetChanged();

                  }

              }else{
                  message=jsonObject.getString("err");
                  Utils.showToast(getActivity(),message);
              }

           }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
