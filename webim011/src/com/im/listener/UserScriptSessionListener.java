package com.im.listener;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;

import com.im.bean.user.User;
import com.im.service.dwr.ScriptSessionService;
import com.im.util.IMLog;

public class UserScriptSessionListener implements ScriptSessionListener
{
	public UserScriptSessionListener() throws Exception{
		IMLog.info("初始化UserScriptSessionListener");
	}
	
	//ScriptSession被创建时执行
	@SuppressWarnings("deprecation")
	public final void sessionCreated(ScriptSessionEvent sSessionEvent) {
		ScriptSession scriptSession = sSessionEvent.getSession();
		IMLog.info("ScriptSession:"+scriptSession.getId()+"被创建");
	}

	@SuppressWarnings("deprecation")
	public final void sessionDestroyed(ScriptSessionEvent sSessionEvent) {
		ScriptSession scriptSession = sSessionEvent.getSession();
	
		IMLog.info("ScriptSession:"+scriptSession.getId()+"被销毁");
	}
}
