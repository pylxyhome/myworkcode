package com.im.bean.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.im.util.base.UUIDUtil;

/**
 * 用户详细
 * @author 羽龙共舞
 *
 */
@Entity
@Table(name="im_userInfo")
public class UserInfo {
	private Integer userinfoId;
	/**所属用户**/
	private User user;
	/**头像路径**/
	private String headpath = "/img/defeultFace.jpg";
	/**真实名称**/
	private String realName;
	/**电话**/
	private String phone;
	

	@OneToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="userid")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Column
	public String getHeadpath() {
		return headpath;
	}
	public void setHeadpath(String headpath) {
		this.headpath = headpath;
	}
	@Column
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getUserinfoId() {
		return userinfoId;
	}
	public void setUserinfoId(Integer userinfoId) {
		this.userinfoId = userinfoId;
	}
}
