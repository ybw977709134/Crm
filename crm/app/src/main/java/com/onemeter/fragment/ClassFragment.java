package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.activity.ClassDetailActivity;
import com.onemeter.adapter.classlistAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.ClasslistInfo;
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
 * 描述：班级页面
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/4 20:32
 * 备注：
 */

public class ClassFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener {
    View view;
    private PopupWindow logout_popupwindow;//退出选择弹窗
    private EditText  class_soso_et;//收索框
    private ImageButton class_btn_sousuo;//收索按钮
    private TextView   class_left_course_type_all;//全部
    private Button   btn_user_logout;//退出登录
    private ListView  class_left_course_type_listview;//课程分类列表组件
    private XListView  class_list;//班级列表组件
    private CourseTypeAdapter  mCourseTypeAdapter;
    private List<String>  mcoursetypeList;//课程类别名称集合
    private List<String>  mcoursetypeIdList;//课程类别课程ID集合

    private classlistAdapter  mclasslistAdapter;
    private List<ClasslistInfo>  mClasslistInfoList;//班级列表集合
    Intent  intent;
    private Button  btn_logout;

    ProgressDialog prodialog;// 加载进度条对话框
    //分页字段
    private  int page=1;//第几页
    private  int pageSize=20;//单页数据条数

    Handler  mHandler;
    private  static  String  TypePush="正常列表";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class_main, container, false);
        mcoursetypeList=new ArrayList<String>();
        mcoursetypeIdList=new ArrayList<String>();
        mClasslistInfoList=new ArrayList<ClasslistInfo>();
        mHandler=new Handler();
        prodialog=new ProgressDialog(getActivity());
        initView();
        prodialog.setMessage("加载中");
        prodialog.show();
        getNetPostClassListData(page);
        return view;
    }



    /**
     * 提交请求获取班级列表信息
     */
    private void getNetPostClassListData(int page) {
         mClasslistInfoList.clear();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        //提交的数据请求部分

            final String apiurl = Constants.SERVER_UL+"api/queryClasses";
            final String userid  = MyApplication.userInfos.get(0).getUid()+"";
            final String privateKey =MyApplication.userInfos.get(0).getToken();
            final Map<String, String> data = new HashMap<String, String>();
            data.put("uid", MyApplication.userInfos.get(0).getUid() + "");
            data.put("page", page + "");
            data.put("keyword", "");
                data.put("nav", 1 + "");
            data.put("pageSize", pageSize + "");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, ClassFragment.this, "获取班级列表信息");

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
        }, 200);

    }


    /**
     * 初始化组件
     */
    private void initView() {
        class_left_course_type_all= (TextView) view.findViewById(R.id.class_left_course_type_all);
        class_left_course_type_listview= (ListView) view.findViewById(R.id.class_left_course_type_listview);
        mCourseTypeAdapter=new CourseTypeAdapter(getActivity());
        class_left_course_type_listview.setAdapter(mCourseTypeAdapter);

        class_list= (XListView) view.findViewById(R.id.class_list);
        mclasslistAdapter=new classlistAdapter(getActivity(),mClasslistInfoList);
        class_list.setAdapter(mclasslistAdapter);
        class_list.setPullLoadEnable(true);
        class_list.setPullRefreshEnable(true);
        class_list.setXListViewListener(this);

        class_soso_et= (EditText) view.findViewById(R.id.class_soso_et);
        class_btn_sousuo= (ImageButton) view.findViewById(R.id.class_btn_sousuo);
        btn_user_logout= (Button) view.findViewById(R.id.btn_user_logout);
        btn_user_logout.setText(MyApplication.userInfos.get(0).getUsername());

        class_btn_sousuo.setOnClickListener(this);
        btn_user_logout.setOnClickListener(this);
        class_left_course_type_all.setOnClickListener(this);

        class_left_course_type_listview.setOnItemClickListener(this);
        class_list.setOnItemClickListener(this);
        class_left_course_type_listview.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mCourseTypeAdapter.changeLeftSelected(arg2);//刷新

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            // 模拟加载数据，1s之后停止加载
            @Override
            public void run() {
                page = 1;
                if (TypePush.equals("正常列表")) {
                    getNetPostClassListData(page);
                } else if (TypePush.equals("搜索列表")) {
                    getNetNewClassListData(MyApplication.courser_id);
                }
                class_list.stopRefresh();
            }
        }, 900);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            // 模拟加载数据，1s之后停止加载
            @Override
            public void run() {
                class_list.stopLoadMore();
            }
        }, 100);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_user_logout:
                //退出登录
                if (logout_popupwindow != null&&logout_popupwindow.isShowing()) {
                    logout_popupwindow.dismiss();
                    return;
                } else {
                    initLogoutPopupWindowView(btn_user_logout);
                }
                break;
            case R.id.class_btn_sousuo:
                //搜索按钮
                Utils.showToast(getActivity(),"搜索");
                break;
            case R.id.btn_logout:
                //退出登录
                getLogout();
                logout_popupwindow.dismiss();
                break;
            case R.id.class_left_course_type_all:
                //全部
                prodialog.setMessage("加载中");
                prodialog.show();
                TypePush="正常列表";
                mClasslistInfoList.clear();
                page = 1;
                getNetPostClassListData(page);

                break;

        }
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if(parent==class_list){
            MyApplication.studentId.clear();
            MyApplication.studentName.clear();
            MyApplication.studentId.add(mClasslistInfoList.get(position-1).getClassId());
            MyApplication.studentName.add(mClasslistInfoList.get(position-1).getName());

//            Utils.showToast(getActivity(),"进入详细页面");
            intent = new Intent(getActivity(), ClassDetailActivity.class);
            startActivity(intent);

        }else{
                    mCourseTypeAdapter.changeLeftSelected(position);//刷新
                    String name = mcoursetypeIdList.get(position).toString();
                    TypePush="搜索列表";
                    prodialog.setMessage("加载中");
                    prodialog.show();
                    MyApplication.courser_id=name;
                    getNetNewClassListData(name);
        }

    }

    /**
     * 根据课程名称获取班级列表信息
     */
    private void getNetNewClassListData(String name) {
        mClasslistInfoList.clear();
        mclasslistAdapter.notifyDataSetChanged();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }

        //提交的数据请求部分

            final String apiurl = Constants.SERVER_UL+"api/queryClasses";
            final String userid  = MyApplication.userInfos.get(0).getUid()+"";
            final String privateKey =MyApplication.userInfos.get(0).getToken();
            final Map<String, String> data = new HashMap<String, String>();
            data.put("uid", MyApplication.userInfos.get(0).getUid()+"");
            data.put("page",page+"");
            data.put("keyword",name);
            data.put("pageSize",pageSize+"");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    new httpUtils(getActivity()).getData(apiurl, userid, privateKey, data, ClassFragment.this,"更新班级列表信息");
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
        },200);

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
            Log.e("result",result);
            code = jsonObject.getString("code");
