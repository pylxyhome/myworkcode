package cn.gzjp.wap.proxy.servlet.handler;

import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.wap.proxy.RequiredIOCopyException;
/**
 * 
 * @author gzwenny
 *
 */
public class ContentTypeHandler implements ResponseHandler {
	
	private static final Log log = LogFactory.getLog(ContentTypeHandler.class);
	private Map<String,ResponseHandler> handlers=new Hashtable<String, ResponseHandler>();
	
	public ContentTypeHandler(){
		DocumentHandler doc=new DocumentHandler();
		register("text/html",doc);
		register("application/xhtml+xml",doc);
		register("text/vnd.wap.wml",doc);
		//TODO 处理RSS内容
		register("text/xml",doc);
		register("application/xml",doc);
	}
	
	public void register(String type,ResponseHandler handler){
		handlers.put(type, handler);
	}

	@Override
	public void handle(HttpServletRequest req, HttpURLConnection conn, HttpServletResponse res, Map<String,String> args) throws Exception {
		String contentType = conn.getContentType();
		if (contentType == null)throw new RequiredIOCopyException("the content-type is null,default stream copy.");
		log.info("the response content type is [" + contentType+"]");
		String[] items = contentType.split(";");
		contentType = items[0];
		contentType=contentType.toLowerCase();
		
		args.put("content-type",contentType);
		ResponseHandler handler=handlers.get(contentType);
		log.info("the contentType ["+contentType+"] handler is "+handler);
		if(handler==null)
			throw new RequiredIOCopyException("not found handler for content-type ["+contentType+"]");
		handler.handle(req, conn, res, args);
	}

}
