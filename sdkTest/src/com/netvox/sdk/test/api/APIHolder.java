package com.netvox.sdk.test.api;

import com.netvox.smarthome.common.api.API;
import com.netvox.smarthome.common.api.APIImpl;

/***
 * API单例
 * 
 * @author Administrator
 *
 */
public class APIHolder {
	private static API apiHolder = APIImpl.GetInstance();

	private APIHolder() {
	}

	public static API getInstance() {
		return apiHolder;
	}
}
