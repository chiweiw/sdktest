package com.netvox.sdk.login;

import com.netvox.smarthome.common.api.API;
import com.netvox.smarthome.common.api.APIImpl;

/**
 * login单例
 * 
 * @author Administrator
 *
 */
public class LoginHolder {

	private static Base loginHolder ;

	private LoginHolder() {
	}

	public static Base getInstance() {
		if(loginHolder==null) {
			loginHolder = new Base();
		}
		return loginHolder;
	}
	
}
