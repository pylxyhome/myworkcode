package com.im.service.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.im.bean.user.User;

public interface ILoginService {
	
	/**
	* 登陆Im模块
	* @param account(username)
	* @return
	*/
	public User login(String account,HttpServletRequest request,HttpServletResponse response);

}