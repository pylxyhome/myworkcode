package com.im.service;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
public class SendPushService {
   	 ScriptBuffer scriptBuffer = new ScriptBuffer(); //构造js脚本
	 public void send(String msg){  
		 	System.out.println("==========调用了send方法==========");
			WebContext webContext=WebContextFactory.get();
			final ScriptSession myScSession = webContext.getScriptSession();
			scriptBuffer.appendScript("dwrtest(");
			scriptBuffer.appendData(msg);
			scriptBuffer.appendScript(")");
			/**向访问当前页面响应js函数**/
		   Browser.withCurrentPage(new Runnable() {   
				public void run() {
					myScSession.addScript(scriptBuffer);
				}});
	 		}
}
