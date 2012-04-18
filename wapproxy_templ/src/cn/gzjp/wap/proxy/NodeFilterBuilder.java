package cn.gzjp.wap.proxy;

import org.htmlparser.NodeFilter;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.StyleTag;

import cn.gzjp.wap.proxy.html.handler.AddContentNodeHandler;
import cn.gzjp.wap.proxy.html.handler.AppendNodeHandler;
import cn.gzjp.wap.proxy.html.handler.BodyTagHandler;
import cn.gzjp.wap.proxy.html.handler.CardTagHandler;
import cn.gzjp.wap.proxy.html.handler.FormTagHandler;
import cn.gzjp.wap.proxy.html.handler.IFrameTagHandler;
import cn.gzjp.wap.proxy.html.handler.ImageTagHandler;
import cn.gzjp.wap.proxy.html.handler.LinkResourceTagHandler;
import cn.gzjp.wap.proxy.html.handler.LinkTagHandler;
import cn.gzjp.wap.proxy.html.handler.MetaTagHandler;
import cn.gzjp.wap.proxy.html.handler.TagNodeHandler;
import cn.gzjp.wap.proxy.html.handler.WMLCardTagHandler;
import cn.gzjp.wap.proxy.html.handler.WMLGoTagHandler;
import cn.gzjp.wap.proxy.html.tag.WMLCardTag;

public class NodeFilterBuilder {
	private NodeHandlerFilter filter;

	public NodeFilterBuilder(){
		filter=new NodeHandlerFilter();
	}
	
	public NodeFilterBuilder buildImageHandler(){
		// TODO 建立图片过处理器
		filter.register(new ImageTagHandler());
		return this;
	}
	
	public NodeFilterBuilder buildLinkTagHandler(String url){
		LinkTagHandler link=new LinkTagHandler();
		link.setUrl(url);
		filter.register(link);
		return this;
	}
	
	public NodeFilterBuilder buildFormTagHandler(String url){
		FormTagHandler form=new FormTagHandler();
		form.setUrl(url);
		filter.register(form);
		return this;
	}
	
	public NodeFilterBuilder buildStyleTagHandler(String url){
		TagNodeHandler style=new TagNodeHandler();
		style.setType(StyleTag.class);
		style.setUrl(url);
		filter.register(style);
		return this;
	}
	
	public NodeFilterBuilder buildScriptTagHandler(String url){
		TagNodeHandler script=new TagNodeHandler();
		script.setType(ScriptTag.class);
		script.setUrl(url);
		filter.register(script);
		return this;
	}
	
	public NodeFilterBuilder buildGoTagHandler(String url){
		WMLGoTagHandler go=new WMLGoTagHandler();
		go.setUrl(url);
		filter.register(go);
		return this;
	}
	
	public NodeFilterBuilder buildAddScriptHandler(String content){
		//往页面加入内容
		//加入脚本
		AddContentNodeHandler script=new AddContentNodeHandler();
		script.setBefore(false);
		script.setType(HeadTag.class);
		script.setContent("<script>"+content+"</script>");
		filter.register(script);
		return this;
	}
	
	public NodeFilterBuilder buildAddStyleHandler(String content){
		//加入样式
		AddContentNodeHandler style=new AddContentNodeHandler();
		style.setBefore(false);
		style.setType(HeadTag.class);
		style.setContent("<style>"+content+"</style>");
		filter.register(style);
		return this;
	}
	
	public NodeFilterBuilder buildAddBodyHandler(String content){
		//加入内容
		AddContentNodeHandler body=new AddContentNodeHandler();
		body.setBefore(true);
		body.setType(BodyTag.class);
		body.setContent(content);
		filter.register(body);
		return this;
	}
	
	public NodeFilterBuilder buildAddCardHandler(String content){
		//加入内容	
		AddContentNodeHandler card=new AddContentNodeHandler();
		card.setBefore(true);
		card.setType(WMLCardTag.class);
		card.setContent(content);
		filter.register(card);
		return this;
	}
	
	public NodeFilterBuilder buildAddStyleRefHandler(String ref){
		//增加外部样式
		StyleTag style=new StyleTag();
		style.setAttribute("src", ref);
		AppendNodeHandler head=new AppendNodeHandler();
		head.setBefore(false);
		head.setType(HeadTag.class);
		head.setContent(style);
		filter.register(head);
		return this;
	}
	
	public NodeFilterBuilder buildAddScriptRefHandler(String ref){
		//增加外部脚本
		ScriptTag script=new ScriptTag();
		script.setAttribute("src",ref);
		AppendNodeHandler head=new AppendNodeHandler();
		head.setBefore(false);
		head.setType(HeadTag.class);
		head.setContent(script);
		filter.register(head);
		return this;
	}
	
	public NodeFilterBuilder buildIFrameTagHandler(String url){
		IFrameTagHandler iframe=new IFrameTagHandler();
		iframe.setUrl(url);
		filter.register(iframe);		
		return this;
	}
	
	public NodeFilterBuilder buildLinkResourceTagHandler(String url){
		LinkResourceTagHandler resource=new LinkResourceTagHandler();
		filter.register(resource);
		return this;
	}
	
	public NodeFilterBuilder buildWMLCardTagHandler(String url){
		WMLCardTagHandler card=new WMLCardTagHandler();
		card.setUrl(url);
		filter.register(card);
		return this;
	}
	
	public NodeFilterBuilder buildWMLGoTagHandler(String url){
		WMLGoTagHandler go=new WMLGoTagHandler();
		go.setUrl(url);
		filter.register(go);
		return this;
	}
	public NodeFilterBuilder buildMetaTagHandler(String url){
		MetaTagHandler meta=new MetaTagHandler();
		meta.setUrl(url);
		filter.register(meta);
		return this;
	}
	public NodeFilterBuilder buildBodyTagHandler(BodyTagHandler bodyHandler) {
		filter.register(bodyHandler);
		return this;
	}
	public NodeFilterBuilder buildCardTagHandler(CardTagHandler cardTagHandler) {
		filter.register(cardTagHandler);
		return this;
	}
	
	public NodeFilter getNodeFilter(){
		return filter;
	}

	
}
