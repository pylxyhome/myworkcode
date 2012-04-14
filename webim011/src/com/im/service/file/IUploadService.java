package com.im.service.file;

import com.im.bean.file.UploadFile;
import com.im.service.base.BaseDao;

public interface IUploadService extends BaseDao<UploadFile> {

	/**
	 * 创建文件目录
	 * @param loginUserId
	 * @param targetUserId
	 * @param savePath
	 * @return
	 */
	public String createSendFold(int loginUserId,int targetUserId,String savePath);
	/**
	 * 保存文件路径到数据库
	 * @param loginUserId
	 * @param targetUserId
	 * @param filePath
	 */
	public void saveSendFold(int loginUserId,int targetUserId,String filePath);
}
