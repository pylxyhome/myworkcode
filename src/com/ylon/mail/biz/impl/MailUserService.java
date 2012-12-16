package com.ylon.mail.biz.impl;

import org.springframework.stereotype.Service;

import com.ylon.dao.base.BaseDao;
import com.ylon.mail.biz.IMailUserService;
import com.ylon.mail.entity.MailUser;

@SuppressWarnings("serial")
@Service
public class MailUserService extends BaseDao<MailUser> implements IMailUserService{


}
