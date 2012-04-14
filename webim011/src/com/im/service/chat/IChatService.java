package com.im.service.chat;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.im.bean.message.Message;
/**
 * 聊天服务接口
 * @author ylgw
 *
 */
public interface IChatService {
	/**
	 * 绑定聊天窗口ScriptSession
	 * @param loginId
	 * @param targetId
	 * @param request
	 */
	public void bindingChatScriptSession(int loginId, int targetId,HttpServletRequest request);
	/**
	 * 移除
	 * @param loginId
	 * @param targetId
	 * @param request
	 */
	public void removeChatScriptSession(int loginId, int targetId,HttpServletRequest request);
	
	public void removeChatScriptSession(int loginId,HttpServletRequest request);
	
	public void removeChatScriptSession(int loginId,ServletContext context);
	/**
	 * 发送消息
	 * @param senderName 发送者
	 * @param receiverid 接收者id
	 * @param msg 消息内容
	 * @param request
	 */
	void send(int senderID, String senderName, int receiverid, String msg,
			HttpServletRequest request);
	/**
	 * 获取与某用户的未读消息
	 * @param loginId
	 * @param senderId
	 * @param request
	 * @return
	 */
	public List<Message> getUnreadMsg(int loginId, int senderId,
			HttpServletRequest request);
	/**
	 * 更新未读消息为已读
	 * @param msgId
	 */
	public void readMsg(Integer msgId);
	/**
	 * 取得登陆用户未读消息列表
	 * @param loginId
	 * @param request
	 * @return
	 */
	public List<Message> getAllUnreadMsg(int loginId,HttpServletRequest request);
}
