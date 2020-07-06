package com.netvox.sdk.test.javassist;


import java.util.ArrayList;
import java.util.List;

import com.netvox.sdk.test.api.APIHolder;
import com.netvox.sdk.test.javassist.interfaces.ConsoleListener;
import com.netvox.sdk.test.javassist.interfaces.LifeCycle;
import com.netvox.smarthome.common.api.event.IListener;

public class TestTemplate implements LifeCycle,IListener {
	
	protected List<ConsoleListener> consoleListeners = new ArrayList<ConsoleListener>();
	
	@Override
	public void init() {
		APIHolder.getInstance().AddListener(this);
	}
	
	@Override
	public void uninit() {
		// TODO Auto-generated method stub
	}
	
	public void addConsoleOutputListener(ConsoleListener listener)
	{
		if(!consoleListeners.contains(listener))
		{
			consoleListeners.add(listener);
		}
	}

	public void removeConsoleOutputListener(ConsoleListener listener)
	{
		consoleListeners.remove(listener);
	}
	
	public void removeAllConsoleOutputListener()
	{
		consoleListeners.clear();
	}
	
	public void fireEvent(Object res)
	{
		for(ConsoleListener listener : consoleListeners)
		{
			listener.consoleOutput(res);
		}
	}

}
