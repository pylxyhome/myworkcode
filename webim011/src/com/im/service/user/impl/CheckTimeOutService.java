package com.im.service.user.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.directwebremoting.ScriptSession;
import org.springframework.stereotype.Service;

import com.im.service.dwr.ScriptSessionService;
import com.im.service.dwr.actionscript.CheckTimeOutScript;
import com.im.service.user.ICheckTimeOutService;
import com.im.util.IMLog;

@Service
public class CheckTimeOutService extends ScriptSessionService implements ICheckTimeOutService {
	public static HashSet <Integer> onlineUser=new HashSet<Integer>();
	//向用户发送检测是否在线请求
	public void pushCheckTimeOut() {
		Collection<ScriptSession> collectionSession=new ArrayList<ScriptSession>();
		for (Integer userid : mapUsers.keySet()) {
			ScriptSession scriptSession=super.getScriptSessionByID(userid);
			if(scriptSession!=null){
				collectionSession.add(scriptSession);
			}
		
		CheckTimeOutScript.checkTimeOut(collectionSession);
		
		}
	}
	public synchronized void checkTimeOutCallBack(int userId){
		synchronized(onlineUser){
			//IMLog.info(userId+"加到了onlineUser中...");
			onlineUser.add(userId);
		}
	}

	public void CheckTimeOutInit(){
		onlineUser.clear();
		onlineUser=new HashSet<Integer>();
	}

	public HashSet<Integer> getOnlineUser() {
		return onlineUser;
	}
}
