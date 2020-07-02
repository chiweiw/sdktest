package com.netvox.sdk.javassist;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import com.netvox.sdk.gui.SdkGui;

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
 *
 */
public class AssistClass {

	// public static ClassPool pool = ClassPool.getDefault();
	public static void creatNewClass(String listener, List<String> methods, String package_, String MethodName,
			List<Map<String, Object>> inputAttr) {
		if (listener == null || methods == null || package_ == null) {
			return;
		}

		// 创建线程池
		ClassPool pool = ClassPool.getDefault();
		// 添加依赖包
		pool.importPackage(package_);
		pool.importPackage("com.netvox.sdk.api.APIHolder");
		pool.importPackage("com.netvox.sdk.gui.SdkGui");
		pool.importPackage("com.netvox.smarthome.common.api.API");
		pool.importPackage("java.util.List");
		pool.importPackage("java.util.Map");
		pool.importPackage("javax.swing.JTextField");

		CtClass clazz;
		CtClass clazz1;
		try {

			clazz = pool.makeClass("com.netvox.sdk.javassist.Apiassist$" + MethodName);

			// 添加接口
			clazz.setInterfaces(new CtClass[] { pool.makeInterface(listener) });
//			;
			// 接口的实现类，将值传递给常量
			for (String method : methods) {
				CtMethod eat = CtNewMethod
						.make("public void " + method + "(String seq,Object arr){\n" +
				"SdkGui.getInstance().getReturnValue().setText(arr.toString());\n" + " }", clazz);
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

	}

	/**
	 * 生成方法主体
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
	//	com.netvox.smarthome.common.api.API
		StringBuffer apiRequest = new StringBuffer(" com.netvox.sdk.api.APIHolder.getInstance()." + methodName + "(");

		// 生成方法主体
		for (int i = 0; i < attr.size(); i++) {

			switch (attr.get(i).get("paramType").toString()) {

			case "int":
				method.append("int arg" + i);
				// apiRequest.append((int) attr.get(i).get("param"));

				break;
			case "String":
				method.append("String arg" + i);
				// apiRequest.append("\""+attr.get(i).get("param").toString()+"\"");
				break;
			case "ArrayList<String>":
				// method.append(" ArrayList<String> arg" + i);
			}
			apiRequest.append("arg" + i);
			if (i == attr.size() - 1) {
				apiRequest.append(")");
			} else {
				apiRequest.append(", ");
				method.append(", ");
			}

		}
		method.append("){\n").append(apiRequest).append(";}");
		System.out.println(method);
		return method.toString();

	}

}
