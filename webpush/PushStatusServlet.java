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
 * 获取群发任务状态servlet接口
 * @description
 * @author panyl
 * @date 2011-11-30
 */
public class PushStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 115267641048173901L;

	private final static Logger log = LoggerFactory
			.getLogger(PushStatusServlet.class);

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//接受信息
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8"); 
		String taskid = req.getHeader("taskid");
		if(!TaskIdGenerator.validateTaskId(taskid)){ 
			log.info("taskid error:"+taskid);
			resp.getWriter().print(PushConstant.TASKID_ERROR+"_"+0);
		}
		TaskService taskService=new TaskService();
		//int status=taskService.getTaskStatus(taskid);
		String status=taskService.getTaskStatusAndLogCnt(taskid);
		resp.getWriter().print(status);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	

}
