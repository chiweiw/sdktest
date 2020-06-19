package com.netvox.sdk.api;

import java.io.IOException;
import java.util.List;

import javassist.*;
import javassist.bytecode.AccessFlag;

/**
 * 创建一个新类
 * 
 * @author Administrator
 *
 */
public class AssistClass {

	// public static ClassPool pool = ClassPool.getDefault();
	public static void creatNewClass(List<String> listenerList)
			throws CannotCompileException, IOException, NotFoundException {

		try {
			// 创建线程池
			ClassPool pool = ClassPool.getDefault();
			CtClass clazz = pool.makeClass("com.netvox.sdk.api.Employ");
			if (listenerList == null && listenerList.size() == 0) {
				return;

			}
			  // 添加一个object类型的共有属性
	        CtField fieldId = new CtField(CtClass.voidType, "returnValue", clazz);
	        fieldId.setModifiers(AccessFlag.PUBLIC);
	        clazz.addField(fieldId);
			//添加接口
			for (String s : listenerList) {
				clazz.setInterfaces(new CtClass[] { pool.makeInterface(s) });
				  CtMethod eat = CtNewMethod.make(
			                "public void"+s+"(String seq,Object arr){"
			                		+ " this.returnValue = arr;}"
			                ,clazz
			        );
				  clazz.addMethod(eat);
			}
			
			  // 将生成的.class文件保存到磁盘
			clazz.writeFile();
			// 创建属性方法一
//			CtField ctFieldOne = CtField.make("private Integer empId;", clazz);
//			clazz.addField(ctFieldOne);
//			CtField ctFieldTwo = CtField.make("private Integer empAge;", clazz);
//			clazz.addField(ctFieldTwo);

		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

}
