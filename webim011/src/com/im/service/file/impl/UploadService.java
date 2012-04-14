package com.im.service.file.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.im.bean.file.UploadFile;
import com.im.bean.user.User;
import com.im.service.base.DaoSupport;
import com.im.service.file.IUploadService;
import com.im.service.user.IUserService;

@Service("uploadService")
public class UploadService extends DaoSupport<UploadFile> 
					implements IUploadService {
	@Resource
	private IUserService userService;

	public String createSendFold(int loginUserId, int targetUserId,
			String savePath) {
		try {
			User loginUser=userService.find(loginUserId);
			User targetUser=userService.find(targetUserId);
			savePath="\\uploadFile\\"+loginUser.getUsername()+"\\"+formatDate(new Date())+"\\"+targetUser.getUsername()+"\\";
		} catch (Exception e) {
			return null;
		}
		return savePath;		
	}
	public void saveSendFold(int loginUserId, int targetUserId,
			String filePath) {
		User loginUser=new User();
		loginUser.setUserId(loginUserId);
		User targetUser=new User();
		targetUser.setUserId(targetUserId);
		UploadFile uploadfile=new UploadFile();
		uploadfile.setSender(loginUser);
		uploadfile.setReceiver(targetUser);
		uploadfile.setShareDate(Calendar.getInstance().getTime());
		uploadfile.setSharePath(filePath);
		this.save(uploadfile);
	}
	/**
	 * 构建日期目录
	 * @param date
	 * @return
	 */
	private String formatDate(Date date){
		SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd");
		return myFmt.format(date);
	}
}
