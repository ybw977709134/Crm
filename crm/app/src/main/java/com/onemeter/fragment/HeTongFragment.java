package com.onemeter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.activity.PrintPreviewActivity;
import com.onemeter.adapter.StudentHeTongFragmentAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.StudentHetongInfo;
import com.onemeter.net.httpUtils;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：合同(打印)页面
 * 项目名称：CrmWei
 * 时间：2016/5/6 13:45
 * 备注：
 */
public class HeTongFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    View view;
    private XListView hetong_list;
    private StudentHeTongFragmentAdapter mStudentHeTongFragmentAdapter;
    public List<StudentHetongInfo> sshtfo;
    Handler  mHandler;
    private  int page=1;//第几页
    private  int pageSize=10;//单页数据条数
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hetong_main, container, false);
        initView();
        initgetNetHeTongData(page);
        return view;
    }




    /**
     * 初始化组件
     */
    private void initView() {
        mHandler=new Handler();
        sshtfo = new ArrayList<StudentHetongInfo>();
        hetong_list= (XListView) view.findViewById(R.id.hetong_list);
        hetong_list.setPullRefreshEnable(true);
        hetong_list.setPullLoadEnable(true);
        hetong_list.setXListViewListener(this);
        mStudentHeTongFragmentAdapter=new StudentHeTongFragmentAdapter(getActivity(),sshtfo);
        hetong_list.setAdapter(mStudentHeTongFragmentAdapter);
        hetong_list.setOnItemClickListener(this);

    }

    /**
     * 提交请求获取合同信息
     */
    private void initgetNetHeTongData(int page) {
        //提交的数据请求部分
        try {
            String apiurl = Constants.SERVER_UL+"/api/queryContracts";
            String uid  = MyApplication.userInfos.get(0).getUid()+"";
            String privateKey =MyApplication.userInfos.get(0).getToken();
            Map<String, String> data = new HashMap<String, String>();
//          data.put("uid", MyApplication.userInfos.get(0).getUid()+"");
            data.put("studentId", MyApplication.studentId.get(0).toString());
            data.put("page", page + "");
            data.put("pageSize", pageSize + "");
            new httpUtils(getActivity()).getData(apiurl, uid, privateKey, data, HeTongFragment.this,"获取合同信息");
        } catch (Exception e) {
            e.printStackTrace();

        }
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
            /***************************************************************************/
            if(action.equals("获取合同信息")){
                if(code.equals("10000")){
                    JSONArray jsonArray = jsonObject.getJSONArray("datas");
                    if (jsonArray.length() == 0) {
                        return;
                    } else{
                        MyApplication.hetongInfo.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            StudentHetongInfo  hetongInfo=new StudentHetongInfo();
                            JSONObject  jsonObject1=jsonArray.getJSONObject(i);
                            if(jsonObject1.isNull("serial")){
                                  hetongInfo.setHeTongNum("");
                            }else {
                                  hetongInfo.setHeTongNum(jsonObject1.getString("serial"));
                                Log.e("serial",jsonObject1.getString("serial"));
                            }

                            if(jsonObject1.isNull("total")){
                                hetongInfo.setTotal("");
                            }else {
                                hetongInfo.setTotal(jsonObject1.getString("total"));
                            }

                            if(jsonObject1.isNull("refund")){
                                hetongInfo.setRefundMoney("");
                            }else {
                                hetongInfo.setRefundMoney(jsonObject1.getString("refund"));
                            }

                            if(jsonObject1.isNull("name")){
                                hetongInfo.setCourseName("");
                            }else {
                                hetongInfo.setCourseName(jsonObject1.getString("name"));
                            }

                            if(jsonObject1.isNull("classHours")){
                                hetongInfo.setLessonNum("");
                            }else {
                                hetongInfo.setLessonNum(jsonObject1.getString("classHours"));
                            }

                            if(jsonObject1.isNull("type")){
                                hetongInfo.setClassName("");
                            }else {
                                hetongInfo.setClassName(jsonObject1.getString("type"));
                            }
                            sshtfo.add(hetongInfo);
                            MyApplication.hetongInfo.add(hetongInfo);
                        }
                }
                  mStudentHeTongFragmentAdapter.notifyDataSetChanged();
                }else {
                    message=jsonObject.getString("err");
                    Utils.showToast(getActivity(), message);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sshtfo.clear();
                page=1;
                initgetNetHeTongData(page);
                hetong_list.stopRefresh();
            }
        },900);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                initgetNetHeTongData(page);
                hetong_list.stopLoadMore();
            }
        },900);
    }


    /**
     * 点击item打印信息
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(MyApplication.device_adress.size()==0){
            Toast.makeText(getActivity(), "请先设置打印机",Toast.LENGTH_SHORT).show();
            return;
        }
        MyApplication.hetongInfo.clear();
        String   bianhao=  sshtfo.get(position-1).getHeTongNum();
        for(int i=0;i<sshtfo.size();i++){
            if(sshtfo.get(i).getHeTongNum().equals(bianhao)){
               MyApplication.hetongInfo.add(sshtfo.get(i));
            }
        }
        Intent   intent=new Intent(getActivity(), PrintPreviewActivity.class);
        startActivity(intent);

    }
}
