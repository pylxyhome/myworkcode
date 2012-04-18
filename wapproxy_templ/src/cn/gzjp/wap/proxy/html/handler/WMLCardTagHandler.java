package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;

import cn.gzjp.wap.proxy.html.tag.WMLCardTag;
/**
 * 
 * @author gzwenny
 *
 */
public class WMLCardTagHandler implements NodeHandler {
	private String url;

	@Override
	public Class getType() {
		return WMLCardTag.class;
	}

	@Override
	public void handle(Node node) {
		WMLCardTag c=(WMLCardTag)node;
		String link=c.getOntimerLink();
		if(link!=null&&!"".equals(link))
			c.setOntimerLink(url+"?"+link);		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
