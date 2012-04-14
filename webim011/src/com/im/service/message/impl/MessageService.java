package com.im.service.message.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.constant.MsgConstant;
import com.im.bean.message.Message;
import com.im.bean.page.PageView;
import com.im.bean.page.QueryResult;
import com.im.service.base.DaoSupport;
import com.im.service.message.IMessageService;

@Service
public class MessageService extends DaoSupport<Message> 
					implements IMessageService{

	//@Override
	public List<Message> findByReceiveId(Integer loginId, int status) {
		String[] propertyNames = new String[]{"receiver.userId","status"};
		Object[] values= new Object[]{loginId,status};
		return super.findListByProperty(propertyNames, values);
	}

	//@Override
	public List<Message> findUnReadListByReceiveId(Integer loginId) {
		return findByReceiveId(loginId,MsgConstant.MSG_STATE_UNREAD);
	}

	//@Override
	public List<Message> findUnReadsByReceiveAandSender(Integer loginId,
			Integer senderId) {
		String[] propertyNames = new String[]{"receiver.userId","sender.userId","status"};
		Object[] values= new Object[]{loginId,senderId,MsgConstant.MSG_STATE_UNREAD};
		return super.findListByProperty(propertyNames, values);
	}

	 @Transactional(propagation=Propagation.REQUIRED)
	public void updateStatusToRead(Integer msgId) {
		super.updateProperty(msgId, "status", MsgConstant.MSG_STATE_READ);
	}

    
	public PageView<Message> getMsgRecordPageView(Integer loginId,
			Integer receiverId, int maxresult, int currentpage, String keywords) {
		StringBuffer hql = 
			new StringBuffer("((o.sender.userId=? and o.receiver.userId=?) or " +
					"(o.sender.userId=? and o.receiver.userId=?)) ");
		List<Object> params = new ArrayList<Object>();
		params.add(loginId);
		params.add(receiverId);
		params.add(receiverId);
		params.add(loginId);
		if(!"".equals(keywords)&&null!=keywords){
			hql.append(" and o.message like '%").append(keywords).append("%' ");
		}
		PageView<Message> pageView=new PageView<Message>(30,currentpage);
		LinkedHashMap<String, String>orderby=new LinkedHashMap<String, String>();
		orderby.put("senddate","asc"); //按日期正序
		QueryResult<Message> qr =this.getScrollData(pageView.getFirstResult(), 
				pageView.getMaxresult(),hql.toString(), params.toArray(),orderby);  
		pageView.setQueryResult(qr);
		return pageView;
	}
}
