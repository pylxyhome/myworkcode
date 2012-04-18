package cn.gzjp.wap.proxy;

import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Page;
import org.htmlparser.scanners.ScriptScanner;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import cn.gzjp.wap.proxy.html.tag.HeadMetaTag;
import cn.gzjp.wap.proxy.html.tag.IFrameTag;
import cn.gzjp.wap.proxy.html.tag.LinkResourceTag;
import cn.gzjp.wap.proxy.html.tag.WMLCardTag;
import cn.gzjp.wap.proxy.html.tag.WMLGoTag;
import cn.gzjp.wap.proxy.service.CtxSpCleaner;
import cn.gzjp.wap.proxy.service.TemplateService;
import cn.gzjp.wap.proxy.util.HttpUtils;

/**
 * 页面装饰类，主要功能是为页面链接
 * @author gzwenny
 */
public class PageDecorator {
	
	private static final Log log = LogFactory.getLog(PageDecorator.class);
	
	public static ThreadLocal<String> thLocal = new ThreadLocal<String>();

	private NodeList html;
	private boolean gzip=false;
	private boolean deflate=false;
	private String charset;
	private NodeFilter filter;
	
	public PageDecorator(URLConnection conn, Map<String,String> args)
	throws ParserException {
		if("gzip".equalsIgnoreCase(conn.getContentEncoding()))gzip=true;
		if("deflate".equalsIgnoreCase(conn.getContentEncoding()))deflate=true;
		PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();
		factory.registerTag(new WMLGoTag());
		factory.registerTag(new WMLCardTag());
		factory.registerTag(new IFrameTag());
		factory.registerTag(new LinkResourceTag());
		factory.registerTag(new HeadMetaTag());
		//修复htmlparser的bug
		ScriptScanner.STRICT = false;
		HttpUtils.getBaseUrl(conn.getURL().toString());
		Parser parser = new Parser(conn);
		
		
		String dest_url = args.get("url");
		Set<String> urlprxs = TemplateService.mapUrlCharset.keySet();
		for(String urlprx : urlprxs){
			if(dest_url.startsWith(urlprx)){
				parser.setEncoding(TemplateService.mapUrlCharset.get(urlprx));//debug
			}
		}
		
		
		parser.setNodeFactory(factory);
		html = parser.parse(null);
		Page page=parser.getLexer().getPage();
		charset=page.getEncoding();

		log.info("the page content type is ["+page.getContentType()+"] and charset is ["+charset+"]");
	}

	public void decorate(HttpServletResponse response) throws Exception {
		thLocal.set(charset);
		html.extractAllNodesThatMatch(filter,true);
				
		CtxSpCleaner ctxSpCleaner = new CtxSpCleaner(charset);
		html.extractAllNodesThatMatch(ctxSpCleaner,true);
		
		OutputStream out=response.getOutputStream();
		if (gzip)out = new GZIPOutputStream(out);
		if(deflate)out =new DeflaterOutputStream(out);
		
		String htmlStr = html.toHtml(true);
		System.out.println(htmlStr);//debug
		htmlStr = ctxSpCleaner.cleanSp(htmlStr);
		
		
//		System.out.println("charset=" + charset);//debug
		byte[]data=htmlStr.getBytes(charset);
		//得到新生成文档的长度，需要设定ContentLength的值。保证文档完整。
		response.setContentLength(data.length);
		out.write(data);
		out.flush();
		if (gzip)((GZIPOutputStream) out).finish();
		if(deflate)((DeflaterOutputStream)out).finish();
	}
	
	public void setNodeFilter(NodeFilter filter){
		this.filter=filter;
	}
}
