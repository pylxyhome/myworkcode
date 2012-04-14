package com.im.service.dwr.actionscript;

import java.util.Collection;

import org.directwebremoting.ScriptSession;

import com.im.service.dwr.base.BaseScript;

public class CheckTimeOutScript{
	public static final String SCRIPT_CHECK_TIMEOUT="userModule.checkTimeOut";
	/**
	 * 检查连线状态
	 * @param scriptSessions
	 */
	public static void checkTimeOut(Collection<ScriptSession> scriptSessions){
		BaseScript.excuteScript(scriptSessions,SCRIPT_CHECK_TIMEOUT,new Object[]{});
	}
}
