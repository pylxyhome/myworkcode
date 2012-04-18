package cn.gzjp.wap.proxy.servlet.handler;

import java.net.HttpURLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;

import cn.gzjp.wap.proxy.Configure;
import cn.gzjp.wap.proxy.NodeFilterBuilder;
import cn.gzjp.wap.proxy.PageDecorator;
import cn.gzjp.wap.proxy.html.handler.BodyTagHandler;
import cn.gzjp.wap.proxy.html.handler.CardTagHandler;
import cn.gzjp.wap.proxy.service.TemplateService;
/**
 * 
 * @author gzwenny
 *
 */
public class DocumentHandler implements ResponseHandler {
	
	private static Log log=LogFactory.getLog(DocumentHandler.class);
	private NodeFilter filter;
	
	private static BodyTagHandler bodyHdl = new BodyTagHandler();
	private static CardTagHandler cardHdl = new CardTagHandler();
	
	public DocumentHandler(){
		String url=Configure.getConfig().getDecorateUrl();
		filter=new NodeFilterBuilder()
		.buildLinkTagHandler(url)
		.buildFormTagHandler(url)
		.buildScriptTagHandler(url)
		.buildScriptTagHandler(url)
		.buildIFrameTagHandler(url)
		.buildLinkResourceTagHandler(url)
		.buildWMLCardTagHandler(url)
		.buildImageHandler()
		.buildWMLGoTagHandler(url)
		.buildMetaTagHandler(url)
		.buildBodyTagHandler(bodyHdl)
		.buildCardTagHandler(cardHdl)
		.getNodeFilter();
		
	}
	

	@Override
	public void handle(HttpServletRequest req ,HttpURLConnection conn, HttpServletResponse res, Map<String,String> args)
			throws Exception {
		String addr = "";
		addr = req.getHeader(TemplateService.REMOTE_HOST_HEAD);
		if(null == addr){
			addr = req.getRemoteHost();
		}
		bodyHdl.setIp(addr);
		cardHdl.setIp(addr);
		bodyHdl.setDest_url(args.get("url"));
		cardHdl.setDest_url(args.get("url"));
		
		//按照这种设计方式，没有必要区分html页面和wml页面，因为修饰的内容碰到合适的结点才会处理
		long t1 = System.currentTimeMillis();
		PageDecorator decorator = new PageDecorator(conn, args);
		decorator.setNodeFilter(filter);
		decorator.decorate(res);
		long t2 = System.currentTimeMillis();
		log.info("parse the html page waste time " + (t2 - t1)+" ms");
	}

}
