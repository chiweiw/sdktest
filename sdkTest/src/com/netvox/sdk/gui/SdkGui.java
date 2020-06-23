package com.netvox.sdk.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

import com.netvox.sdk.api.AssistClass;
import com.netvox.sdk.login.Base;
import com.netvox.sdk.login.LoginHolder;
import com.netvox.sdk.utils.ClassUtils;
import com.netvox.sdk.utils.Consts;
import com.netvox.smarthome.common.api.API;
import com.netvox.smarthome.common.api.APIImpl;
import com.netvox.smarthome.common.api.config.Config;
import com.netvox.smarthome.common.api.model.cloud.HouseInfo;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

/**
 * gui代码区域
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("all")
public class SdkGui extends JFrame {

	// 单例
	private static SdkGui instance = null;

	public static SdkGui getInstance() {
		if (instance == null) {
			instance = new SdkGui();
		}
		return instance;
	}

	private JPanel contentPane;
	private JTextField ipaddress;
	private JTextField username;
	private JTextField password;
	private JTextField returnValue; // 返回值

	public JTextField getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(JTextField returnValue) {
		this.returnValue = returnValue;
	}

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JPanel panel;
	private JList jMethodList;
	private JList jListListener;// 所有监听的集合
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

	// 获取login实例
	private Base loginExamples = LoginHolder.getInstance();

	private API apiHolder = APIImpl.GetInstance();

	/**
	 * main方法，先注释掉
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SdkGui frame = new SdkGui();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

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
		contentPane.add(ipaddress);
		ipaddress.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("姓  名");
		lblNewLabel_1.setBounds(421, 89, 54, 15);
		contentPane.add(lblNewLabel_1);

		username = new JTextField();
		username.setBounds(504, 86, 149, 21);
		contentPane.add(username);
		username.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("密  码");
		lblNewLabel_2.setBounds(421, 130, 54, 15);
		contentPane.add(lblNewLabel_2);

		password = new JTextField();
		password.setBounds(504, 127, 149, 21);
		contentPane.add(password);
		password.setColumns(10);

		JButton login = new JButton("登 录");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 登录
				setAndLogin();

			}
		});
		login.setBounds(421, 171, 73, 23);
		contentPane.add(login);
		// 网关
		JComboBox houseIeeeList = new JComboBox();
		houseIeeeList.setBounds(504, 204, 168, 21);
		contentPane.add(houseIeeeList);
		houseIeeeList.addItemListener(new ItemListener() {
			// 选项改变的监听
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
			public void actionPerformed(ActionEvent e) {

				// 根据获取到的网关列表 生成combox

				houseIeeeList.removeAll();
				houseInfoList = Consts.getConsts().getHouses();

				if (houseInfoList == null || houseInfoList.isEmpty()) {
					return;
				} else {
					houseIeeeList.addItem("请选择网关");
					for (HouseInfo house : houseInfoList) {
						houseIeeeList.addItem(house.getHouse_ieee());
					}
				}

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
		panel.setBounds(444, 253, 209, 359);
		contentPane.add(panel);
		panel.setLayout(null);

		// 方法列表
		jMethodList = new JList();
		// 初始化方法列表
		initMethodList("com.netvox.smarthome.common.api.API", jMethodList, true);
		// 添加监听
		jMethodList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				methodVlusechange(arg0);
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
		contentPane.add(returnValue);
		returnValue.setColumns(10);

		// 生成类并测试
		addtest = new JButton("测 试");
		addtest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				List<String> attr = new ArrayList<String>();
				attr.add(jListListener.getSelectedValue().toString());
				// 方法名称
				List<String> methodList = getListenerMethod(attr);
				// 接口名称
				String interfaceName = jListListener.getSelectedValue().toString();

				String interPackName = getInterfacePackageName(interfaceName);
				System.out.println("methodList " + methodList);
				System.out.println(" interfaceName " + interfaceName);
				System.out.println("interPackName " + interPackName);
				try {
					AssistClass.creatNewClass(new ArrayList<String>(selcetListenerSet), methodList);
				} catch (CannotCompileException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		addtest.setBounds(503, 622, 93, 38);
		contentPane.add(addtest);

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

				// 参数名称
				String[] parameterNames = new String[parameterTypes.length];
				for (int i = 0; i < method.getParameters().length; i++) {
					parameterNames[i] = method.getParameters()[i].getName();
				}

				int count = parameterTypes.length;
				switch (count) {
				case 6:
					break;
				case 5:
					JLabel lblNewLabel_8 = new JLabel(parameterNames[2]);
					lblNewLabel_8.setBounds(10, 225, 54, 15);
					panel.add(lblNewLabel_8);

					textField_2 = new JTextField();
					textField_2.setBounds(97, 222, 81, 21);
					panel.add(textField_2);
					textField_2.setColumns(10);
				case 4:
					JLabel lblNewLabel_7 = new JLabel(parameterNames[2]);
					lblNewLabel_7.setBounds(10, 175, 54, 15);
					panel.add(lblNewLabel_7);

					textField_2 = new JTextField();
					textField_2.setBounds(97, 172, 81, 21);
					panel.add(textField_2);
					textField_2.setColumns(10);
				case 3:
					JLabel lblNewLabel_6 = new JLabel(parameterNames[2]);
					lblNewLabel_6.setBounds(10, 125, 54, 15);
					panel.add(lblNewLabel_6);

					textField_2 = new JTextField();
					textField_2.setBounds(97, 122, 81, 21);
					panel.add(textField_2);
					textField_2.setColumns(10);
				case 2:
					JLabel lblNewLabel_5 = new JLabel(parameterNames[1]);
					lblNewLabel_5.setBounds(10, 75, 54, 15);
					panel.add(lblNewLabel_5);

					textField_1 = new JTextField();
					textField_1.setBounds(97, 72, 81, 21);
					panel.add(textField_1);
					textField_1.setColumns(10);
				case 1:
					JLabel lblNewLabel_4 = new JLabel(parameterNames[0]);
					lblNewLabel_4.setBounds(10, 24, 54, 15);
					panel.add(lblNewLabel_4);

					textField = new JTextField();
					textField.setBounds(97, 21, 81, 21);
					panel.add(textField);
					textField.setColumns(10);
				case 0:
					break;

				}
			}
		}

		// 刷新画面
		this.panel.repaint();

	}

	/**
	 * 返回指定class的所有方法
	 * 
	 * @param className
	 * @param jList
	 */
	private List<String> initMethodList(String className, JList jList, Boolean setjList) {
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

	private List<String> initListenerList() {
		// 获取包下的所有类
		cbSets = ClassUtils.getClassName("com.netvox.smarthome.common.api.event.listener.cb", false);
		cloudSets = ClassUtils.getClassName("com.netvox.smarthome.common.api.event.listener.cloud", false);
		shcSets = ClassUtils.getClassName("com.netvox.smarthome.common.api.event.listener.shc", false);
		// 获取类名
		cbList = getmethodName(cbSets, "cb.");
		cloudList = getmethodName(cloudSets, "cloud.");
		shcList = getmethodName(shcSets, "shc.");
		int size = cbList.size() + cloudList.size() + shcList.size();

		listenerList = new ArrayList<String>(size);
		listenerList.addAll(cbList);
		listenerList.addAll(cloudList);
		listenerList.addAll(shcList);
		Collections.sort(listenerList);
		return listenerList;
	}

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
		Config.getConfig().setUserName(username.getText());
		Config.getConfig().setPassWord(password.getText());
		loginExamples.execute();
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
			try {
				// 反射获取接口
				if (cbList.contains(s)) {
					clazz = Class.forName("com.netvox.smarthome.common.api.event.listener.cb." + s);
				}
				if (cloudList.contains(s)) {
					clazz = Class.forName("com.netvox.smarthome.common.api.event.listener.cloud." + s);
				}
				if (shcList.contains(s)) {
					clazz = Class.forName("com.netvox.smarthome.common.api.event.listener.shc." + s);
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		if (cbList.contains(interNmae)) {
			return "com.netvox.smarthome.common.api.event.listener.cb." + interNmae;
		}
		if (cloudList.contains(interNmae)) {
			return "com.netvox.smarthome.common.api.event.listener.cloud." + interNmae;
		}
		if (shcList.contains(interNmae)) {
			return "com.netvox.smarthome.common.api.event.listener.shc." + interNmae;

		}
		return null;
	}
}
