package com.im.web.action.message;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.bean.message.Message;
import com.im.bean.page.PageView;
import com.im.service.message.IMessageService;
import com.im.service.user.IUserService;
import com.im.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
public class MessageAction extends BaseAction{
	
	private static final long serialVersionUID = 1L;

	@Resource
	private IMessageService messageService;
	
	/**
	 * 获取消息记录
	 * @return
	 * @throws Exception
	 */
	public String getRecordMsg() throws Exception {

		return "success";
	}
	/**
	 * 获取左边的用户树
	 * @return
	 * @throws Exception
	 */
	public String getLeftTree() throws Exception {
		
		return "success";
	}
	
	@SuppressWarnings("unchecked")
	public String listRightMsg() throws Exception{  
		System.out.println("--------------获取聊天记录--------------");
		Map map = (Map)ActionContext.getContext().get("request");
		if("false".equals(this.getQuery())){this.keywords=null;}
		PageView<Message> pageView = messageService
		.getMsgRecordPageView(loginUserId, receiverId, 20, this.getPage(), keywords);
		map.put("pageView", pageView);
		System.out.println("loginUserId:"+loginUserId);
		System.out.println("receiverID:"+receiverId);
		System.out.println("总记录数:"+pageView.getTotalrecord());   
		return "success";
	}
	public void setReceiverId(Integer receiverId){
		this.receiverId=receiverId;
	}
	
	public Integer getReceiverId(){
		return receiverId;
	}
}
