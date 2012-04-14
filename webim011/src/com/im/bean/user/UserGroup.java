package com.im.bean.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.im.bean.group.Group;
import com.im.util.base.UUIDUtil;

/**
 * 建立User-Group之间的关系实体
 * @author Administrator
 *
 */
@Entity
@Table(name="im_usergroup")
public class UserGroup {

	private Integer userGroupId;
	
	private User user;
	
	private Group group;
	
	public UserGroup() {
		super();
	}

	public UserGroup(User user, Group group) {
		super();
		this.user = user;
		this.group = group;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="userId",nullable=true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="groupId",nullable=true)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}
