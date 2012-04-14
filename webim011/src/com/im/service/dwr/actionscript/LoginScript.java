package com.im.service.dwr.actionscript;

import java.util.Collection;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

import com.im.bean.user.User;
import com.im.service.dwr.base.BaseScript;
import com.im.util.IMLog;

public class LoginScript {

	public static final String SCRIPT_LOGIN_REFRESH="refreshUserList";   
	public static final String SCRIPT_LOGOUT="logout";

	/**
	 * 登录时刷新用户列表
	 */
	public static void refreshUserTree(Collection<ScriptSession> scriptSessions){
		System.out.println("刷新服务人员在线列表");
		BaseScript.excuteScript(scriptSessions,new ScriptBuffer("userModule.refreshUserList()"));
	}
	/**
	 * 登录时刷新用户列表
	 */
	public static void refreshUserTree(Collection<ScriptSession> scriptSessions,User notifyUser,Integer isOnline){
		IMLog.info(LoginScript.class+"刷新好友在线列表");
		BaseScript.excuteScript(scriptSessions,"userModule.refreshUserTree",new Object[]{notifyUser,isOnline});
	}
	public static void refreshUserTree(ScriptSession scriptSession){
		BaseScript.excuteScript(scriptSession,SCRIPT_LOGIN_REFRESH);
	}
}
