package cn.gzjp.push2.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

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
 * 获取群发日志信息,gzip版
 * @description
 * @author panyl
 * @date 2011-11-30
 */
public class PushLogGzipServlet extends HttpServlet {
	private static final long serialVersionUID = 115267641048173901L;
	/**
	 * 输出缓冲区大小 (字节)
	 */
	private static final int BUFFER=512;
	private final static Logger log = LoggerFactory
			.getLogger(PushLogGzipServlet.class);
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
			log.debug("taskid->logStr:"+taskid);
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		//10714行
		
		//1024 use time:510
		//默认512 
		GZIPOutputStream gzipout = new GZIPOutputStream(resp.getOutputStream(),BUFFER);
		PrintWriter pw = new PrintWriter(gzipout);
		pw.print(logStr);
		pw.flush();
		gzipout.finish();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	

}
