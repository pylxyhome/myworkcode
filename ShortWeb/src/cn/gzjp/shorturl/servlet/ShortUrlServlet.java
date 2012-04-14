package cn.gzjp.shorturl.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.shorturl.entity.UrlMap;
import cn.gzjp.shorturl.service.UrlMapService;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class ShortUrlServlet extends HttpServlet{
	
	private static final Log log = LogFactory.getLog(ShortUrlServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String uri=req.getRequestURI();
		if(uri.length()>1){
			String shortUrl=uri.substring(1);
			if(!shortUrl.contains("/")&&!shortUrl.contains(".")){ 
				try{
					//应用启动第一次查询耗时：267ms  左右
					//以后查询耗时：0~16ms 
					long startTime=System.currentTimeMillis();
					Mongo mongo = new Mongo("localhost", 27017);
					
					Morphia morphia = new Morphia(); 
					UrlMapService mapService=new UrlMapService(morphia,mongo);
					UrlMap urlMap=mapService.findOne("id", Integer.valueOf(shortUrl,16));
					log.debug(Integer.valueOf(shortUrl,16)+"原地址:"+urlMap.getUrl());
					mongo.close(); //释放资源
					long endTime=System.currentTimeMillis();
					log.debug("查询耗时："+(endTime-startTime)+"ms");
				}catch (Exception e) {
					e.printStackTrace();
					log.debug(Integer.valueOf(shortUrl,16)+"找不到对应的原地址");
				}
			}
		}
		return;
     
	}
}
