package cn.gzjp.shorturl.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.shorturl.service.TaskService;

public class SmsServlet extends HttpServlet{
	
	private static final Log log = LogFactory.getLog(SmsServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		//号码数量处理
//		int maxSize = 100;
//		int mcnt = 0;
//		ArrayList<String> mdns = new ArrayList<String>(maxSize);
//		BufferedReader rd = new BufferedReader(new InputStreamReader(req.getInputStream()));		
////		log.debug("GetRequestMdns: read()=" + rd.read());
//		String str = "";
//		while((str = rd.readLine()) != null){
//			log.info("GetRequestMdns: str=" + str);
//			mdns.add(str);
//			++mcnt;
//			if(mcnt == maxSize){//真实数据超出最大值就取消后续部份。
//				log.info("GetRequestMdns: In MaxSize break, maxSize=" + maxSize);
//				break;
//				
//			}
//		}
//		rd.close();
//		String users=req.getParameter("users");
//		System.out.println("users:"+users);
//		
//		String msg=req.getParameter("msg");
//		final String mdns=req.getParameter("mdns");
//		
//		 String push_url=req.getParameter("push_url");
//		System.out.println("msg:"+msg);
//		new Thread(new Runnable(){
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println("mdns大小:"+mdns.getBytes().length);
//				
//					//Thread.sleep(5000);
//				
//			}}).start();
//		
		//接受信息
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8"); 
		String push_url = req.getHeader("push_url");
		String msg = req.getHeader("msg");
		System.out.println("push_url:"+URLDecoder.decode(push_url,"utf-8"));
		System.out.println("msg:"+	URLDecoder.decode(msg,"utf-8"));
//		URL url=Thread.currentThread().getContextClassLoader().getResource("");
//		File file=new File(url.getFile(),"ready"+File.separator+"1111"+File.separator+"data.txt");
//		//如果目录不存在则创建
//		if(!file.getParentFile().exists()){
//			file.getParentFile().mkdirs();
//		}
//		OutputStream outStream=new FileOutputStream(file);
//		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outStream));
//		//输入号码数据句柄
//		BufferedReader rd = new BufferedReader(new InputStreamReader(req.getInputStream()));		
//		String str = "";
//		while((str = rd.readLine()) != null){
//			System.out.println(str);
//			bw.write(str);
//			bw.write("\n");
//		}
//		bw.flush();
//		bw.close();
//		rd.close();
//		outStream.close();
		InputStream inStream=req.getInputStream();
		TaskService taskService=new TaskService();
		taskService.putIntoReadFile("1111", msg, push_url, inStream);
		resp.getWriter().print(1111);
		System.out.println("-------------------------------------");
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
