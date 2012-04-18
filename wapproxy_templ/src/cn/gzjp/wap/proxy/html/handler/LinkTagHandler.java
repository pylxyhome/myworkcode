package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;

import cn.gzjp.wap.proxy.util.HttpUtils;
/**
 * 
 * @author gzwenny
 *
 */
public class LinkTagHandler implements NodeHandler {
	
	private String url;

	@Override
	public void handle(Node node) {
		LinkTag tag=(LinkTag)node;
		
		String link=tag.extractLink();
		//这里可能会存在把原来&amp;替换成&amp;amp;的情况
		//如果连接是https，只需要把连接改成绝对地址即可
		if(tag.isHTTPSLink()){
			if(!"".equals(link))tag.setLink(link);
		}else{
			if(!"".equals(link)){
				link = HttpUtils.tranLinkHasAnder(link);
				
				if(link.startsWith("http://")){
					tag.setLink(url+"?"+link);
				}else{
					tag.setLink(link);
				}
			}
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Class<LinkTag> getType(){
		return LinkTag.class;
	}

}
