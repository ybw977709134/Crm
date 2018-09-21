package com.onemeter.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.dayin.PrintDataAction;
import com.onemeter.service.PrintDataService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：打印预览页面
 * 项目名称：crm
 * 时间：2016/7/5 9:58
 * 备注：
 */
public class PrintPreviewActivity extends BaseActivity{
    private Context context = null;
    private ImageButton   print_data_img_return;//返回键
    private Button   print_data_action_setting;//设置打印设备
    private Button   print_data_action_start;//打印按钮
    private EditText  print_data_text;//打印的信息
    private TextView  device_name;//设备名称
    private TextView  connect_state;//连接状态
    private Button   command;//指令
    private  String  text="";
    Date date=new Date();
    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time;
    double  total=0.00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_print_preview_main);
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.RIGHT;//设置对话框右边显示
        win.setAttributes(lp);
        this.context = this;
        time =format.format(date);
      initView();
    }


    /**
     * 初始化组件
     */

    private void initView() {
        command= (Button) this.findViewById(R.id.command);
        device_name= (TextView)this.findViewById(R.id.device_name);
        connect_state= (TextView)this. findViewById(R.id.connect_state);
        print_data_img_return= (ImageButton)this.findViewById(R.id.print_data_img_return);
        print_data_action_setting= (Button)this.findViewById(R.id.print_data_action_setting);
        print_data_action_start= (Button)this.findViewById(R.id.send);
        print_data_text= (EditText)this.findViewById(R.id.print_data_text);
        /****************************************合同打印排版****************************************************/
         text="         合同明细\n\n"+"学员：      "+MyApplication.studentName.get(0).toString()+"\n时间：      "+time+"\n\n";
        //------------------------------------------------------------------------------------------------------------------------
        StringBuffer sb = new StringBuffer(text);
        for(int i=0;i<MyApplication.hetongInfo.size();i++){
            total+=Double.valueOf(MyApplication.hetongInfo.get(i).getTotal()).doubleValue();
            String txt= "合同编号："+MyApplication.hetongInfo.get(i).getHeTongNum()+"\n"+"课程名称："+MyApplication.hetongInfo.get(i).getCourseName()
                    +"\n"+"班级名称："+MyApplication.hetongInfo.get(i).getClassName()+"\n"+"课时时长："+MyApplication.hetongInfo.get(i).getLessonNum()
                    +"\n"+"总价："+MyApplication.hetongInfo.get(i).getTotal()+"\n"+"退款："+MyApplication.hetongInfo.get(i).getRefundMoney();
            sb.append(txt);
            if(i!=MyApplication.hetongInfo.size()-1){
                sb.append("\n\n\n");
            }
        }
//--------------------------------------------------------------------------------------------------------------------------------------
         String  dibutext="\n************************\n             总计"+total+"\n\n             确认__________";
         sb.append(dibutext);
        print_data_text.setText(sb.toString());
        //初始化打印设备功能
        if(MyApplication.device_adress.size()!=0) {
            PrintDataAction printDataAction = new PrintDataAction(this.context, MyApplication.device_adress.get(0).toString(), device_name, connect_state,PrintPreviewActivity.this);
            printDataAction.setPrintData(print_data_text);
            print_data_action_start.setOnClickListener(printDataAction);
            command.setOnClickListener(printDataAction);
            print_data_img_return.setOnClickListener(printDataAction);

        }


    }



    @Override
    protected void onDestroy() {
        if(MyApplication.device_adress.size()!=0){
            PrintDataService.disconnect();
        }
        super.onDestroy();
    }
}
