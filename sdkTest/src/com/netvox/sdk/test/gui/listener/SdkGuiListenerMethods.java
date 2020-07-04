package com.netvox.sdk.test.gui.listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JTextField;

import com.netvox.smarthome.common.api.config.Config;

public class SdkGuiListenerMethods {

    private static String cbPackage = "com.netvox.smarthome.common.api.event.listener.cb.";
    private static String cloudPackage = "com.netvox.smarthome.common.api.event.listener.cloud.";
    private static String shcPackage = "com.netvox.smarthome.common.api.event.listener.shc.";

    /**
     * 返回指定class的所有方法
     *
     * @param className
     */
    public static List<String> initMethodList(String className) {
        List attrList = new ArrayList<String>();
        try {
            Class<?> clazz = Class.forName(className);

            Method[] methods = clazz.getMethods();

            for (Method method : methods) {
                attrList.add(method.getName());
            }
            Collections.sort(attrList);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return attrList;

    }


    /**
     * 传入类的全部地址，返回类名
     *
     * @param sets 一个类的集合
     * @param type listener的种类
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
    public static void setloginInfo(String ipaddress, String username, String password) {

        // 设置用户名和密码
        Config.getConfig().setCloudIP(ipaddress);
        Config.getConfig().setUserName(username);
        Config.getConfig().setPassWord(password);
    }

    /**
     * 将jlist中输入的内容赋给input
     *
     * @param input
     * @param jlist
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> addinput(List<Map<String, Object>> input, List<JTextField> jlist)
            throws Exception {
        if (input.size() != jlist.size()) {
            throw new Exception("input.size != jlist.size,please check it");
        }

        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).get("paramType").equals("String") || input.get(i).get("paramType").equals("int")) {
                input.get(i).put("param", jlist.get(i).getText());
            } else {
                System.out.println("暂时不支持该方法");
            }

        }
        return input;
    }

    /**
     * 根据接口名称返回包名
     *
     * @param interNmae
     * @return
     */
    public static String getInterfacePackageName(String interNmae, List<String> cbList, List<String> cloudList, List<String> shcList) {
        // 反射获取接口
        if (cbList.contains(interNmae)) {
            return cbPackage + interNmae;
        }
        if (cloudList.contains(interNmae)) {
            return cloudPackage + interNmae;
        }
        if (shcList.contains(interNmae)) {
            return shcPackage + interNmae;

        }
        return null;
    }


    /**
     * 根据listener返回第二个参数的类型
     * 返回一个string类型的数据
     *
     * @param listners
     * @param cbList
     * @param cloudList
     * @param shcList
     * @return
     */
    public static String getListenerParamType(List<String> listners, List<String> cbList, List<String> cloudList, List<String> shcList) {
        Class<?> clazz = null;
        Method[] methods = null;
        for (String s : listners) {
            clazz = getListenreClass(listners, cbList, cloudList, shcList);
            methods = clazz.getMethods();
            // 将监听的方法传输list
            if (methods != null) {

                Method method = methods[0];
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 2) {
                    return parameterTypes[1].getName();

                } else {
                    System.out.println("parameterTypes.length!=2");
                }
            }
        }
        return null;
    }


    /**
     * 获取listerer所在的class
     *
     * @param listners
     * @param cbList
     * @param cloudList
     * @param shcList
     * @return
     */
    public static Class getListenreClass(List<String> listners, List<String> cbList, List<String> cloudList, List<String> shcList) {
        Class<?> clazz = null;
        for (String s : listners) {
            try {
                // 反射获取接口
                if (cbList.contains(s)) {
                    clazz = Class.forName(cbPackage + s);
                } else if (cloudList.contains(s)) {
                    clazz = Class.forName(cloudPackage + s);
                } else if (shcList.contains(s)) {
                    clazz = Class.forName(shcPackage + s);
                }

            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return clazz;
    }

}
