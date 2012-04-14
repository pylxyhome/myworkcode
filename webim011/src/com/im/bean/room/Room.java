package com.im.bean.room;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.im.bean.user.User;
import com.im.util.base.UUIDUtil;

/**
 * 聊天室  类似qq群
 * @author Administrator
 *
 */
@Entity
@Table(name="im_room")
public class Room {
	
	private String roomId=UUIDUtil.getRandomUUID();
	/**聊天室名称**/
	private String roomName;
	/**创建时间**/
	private Date createTime;
	/**创建者**/
	private User creator;
	/**聊天室公告**/
	private String roomNotice;
	
	@Id 
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	@Column
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	@Column
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="userId",nullable=true)
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	@Column
	public String getRoomNotice() {
		return roomNotice;
	}

	public void setRoomNotice(String roomNotice) {
		this.roomNotice = roomNotice;
	}
}
