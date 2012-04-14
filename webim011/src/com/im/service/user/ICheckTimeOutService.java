package com.im.service.user;

import java.util.HashSet;

public interface ICheckTimeOutService {
	/**
	 * 主动向用户发送检测是否在线请求
	 */
	public void pushCheckTimeOut();
	/**
	 * 给当前在线用户调用的接口
	 * @param userId
	 */
	public void  checkTimeOutCallBack(int userId);
	/**
	 * 初始化集合变量
	 */
	public void CheckTimeOutInit();
	/**
	 * 获取当前在线的用户id集合
	 * @return
	 */
	public HashSet<Integer> getOnlineUser();

}
