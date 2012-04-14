package cn.gzjp.push2.action;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzjp.push2.work.TaskService;

/**
 * 接收群发任务servlet接口
 * @description
 * @author panyl 
 * @date 2011-11-30
 */
public class PushServletAsy extends HttpServlet {
	private static final long serialVersionUID = 115267641048173901L;

	private final static Logger log = LoggerFactory
			.getLogger(PushServletAsy.class);

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 接受信息
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8"); 
		String push_url = req.getHeader("push_url");
		String msg = req.getHeader("msg");
		if(push_url!=null){
			push_url=URLDecoder.decode(push_url,"utf-8");
		}
		msg=URLDecoder.decode(msg,"utf-8");

		InputStream inStream=req.getInputStream();
		TaskService taskService=new TaskService();
		
		long taskid=taskService.putIntoReadFile(msg, push_url, inStream);
		
		try {
			TaskService.triggerReadTask(String.valueOf(taskid));
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		log.info("taskid:"+taskid);
		//返回taskid给客户端
		resp.getWriter().print(taskid);
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

//	private String getIpAddr(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
//		return ip;
//	}

}
