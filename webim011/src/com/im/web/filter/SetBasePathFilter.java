package com.im.web.filter;

import java.io.IOException;
import java.util.logging.LogRecord;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SetBasePathFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//转换成HttpServletRequest
		HttpServletRequest req = (HttpServletRequest)request;
		//统一设置basePath
		ServletContext context = req.getSession().getServletContext();
		if(context.getAttribute("basePath")==null){
			String path = req.getContextPath();
			String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path+"/";
			context.setAttribute("basePath", basePath);
System.out.println(this.getClass()+":basePath==>"+basePath);
		}	
		chain.doFilter(request,response);   

	}

	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

}
