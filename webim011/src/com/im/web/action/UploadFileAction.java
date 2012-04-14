package com.im.web.action;

import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.bean.user.User;
import com.im.service.chat.IChatService;
import com.im.service.file.IUploadService;
import com.im.service.user.IUserService;
import com.im.web.action.base.BaseUploadFileAction;

@Controller
public class UploadFileAction extends BaseUploadFileAction{
	@Resource
	private IUserService userService;
	@Resource
	private IUploadService uploadService;
	@Resource
	private IChatService chatService;
	public String sendFile() throws Exception {
		User loginUser = userService.find(loginUserId);
		savePath = uploadService
				.createSendFold(loginUserId, receiverId, savePath);
		List<String> successFilePaths = uploadFile(savePath, true, true);
		StringBuffer msg = new StringBuffer("");
		if (successFilePaths != null) {
			// 保存文件地址进数据库
			for (String filePath : successFilePaths) {
				uploadService.saveSendFold(loginUserId,receiverId, filePath);
				msg.append("对方给你发送了文件,").append("<a href='")
					.append(getRequest().getContextPath())
					.append("\\download\\download.action?file=")
					.append(URLEncoder.encode(filePath, "utf-8"))
					.append( "' target='_blank''>请点击下载</a><br/>");
			}
			//通知对方接收文件
			chatService.send(loginUser.getUserId(), loginUser.getUsername(),
					receiverId, msg.toString(), getRequest());
		} 
		Thread.sleep(1500); //休息1500毫秒
		return "success";
	}
}
