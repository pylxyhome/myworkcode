package cn.gzjp.wap.proxy.servlet.handler;

import java.net.HttpURLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author gzwenny
 *
 */
public interface ResponseHandler {

	public void handle(HttpServletRequest req ,HttpURLConnection conn,HttpServletResponse res,Map<String,String> args) throws Exception;
}
