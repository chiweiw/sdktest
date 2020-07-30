package com.netvox.sdk.test.javassist;

import java.io.IOException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netvox.sdk.test.gui.SdkGui;

import com.netvox.smarthome.common.api.framework.annotation.Listener;
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

    private static HashMap<String, Class> ClassCache = new HashMap<>();


    /**
     * 动态生成类
     *
     * @param listenerMethodName      接口方法名称
     * @param package_                接口的packageName
     * @param methodName              api的方法名称
     * @param inputAttr               用户输入的方法名称
     * @param listenerMethodParamType 接口方法第二个参数的类型
     */
    public static Class creatNewClass(String listenerMethodName, String package_, String methodName, List<Map<String, Object>> inputAttr,
                                      String listenerMethodParamType) {
        if (listenerMethodName == null || package_ == null) {
            return null;
        }
        String className = "com.netvox.sdk.test.javassist.Apiassist$" + methodName;
        Class retClass = ClassCache.get(className);
        if (retClass != null) {
            return retClass;
        }

        // 创建线程池
        ClassPool pool = ClassPool.getDefault();
        // 添加依赖包


        CtClass clazz;
        try {
            clazz = pool.get("com.netvox.sdk.test.gen.Apiassist$" + methodName);
            retClass = clazz.toClass();
            ClassCache.put(className, retClass);
            return retClass;
        } catch (NotFoundException e1) {

            try {
                pool.importPackage(package_);
                pool.importPackage("com.netvox.sdk.api.APIHolder");
                pool.importPackage("com.netvox.smarthome.common.api.API");

                // 获取要继承的类
                CtClass clazz1 = pool.get("com.netvox.sdk.test.javassist.TestTemplate");
//                com.netvox.sdk.test.gen
                clazz = pool.makeClass("com.netvox.sdk.test.gen.Apiassist$" + methodName);
                // 继承公有类methods
                clazz.setSuperclass(clazz1);

                // 添加接口
                clazz.setInterfaces(new CtClass[]{pool.makeInterface(package_)});

                // 接口的实现类，将值传递给方法

                CtMethod eat = CtNewMethod.make("public void " + listenerMethodName + "(String seq," + listenerMethodParamType
                        + " arr){\n" + "fireEvent((Object)arr);\n" + " }", clazz);
                clazz.addMethod(eat);


                // api调用的方法
                String genMethod = generateMethod(methodName, inputAttr);

                // 生成并添加方法
                CtMethod apiUse = CtNewMethod.make(genMethod, clazz);
                // 添加请求类
                clazz.addMethod(apiUse);

                // 将生成的.class文件保存到磁盘
//                clazz.writeFile();
                retClass = clazz.toClass();
                ClassCache.put(className, retClass);
                return retClass;
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CannotCompileException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
        } catch (CannotCompileException e) {
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
        StringBuffer method = new StringBuffer("public void  invoke(");
        // com.netvox.smarthome.common.api.API
        StringBuffer apiRequest = new StringBuffer("com.netvox.sdk.test.api.APIHolder.getInstance()." + methodName + "(");

        // 生成方法主体
        for (int i = 0; i < attr.size(); i++) {

            switch (attr.get(i).get("paramType").toString()) {

                case "int":
                    method.append("int arg" + i);
                    break;
                case "String":
                    method.append("String arg" + i);
                    break;
//                case "List":
//                    method.append("List arg" + i);

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
        method.append("){").append(apiRequest).append(";}");
        System.out.println(method);
        return method.toString();

    }


    /**
     * 用户选中的方法
     *
     * @param method
     * @return
     */
//    @Test
    public static Class creatNewClass(Method method, List<Map<String, Object>> inputAttr) {

        if (method == null) {
            return null;
        }

        if (method.isAnnotationPresent(Listener.class)) {
            Listener listener = (Listener) method.getAnnotation(Listener.class);
            String listenerName = listener.listener()[0];
            Class<?> tClass;
            try {

                listenerName = listenerName.replace(" ", "");
                tClass = Class.forName(listenerName);
                Method[] methods = tClass.getMethods();
                if (methods.length != 1) {
                    throw new ClassNotFoundException(" input class has two methods,need one method class");
                }
                if (methods[0].getParameterTypes().length != 2) {
                    throw new ClassNotFoundException(" input class methods,need one method class");
                }
                String listenerMethodName = methods[0].getName();

                String ListenerMethodSecondParamName = methods[0].getParameterTypes()[1].getName();
                return creatNewClass(listenerMethodName, listenerName, method.getName(), inputAttr, ListenerMethodSecondParamName);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }


        return null;
    }
}
