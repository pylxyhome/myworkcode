package com.im.listener;

import org.directwebremoting.impl.DefaultScriptSessionManager;

public class UserScriptSessionManager extends DefaultScriptSessionManager {
	public UserScriptSessionManager(){
		try {
			//注册自定义的ScriptSession监听
			super.addScriptSessionListener(new UserScriptSessionListener());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
