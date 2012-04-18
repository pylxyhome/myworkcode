package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
/**
 * 
 * @author gzwenny
 *
 */
public class FormTagHandler implements NodeHandler {
	
	private String url;

	@Override
	public void handle(Node node) {
		FormTag form=(FormTag)node;
		//TODO 由于有些表格为了方便用户以书签形式收藏，所以采用get形式，但get形式浏览器会
		//重新计算表单字段形成新的url，会扰乱第一个参数为真实的url设计，以后再考虑支持
		//form.setAttribute("method", "post");
//		String location=form.getFormLocation();
//		NodeList children=new NodeList();
//		InputTag inputTag=new InputTag();
//		inputTag.setAttribute("name", "url");
//		inputTag.setAttribute("value", location);
//		inputTag.setAttribute("type", "hidden");
//		children.add(inputTag);
//		children.add(form.getChildren());
//		form.setChildren(children);
//		System.out.println("FormTagHandler->url:"+url);
//		System.out.println("FormTagHandler->location:"+location);
//		form.setFormLocation("/");
		String location=form.getFormLocation();
		form.setFormLocation("/_"+location);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Class getType(){
		return FormTag.class;
	}

}
