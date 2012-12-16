package com.ylon.mail.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * email
 * @author  panyl
 * @description
 * @date 2012-12-16
 */
@Entity
public class MailUser implements Serializable{

	private static final long serialVersionUID = 5692257724981046019L;
	@Id
	@GeneratedValue
	private Integer id;
	@Column
	private String fromUser;
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private String host;
	@Column
	private Integer port;
	@Column(name="isAuth")
	private boolean auth;
	@Column(name="isEnable")
	private boolean enable;
	@Column
	private Integer maximum;
	@Column
	@Temporal(TemporalType.TIMESTAMP) 
	private Date addTime;
	@Column
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public Integer getMaximum() {
		return maximum;
	}
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	
}
