package com.netvox.sdk.test.javassist;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import com.netvox.sdk.test.gui.SdkGui;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 修改apiassist
 *
 * @author Administrator
 */
public class AssistClass {

	// public static ClassPool pool = ClassPool.getDefault();

	/**
	 * 动态生成类
	 *
	 * @param listener                接口名称
	 * @param methods                 接口方法的集合，只有一个方法
	 * @param package_                接口的packageName
	 * @param MethodName              api的方法名称
	 * @param inputAttr               用户输入的方法名称
	 * @param listenerMethodParamType 接口方法第二个参数的类型
	 */
	public static Class creatNewClass(String listener, List<String> methods, String package_, String MethodName,
			List<Map<String, Object>> inputAttr, String listenerMethodParamType) {
		if (listener == null || methods == null || package_ == null) {
			return null;
		}
		System.out.println("listenerMethodParamType  " + listenerMethodParamType);

		// 创建线程池
		ClassPool pool = ClassPool.getDefault();
		// 添加依赖包

		pool.importPackage(package_);
		pool.importPackage("com.netvox.sdk.api.APIHolder");
		pool.importPackage("com.netvox.smarthome.common.api.API");

		CtClass clazz;
		try {

			// 获取要继承的类
			CtClass clazz1 = pool.get("com.netvox.sdk.test.javassist.TestTemplate");
			clazz = pool.makeClass("com.netvox.sdk.javassist.Apiassist$" + MethodName);
			// 继承公有类
			clazz.setSuperclass(clazz1);

			// 添加接口
			clazz.setInterfaces(new CtClass[] { pool.makeInterface(package_) });

			// 接口的实现类，将值传递给方法
			for (String method : methods) {
				CtMethod eat = CtNewMethod.make("public void " + method + "(String seq," + listenerMethodParamType
						+ " arr){\n" + "fireEvent(paramString);\n" + " }", clazz);
				clazz.addMethod(eat);
			}

			// api调用的方法
			String genMethod = generateMethod(MethodName, inputAttr);

			// 生成并添加方法
			CtMethod apiUse = CtNewMethod.make(genMethod, clazz);
			clazz.addMethod(apiUse);
			// 添加请求类

			// 将生成的.class文件保存到磁盘

			clazz.writeFile();
			return clazz.toClass();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成调用api的方法主体
	 *
	 * @param methodName 方法名称
	 * @param attr
	 * @return
	 */
	private static String generateMethod(String methodName, List<Map<String, Object>> attr) {

		if (methodName == null || attr == null) {
			return null;
		}
		StringBuffer method = new StringBuffer("public void  sendRequest(");
		// com.netvox.smarthome.common.api.API
		StringBuffer apiRequest = new StringBuffer(" com.netvox.sdk.api.APIHolder.getInstance()." + methodName + "(");

		// 生成方法主体
		for (int i = 0; i < attr.size(); i++) {

			switch (attr.get(i).get("paramType").toString()) {

			case "int":
				method.append("int arg" + i);
				break;
			case "String":
				method.append("String arg" + i);
				break;
			case "ArrayList<String>":

			}
			apiRequest.append("arg" + i);
			if (i == attr.size() - 1) {
				apiRequest.append(")");
			} else {
				apiRequest.append(", ");
				method.append(", ");
			}

		}

		// 添加监听和方法
		method.append("){\n").append(apiRequest).append(";}");
		System.out.println(method);
		return method.toString();

	}

}
