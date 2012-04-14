package com.im.bean.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.im.bean.room.Room;
import com.im.util.base.UUIDUtil;

/**
 * 建立User-Room之间的关联关系
 * @author Administrator
 *
 */
@Entity
@Table(name="im_userroom")
public class UserRoom {

	private String userRoomId=UUIDUtil.getRandomUUID();
	
	private User user;
	
	private Room room;
	@Id
	public String getUserRoomId() {
		return userRoomId;
	}

	public void setUserRoomId(String userRoomId) {
		this.userRoomId = userRoomId;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="userId",nullable=true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="roomId",nullable=true)
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	
}
