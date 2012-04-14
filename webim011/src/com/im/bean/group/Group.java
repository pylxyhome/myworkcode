package com.im.bean.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户分组,类似qq好友分组
 * @author Administrator
 *
 */
@Entity
@Table(name="im_group")
public class Group {

	private Integer groupId;
	
	private String groupName;
	/**排序号**/
	private int orderNo=0;
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	@Column
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Column
	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	
}
