package com.netvox.sdk.javassist;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import com.netvox.sdk.api.APIHolder;
import com.netvox.sdk.gui.SdkGui;
import com.netvox.smarthome.common.api.API;

public class Apiassist {
	
	public static Object returnV;
	
	JTextField inputs =  SdkGui.getInstance().getReturnValue();
	
	private API api = APIHolder.getInstance();
	
	
//	public
	
	public void useMethod(String methodName,List<Map<String,Object>> attr) {


		Method[] method;
		try {
			//method = Apiassist.class.getMethods();
			//method[0].getName().contentEquals(arg0)
					Object[] obj = new Object[3];
			//method.invoke(new Apiassist(), obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	

}
