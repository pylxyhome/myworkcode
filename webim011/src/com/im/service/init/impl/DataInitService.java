package com.im.service.init.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.group.Group;
import com.im.bean.user.User;
import com.im.bean.user.UserGroup;
import com.im.bean.user.UserInfo;
import com.im.service.group.IGroupService;
import com.im.service.init.IDataInitService;
import com.im.service.user.IUserInfoService;
import com.im.service.user.IUserService;
import com.im.service.usergroup.IUserGroupService;

@Service @Transactional
public class DataInitService implements IDataInitService{
	private static Log logger = LogFactory.getLog(DataInitService.class);
	private String file;
	@Resource
	private IGroupService groupService;
	@Resource
	private IUserService userService;
	@Resource
	private IUserGroupService userGroupService;
	@Resource
	private IUserInfoService userInfoService;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void initImDatas(String xmlFilePath){
		try {
			Document document = new SAXReader().read(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFilePath)
			);
			importGroups(document.selectNodes("//Groups/Group"));
			logger.info("初始化数据成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化数据生成有误！");
		}
	}
	//导入用户组及用户数据
	@SuppressWarnings("unchecked")
	protected void importGroups(List groups){
		for (Iterator iter = groups.iterator(); iter.hasNext();) {
			Element groupelement = (Element) iter.next();
			Group group = new Group();
			group.setGroupName(groupelement.attributeValue("name"));
			group.setOrderNo(Integer.parseInt(groupelement.attributeValue("orderNo")));
			groupService.save(group);
			List users = groupelement.selectNodes("User");
			for (Iterator iterator = users.iterator(); iterator.hasNext();) {
				Element userelement= (Element) iterator.next();
				User user = new User();
				user.setUsername(userelement.attributeValue("username"));
				user.setPassword(userelement.attributeValue("password"));
				userService.save(user);
				Element userinfoelement = (Element)userelement.selectSingleNode("UserInfo");
				UserInfo userInfo = new UserInfo();
				userInfo.setHeadpath(userinfoelement.attributeValue("headpath"));
				userInfo.setPhone(userinfoelement.attributeValue("phone"));
				userInfo.setRealName(userinfoelement.attributeValue("realName"));
				userInfo.setUser(user);
				userInfoService.save(userInfo);
				UserGroup userGroup = new UserGroup(user,group);
				userGroupService.save(userGroup);
			}
		}
	}
}
