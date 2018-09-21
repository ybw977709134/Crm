package com.onemeter.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onemeter.R;
import com.onemeter.adapter.StudentDongTaiFragmentAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.DongtaiInfo;
import com.onemeter.net.httpUtils;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: 联系动态页面</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @param
 * @author john
 * @return
 * @date  2016-06-30
 */
public class DongTaiFragment  extends Fragment implements XListView.IXListViewListener{
    private View view;
    Handler mHandler;
    private XListView dongtai_list;
    private StudentDongTaiFragmentAdapter mStudentDongTaiFragmentAdapter;
    public List<DongtaiInfo> dInfo;
    private  int page=1;//第几页
    private  int pageSize=10;//单页数据条数
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dongtai_main, container, false);
        initView();
        initgetNetDongTaiData(page);
        return view;
    }

    private void initView() {
        mHandler=new Handler();
        dInfo = new ArrayList<DongtaiInfo>();
        dongtai_list= (XListView) view.findViewById(R.id.dongtai_list);
        dongtai_list.setPullRefreshEnable(true);
        dongtai_list.setPullLoadEnable(true);
        dongtai_list.setXListViewListener(this);
        mStudentDongTaiFragmentAdapter = new StudentDongTaiFragmentAdapter(getActivity(), dInfo);
        dongtai_list.setAdapter(mStudentDongTaiFragmentAdapter);
    }

    /**
     * 提交请求获取联系动态信息
     */
    private void initgetNetDongTaiData(int page) {
        //提交的数据请求部分
        try {
            String apiurl = Constants.SERVER_UL+"/api/queryContacts";
            String uid  = MyApplication.userInfos.get(0).getUid()+"";
            String privateKey =MyApplication.userInfos.get(0).getToken();
            Map<String, String> data = new HashMap<String, String>();
            data.put("uid", MyApplication.userInfos.get(0).getUid()+"");
            data.put("studentId", MyApplication.studentId.get(0).toString());
            data.put("page", page + "");
            data.put("pageSize", pageSize + "");
            new httpUtils(getActivity()).getData(apiurl, uid, privateKey, data, DongTaiFragment.this,"获取联系动态信息");

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
            Log.e("result", result);
            code = jsonObject.getString("code");
//            Utils.showToast(getActivity(), code + "");
            /***************************************************************************/
            if(action.equals("获取联系动态信息")) {
                if (code.equals("10000")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("datas");
                    if (jsonArray.length() == 0) {
                        Utils.showToast(getActivity(),"没有更多数据");
                        return;
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            DongtaiInfo sdf = new DongtaiInfo();
                            JSONObject  jsonObject1=jsonArray.getJSONObject(i);
                            if (jsonObject1.isNull("create_date")) {
                                sdf.setDongtaiDate("");
                            } else {
                                sdf.setDongtaiDate(jsonObject1.getString("create_date"));
                            }
                            if (jsonObject1.isNull("way")) {
                                sdf.setDongtaiAction("");
                            } else {
                                sdf.setDongtaiAction(jsonObject1.getString("way"));
                            }

                            if (jsonObject1.isNull("comment")) {
                                sdf.setDongtaiContext("");
                            } else {
                                sdf.setDongtaiContext(jsonObject1.getString("comment"));
                            }
                            if (jsonObject1.isNull("nickname")) {
                                sdf.setDongtaiSalesman("");
                            } else {
                                sdf.setDongtaiSalesman(jsonObject1.getString("nickname"));
                            }
                            dInfo.add(sdf);

                        }
                    }
                    mStudentDongTaiFragmentAdapter.notifyDataSetChanged();
                    }else{
                        message = jsonObject.getString("err");
                        Utils.showToast(getActivity(), message);
                    }

                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dInfo.clear();
                page=1;
                initgetNetDongTaiData(page);
                dongtai_list.stopRefresh();
            }
        }, 900);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                initgetNetDongTaiData(page);
                dongtai_list.stopLoadMore();
            }
        }, 900);
    }
}
