package cn.gzjp.wap.proxy.html.handler;

import org.htmlparser.Node;
import org.htmlparser.tags.ImageTag;
/**
 * 
 * @author gzwenny
 *
 */
public class ImageTagHandler implements NodeHandler{

	@Override
	public void handle(Node node) {
		ImageTag tag=(ImageTag)node;
		String src=tag.getImageURL();
		//TODO 某些站点会采用dynamic-src属性,不存在src属性,看以后没有必要处理dynamic-src
		if(src!=null&&!"".equals(src))tag.setAttribute("src",src);			
	}
	
	public Class getType(){
		return ImageTag.class;
	}

}
