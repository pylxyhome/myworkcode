package com.im.service.user.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.user.UserInfo;
import com.im.service.base.DaoSupport;
import com.im.service.user.IUserInfoService;

@Service @Transactional
public class UserInfoService extends DaoSupport<UserInfo>
	implements IUserInfoService{

}
