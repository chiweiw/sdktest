package com.netvox.sdk.test.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.gson.Gson;
import com.netvox.sdk.test.api.APIHolder;
import com.netvox.sdk.test.api.Consts;
import com.netvox.sdk.test.gui.listener.SdkGuiListenerMethods;
import com.netvox.sdk.test.javassist.AssistClass;
import com.netvox.sdk.test.javassist.TestTemplate;
import com.netvox.sdk.test.javassist.interfaces.ConsoleListener;
import com.netvox.sdk.test.javassist.interfaces.LifeCycle;
import com.netvox.sdk.test.utils.ClassUtils;
import com.netvox.smarthome.common.api.API;
import com.netvox.smarthome.common.api.APIImpl;
import com.netvox.smarthome.common.api.config.Config;
import com.netvox.smarthome.common.api.event.listener.cloud.OnHouseListListener;
import com.netvox.smarthome.common.api.model.cloud.HouseInfo;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * gui代码区域
 *
 * @author Administrator
 */
@SuppressWarnings("all")
public class SdkGui extends JFrame implements OnHouseListListener, ConsoleListener {

    private JPanel contentPane;
    private JTextField ipaddress;
    private JTextField username;
    private JTextField password;
    private JTextField returnValue; // 返回值
    private LifeCycle testLifeCycle; //声明一个生命周期接口

    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JPanel panel;
    private JList jMethodList;
    private JList jListListener;// 所有监听的集合
    private JComboBox houseIeeeList;
    private List<String> methodList;// 所有方法
    private Set<String> cbSets;// com.netvox.smarthome.common.api.event.listener.cb 下的所有监听类
    private Set<String> cloudSets;
    private Set<String> shcSets;
    private List<String> cbList;// com.netvox.smarthome.common.api.event.listener.cb 下的所有监听类的名称
    private List<String> cloudList;
    private List<String> shcList;
    private List<String> listenerList;// 所有listener类名称的集合
    private Set<String> selcetListenerSet = new HashSet<String>();// 被选中listener的集合
    private JButton addtest;// 点击测试

    private List<HouseInfo> houseInfoList;

    private List<Map<String, Object>> inputAttr;// 用户输入的数据

    private String methodName;// 选中的方法名称

    private List<JTextField> testList;

    // 获取login实例
    private BaseInfo loginExamples = new BaseInfo();

    private API apiHolder = APIImpl.GetInstance();

    /**
     * Create the frame.
     */
    public SdkGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 698, 722);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Ip 地址");
        lblNewLabel.setBounds(421, 45, 73, 18);
        contentPane.add(lblNewLabel);

        ipaddress = new JTextField();
        ipaddress.setBounds(504, 44, 149, 21);
        ipaddress.setText("mng.netvoxcloud.com");
        contentPane.add(ipaddress);
        ipaddress.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("用 户 名");
        lblNewLabel_1.setBounds(421, 89, 54, 15);
        contentPane.add(lblNewLabel_1);

        username = new JTextField();
        username.setBounds(504, 86, 149, 21);
        username.setText("13215929890");
        contentPane.add(username);
        username.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("密  码");
        lblNewLabel_2.setBounds(421, 130, 54, 15);
        contentPane.add(lblNewLabel_2);

        password = new JTextField();
        password.setBounds(504, 127, 149, 21);
        password.setText("123456");
        contentPane.add(password);
        password.setColumns(10);

        JButton login = new JButton("登 录");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 登录

                SdkGuiListenerMethods.setloginInfo(ipaddress.getText(), username.getText(), password.getText());
                loginExamples.execute();
            }
        });
        login.setBounds(421, 171, 73, 23);
        contentPane.add(login);
        // 网关
        houseIeeeList = new JComboBox();
        houseIeeeList.setBounds(504, 204, 168, 21);
        contentPane.add(houseIeeeList);
        houseIeeeList.addItemListener(new ItemListener() {
            // 选项改变的监听
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 如果选项是选中的 并且选项名称不为 请选择网关
                if (e.getStateChange() == ItemEvent.SELECTED && !(e.getItem().equals("请选择网关"))) {

                    for (HouseInfo house : houseInfoList) {
                        if (e.getItem().equals(house.getHouse_ieee())) {
                            // 设定参数
                            Config.getConfig().setHouseInfo(house);
                            // mqtt连接
                            loginExamples.mqttConnect();
                        }
                    }
                }
            }
        });

        JButton uodateHouseieee = new JButton("更新网关列表");
        uodateHouseieee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        uodateHouseieee.setBounds(537, 171, 116, 23);
        contentPane.add(uodateHouseieee);

        JLabel lblNewLabel_3 = new JLabel("网关列表");
        lblNewLabel_3.setBounds(425, 207, 54, 15);
        contentPane.add(lblNewLabel_3);

        // 参数列表
        panel = new JPanel();
        panel.setToolTipText("参数列表");
        panel.setBounds(421, 253, 232, 359);
        contentPane.add(panel);
        panel.setLayout(null);

        // 方法列表
        jMethodList = new JList();
        // 初始化方法列表
        List<String> methodLists = SdkGuiListenerMethods.initMethodList("com.netvox.smarthome.common.api.API");
        jMethodList.setListData(methodLists.toArray());

        // 添加监听
        jMethodList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!jMethodList.getValueIsAdjusting()) {
                    methodVlusechange(arg0);
                }

            }
        });
        jMethodList.setBounds(10, 32, 181, 424);
        contentPane.add(jMethodList);

        // jMethodList 添加滚动条
        JScrollPane jsp = new JScrollPane(jMethodList);
        jsp.setBounds(5, 28, 181, 424);
        contentPane.add(jsp);

        // 获取所有监听类
        initListenerList();
        // 所有监听的集合
        jListListener = new JList();
        jListListener.setListData(listenerList.toArray());
        jListListener.setBounds(221, 195, 173, 261);
        contentPane.add(jListListener);

        JScrollPane listenerScroll = new JScrollPane(jListListener);
        listenerScroll.setBounds(216, 190, 173, 261);
        contentPane.add(listenerScroll);

        // 返回值的显示
        returnValue = new JTextField();
        returnValue.setBounds(10, 510, 346, 135);
