package com.im.service.dwr.base;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;

public class DWRBaseService {

	protected final Logger logger=Logger.getLogger(getClass());
	/**
	 * 取得所有页面的scriptSession
	 * @param request 
	 * @return
	 */
	public Collection<ScriptSession> getAllSessions(HttpServletRequest request) {
		// 获得DWR上下文
		//ServletContext sc = request.getSession().getServletContext();
		//ServerContext sctx = ServerContextFactory.get();
		Collection<ScriptSession> sessions = ServerContextFactory.get().getAllScriptSessions();
		return sessions;
	}
	public Collection<ScriptSession> getAllSessions(ServletContext context) {
		// 获得DWR上下文
		ServerContext sctx = ServerContextFactory.get();
		if(sctx==null){
			return null;
		}
		Collection<ScriptSession> sessions = sctx.getAllScriptSessions();
		return sessions;
	}
	public Collection<ScriptSession> getAllSessions() {
		// 获得DWR上下文
		ServerContext sctx = ServerContextFactory.get();
		if(sctx==null){
			return null;
		}
		Collection<ScriptSession> sessions = sctx.getAllScriptSessions();
		return sessions;
	}
	/**
	 * 取得指定的session
	 * 
	 * @param scriptValue        对比值
	 * @param param              属性值
	 * @param request
	 * @return
	 */
	public ScriptSession getScriptSession( String param,
			Object scriptValue, HttpServletRequest request) {
		ScriptSession scriptSessions = null;
		Collection<ScriptSession> sessions = getAllSessions(request);
		for (ScriptSession session : sessions) {
			Object serverValue = session.getAttribute(param);
			//比较获取userId的值
			if (serverValue != null && serverValue.equals(scriptValue)) {
				scriptSessions = session;
			}
		}
		return scriptSessions;
	}
	public ScriptSession getScriptSession( String param,
			Object scriptValue, ServletContext context) {
		ScriptSession scriptSessions = null;
		Collection<ScriptSession> sessions = getAllSessions(context);
		for (ScriptSession session : sessions) {
			Object serverValue = session.getAttribute(param);
			if (serverValue != null && serverValue.equals(scriptValue)) {
				scriptSessions = session;
			}
		}
		return scriptSessions;
	}
	
	/**
	 * 将属性和页面脚本session绑定
	 * @param userid
	 */
	public void bindScriptSession(String param,Object value) {
		System.out.println("绑定:"+param+"=="+value);
		WebContextFactory.get().getScriptSession().setAttribute(param, value);
	}
	public ScriptSession getScriptSession(String param, int userid) {
		ScriptSession scriptSessions = null;
		Collection<ScriptSession> sessions = getAllSessions();
		for (ScriptSession session : sessions) {
			Object serverValue = session.getAttribute(param);
			if (serverValue != null && serverValue.equals(userid)) {
				scriptSessions = session;
			}
		}
		return scriptSessions;
	}
}
