package com.im.service.dwr.base;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

public class BaseScript {

	/**
	 * 通过scriptSession调用相关的脚本
	 * @param scriptSession
	 * @param scriptBuffer
	 */
	public static void  excuteScript(ScriptSession scriptSession,ScriptBuffer... scriptBuffers){
		if(scriptSession!=null){
			final ScriptSession myscriptSession=scriptSession;
			for (ScriptBuffer scriptBuffer : scriptBuffers) {
				final ScriptBuffer sb = scriptBuffer;
				Browser.withSession(scriptSession.getId(), new Runnable() {   
					public void run() {
						myscriptSession.addScript(sb);      
						}});
					}
			}
	}
     
	public static void excuteScript(ScriptSession scriptSession,String method){
		ScriptBuffer scriptBuffer=createScriptBuffer(method);    
		excuteScript(scriptSession,scriptBuffer);
	}
	
	public static void excuteScript(ScriptSession scriptSession,String method,Object data){
		ScriptBuffer scriptBuffer=createScriptBuffer(method, data);
		excuteScript(scriptSession,scriptBuffer);
	}
	
	public static void excuteScript(ScriptSession scriptSession,String method,Object[] datas){
		ScriptBuffer scriptBuffer=createScriptBuffer(method, datas);
		excuteScript(scriptSession,scriptBuffer);
	}
	
	public static void excuteScript(Collection<ScriptSession> scriptSessions,ScriptBuffer scriptBuffer){
		System.out.println("scriptSessions大小:"+scriptSessions.size());
		for(ScriptSession scriptSession : scriptSessions){
			excuteScript(scriptSession,scriptBuffer);
		}
	}
	
	public static void excuteScript(Collection<ScriptSession> scriptSessions,String method,Object data){
		for(ScriptSession scriptSession : scriptSessions){
			excuteScript(scriptSession,method,data);
		}
	}
	
	public static void excuteScript(Collection<ScriptSession> scriptSessions,String method,Object[] datas){
		for(ScriptSession scriptSession : scriptSessions){
			System.out.println("调用方法:"+method);
			excuteScript(scriptSession,method,datas);
		}
	}
	/**
	 * 生成将要执行的脚本(单参数)
	 * @param method
	 * @param datas 
	 * @return
	 */
	public static ScriptBuffer createScriptBuffer(String method){
		return createScriptBuffer(method, null);
	}
	
	/**
	 * 生成将要执行的脚本(单参数)
	 * @param method
	 * @param datas 
	 * @return
	 */
	public static ScriptBuffer createScriptBuffer(String method,Object data){
		Object[] datas={data};
		return createScriptBuffer(method, datas);
	}
	
	/**
	 * 生成将要执行的脚本(多参数)
	 * @param method
	 * @param datas 
	 * @return
	 */
	public static ScriptBuffer createScriptBuffer(String method,Object[] datas){
		ScriptBuffer scriptBuffer=new ScriptBuffer();
		scriptBuffer.appendScript(method+"(");
		if(datas!=null){
			for (int i = 0; i < datas.length; i++) {
				scriptBuffer.appendData(datas[i]);   
				if(i!=datas.length-1){
				scriptBuffer.appendScript(",");
				}
			}
		}
		scriptBuffer.appendScript(");");
		System.out.println("执行脚本："+scriptBuffer.toString());
		return scriptBuffer;
	}
}
