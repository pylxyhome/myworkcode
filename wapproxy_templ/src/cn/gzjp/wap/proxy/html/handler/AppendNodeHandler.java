package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
/**
 * 
 * @author gzwenny
 *
 */
@SuppressWarnings("unchecked")
public class AppendNodeHandler implements NodeHandler {
	//要插入内容结点类型	
	private Class clazz;
	//要插入的内容
	private Node content;
	//插入位置
	private boolean isBefore;

	@Override
	public void handle(Node node) {
		if(isBefore){
			node.getChildren().prepend(content);
		}else{
			node.getChildren().add(content);
		}
	}
	
	public void setType(Class clazz){
		this.clazz=clazz;
	}
	
	public Class getType(){
		return clazz;
	}

	public Node getContent() {
		return content;
	}

	public void setContent(Node content) {
		this.content = content;
	}

	public boolean isBefore() {
		return isBefore;
	}

	public void setBefore(boolean isBefore) {
		this.isBefore = isBefore;
	}

}
