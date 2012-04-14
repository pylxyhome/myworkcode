package com.im.web.action.base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.im.service.tree.IGroupUserTreeService;
import com.im.util.IMLog;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction  extends ActionSupport{
	@Resource
	protected IGroupUserTreeService groupUserTreeService;
	
	/**第几页**/
	protected Integer page;
	
	protected String query="false";
	/**查询关键字**/
	protected String keywords;
	
	protected Integer receiverId;
	
	protected Integer loginUserId;

	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	public Integer getPage() {
		return (page==null || page<1)?1:page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	public HttpSession getSession(){
		return getRequest().getSession();
	}
	
	public ServletContext getServletContext(){
		return ServletActionContext.getServletContext();
	}
	
	public String getRealyPath(String path){
		return getServletContext().getRealPath(path);
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public Integer getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(Integer loginUserId) {
		this.loginUserId = loginUserId;
	}
	//文件复制
	public  boolean  copy(File src, File dst,int BUFFER_SIZE) {   
        boolean result=false;   
        InputStream in = null;   
        OutputStream out = null;   
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);   
            out = new BufferedOutputStream(new FileOutputStream(dst),   
                    BUFFER_SIZE);   
            byte[] buffer = new byte[BUFFER_SIZE];   
            int len = 0;   
            while ((len = in.read(buffer)) > 0) {   
                out.write(buffer, 0, len);   
            }   
            result=true;   
        } catch (Exception e) {   
            e.printStackTrace();   
            result=false;   
        } finally {   
            if (null != in) {   
                try {   
                    in.close();   
                } catch (IOException e) {   
                    e.printStackTrace();   
                }   
            }   
            if (null != out) {   
                try {   
                    out.close();   
                } catch (IOException e) {   
                    e.printStackTrace();   
                }   
            }   
        }   
        return result;   
    }   
	 //建立文件夹
	  public void createFold(String path){
	    	try{
	    		path=getRealyPath(path);
		    	File folder = new File(path); 
				if(!folder.exists()){
					folder.mkdirs();
					IMLog.info("建立文件�夹:"+folder.getPath());
				}				
	    	}catch (Exception e) {
	    		IMLog.info("建立文件夹失败");
	    		e.printStackTrace();
			}
	    }

}
