package cn.gzjp.push2.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzjp.push2.constant.PushConstant;
import cn.gzjp.push2.work.TaskService;
import cn.gzjp.util.TaskIdGenerator;

/**
 * 获取群发日志信息
 * @description
 * @author panyl
 * @date 2011-11-30
 */
public class PushLogServlet extends HttpServlet {
	private static final long serialVersionUID = 115267641048173901L;

	private final static Logger log = LoggerFactory
			.getLogger(PushLogServlet.class);
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//接受信息
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8"); 
		String taskid = req.getHeader("taskid");
		if(!TaskIdGenerator.validateTaskId(taskid)){
			log.debug("taskid error:"+taskid);
			resp.getWriter().print(PushConstant.TASKID_ERROR);
			return;
		}
		TaskService taskService=new TaskService();
		String logStr="";
		try {
			logStr = taskService.getPushLog(taskid);
			log.debug("logStr:"+taskid);
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		resp.getWriter().print(logStr);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
