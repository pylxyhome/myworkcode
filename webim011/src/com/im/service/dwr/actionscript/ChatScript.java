package com.im.service.dwr.actionscript;

import org.directwebremoting.ScriptSession;

import com.im.bean.message.Message;
import com.im.service.dwr.base.BaseScript;

public class ChatScript{
	
	public static final String SCRIPT_RECEIVE_MSG="receiveMessage";
	public static final String SCRIPT_SHAKE_CHAT="shakeChat";
	public static final String SCRIPT_SHAKE_USER_ICON="shakeUserIcon";
	/**
	 *调用showChat脚本中的 receiveMessage(message, senderName)
	 *作用:通知用户接收信息
	 * @param scriptSession
	 * @param sysMessage 信息
	 * @param senderName 发道人名
	 */
	public static void receiveMessage(ScriptSession scriptSession,Message imMessage,String senderName){
		Object[] tempObject={imMessage,senderName};
		BaseScript.excuteScript(scriptSession, SCRIPT_RECEIVE_MSG, tempObject);
	}
	/**
	 * 通知im闪烁用户图标
	 * @param scriptSession
	 * @param targetId
	 */
	public static void shakeChat(ScriptSession scriptSession,int targetId){
		BaseScript.excuteScript(scriptSession, SCRIPT_SHAKE_CHAT, targetId);
	}
	/**
	 * 通知im闪烁用户图标
	 * @param scriptSession
	 * @param targetId
	 */
	public static void shakeUserIcon(ScriptSession scriptSession,int targetId){
		BaseScript.excuteScript(scriptSession, SCRIPT_SHAKE_USER_ICON, targetId);
	}
}
