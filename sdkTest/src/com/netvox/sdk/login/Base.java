package com.netvox.sdk.login;

import java.util.ArrayList;

import com.netvox.sdk.api.APIHolder;
import com.netvox.sdk.gui.SdkGui;
import com.netvox.sdk.utils.Consts;
import com.netvox.smarthome.common.api.API;
import com.netvox.smarthome.common.api.config.Config;
import com.netvox.smarthome.common.api.event.listener.cloud.OnHouseListListener;
import com.netvox.smarthome.common.api.event.listener.cloud.OnMQTTConnectListener;
import com.netvox.smarthome.common.api.event.listener.shc.RoomListListener;
import com.netvox.smarthome.common.api.model.cloud.HouseInfo;
import com.netvox.smarthome.common.api.model.cloud.MQTTConnectResult;
import com.netvox.smarthome.common.api.model.shc.RoomInfo;

/**
 * 基础类，所有其他都需继承该类,使用前请设置用户名和密码
 * 
 * @author Administrator
 *
 */
public class Base implements OnHouseListListener, OnMQTTConnectListener, RoomListListener {

	private API api = APIHolder.getInstance();


	
	// 登录并获取账号下所有网关
	public void execute() {

		//// 初始化
		api.Init();
		// 添加监听
		api.AddListener(this);
		// 设置用户名和密码
		// Config.getConfig().setUserName(consts.getUSER_NAME());
		// Config.getConfig().setPassWord(consts.getPASSWORD());
		// 获取用户账号下所有网关
//		 private String cloudMngIp = "mng.netvoxcloud.com";
		api.GetAllHomes(1, 0);
	}

	// 连接mqtt，外部调用触发
	public void mqttConnect() {
		api.StartChannel();// Mqtt通道 连接
		api.SwitchHome();
	}

	@Override
	public void onRoomListBack(String arg0, ArrayList<RoomInfo> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMQTTConnect(String seq, MQTTConnectResult result) {
		// TODO Auto-generated method stub
		if (result != null) { // MQTT连接成功
			if (result.getResult() == MQTTConnectResult.CONNECT_SUCCESS) {

				System.out.println(result);

				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						api.GetRoomList();
					}
				}.start();

			}
		}

	}

	@Override
	public void onHouseListBack(String seq, ArrayList<HouseInfo> houses) {
		// TODO Auto-generated method stub
		System.out.println(houses);

		SdkGui frame = SdkGui.getInstance();
		//frame.getReturnValue().setText("22222222222222\n22222222222");

		Consts.getConsts().setHouses(houses);


	}
}
