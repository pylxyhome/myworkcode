package com.im.web.action;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.im.bean.user.User;
import com.im.service.chat.IChatService;
import com.im.service.file.IUploadService;
import com.im.service.user.IUserService;
import com.im.web.action.base.BaseAction;

@Controller
public class UploadAction extends BaseAction {
	public static final int BUFFER_SIZE = 16 * 1024;
	private File uploadify;
	private String uploadifyFileName;
	@Resource
	private IUserService userService;
	@Resource
	private IUploadService uploadService;
	@Resource
	private IChatService chatService;

	private String savePath = "";
	@SuppressWarnings("deprecation")
	public String uploadFile() throws Exception {
//		System.out.println("---------------上传文件----------------");
//		System.out.println("---------------上传文件----------------");
//		System.out.println("loginUserId="+loginUserId);
//		System.out.println("receiverId="+receiverId); 
//		//loginUserId=2;
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setCharacterEncoding("utf-8");
		User loginUser = userService.find(loginUserId);
		String saveFilePath="";
		saveFilePath = uploadService.createSendFold(loginUserId, receiverId,
				savePath);
		String filePath = uploadSendFile(saveFilePath, true, true);
		StringBuffer msg = new StringBuffer("");
		if (filePath != null) {
			uploadService.saveSendFold(loginUserId, receiverId, filePath);
			msg.append("对方给你发送了文件,").append("<a href='").append(
					getRequest().getContextPath()).append(
					"\\download\\download.action?file=").append(
					URLEncoder.encode(filePath, "utf-8")).append(
					"' target='_blank''>请点击下载</a><br/>");
			// 通知对方接收文件
			chatService.send(loginUser.getUserId(), loginUser.getUsername(),
					receiverId, msg.toString(), getRequest());
		}
		super.getRequest().setAttribute("msg", uploadifyFileName + "上传成功");
		return "success"; 
	}
	public String uploadInput() throws Exception {
		User receiverUser = userService.find(receiverId);
		super.getRequest().setAttribute("receiverUser", receiverUser);
		return "success";
		
	}
	public File getUploadify() {
		return uploadify;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public String getUploadifyFileName() {
		return uploadifyFileName;
	}

	public void setUploadifyFileName(String uploadifyFileName) {
		this.uploadifyFileName = uploadifyFileName;
	}

	// 上传文件方法
	public String uploadSendFile(String path, boolean isFullPath,
			boolean isPrototype) {
		String filePath = "";
		// 根据服务器的文件保存地址和原文件名创建目录文件全路径
		// 取得文件的后綴
		String FileExtensions = uploadifyFileName.substring(uploadifyFileName
				.lastIndexOf("."), uploadifyFileName.length());
		String fileName = "";
		if (isPrototype) {
			fileName = uploadifyFileName;
		} else {
			fileName = Calendar.getInstance().getTime().getTime()
					+ FileExtensions;
		}
		createFold(path);
		String dstPath = getRealyPath(path) + "\\" + fileName;
		File dstFile = new File(dstPath);
		if (copy(uploadify, dstFile, BUFFER_SIZE)) {
			if (isFullPath) {
				filePath = path + fileName;
			} else {
				filePath = fileName;
			}
		}

		return filePath;
	}
}
