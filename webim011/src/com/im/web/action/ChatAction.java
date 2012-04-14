package com.im.web.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.service.chat.IChatService;
import com.im.util.IMLog;
import com.im.web.action.base.BaseAction;

@Controller
public class ChatAction extends BaseAction{

	private Integer loginId;
	
	private Integer targetId;
	@Resource
	private IChatService chatService;
	public String leaveChat() throws Exception {
		IMLog.info("========调用了ChatAction.leaveChat======");
		//移除绑定的聊天窗口
		chatService.removeChatScriptSession(loginId, targetId, getRequest());
		return null;
	}
	public Integer getLoginId() {
		return loginId;
	}
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	public Integer getTargetId() {
		return targetId;
	}
	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}
}
