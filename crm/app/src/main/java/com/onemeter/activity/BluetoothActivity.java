package com.onemeter.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.dayin.BluetoothAction;

/**
 * 初始化设置打印机页面
 */
public class BluetoothActivity extends BaseActivity {
	private Context context = null;
	private  ListView unbondDevices;//未绑定的蓝牙设备
	private  ListView bondDevices;//已绑定的蓝牙设备
	private  Button switchBT;//打开蓝牙设备开关
	private  Button searchDevices;//收索蓝牙设备
    private  ImageButton returnButton;//返回键

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.bluetooth_layout);
		initView();
	}

	private void initView() {
		unbondDevices = (ListView) this.findViewById(R.id.unbondDevices);
		bondDevices = (ListView) this.findViewById(R.id.bondDevices);
		switchBT= (Button) this.findViewById(R.id.openBluetooth_tb);
		searchDevices= (Button) this.findViewById(R.id.searchDevices);

		BluetoothAction bluetoothAction = new BluetoothAction(this.context, unbondDevices, bondDevices, switchBT, searchDevices, BluetoothActivity.this);
		returnButton= (ImageButton) this.findViewById(R.id.return_Bluetooth_btn);
		bluetoothAction.setSearchDevices(searchDevices);
		bluetoothAction.initView();

		switchBT.setOnClickListener(bluetoothAction);
		searchDevices.setOnClickListener(bluetoothAction);
		returnButton.setOnClickListener(bluetoothAction);
	}



	//屏蔽返回键的代码:
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_BACK:return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
