package com.onemeter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.adapter.ClassdetailAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.classDetailInfo;
import com.onemeter.net.httpUtils;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

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

/**班级详情页面
 * Created by G510 on 2016/5/7.
 */
public class ClassDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton  class_detail_img_return;//返回键
    private TextView  class_name;//班级名称
    private ListView  class_detail_list;//班级成员列表组件
    private ClassdetailAdapter mclassdetailAdapter;//班级成员列表适配器
    public List<classDetailInfo> csdfo;//班级成员列表集合
    ProgressDialog prodialog;// 加载进度条对话框
    //分页字段
    private  String page=1+"";//第几页
    private  String pageSize=20+"";//单页数据条数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.class_fragment_detail_layout);
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.RIGHT;//设置对话框右边显示
        win.setAttributes(lp);
        csdfo=new ArrayList<classDetailInfo>();
        prodialog=new ProgressDialog(this);
//        initData();
        initView();
        getNetPostClassDetailData();

    }

    /**
     * 请求班级详情数据信息
     */
    private void getNetPostClassDetailData() {
        csdfo.clear();
        mclassdetailAdapter.notifyDataSetChanged();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        //提交的数据请求部分
        try {
            String apiurl = Constants.SERVER_UL+"api/queryClassStudent";
            String userid  = MyApplication.userInfos.get(0).getUid()+"";
            String privateKey =MyApplication.userInfos.get(0).getToken();
            Map<String, String> data = new HashMap<String, String>();
            data.put("classId", MyApplication.studentId.get(0).toString());
            data.put("page",page);
            data.put("pageSize",pageSize);
            new httpUtils(this).getData(apiurl, userid, privateKey, data, ClassDetailActivity.this,"获取班级详情信息");

        } catch (IOException e) {
            if (prodialog != null && prodialog.isShowing()) {
                prodialog.dismiss();
                Log.i("Onemeter", "关闭prodialog");
            }
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }



    }


    /**
     * 初始化组件
     */
    private void initView() {
        class_detail_list= (ListView) findViewById(R.id.class_detail_list);
        mclassdetailAdapter=new ClassdetailAdapter(getApplicationContext(),csdfo);
        class_detail_list.setAdapter(mclassdetailAdapter);
        class_detail_img_return= (ImageButton) findViewById(R.id.class_detail_img_return);
        class_name= (TextView) findViewById(R.id.class_name);
        class_name.setText(MyApplication.studentName.get(0).toString());


        class_detail_img_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.class_detail_img_return:
                //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
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
            code = jsonObject.getString("code");
//            Utils.showToast(this, code + "");
            /***************************************************************************/
            if(action.equals("获取班级详情信息")){
                if (prodialog != null && prodialog.isShowing()) {
                    prodialog.dismiss();
                    Log.i("Onemeter", "关闭prodialog");
                }

                if(code.equals("10000")){
//                    Utils.showToast(this,"请求成功");
                    JSONArray   jsonArray=jsonObject.getJSONArray("data");
               if(jsonArray.length()==0||jsonArray.getJSONArray(0).length()==0){
                   return;
               }else{
                  for (int i=0;i<jsonArray.getJSONArray(0).length();i++){
                      classDetailInfo  ccif=new classDetailInfo();
                      if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("student_id")){
                          ccif.setStudentId("");
                      }else {
                          ccif.setStudentId(jsonArray.getJSONArray(0).getJSONObject(i).getString("student_id"));
                      }
                      if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("studentName")){
                          ccif.setName("");
                      }else {
                          ccif.setName(jsonArray.getJSONArray(0).getJSONObject(i).getString("studentName"));
                      }
                      if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("totalPrice")){
                          ccif.setTotal("");
                      }else {
                          ccif.setTotal(jsonArray.getJSONArray(0).getJSONObject(i).getString("totalPrice"));
                      }
                      if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("date")){
                          ccif.setCtime("");
                      }else {
                          String  time=jsonArray.getJSONArray(0).getJSONObject(i).getString("date");
                          ccif.setCtime(Utils.parseDate(time));
                      }

                      if(jsonArray.getJSONArray(0).getJSONObject(i).isNull("classHours")){
                          ccif.setHours("");
                      }else {
                          ccif.setHours(jsonArray.getJSONArray(0).getJSONObject(i).getString("classHours"));
                      }
                      csdfo.add(ccif);

                  }

               }
                    mclassdetailAdapter.notifyDataSetChanged();

                }else{
                    message=jsonObject.getString("err");
                    Utils.showToast(this,message);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

}
