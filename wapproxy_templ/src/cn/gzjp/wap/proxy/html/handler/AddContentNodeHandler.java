package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
/**
 * 
 * @author gzwenny
 *
 */
@SuppressWarnings("unchecked")
public class AddContentNodeHandler implements NodeHandler {
	//要插入内容结点类型
	
	private Class clazz;
	//要插入的内容
	private String content;
	//插入位置
	private boolean isBefore;

	@Override
	public void handle(Node node) {
		// TODO 插入内容，考虑可以设计成一个插入内容的基类，但这样接口实现类就成了最终业务实现代码了
		//这类实现类可以是内部的，由pageDecorator调用，pageDecorator再提供接口
		if(isBefore){
			node.getChildren().prepend(new TextNode(content));
		}else{
			node.getChildren().add(new TextNode(content));
		}
	}
	
	public void setType(Class clazz){
		this.clazz=clazz;
	}
	
	public Class getType(){
		return clazz;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isBefore() {
		return isBefore;
	}

	public void setBefore(boolean isBefore) {
		this.isBefore = isBefore;
	}

}
