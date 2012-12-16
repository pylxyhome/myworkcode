package com.ylon.mail.biz;

import java.util.Map;

public interface IMailSender {
	/**
	 * 发送邮件
	 * @author panyl
	 * @description
	 * @date 2012-12-16 
	 * @param toUser  接收的邮件用户
	 * @param parameterMap 参数
	 * @param bcc   附件 
	 * @param templateId  模板id
	 */
	public void send(String[] toUser,Map<String,Object> parameterMap, String bcc,Integer templateId);
}
