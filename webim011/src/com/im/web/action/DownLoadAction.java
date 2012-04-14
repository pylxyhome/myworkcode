package com.im.web.action;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.im.web.action.base.BaseUploadFileAction;
 
@Controller
public class DownLoadAction extends BaseUploadFileAction {    

	private String fileName;// 初始的通过param指定的文件名属性
	private String file;

	public InputStream getInputStream() throws Exception {
		try {
			return ServletActionContext.getServletContext().getResourceAsStream(file);
		} catch (Exception e) {
			throw new Exception("不存在文件");
		}		
	}    
  
	@Override
	public String execute() throws Exception {    
		return SUCCESS;    	  
	}    
		
	public String cleanPath(String path){
		while(StringUtils.countMatches(path, "//")>0 || 
				StringUtils.countMatches(path, "\\")>0){
			path=StringUtils.replace(path,"//","/");
			path=StringUtils.replace(path,"\\","/");
			if(path.equals("")){
				path="/";
			}
		}
		return path;
	}
	/** 提供转换编码后的供下载用的文件名 */    
	public String getDownloadFileName() {    
		String downFileName = fileName;    
		try {    
			downFileName = new String(downFileName.getBytes(), "ISO8859-1");    
		} catch (UnsupportedEncodingException e) {    
			e.printStackTrace();    
		}    
		return downFileName;    
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		try {
			file =cleanPath(new String(file.getBytes("ISO8859-1"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String fileName=StringUtils.substringAfterLast(file,"/");
		setFileName(fileName);
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


  
}  