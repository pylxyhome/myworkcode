package com.im.bean.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.im.bean.constant.RelationType;

/**
 * 用户与用户之间的关系模型
 * @author ylgw
 * 
 */
@Entity
@Table(name="im_userrelation")
public class UserRelation {
	private Integer relationId;
	private Integer relation=RelationType.FRIEND;
	public User targetUser;
	public User loginUser;

	public UserRelation() {
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}
	@Column
	public Integer getRelation() {
		return relation;
	}

	public void setRelation(Integer relation) {
		this.relation = relation;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="targetId",nullable=true)
	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="loginId",nullable=true)
	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

}
