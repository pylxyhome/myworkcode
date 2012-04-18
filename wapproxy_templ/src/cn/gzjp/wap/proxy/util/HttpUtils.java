package cn.gzjp.wap.proxy.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.util.ParserException;
/**
 * 跟处理连接相关的类
 * @author gzwenny
 *
 */
public class HttpUtils {
	public static final String ANDER = "&amp;";

	/**
	 * 提取链接中的host,例如http://g.iuni.com.cn/index.jsp的host为g.iuni.com.cn
	 * @param url
	 * @return
	 */
	public static String hostname(String url){
		//TODO 处理url为空和找不到://的异常
		int start=url.indexOf("://")+3;
		int end=url.indexOf('/',start);
		if(end==-1){
			url=url.substring(start);
		}else{
			url=url.substring(start,end);
		
		}
		//判断里面是否含有问号，有问号则不是正确的host
		int loc=url.indexOf('?');
		if(loc>1)url=url.substring(0,loc);
		//判断url里面是否含有&号
		loc=url.indexOf('&');
		if(loc>1)url=url.substring(0,loc);
		return url;
	}
	/**
	 * 提取带context的地址，例如http://www.abc.com/go/to/index.jsp?id=123"变成http://www.abc.com/go/to/
	 * @param url
	 * @return
	 * @throws ParserException
	 */
	public static String getBaseUrl(String url) throws ParserException{
		//处理baseUrl
		if(url==null||url.indexOf("http://")==-1)
			throw new ParserException("bad real url ["+url+"]");
		//先检查是不是参数的复杂连接（参数中可以含有/）
		int loc=url.indexOf('?');
		//存在问号
		if(loc!=-1){
			url=url.substring(0,loc);
			//检查从问号倒数第一个遇到的/
			loc=url.lastIndexOf('/');
			//检查是否为http://里面的/
			if(loc>6){
				url=url.substring(0,loc+1);
			}else{
				url=url+"/";
			}
			return url;
		}
		loc=url.lastIndexOf('/');
		//防止那些没有以/结尾的连接
		if(loc<=6){
			return url+"/";
		}
		return url.substring(0,loc+1);
	}
	
	public static String getBaseUrl2(String url){
		//先检查是不是参数的复杂连接（参数中可以含有/）
		int loc=url.indexOf('?');
		//存在问号
		if(loc!=-1){
			url=url.substring(0,loc);
			//检查从问号倒数第一个遇到的/
			loc=url.lastIndexOf('/');
			//检查是否为http://里面的/
			if(loc>6){
				url=url.substring(0,loc+1);
			}else{
				url=url+"/";
			}
			return url;
		}
		loc=url.lastIndexOf('/');
		//防止那些没有以/结尾的连接
		if(loc<=6){
			return url+"/";
		}
		return url.substring(0,loc+1);
	}
	
	/**
	 * 提取完整服务地址，例如http://www.abc.com/go/123.html变成http://www.abc.com
	 * @param url
	 * @return
	 */
	public static String getFullHost(String url){
		String host=hostname(url);
		String protocol=url.substring(0,url.indexOf("://"));
		return protocol+"://"+host;
	}
	
	public static String decode(String url) {
		return url.replace("%3A",":").replace("%2F","/").replace("%3F","?");
	}
	/**
	 * http://proxy/?http://m.taobao.com的referer格式还原为http://m.taobao.com
	 * 需要处理下面特殊格式
	 * 1./?http://www.abc.com/?abc=1
	 * 2../?http://www.abc.com/?abc=1
	 * 3.http://www.realpath.com
	 */
	
	public void test(){
		
	}
	
	@SuppressWarnings("unchecked")
	public static String getHeaders(HttpServletRequest req){
		StringBuffer sb=new StringBuffer();
		sb.append("headers[");
		Enumeration<String>names=req.getHeaderNames();
		while(names.hasMoreElements()){
			String name=names.nextElement();
			sb.append(name).append('=');
			sb.append(req.getHeader(name));
			sb.append('|');
		}
		sb.append("remote=").append(req.getRemoteHost());
		sb.append(']');
		return sb.toString();
	}
	
	public static String addParameterToURL(String url, String para){
		if(url.indexOf('?')!=-1)
			url=url+"&" + para;
		else{
			if(url.equals(getFullHost(url)))
				url=url+"/?" + para;
			else
				url=url+"?" + para;
		}
		return url;
	}
	
	public static String proxyId(String url){
		if(url.indexOf('?')!=-1)
			url=url+"&gzjpproxy=1";
		else{
			if(url.equals(getFullHost(url)))
				url=url+"/?gzjpproxy=1";
			else
				url=url+"?gzjpproxy=1";
		}
		return url;
	}
	
	public static String decodeByReferer(String url,String ref){
		if(ref==null)return null;
		if(url.startsWith("/")){
			url="http://"+HttpUtils.hostname(ref)+url;
		}else{
			url=HttpUtils.getBaseUrl2(ref)+url;
		}
		return url;
	}
	/*
	 * 修改link中的&符号，将其转义成＆amp;
	 * 1.2页面中的链接中的&符是需要转义的，不然页面会报错。
	 */
	public static String tranLinkHasAnder(String link){
		
		StringBuilder nStr = new StringBuilder();
		char t;
		for(int i = 0; i < link.length(); ++i){
			t = link.charAt(i);
			if('&' == t){
				if((i + ANDER.length()) < link.length()){
					String ffix = link.substring(i, i + ANDER.length());
					//System.out.println("ffix=" + ffix);
					if(!ANDER.equals(ffix)){
						nStr.append(ANDER);
						continue;
					}
				}
				if((i + 1) ==  link.length()){
					nStr.append(ANDER);
					continue;
				}
			}
			nStr.append(t);
		}
		
		return nStr.toString();
	}
	public static void main(String[] args){
		String outStr = tranLinkHasAnder("http://sports.3g.cn/Chinese/index.aspx?sid=00A4081E947&amp;cin=96806&");
		System.out.println(outStr);
	}
}
