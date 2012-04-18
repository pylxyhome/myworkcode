package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;

import cn.gzjp.wap.proxy.util.HttpUtils;
/**
 * 
 * @author gzwenny
 *
 */
public class TagNodeHandler implements NodeHandler {
	
	private String url;
	private Class type;

	@Override
	public void handle(Node node) {
		String src=((TagNode)node).getAttribute("src");
		//TODO 如果src使用绝对地址，直接返回，否则处理成绝对地址，这里功能以后可以移动专门的类来处理
		if(src==null || src.indexOf("http://")!=-1)return;
		String dest=null;
		if(src.startsWith("/")){
			//使用相对于服务器的绝对地径
			String host=HttpUtils.getFullHost(url);
			dest=host+src;
		}else{
			dest=url+src;
		}
		((TagNode)node).setAttribute("src",dest);

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setType(Class clazz){
		this.type=clazz;
	}
	
	public Class getType(){
		return type;
	}
	
}
