package com.im.web.action.base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.im.util.IMLog;

  
public class BaseUploadFileAction extends BaseAction {   
    public static final int BUFFER_SIZE=16*1024;   
    // 用File数组来封装多个上传文件域对象   
    public File[] upload;   
    // 用String数组来封装多个上传文件名   
    public String[] uploadFileName;   
    // 用String数组来封装多个上传文件类�  
    public String[] uploadContentType;   
    // 保存文件的目录路徑
    public String savePath="";   
    
    public BaseUploadFileAction(){	
    }

    
	
	  //上传文件方法
	  public List<String> uploadFile(String path,boolean isFullPath){
		  return uploadFile(path,isFullPath,false);
	  }
	  
	  //上传文件方法
	  public List<String> uploadFile(String path,boolean isFullPath,boolean isPrototype){
   	   List<String> successFileList=new ArrayList<String>();   
          // 处理每个要上传的文件   
	   	   try {
	   		 for (int i = 0; i < upload.length; i++) {   
	             // 根据服务器的文件保存地址和原文件名创建目录文件全路径  
	       	  String srcFilesInfo=uploadFileName[i].toString();
	       	  //取得文件的后綴 
	       	  String  FileExtensions=srcFilesInfo.substring(srcFilesInfo.lastIndexOf("."),srcFilesInfo.length());
	       	  String fileName="";
	       	  if(isPrototype){
	       		  fileName=srcFilesInfo;
	       	  }else{
	       		  fileName=Calendar.getInstance().getTime().getTime()+i+FileExtensions;
	       	  }
	       	  	createFold(path);
	            String dstPath = getRealyPath(path) + "\\"+fileName;  
	             File dstFile = new File(dstPath);   
	             if(copy(upload[i], dstFile,BUFFER_SIZE)){   
	           	  if(isFullPath){
	           		successFileList.add(path+fileName);  
	           	  }else{
	                   successFileList.add(fileName);
	           	  }
	             }  
	             if(successFileList.size()<1){
	             	return null;
	             }
	         } 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
        return successFileList;
	  }
	  //上传文件方法
	  public List<String> uploadFile(String path) throws IOException{
		  return uploadFile(path,false);
	  }
	  
	
	public File[] getUpload() {
		return upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}

	public String[] getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String[] getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public static int getBUFFER_SIZE() {
		return BUFFER_SIZE;
	}
}  