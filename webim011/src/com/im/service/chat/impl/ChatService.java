package com.im.service.chat.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.ScriptSession;
import org.springframework.stereotype.Service;

import com.im.bean.constant.MsgConstant;
import com.im.bean.message.Message;
import com.im.service.chat.IChatService;
import com.im.service.dwr.ScriptSessionService;
import com.im.service.dwr.actionscript.ChatScript;
import com.im.service.message.IMessageService;
import com.im.service.user.IUserService;
import com.im.util.IMLog;

@Service
public class ChatService extends ScriptSessionService implements IChatService {

	@Resource
	private IMessageService messageService;
	@Resource
	private IUserService userService;

	public void bindingChatScriptSession(int loginId, int targetId,
			HttpServletRequest request) {
		ScriptSession serviceSession = getChatScriptSession(loginId, targetId,
				request);
		if (serviceSession != null) {
			removeChatScriptSession(loginId, targetId, request);
		}
		super.bindScriptSession("chat", loginId + "_" + targetId);
		IMLog.info(this.getClass() + "已绑定聊天窗口: loginId=" + loginId
				+ ",targetId=" + targetId);

	}

	public void removeChatScriptSession(int loginId, int targetId,
			HttpServletRequest request) {
		ScriptSession scriptSession = getChatScriptSession(loginId, targetId,
				request);
		if (scriptSession != null) {
			scriptSession.invalidate();
			IMLog.info(this.getClass() + "移除绑定的窗口: loginId=" + loginId
					+ ",targetId=" + targetId);
		}
	}

	public void removeChatScriptSession(int loginId, HttpServletRequest request) {
		Collection<ScriptSession> sessions = getAllSessions(request);
		for (ScriptSession session : sessions) {
			Object serverValue = session.getAttribute("chat");
			if (serverValue != null
					&& serverValue.toString().startsWith(
							String.valueOf(loginId))) {
				IMLog.info("清空" + loginId + "聊天scriptSession");
				session.invalidate();
			}
		}
	}

	public void removeChatScriptSession(int loginId, ServletContext context) {
		Collection<ScriptSession> sessions = getAllSessions(context);
		for (ScriptSession session : sessions) {
			Object serverValue = session.getAttribute("chat");
			if (serverValue != null
					&& serverValue.toString().startsWith(
							String.valueOf(loginId))) {
				IMLog.info("清空" + loginId + "聊天session");
				session.invalidate();
			}
		}

	}

	private ScriptSession getChatScriptSession(int loginId, int targetId,
			HttpServletRequest request) {
		ScriptSession serviceSession = super.getScriptSession("chat", loginId
				+ "_" + targetId, request);
		if (serviceSession != null) {
			IMLog.info("已取得绑定: user=" + loginId + ",targetId=" + targetId);
			IMLog.info("绑定状态:" + serviceSession.getCreationTime());
			return serviceSession;
		}
		IMLog.info("取得null绑定: loginId=" + loginId + ",targetId=" + targetId);
		return null;
	}

	public void send(int senderId, String senderName, int receiverid,
			String msg, HttpServletRequest request) {
		long startTime=System.currentTimeMillis();
		Message message = new Message();
		message.setSender(userService.find(senderId));
		message.setReceiver(userService.find(receiverid));
		message.setMessage(msg);
		message.setSenddate(new Date());
		// 获取对方聊天窗口ScriptSession
		ScriptSession receiverChatSession = getChatScriptSession(receiverid,
				senderId, request);
		IMLog.info("receiverChatSession:" + receiverChatSession);
		// 取到接收者的IM SESSION
		ScriptSession receiverSession = getScriptSessionByID(receiverid,
				request);
		if(receiverSession!=null){
		if (receiverChatSession != null) {//判断是否有打开聊天窗口
			// 设置消息为已读状态
			message.setStatus(MsgConstant.MSG_STATE_READ);
			// 将消息推送到目标用户
			ChatScript.receiveMessage(receiverChatSession, message, senderName);
			// 通知闪动接收人的对话框
			ChatScript.shakeChat(receiverSession, senderId);
			IMLog.info("发送在线信息: sender=" + senderName + ",targetId="
					+ receiverid + ",msg=" + msg);
		}else {
			message.setStatus(MsgConstant.MSG_STATE_UNREAD);  //设置消息状态为未读
			IMLog.info("发送在线信息等待读取: sender=" + senderName +
					",targetId="+ receiverid+",msg="+message.getMessage());
			List list = this.getUnreadMsg(receiverid, senderId, request);
			//防止注册多次事件,如果是同一个用户连续发来的多条信息，也只通知一次
			if(list.size()==0){
					this.shakeUserIcon(senderId, receiverid, request);
				}
		 }

		}else{
			message.setStatus(MsgConstant.MSG_STATE_UNREAD);
			IMLog.info("发送不在线信息: sender=" + senderName + ",targetId="+ receiverid+",msg="+message.getMessage());
		}
		// 保存聊天记录
		messageService.save(message);
		long endTime=System.currentTimeMillis();
		System.out.println("发送耗时："+(endTime-startTime)+"ms");
	}
	
	//add
	private void shakeUserIcon(int loginId, int targetId,HttpServletRequest request) {
		ScriptSession scriptSession = getScriptSessionByID(targetId, request);
		IMLog.info("targetIdScriptSession==>"+scriptSession);
		ChatScript.shakeUserIcon(scriptSession, loginId);
	}
	//@Override
	public List<Message> getUnreadMsg(int loginId, int senderId,
			HttpServletRequest request) {
		IMLog.info("开始执行getUnreadMsg方法");
		List<Message> unreadMsgs = messageService.findUnReadsByReceiveAandSender(loginId,senderId);
		IMLog.info("获取未读信息大小:"+unreadMsgs.size());
		return unreadMsgs;
	}
	/**
	 * 获取所有未读信息,用户登陆初始化未读信息用 add
	 */
	//@Override
	public List<Message> getAllUnreadMsg(int loginId, HttpServletRequest request) {
		List<Message> unreadMsgs = messageService.findUnReadListByReceiveId(loginId); 
		return unreadMsgs;
	}
	//@Override
	public void readMsg(Integer msgId) {
		//Message message = messageService.find(msgId);
		//message.setStatus(MsgConstant.MSG_STATE_READ);
		messageService.updateStatusToRead(msgId);
	}
}
