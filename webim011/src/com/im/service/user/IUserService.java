package com.im.service.user;

import com.im.bean.user.User;
import com.im.service.base.DAO;

public interface IUserService extends DAO<User>{

	public User findUserByUsername(String username);
}
