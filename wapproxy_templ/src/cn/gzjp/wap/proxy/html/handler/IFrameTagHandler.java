package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;

import cn.gzjp.wap.proxy.html.tag.IFrameTag;
/**
 * 
 * @author gzwenny
 *
 */
public class IFrameTagHandler implements NodeHandler {
	
	private String url;

	@Override
	public Class getType() {
		return IFrameTag.class;
	}

	@Override
	public void handle(Node node) {
		IFrameTag iframe=(IFrameTag)node;
		String link=iframe.getLink();
		if(link!=null&&!"".equals(link))
			iframe.setLink(url+"?"+link);	
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
