package com.im.service.message;

import java.util.List;

import com.im.bean.message.Message;
import com.im.bean.page.PageView;
import com.im.service.base.DAO;

public interface IMessageService extends DAO<Message>{

	/**
	 * 根据接收者id、信息状态获取信息列表
	 * @param loginId
	 * @param status
	 * @return
	 */
	public List<Message> findByReceiveId(Integer loginId,int status);
	/**
	 * 根据接收者id 获取未读信息列表
	 * @param loginId
	 * @return
	 */
	public List<Message> findUnReadListByReceiveId(Integer loginId);
	/**
	 * 根据发送者id与接收者id获取未读信息列表
	 * @param loginId
	 * @param senderId
	 * @return
	 */
	public List<Message> findUnReadsByReceiveAandSender(Integer loginId,Integer senderId);
	/**
	 * 将信息更新为已读状态
	 * @param msgId
	 */
	public void updateStatusToRead(Integer msgId);
	
	/**
	 * @param loginId  当前登陆的用户id
	 * @param receiverId 对方id
	 * @param maxresult 每页显示记录数
	 * @param currentpage 当前页
	 * @param keywords 关键字
	 * @return
	 */
	public PageView<Message> getMsgRecordPageView(Integer loginId,Integer receiverId,
							int maxresult,int currentpage,String keywords);
}
