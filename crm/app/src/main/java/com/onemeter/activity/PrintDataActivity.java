package com.onemeter.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.dayin.PrintDataAction;
import com.onemeter.service.PrintDataService;

/**
 * 打印页面
 */
public class PrintDataActivity extends BaseActivity {
    private Context context = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("蓝牙打印");
        this.setContentView(R.layout.printdata_layout);
        this.context = this;
        this.initListener();
    }

    private void initListener() {
        TextView deviceName = (TextView) this.findViewById(R.id.device_name);
        TextView connectState = (TextView) this.findViewById(R.id.connect_state);
        PrintDataAction printDataAction = new PrintDataAction(this.context, MyApplication.device_adress.get(0).toString(), deviceName, connectState,PrintDataActivity.this);
        EditText printData = (EditText) this.findViewById(R.id.print_data);
        String   text= "合同编号："+MyApplication.hetongInfo.get(0).getHeTongNum()+"\n"+"课程名称："+MyApplication.hetongInfo.get(0).getCourseName()
                +"\n"+"班级名称："+MyApplication.hetongInfo.get(0).getClassName()+"\n"+"课时时长："+MyApplication.hetongInfo.get(0).getLessonNum()
                +"\n"+"总价："+MyApplication.hetongInfo.get(0).getTotal()+"\n"+"退款："+MyApplication.hetongInfo.get(0).getRefundMoney();
        printData.setText(text);
        Button send = (Button) this.findViewById(R.id.send);
        Button command = (Button) this.findViewById(R.id.command);
        printDataAction.setPrintData(printData);

        send.setOnClickListener(printDataAction);
        command.setOnClickListener(printDataAction);
    }


    @Override
    protected void onDestroy() {
        PrintDataService.disconnect();
        super.onDestroy();
    }

}  