package com.im.service.usergroup.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.im.bean.user.UserGroup;
import com.im.service.base.DaoSupport;
import com.im.service.usergroup.IUserGroupService;

@Service
public class UserGroupService extends DaoSupport<UserGroup>
			implements IUserGroupService{  

	
	public List<UserGroup> findListByGroupId(Integer groupid) {
		return this.findListByProperty("group.groupId", groupid);
	}

}
