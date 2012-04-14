package com.im.web.action;

import org.springframework.stereotype.Controller;

import com.im.util.IMLog;
import com.im.web.action.base.BaseAction;

@Controller
public class GroupUserTreeAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String execute() throws Exception {
		IMLog.info("调用了GroupUserTreeAction");
		String treeString = super.groupUserTreeService.getGroupTree(super.getRequest());
		super.getResponse().setCharacterEncoding("utf-8");
		//向客户端写josn树
		super.getResponse().getWriter().print(treeString);
		return null;
	}
}
