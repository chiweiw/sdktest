package com.netvox.sdk.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JTextField;

import com.netvox.smarthome.common.api.config.Config;

public class SdkGuiListener {

	private Set<String> cbSets;

	/**
	 * 返回指定class的所有方法
	 * 
	 * @param className
	 * @param jList
	 */
	 List<String> initMethodList(String className, JList jList, Boolean setjList) {
		List attrList = new ArrayList<String>();
		try {
			Class<?> clazz = Class.forName(className);

			Method[] methods = clazz.getMethods();

			for (Method method : methods) {
				attrList.add(method.getName());
			}

			Collections.sort(attrList);
			if (setjList) {
				jList.setListData(attrList.toArray());
				return null;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attrList;

	}

	/**
	 * 初始化listener方法
	 * 
	 * @return 一个3个listener的集合
	 */



	/**
	 * 传入类的全部地址，返回类名
	 * 
	 * @param setssets 一个类的集合
	 * @param type     listener的种类
	 * @return类名称的集合
	 */
	public static List<String> getmethodName(Set<String> sets, String type) {

		List<String> str = new ArrayList<String>(sets.size());

		if (sets != null) {
			for (String set : sets) {
				str.add(set.replace("com.netvox.smarthome.common.api.event.listener." + type, ""));
			}
		}

		return str;

	}

	/**
	 * 登录
	 */
	private void setAndLogin() {
		// System.out.println(username.getText());
		// System.out.println(password.getText());
		// 设置用户名和密码
//		 private String cloudMngIp = "mng.netvoxcloud.com";
//		Config.getConfig().setCloudIP(ipaddress.getText());
//		Config.getConfig().setUserName(username.getText());
//		Config.getConfig().setPassWord(password.getText());
//		loginExamples.execute();
	}

	/**
	 * 根据listener反射获取接口的方法
	 */
	private List<String> getListenerMethod(List<String> listenerList) {
		if (listenerList == null || listenerList.size() == 0) {
			return null;
		}

		// 所有listener的方法，返回用
		List<String> methodList = new ArrayList<>(listenerList.size());
		Class<?> clazz = null;
		Method[] methods = null;
		for (String s : listenerList) {
//			try {
//				// 反射获取接口
//				if (cbList.contains(s)) {
//					clazz = Class.forName("com.netvox.smarthome.common.api.event.listener.cb." + s);
//				}
//				if (cloudList.contains(s)) {
//					clazz = Class.forName("com.netvox.smarthome.common.api.event.listener.cloud." + s);
//				}
//				if (shcList.contains(s)) {
//					clazz = Class.forName("com.netvox.smarthome.common.api.event.listener.shc." + s);
//				}
//
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			methods = clazz.getMethods();
			// 将监听的方法传输list
			if (methods != null) {
				for (Method method : methods) {
					methodList.add(method.getName());
				}
			}
		}

		// System.out.println("methodList " + methodList);
		return methodList;
	}

	/**
	 * 根据接口名称返回包名
	 * 
	 * @param interNmae
	 * @return
	 */
	private String getInterfacePackageName(String interNmae) {
		// 反射获取接口
//		if (cbList.contains(interNmae)) {
//			return "com.netvox.smarthome.common.api.event.listener.cb." + interNmae;
//		}
//		if (cloudList.contains(interNmae)) {
//			return "com.netvox.smarthome.common.api.event.listener.cloud." + interNmae;
//		}
//		if (shcList.contains(interNmae)) {
//			return "com.netvox.smarthome.common.api.event.listener.shc." + interNmae;
//
//		}
		return null;
	}

	/**
	 * 将jlist中输入的内容赋给input 
	 * @param input
	 * @param jlist
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> addinput(List<Map<String, Object>> input, List<JTextField> jlist) throws Exception
			 {
		if (input.size() != jlist.size()) {
			throw new Exception("input.size != jlist.size,please check it");
		}

		for (int i = 0; i < input.size(); i++) {
			if (input.get(i).get("paramType").equals("String") || input.get(i).get("paramType").equals("int")) {
				input.get(i).put("param", jlist.get(i).getText().toString());
			} else {
				System.out.println("暂时不支持该方法");
			}

		}
		return input;
	}
}
