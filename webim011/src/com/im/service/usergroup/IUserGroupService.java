package com.im.service.usergroup;

import java.util.List;

import com.im.bean.tree.Tree;
import com.im.bean.user.UserGroup;
import com.im.service.base.DAO;

public interface IUserGroupService extends DAO<UserGroup>{

	public List<UserGroup> findListByGroupId(Integer groupid);
	
}
