package com.im.service.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.user.User;
import com.im.service.base.DaoSupport;
import com.im.service.user.IUserService;


@Service @Transactional
public class UserService extends DaoSupport<User>
							implements IUserService {

	public User findUserByUsername(String username) {
		List<User> users = super.findListByProperty("username", username);
		if(users.size()>0){
			return users.get(0);
		}
		return null;
	}

	
}
