package com.netvox.sdk.test.gui.listener;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import com.netvox.smarthome.common.api.config.Config;
import com.netvox.smarthome.common.api.framework.annotation.Listener;

public class SdkGuiListenerMethods {


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


    public static void writeFile(List<String> methods) {

        try {
            File file = new File("api1.txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName(), true);

            for (String me : methods) {
                fileWritter.write(me + "\n\t");
            }


            fileWritter.close();


        } catch (Exception e) {

        }


    }

    /**
     * 设置登录信息
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
     * 返回方法
     *
     * @param methodName
     * @return
     */
    public static Method getMethodByName(String calaaName, String methodName) {
        Class<?> clazz = null;
        Method[] methods;
        try {
            clazz = Class.forName(calaaName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
