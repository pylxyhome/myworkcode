package com.im.service.group.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.group.Group;
import com.im.service.base.DaoSupport;
import com.im.service.group.IGroupService;

@Service @Transactional
public class GroupService extends DaoSupport<Group>
	implements IGroupService {

}
