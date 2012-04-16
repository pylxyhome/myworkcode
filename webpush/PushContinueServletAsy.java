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
 * 根据taskid，如果任务之前被停止过，继续发送这条任务
 * @description
 * @author panyl
 * @date 2011-11-30
 */
public class PushContinueServletAsy extends HttpServlet {
	private static final long serialVersionUID = 115267641048173901L;

	private final static Logger log = LoggerFactory
			.getLogger(PushContinueServletAsy.class);

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 接受信息
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8"); 
		String taskid = req.getHeader("taskid");
		if(!TaskIdGenerator.validateTaskId(taskid)){
			log.debug("taskid error:"+taskid);
			resp.getWriter().print(PushConstant.TASKID_ERROR);
			return;
		}
		TaskService taskService=new TaskService();
		int result=-1;
		try {
			result = taskService.pushAgain(taskid);
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		log.info("result:"+result);
		//返回taskid给客户端
		resp.getWriter().print(result);
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	

}