//            Utils.showToast(getActivity(), code + "");
            /***************************************************************************/
            if(action.equals("获取班级列表信息")){
                if (prodialog != null && prodialog.isShowing()) {
                    prodialog.dismiss();
                    Log.i("Onemeter", "关闭prodialog");
                }
                if(code.equals("10000")){
//                    Utils.showToast(getActivity(),"请求成功");
                    JSONArray  jsonArray=jsonObject.getJSONArray("data");
                    JSONArray  jsonArray1=jsonObject.getJSONArray("nav");

//
                    if(jsonArray.getJSONArray(0).length()==0||jsonArray.length()==0){
                        return;
                    }else {
                    for (int i=0;i<jsonArray.getJSONArray(0).length();i++){
                        ClasslistInfo sdf = new ClasslistInfo();
                       if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("id")){
                           sdf.setClassId("");
                       }else{
                           sdf.setClassId(jsonArray.getJSONArray(0).getJSONObject(i).getString("id"));
                       }
                        if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("name")){
                            sdf.setName("");
                        }else{
                            sdf.setName(jsonArray.getJSONArray(0).getJSONObject(i).getString("name"));
                        }
                        if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("course")){
                            sdf.setCourse("");
                        }else{
                            sdf.setCourse(jsonArray.getJSONArray(0).getJSONObject(i).getString("course"));
                        }
                        if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("type")){
                            sdf.setType("");
                        }else{
                            sdf.setType(jsonArray.getJSONArray(0).getJSONObject(i).getString("type"));
                        }
                        if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("length")){
                            sdf.setMaxStudents("");
                        }else{
                            sdf.setMaxStudents(jsonArray.getJSONArray(0).getJSONObject(i).getString("length"));
                        }
                        if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("attend")){
                            sdf.setPurchasedNum("");
                        } else {
                            sdf.setPurchasedNum(jsonArray.getJSONArray(0).getJSONObject(i).getString("attend"));
                        }
                        if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("status")){
                            sdf.setStatus("");
                        }else{
                            sdf.setStatus(jsonArray.getJSONArray(0).getJSONObject(i).getString("status"));
                        }
                        if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("startdate")){
                            sdf.setStartdate("");
                        }else{
                            String  time=jsonArray.getJSONArray(0).getJSONObject(i).getString("startdate");
                            sdf.setStartdate(Utils.parseDate(time));
                        }
                        mClasslistInfoList.add(sdf);
                    }
                    }
                    /******************************/
                    mcoursetypeList.clear();
                    mcoursetypeIdList.clear();
                    for (int j=0;j<jsonArray1.length();j++){

                        if(jsonArray1.getJSONObject(j).isNull("name")){
                        }else {
                             String courseId=jsonArray1.getJSONObject(j).getString("course_id");
                            String coursename=jsonArray1.getJSONObject(j).getString("name");
                            if(mcoursetypeList.contains(coursename)){
                            }else{
                                mcoursetypeList.add(coursename);
                            }

                            if(mcoursetypeIdList.contains(courseId)){
                            }else{
                                mcoursetypeIdList.add(courseId);
                            }
                        }
                }
                    mCourseTypeAdapter.notifyDataSetChanged();
                    mclasslistAdapter.notifyDataSetChanged();

                }else{
                    message=jsonObject.getString("err");
                    Utils.showToast(getActivity(),message);
                }

            }
           /************************************************************************/
            if(action.equals("更新班级列表信息")){
                if (prodialog != null && prodialog.isShowing()) {
                    prodialog.dismiss();
                    Log.i("Onemeter", "关闭prodialog");
                }
                if(code.equals("10000")){
//                    Utils.showToast(getActivity(),result);
                    JSONArray  jsonArray=jsonObject.getJSONArray("data");
                    if(jsonArray.getJSONArray(0).length()==0||jsonArray.length()==0){
                        return;
                    }else {
                        for (int i=0;i<jsonArray.getJSONArray(0).length();i++){
                            ClasslistInfo sdf = new ClasslistInfo();
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("id")){
                                sdf.setClassId("");
                            }else{
                                sdf.setClassId(jsonArray.getJSONArray(0).getJSONObject(i).getString("id"));
                            }
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("name")){
                                sdf.setName("");
                            }else{
                                sdf.setName(jsonArray.getJSONArray(0).getJSONObject(i).getString("name"));
                            }
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("course")){
                                sdf.setCourse("");
                            }else{
                                sdf.setCourse(jsonArray.getJSONArray(0).getJSONObject(i).getString("course"));
                            }
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("type")){
                                sdf.setType("");
                            }else{
                                sdf.setType(jsonArray.getJSONArray(0).getJSONObject(i).getString("type"));
                            }
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("length")){
                                sdf.setMaxStudents("");
                            }else{
                                sdf.setMaxStudents(jsonArray.getJSONArray(0).getJSONObject(i).getString("length"));
                            }
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("attend")){
                                sdf.setPurchasedNum("");
                            } else {
                                sdf.setPurchasedNum(jsonArray.getJSONArray(0).getJSONObject(i).getString("attend"));
                            }
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("status")){
                                sdf.setStatus("");
                            }else{
                                sdf.setStatus(jsonArray.getJSONArray(0).getJSONObject(i).getString("status"));
                            }
                            if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("startdate")){
                                sdf.setStartdate("");
                            }else{
                                String  time=jsonArray.getJSONArray(0).getJSONObject(i).getString("startdate");
                                sdf.setStartdate(Utils.parseDate(time));
                            }
                            mClasslistInfoList.add(sdf);
                        }
                    }

                    mclasslistAdapter.notifyDataSetChanged();

                }else{
                    message=jsonObject.getString("err");
                    Utils.showToast(getActivity(),message);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * 课程类型数据源适配器 Created by G510 on 2016/5/7.
     */
    public class CourseTypeAdapter extends BaseAdapter {
        Context mContext;
        int mSelect = -1;   //选中项

        public CourseTypeAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public void changeLeftSelected(int positon){ //刷新方法
            if(positon != mSelect){
                mSelect = positon;
                notifyDataSetChanged();
            }
        }
        @Override
        public int getCount() {
            if (mcoursetypeList == null) {
                return 0;
            } else {
                return mcoursetypeList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mcoursetypeList == null) {
                return null;
            } else {
                return mcoursetypeList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.class_fragment_course_type_listview_item_layout,
                        null);
                holder.class_left_course_type_text = (TextView) convertView
                        .findViewById(R.id.class_left_course_type_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置数据

            holder.class_left_course_type_text.setText(mcoursetypeList.get(position)
                    .toString());
//            if(mSelect==position){
//                convertView.setBackgroundColor(Color.parseColor("#E4F2F9"));  //选中项背景
//                holder.class_left_course_type_text.setTextColor(Color.parseColor("#00AEFF"));
//                class_left_course_type_all.setTextColor(Color.parseColor("#949FB5"));
//                class_left_course_type_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            }else{
//                convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));  //其他项背景
//                holder.class_left_course_type_text.setTextColor(Color.parseColor("#949FB5"));
//            }
            return convertView;
        }

        /** 存放控件 */

        public class ViewHolder {
            private TextView class_left_course_type_text;// 分类课程的名称

        }
    }


}
