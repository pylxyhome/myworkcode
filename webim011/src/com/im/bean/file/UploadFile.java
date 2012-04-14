package com.im.bean.file;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.im.bean.user.User;


@Entity
@Table(name="im_uploadfile")
public class UploadFile {
	
	private Integer fileId;
	
	private String fileName;
	
	private User sender;
	
	private User receiver;
	
	private Date shareDate;
	
	private String sharePath;
	
	public UploadFile(){}
	public UploadFile(String fileName){
		this.fileName=fileName;
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	@Column
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="senderId",nullable=true)
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	@ManyToOne(cascade=CascadeType.REFRESH) @JoinColumn(name="receiverId",nullable=true)
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	@Column
	public Date getShareDate() {
		return shareDate;
	}
	public void setShareDate(Date shareDate) {
		this.shareDate = shareDate;
	}
	@Column
	public String getSharePath() {
		return sharePath;
	}
	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
	}
	
}
