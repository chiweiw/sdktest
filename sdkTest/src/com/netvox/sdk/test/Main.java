package com.netvox.sdk.test;

import java.awt.EventQueue;

import com.netvox.sdk.test.gui.SdkGui;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SdkGui frame= new SdkGui();
	 				frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
