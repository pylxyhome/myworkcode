package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;

import cn.gzjp.wap.proxy.html.tag.WMLCardTag;
import cn.gzjp.wap.proxy.service.TemplateService;
/**
 * 
 * @author gzwenny
 *
 */
public class CardTagHandler implements NodeHandler {
	
	private String url;
	
	private String ip = "";
	
	private String dest_url = "";

	@Override
	public void handle(Node node) {
		WMLCardTag tag=(WMLCardTag)node;
		String headCtx = TemplateService.getCtxByIpData(ip, "card", "head", dest_url);
		String tailCtx = TemplateService.getCtxByIpData(ip, "card", "tail", dest_url);
		tag.getChildren().prepend(new TextNode(headCtx));
		tag.getChildren().add(new TextNode(tailCtx));
	}
	
	
	
	public String getDest_url() {
		return dest_url;
	}

	public void setDest_url(String dest_url) {
		this.dest_url = dest_url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Class getType(){
		return WMLCardTag.class;
	}

}
