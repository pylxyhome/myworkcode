package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;

import cn.gzjp.wap.proxy.html.tag.WMLGoTag;
/**
 * 
 * @author gzwenny
 *
 */
public class WMLGoTagHandler implements NodeHandler {
	
	private String url;

	@Override
	public Class getType() {
		return WMLGoTag.class;
	}

	@Override
	public void handle(Node node) {
		WMLGoTag tag=(WMLGoTag)node;
		String link=tag.getLink();
		//这里可能会存在把原来&amp;替换成&amp;amp;的情况
		link=link.replace("&amp;", "&");//防止上面所述情况
		tag.setLink(url+"?"+link.replace("&", "&amp;"));
		String method=tag.getMethod();
		if("get".equalsIgnoreCase(method))tag.setMethod("post");

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
