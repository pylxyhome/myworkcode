package cn.gzjp.wap.proxy.servlet.handler;

import java.net.HttpURLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.wap.proxy.util.HttpUtils;
/**
 * 
 * @author gzwenny
 *
 */
public class RedirectHandler implements ResponseHandler {
	private static Log log=LogFactory.getLog(RedirectHandler.class);

	@Override
	public void handle(HttpServletRequest req ,HttpURLConnection conn, HttpServletResponse res, Map<String,String> args)
			throws Exception {
		String host=args.get("host");
		String decorateURL=args.get("decorate-url");
		String url=args.get("url");
		
		String loc=conn.getHeaderField("location");
		if(loc!=null&&loc.startsWith("/")){
			//不完整地址，相对于host定位
			loc="http://"+host+loc;
		}else if( (loc != null) && (!loc.toLowerCase().startsWith("http://")) ){
			//不完整地址，相对于context定位
			try{
				String ctx=HttpUtils.getBaseUrl(url);
				loc =ctx + loc;
			}catch(Exception ex){
				log.error("process redirect location error.",ex);
			}
		}
		log.info("new location ["+loc+"] to redirect.");
		res.setHeader("location",decorateURL+"?"+loc);
	}

}
