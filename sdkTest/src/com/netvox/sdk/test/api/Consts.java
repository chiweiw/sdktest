package com.netvox.sdk.test.api;

import java.util.List;

import com.netvox.smarthome.common.api.model.cloud.HouseInfo;

public class Consts {
	// 用户绑定的手机号
	private String USER_NAME = "13215929890";

	// 密码
	private String PASSWORD = "123456";

	private String ipAddress = "192.168.1.1";

	private List<HouseInfo> houses;

	private Object attr;

	private static Consts consts;

	private Consts() {}

	public static Consts getConsts() {
		if (consts == null) {
			consts = new Consts();
		}
		return consts;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public List<HouseInfo> getHouses() {
		return houses;
	}

	public void setHouses(List<HouseInfo> houses) {
		this.houses = houses;
	}

	public Object getAttr() {
		return attr;
	}

	public void setAttr(Object attr) {
		this.attr = attr;
	}
	

}
