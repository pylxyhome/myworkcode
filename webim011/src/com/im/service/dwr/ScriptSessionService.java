package com.im.service.dwr;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.ScriptSession;

import com.im.bean.user.User;
import com.im.service.dwr.actionscript.LoginScript;
import com.im.service.dwr.base.DWRBaseService;

public class ScriptSessionService extends DWRBaseService{

	
	public static Map<Integer,User> mapUsers = new ConcurrentHashMap<Integer,User>();
	/**
	 * 根据用户id获得指定用户的页面脚本session
	 * @param scriptValue
	 * @param request
	 * @return
	 */
	public ScriptSession getScriptSessionByID(int userid,HttpServletRequest request){
		ScriptSession serviceSession=super.getScriptSession("userid", userid, request);
		if(serviceSession!=null){
			return serviceSession;
		}
		return null;
	}
	public ScriptSession getScriptSessionByID(int userid,ServletContext context){
		ScriptSession serviceSession=super.getScriptSession("userid", userid, context);
		if(serviceSession!=null){
			return serviceSession;
		}
		return null;
	}
	public ScriptSession getScriptSessionByID(int userid){
		ScriptSession serviceSession=super.getScriptSession("userid", userid);
		if(serviceSession!=null){
			return serviceSession;
		}
		return null;
	}
	/**
	 * 根据用户名获得指定用户的页面脚本session
	 * @param scriptValue
	 * @param request
	 * @return
	 */
	public ScriptSession getScriptSessionUserName(String username,HttpServletRequest request){
		ScriptSession serviceSession=super.getScriptSession("username", username, request);
		if(serviceSession!=null){
			return serviceSession;
		}
		return null;
	}
	public ScriptSession getScriptSessionByUserName(String username,ServletContext context){
		ScriptSession serviceSession=super.getScriptSession("username", username, context);
		if(serviceSession!=null){
			return serviceSession;
		}
		return null;
	}
	
	/**
	 * 将用户id和页面脚本session绑定
	 * @param userid
	 */
	public void setScriptSessionFlag(int userid,String username) {
		super.bindScriptSession("username",username);
		super.bindScriptSession("userid",userid);
	}

	public void removeScriptSessionFlag(int userid,HttpServletRequest request){
		ScriptSession scriptSession=getScriptSessionByID(userid,request);
		if(scriptSession!=null){
			scriptSession.invalidate();
		}
	}
	public void refreshAllUser(HttpServletRequest request) {
		Collection<ScriptSession> scriptSessions=new HashSet<ScriptSession>();
		for(Integer userid : mapUsers.keySet()){
			ScriptSession onLineUserSession=getScriptSessionByID(userid,request);
			if(onLineUserSession!=null){
				scriptSessions.add(onLineUserSession);
			}
		}
		//刷新服务人员在线列表
		LoginScript.refreshUserTree(scriptSessions);
	}

	/**
	 * 通知所有在线用户
	 */
	public void refreshAllUser(HttpServletRequest request,User notifyUser,Integer isOnline) {
		Collection<ScriptSession> scriptSessions=new HashSet<ScriptSession>();
		for(Integer userid : mapUsers.keySet()){
			if(notifyUser.getUserId().equals(userid))continue;
			ScriptSession onLineUserSession=getScriptSessionByID(userid,request);
			//IMLog.info("onLineUserSession id:"+onLineUserSession.getId());    
			if(onLineUserSession!=null){
				scriptSessions.add(onLineUserSession);
			}
		}
		//刷新服务人员在线列表
		LoginScript.refreshUserTree(scriptSessions,notifyUser,isOnline);
	}

}
