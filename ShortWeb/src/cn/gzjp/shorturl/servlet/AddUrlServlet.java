package cn.gzjp.shorturl.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.shorturl.entity.UrlMap;
import cn.gzjp.shorturl.service.UrlMapService;
import cn.gzjp.sproxy.util.AutoIncreaseIDUtil;
import cn.gzjp.sproxy.util.DBConstant;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class AddUrlServlet extends HttpServlet{
	
	private static final Log log = LogFactory.getLog(AddUrlServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Mongo mongo = new Mongo("localhost", 27017); 
		Morphia morphia = new Morphia();
		UrlMapService mapDao=new UrlMapService(morphia,mongo);
		UrlMap urlMap=new UrlMap();
		urlMap.setId(AutoIncreaseIDUtil.getAutoIncreaseID("id", mongo.getDB(DBConstant.MONGOTEST), DBConstant.URLMAP_IDS));
		urlMap.setUrl("http://wap.163.com/"+genRandomNum(10)+".html");
		mapDao.save(urlMap);
		mongo.close();
	}
	public static String genRandomNum(int pwd_len){
		  //35是因为数组是从0开始的，26个字母+10个数字
		  final int  maxNum = 36;
		  int i;  //生成的随机数
		  int count = 0; //生成的密码的长度
		  char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
		    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
		    'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		  StringBuffer pwd = new StringBuffer("");
		  Random r = new Random();
		  while(count < pwd_len){
		   //生成随机数，取绝对值，防止生成负数，
		   i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1
		   if (i >= 0 && i < str.length) {
		    pwd.append(str[i]);
		    count ++;
		   }
		  }
		  return pwd.toString();
		 }
}
