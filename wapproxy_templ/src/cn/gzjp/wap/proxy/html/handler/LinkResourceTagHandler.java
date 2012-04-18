package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;

import cn.gzjp.wap.proxy.Configure;
import cn.gzjp.wap.proxy.html.tag.LinkResourceTag;
/**
 * 
 * @author gzwenny
 *
 */
public class LinkResourceTagHandler implements NodeHandler {

	@Override
	public Class getType() {
		return LinkResourceTag.class;
	}

	@Override
	public void handle(Node node) {
		LinkResourceTag ls=(LinkResourceTag)node;
		String link=ls.getLink();
		if(link!=null&&!"".equals(link))
			ls.setLink(link);
		
		 
		//处理RSS上的Link
		String preLink = Configure.getConfig().getRssPreLink(); 
		String lsCtx = ls.toPlainTextString();
		if(null == lsCtx){
			lsCtx = "";
		}
		NodeList chlRen = ls.getChildren();
		if(null != chlRen){
			chlRen.prepend(new TextNode(preLink));
		}		
	}

}
