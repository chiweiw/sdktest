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

	/**
	 * 动态生成类
	 * @param listener 接口名称
	 * @param methods 接口方法的集合，只有一个方法
	 * @param package_ 接口的packageName
	 * @param MethodName api的方法名称
	 * @param inputAttr 用户输入的方法名称
	 * @param listenerMethodParamType 接口方法第二个参数的类型
	 */
	public static void creatNewClass(String listener, List<String> methods, String package_, String MethodName,
			List<Map<String, Object>> inputAttr,String listenerMethodParamType) {
		if (listener == null || methods == null || package_ == null) {
			return;
		}
		System.out.println("listenerMethodParamType  "+listenerMethodParamType);

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
		try {

			clazz = pool.makeClass("com.netvox.sdk.javassist.Apiassist$" + MethodName);

			// 添加接口
			clazz.setInterfaces(new CtClass[] { pool.makeInterface(listener) });
//			sout
//			;
			// 接口的实现类，将值传递给常量
			for (String method : methods) {
				String c = "public void " + method + "(String seq,"+listenerMethodParamType+" arr){\n" +
						"SdkGui.getInstance().getReturnValue().setText(arr.toString());\n" + " }";
				System.out.println(c);
				CtMethod eat = CtNewMethod
						.make("public void " + method + "(String seq,"+listenerMethodParamType+" arr){\n" +
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
