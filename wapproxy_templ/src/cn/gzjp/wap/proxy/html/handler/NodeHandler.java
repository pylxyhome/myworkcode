package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
/**
 * 
 * @author gzwenny
 *
 */
public interface NodeHandler {

	public void handle(Node node);
	public Class getType();
}
