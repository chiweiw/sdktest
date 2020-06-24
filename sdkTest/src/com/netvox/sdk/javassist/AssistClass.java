package com.netvox.sdk.javassist;

import java.io.IOException;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;

/**
 * 修改apiassist
 * 
 * @author Administrator
 *
 */
public class AssistClass {

	// public static ClassPool pool = ClassPool.getDefault();
	public static void creatNewClass(String listener, List<String> methods, String package_) {
		// 创建线程池
		ClassPool pool = ClassPool.getDefault();
		// 添加依赖包
		pool.importPackage(package_);
		CtClass clazz;
		try {
			clazz = pool.get("com.netvox.sdk.api.Apiassist");
			if (listener == null || methods == null || package_ == null) {
				return;
			}
			
			// 添加接口
			clazz.setInterfaces(new CtClass[] { pool.makeInterface(listener) });

			// 接口的实现类，将值传递给常量
			for (String method : methods) {
				CtMethod eat = CtNewMethod.make("public void " + method + "(String seq,Object arr){\n"
						+ " this.returnV=arr;\n" + " }", clazz);
				clazz.addMethod(eat);
			}
			//修改类
			 CtMethod cm = clazz.getDeclaredMethod("useMethod");
//			  cm.insertAt(1, "{this.api."+}");
			 //cm.insertAfter({""});
			 cm.insertAt(1, "{System.out.println(\"hello HotSwapper.\");}");
		//	 cm.insertAfter(1,"{api."+methodName+"}");
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

}
