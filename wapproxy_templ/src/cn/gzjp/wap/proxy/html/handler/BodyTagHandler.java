package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;

import cn.gzjp.wap.proxy.service.TemplateService;
/**
 * 
 * @author gzwenny
 *
 */
public class BodyTagHandler implements NodeHandler {
	
	private String url;
	
	private String ip = "";
	
	private String dest_url = "";

	@Override
	public void handle(Node node) {
		BodyTag tag=(BodyTag)node;
		String headCtx = TemplateService.getCtxByIpData(ip, "body", "head", dest_url);
		String tailCtx = TemplateService.getCtxByIpData(ip, "body", "tail", dest_url);
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
		return BodyTag.class;
	}

}
