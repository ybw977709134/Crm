package com.onemeter.dayin;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.service.PrintDataService;
import com.onemeter.utils.Utils;


public class PrintDataAction implements OnClickListener {
    private Context context = null;
    private TextView deviceName = null;
    private TextView connectState = null;
    private EditText printData = null;
    private String deviceAddress = null;
    private PrintDataService printDataService = null;
    private Activity activity = null;

    public PrintDataAction(Context context, String deviceAddress,
                           TextView deviceName, TextView connectState, Activity activity) {
        super();
        this.context = context;
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.connectState = connectState;
        this.activity = activity;
        this.printDataService = new PrintDataService(this.context,
                this.deviceAddress);
        this.initView();

    }

    private void initView() {
        // 设置当前设备名称    
        this.deviceName.setText(this.printDataService.getDeviceName());
        // 一上来就先连接蓝牙设备    
        boolean flag = this.printDataService.connect();
        if (flag == false) {
            // 连接失败    
            this.connectState.setText("连接失败！");
        } else {
            // 连接成功    
            this.connectState.setText("连接成功！");

        }
    }

    public void setPrintData(EditText printData) {
        this.printData = printData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.print_data_img_return://返回键
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.send://打印按钮
                if(deviceAddress.length()==0||deviceAddress==null){
                    Utils.showToast(context,"请先设置打印机");
                    return;
                }
                String sendData = this.printData.getText().toString();
                this.printDataService.send(sendData + "\n");
                break;
            case R.id.command://指令
                this.printDataService.selectCommand();
                break;
        }

    }
}