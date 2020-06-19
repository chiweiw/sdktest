package com.netvox.sdk.utils;


/**
 * 常量单体类
 * 
 * @author Administrator
 *
 */
public class ConstsHolder {

	

	private static Consts constsHolder;

	private ConstsHolder() {
	}

	public static Consts getInstance() {
		if (constsHolder == null) {
			constsHolder = new Consts();
		}
		return constsHolder;
	}
}
