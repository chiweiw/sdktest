package com.netvox.sdk.test.javassist;

import com.netvox.sdk.test.api.APIHolder;
import com.netvox.smarthome.common.api.event.listener.shc.DeviceInfoListener;
import com.netvox.smarthome.common.api.model.dev.Device;

public class DeviceListTest extends TestTemplate implements DeviceInfoListener {

	public void onDeviceInfoBack(String paramString, Device paramDevice) {
		fireEvent(paramString);
	}

	public void sendRequest(String paramString) {
		APIHolder.getInstance().Arm(paramString);
	}

}
