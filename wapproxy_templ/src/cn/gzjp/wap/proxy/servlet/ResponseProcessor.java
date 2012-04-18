package cn.gzjp.wap.proxy.servlet;

import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;

import cn.gzjp.wap.proxy.RequiredIOCopyException;
import cn.gzjp.wap.proxy.servlet.handler.ContentTypeHandler;
import cn.gzjp.wap.proxy.servlet.handler.RedirectHandler;
import cn.gzjp.wap.proxy.servlet.handler.ResponseHandler;

public class ResponseProcessor {

	private Map<Integer, ResponseHandler> handlers=new Hashtable<Integer, ResponseHandler>();
	private org.apache.commons.logging.Log log=LogFactory.getLog(ResponseProcessor.class);
	
	public ResponseProcessor(){
		register(200,new ContentTypeHandler());
		register(301,new RedirectHandler());//重定向
		register(302,new RedirectHandler());//重定向
	}
	
	public void handle(HttpServletRequest req ,HttpURLConnection conn,HttpServletResponse res,Map<String,String> args) 
		throws Exception {
		int responseCode=conn.getResponseCode();
		ResponseHandler handler=handlers.get(responseCode);
		log.info("response ["+responseCode+"] handler is "+handler);
		if(handler==null)
			throw new RequiredIOCopyException("not found handler for response code ["+responseCode+"]");
		handler.handle(req, conn, res, args);
	}
	
	public void register(int code,ResponseHandler handler){
		handlers.put(code, handler);
	}
}
