package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;

import cn.gzjp.wap.proxy.html.tag.HeadMetaTag;
/**
 * 
 * @author gzwenny
 *
 */
public class MetaTagHandler implements NodeHandler {
	
	private String url;

	@Override
	public void handle(Node node) {
		HeadMetaTag metaTag = (HeadMetaTag)node;
		
		//<meta http-equiv="refresh" content="0; url=../iphone/index.jsp">对
		//对类似标签进行加入代理处理
		String httpEquiv = metaTag.getAttribute("http-equiv");
		if(null == httpEquiv){
			httpEquiv = "";
		}
		if("refresh".equals(httpEquiv.toLowerCase())){
			String newurl = metaTag.getRefleshUrl();
			newurl = url + "?" + newurl;
			String newContent = 
				updateRefleshContent(metaTag.getAttribute("content"), newurl);
			metaTag.setAttribute("content", newContent);
			//System.out.println(url);
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Class getType(){
		return HeadMetaTag.class;
	}
	
	public static String updateRefleshContent(String content, String url){
		int s = content.indexOf("=") + 1;
		String newContent = content.substring(0, s);
		newContent = newContent + url;
		return newContent;
	}
	public static void main(String[] args){
		String ct = "0; url=../iphone/index.jsp";
		System.out.println(updateRefleshContent(ct, "http://read8.hn165.com/iphone/index.jsp"));
	}

}