//        returnValue.setWordWrap(true);
        contentPane.add(returnValue);
        returnValue.setColumns(10);


        // 生成类并测试
        addtest = new JButton("测 试");
        addtest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //清空返回
                returnValue.setText("");

                List<String> attr = new ArrayList<String>();
                attr.add(jListListener.getSelectedValue().toString());
                // 方法名称
                List<String> methodList = SdkGuiListenerMethods.getListenerMethod(attr, cbList, cloudList, shcList);
                // 接口名称
                String interfaceName = jListListener.getSelectedValue().toString();
                //接口package
                String interPackName = SdkGuiListenerMethods.getInterfacePackageName(interfaceName, cbList, cloudList, shcList);

                List<Object> astrList = new ArrayList<>(testList.size());
                for (JTextField c : testList) {
                    astrList.add(c.getText());
                }
                try {
                    //将input中的数据填充到
                    inputAttr = SdkGuiListenerMethods.addinput(inputAttr, testList);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                String ListenerMethodParamType = SdkGuiListenerMethods.getListenerParamType(attr, cbList, cloudList, shcList);
                // 生成方法并调用
                try {
                    Class clazz = AssistClass.creatNewClass(methodList, interPackName, methodName, inputAttr, ListenerMethodParamType);

                    // new一个对象
                    Object testObject = clazz.newInstance();
                    // 获取被调用的对象
                    TestTemplate tpl = (TestTemplate) testObject;
                    // 初始化模板
                    testLifeCycle = (LifeCycle) testObject;
                    //
                    testLifeCycle.init();
                    //添加监听
                    tpl.addConsoleOutputListener(SdkGui.this);

                    // 调用
                    Method methods[] = clazz.getMethods();
                    for (int i = 0; i < methods.length; i++) {
                        //找到名称为invoke的方法
                        if (methods[i].getName().equals("invoke")) {

                            Object[] args = new Object[inputAttr.size()];
                            for (int j = 0; j < inputAttr.size(); j++) {
                                //将参数输入
                                args[j] = inputAttr.get(j).get("param");
                                if (inputAttr.get(j).get("paramType").equals("int")) {
                                    args[j] = Integer.parseInt(inputAttr.get(j).get("param").toString());
                                }
                            }
                            try {
                                //调用方法
                                methods[i].invoke(testObject, args);
                            } catch (InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (InstantiationException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        addtest.setBounds(503, 622, 93, 38);
        contentPane.add(addtest);
        API api = APIHolder.getInstance();
        api.Init();
        api.AddListener(SdkGui.this);
    }

    /**
     * 根据方法选项的多少 动态修改Gui界面
     *
     * @param arg0
     */
    private void methodVlusechange(ListSelectionEvent arg0) {
        this.panel.removeAll();
        Class<?> clazz = null;
        Method[] methods = null;
        // 将选中的方法名称赋值
        methodName = jMethodList.getSelectedValue().toString();
        // 清空用户输入

        inputAttr = new ArrayList<Map<String, Object>>();

        try {
            clazz = Class.forName("com.netvox.smarthome.common.api.API");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (clazz != null) {
            methods = clazz.getMethods();
        }
        for (Method method : methods) {
            if (method.getName().equals(this.jMethodList.getSelectedValue().toString())) {
                // 参数类型
                Class[] parameterTypes = method.getParameterTypes();

                for (Class c : parameterTypes) {
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    if (c.getName().contains(".")) {
                        // 截取最后一段
                        map1.put("paramType", c.getName().substring(1 + c.getName().lastIndexOf(".")));
                    } else {
                        map1.put("paramType", c.getName());
                    }
                    System.out.println(map1);
                    inputAttr.add(map1);
                }
                // 参数名称
                String[] parameterNames = new String[parameterTypes.length];
                for (int i = 0; i < method.getParameters().length; i++) {
                    parameterNames[i] = method.getParameters()[i].getName();
                }

                testList = new ArrayList<JTextField>();
                int count = parameterTypes.length;
                switch (count) {
                    case 6:
                        JLabel lblNewLabel_9 = new JLabel(parameterNames[5]);
                        lblNewLabel_9.setBounds(10, 275, 80, 15);
                        panel.add(lblNewLabel_9);

                        textField_5 = new JTextField();
                        textField_5.setBounds(97, 272, 81, 21);
                        panel.add(textField_5);
                        textField_5.setColumns(10);
                        testList.add(textField_5);

                    case 5:
                        JLabel lblNewLabel_8 = new JLabel(parameterNames[4]);
                        lblNewLabel_8.setBounds(10, 225, 80, 15);
                        panel.add(lblNewLabel_8);

                        textField_4 = new JTextField();
                        textField_4.setBounds(97, 222, 81, 21);
                        panel.add(textField_4);
                        textField_4.setColumns(10);
                        testList.add(textField_4);
                    case 4:
                        JLabel lblNewLabel_7 = new JLabel(parameterNames[3]);
                        lblNewLabel_7.setBounds(10, 175, 81, 15);
                        panel.add(lblNewLabel_7);

                        textField_3 = new JTextField();
                        textField_3.setBounds(97, 172, 81, 21);
                        panel.add(textField_3);
                        textField_3.setColumns(10);
                        testList.add(textField_3);
                    case 3:
                        JLabel lblNewLabel_6 = new JLabel(parameterNames[2]);
                        lblNewLabel_6.setBounds(10, 125, 81, 15);
                        panel.add(lblNewLabel_6);

                        textField_2 = new JTextField();
                        textField_2.setBounds(97, 122, 81, 21);
                        panel.add(textField_2);
                        textField_2.setColumns(10);
                        testList.add(textField_2);
                    case 2:
                        JLabel lblNewLabel_5 = new JLabel(parameterNames[1]);
                        lblNewLabel_5.setBounds(10, 75, 81, 15);
                        panel.add(lblNewLabel_5);

                        textField_1 = new JTextField();
                        textField_1.setBounds(97, 72, 81, 21);
                        panel.add(textField_1);
                        textField_1.setColumns(10);
                        testList.add(textField_1);
                    case 1:
                        JLabel lblNewLabel_4 = new JLabel(parameterNames[0]);
                        lblNewLabel_4.setBounds(10, 24, 150, 15);
                        panel.add(lblNewLabel_4);

                        textField = new JTextField();
                        textField.setBounds(97, 21, 81, 21);
                        panel.add(textField);
                        textField.setColumns(10);
                        testList.add(textField);
                    case 0:
                        break;

                }
            }
        }
        Collections.reverse(testList);

        // 刷新画面
        this.panel.repaint();

    }


    /**
     * 初始化listener方法
     *
     * @return 一个3个listener的集合
     */

    private List<String> initListenerList() {
        // 获取包下的所有类
        cbSets = ClassUtils.getClassName("com.netvox.smarthome.common.api.event.listener.cb", false);
        cloudSets = ClassUtils.getClassName("com.netvox.smarthome.common.api.event.listener.cloud", false);
        shcSets = ClassUtils.getClassName("com.netvox.smarthome.common.api.event.listener.shc", false);
        // 获取类名
        cbList = SdkGuiListenerMethods.getmethodName(cbSets, "cb.");
        cloudList = SdkGuiListenerMethods.getmethodName(cloudSets, "cloud.");
        shcList = SdkGuiListenerMethods.getmethodName(shcSets, "shc.");
        int size = cbList.size() + cloudList.size() + shcList.size();

        listenerList = new ArrayList<String>(size);
        listenerList.addAll(cbList);
        listenerList.addAll(cloudList);
        listenerList.addAll(shcList);
        Collections.sort(listenerList);
        return listenerList;
    }


    /**
     * 获取list集合的返回
     *
     * @param seq
     * @param houses
     */
    @Override
    public void onHouseListBack(String seq, ArrayList<HouseInfo> houses) {

        houseInfoList = houses;
        if (houses == null || houses.isEmpty()) {
            return;
        } else {
            houseIeeeList.addItem("请选择网关");
            for (HouseInfo house : houses) {
                houseIeeeList.addItem(house.getHouse_ieee());
            }
        }
    }

    @Override
    public void consoleOutput(Object arr) {

        System.out.println(arr);
        Gson gson = new Gson();
        String res = gson.toJson(arr);

        returnValue.setText(res);

    }

}
