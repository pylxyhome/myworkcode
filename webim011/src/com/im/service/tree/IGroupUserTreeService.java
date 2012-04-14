package com.im.service.tree;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.im.bean.tree.Tree;

public interface IGroupUserTreeService {
	/**
	 * 获取组与用户树
	 * @param request
	 * @return  返回一个json数据
	 */
	public String getGroupTree(HttpServletRequest request);
	
	public List<Tree> getGroupUserTreeList();
}
