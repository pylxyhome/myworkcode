package com.ylon.mail.biz.impl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.ylon.mail.biz.IMailSender;
import com.ylon.mail.biz.IMailTempleteService;
import com.ylon.mail.biz.IMailUserService;
import com.ylon.mail.entity.MailTemplate;
import com.ylon.mail.entity.MailUser;

import freemarker.template.Template;
@Service
public class MailSender<MailTemplete> extends JavaMailSenderImpl implements IMailSender{
	@Resource(name="mailUserService")
	private IMailUserService mailUserService;
	@Resource(name="mailTempleteService")
	private IMailTempleteService mailTempleteService;
	@Resource
	protected FreeMarkerConfig freemarkerConfig;
	final static BlockingQueue<JavaMailSenderImpl> mailUserQueue = new LinkedBlockingQueue<JavaMailSenderImpl>(
			100);

	private JavaMailSenderImpl getInstance() {
		synchronized (MailSender.class) {
			try {
				if (mailUserQueue.size() ==0)init();
				if (mailUserQueue.size() > 0) {
					JavaMailSenderImpl instance = (JavaMailSenderImpl) mailUserQueue
								.take();
						mailUserQueue.put(instance);
						return instance;
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void init() {
		System.out.println("-------init-----------");
		List<MailUser> mailUsers = mailUserService.getList();
		for (MailUser mailUser : mailUsers) {
			if (!mailUser.isEnable())
				continue;
			this.setUsername(mailUser.getUsername());
			this.setHost(mailUser.getHost());
			this.setPassword(mailUser.getPassword());
			this.setPort(mailUser.getPort());
			Properties properties = new Properties();
			properties.put("mail.smtp.auth", mailUser.isAuth()?"true":"false");
			properties.put("mail.smtp.timeout", mailUser.getMaximum());
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("fromUser", mailUser.getFromUser());
			this.setJavaMailProperties(properties);
			try {
				mailUserQueue.put(this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void send(String[] toUser,Map<String,Object> parameterMap,String bcc, Integer templateId) {
		
		JavaMailSenderImpl javaMailSender=this.getInstance();
		MailTemplate mailTemplete=mailTempleteService.getById(templateId);
		try{
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setTo(toUser);
			helper.setFrom(javaMailSender.getJavaMailProperties().getProperty("fromUser"));
			helper.setSubject(mailTemplete.getSubject());
			helper.setSentDate(Calendar.getInstance().getTime());
			if(bcc !=null){
				helper.setBcc(bcc);
			}
			Template template = freemarkerConfig.getConfiguration().getTemplate(mailTemplete.getPath());
			//最终输出的位置
			Writer out = new StringWriter();
			if(parameterMap==null){
				parameterMap=new HashMap<String,Object>();
			}
			//模版引擎解释模版 
			template.process(parameterMap, out);
			helper.setText(out.toString(), true);			
			javaMailSender.send(message);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
