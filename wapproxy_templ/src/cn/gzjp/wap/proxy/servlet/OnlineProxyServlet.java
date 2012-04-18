package cn.gzjp.wap.proxy.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.util.IO;

import cn.gzjp.wap.proxy.Configure;
import cn.gzjp.wap.proxy.RequiredIOCopyException;
import cn.gzjp.wap.proxy.util.BlackList;
import cn.gzjp.wap.proxy.util.HttpUtils;
import cn.gzjp.wap.proxy.util.SinaTwiterService;

public class OnlineProxyServlet implements Servlet {

	private static final Log log = LogFactory.getLog(OnlineProxyServlet.class);

	protected HashSet<String> ignoreProxyHeaders = new HashSet<String>();
	{
		ignoreProxyHeaders.add("proxy-connection");
		ignoreProxyHeaders.add("connection");
		ignoreProxyHeaders.add("keep-alive");
		ignoreProxyHeaders.add("transfer-encoding");
		ignoreProxyHeaders.add("te");
		ignoreProxyHeaders.add("trailer");
		ignoreProxyHeaders.add("proxy-authorization");
		ignoreProxyHeaders.add("proxy-authenticate");
		ignoreProxyHeaders.add("upgrade");

		ignoreProxyHeaders.add("host");
		ignoreProxyHeaders.add("location");
		ignoreProxyHeaders.add("referer");
	}

	protected ServletConfig _config;
	protected ServletContext _context;
	protected String decorateURL;

	ResponseProcessor processor = new ResponseProcessor();

	public void init(ServletConfig config) throws ServletException {
		this._config = config;
		this._context = config.getServletContext();
		decorateURL = Configure.getConfig().getDecorateUrl();
		//加载新浪微博免登陆用户信息(电话号码与GSID)
//		try {
//			SinaTwiterService.loadNumGsid();
//		} catch (IOException e) {
//			log.info("load SinaTwiterNumGsid Error" + e.getMessage());
//			e.printStackTrace();
//		}
	}

	public ServletConfig getServletConfig() {
		return _config;
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String host = null;
		
		log.info(HttpUtils.getHeaders(request));

		if ("CONNECT".equalsIgnoreCase(request.getMethod())) {
			handleConnect(request, response);
			return;
		}
		
		String uri = request.getRequestURI();
		log.info("uri:"+uri);  //"/"
		String url="";
		if(uri.contains("_")){
			url=uri.substring(2);
			if (request.getQueryString() != null){
				url+="?"+request.getQueryString();
			}
			log.info("url-->"+url); 
		}
		if (request.getQueryString() != null)
			uri += "?" + request.getQueryString();
		if(url == null || "".equals(url))
		 url = request.getQueryString();
		if (url == null || "".equals(url)) {
			String ctx=request.getRequestURI();
			String ref=getReferer(request);
			if(ref!=null&&ctx!=null){
				if(ctx.startsWith("/"))url="http://"+HttpUtils.hostname(ref)+ctx;
				else
					url=HttpUtils.getBaseUrl2(ref)+ctx;
				log.warn("build url from referfer ["+url+"]");
			}else{
				log.warn("real url is empty,and context path="+ctx);
				response.sendError(404,"page not found.");
				return;
			}
		}
		
		url = HttpUtils.decode(url);
//		if(url.contains("url=")){
//			url=url.substring(4).replaceFirst("&", "?");
//		}
		log.info("real url [" + url + "]");
		
		
		// 特殊处理
		if (BlackList.test(url)) {
			log.warn("internal url of " + url);
			response.sendRedirect(url);
			return;
		}
		// 加上代理标识,对代理portal有影响，现在取掉此功能,本身目标是为了对中兴平台的日志
		//如此链接:
		//http://g.hn165.com/display/go.action?w=wml&ci=ff80808129d0139f0129fdf16e264d7d&url=http://iread.wo.com.cn
		//无法断定加入需不需要加入?
//		url=HttpUtils.proxyId(url);
//		log.info("proxy id url="+url);
		
//		//自动登录新浪微博
//		long s_ng =  System.currentTimeMillis();
//		url = SinaTwiterService.addGsid(url, request);
//		long d = System.currentTimeMillis() - s_ng;
//		log.info("sina num_gsid use(ms):" + d);
		
		//开始链接
		HttpURLConnection http = createConnection(request, url);
		boolean hasContent = copyRequestHeaders(request, http);
		processReferer(request, http);
		//处理无效连接
		if(!url.startsWith("http://")){
			String ref=getReferer(request);
			if(ref!=null){
				if(url.startsWith("/")){
					url="http://"+HttpUtils.hostname(ref)+url;
				}else{
					url=HttpUtils.getBaseUrl2(ref)+url;
				}
			}else{
				log.warn("not support url["+url+"]");
				response.sendError(404,"not support url["+url+"]");
				return;
			}
		}
		// 设置请求host为真实地址的host
		if (url != null) {
			host = HttpUtils.hostname(url);
			log.info("real host=" + host);
			http.addRequestProperty("host", host);
		}

		// customize Connection

		try{
			http.setDoInput(true);
			if (hasContent) {
				http.setDoOutput(true);
				IO.copy(request.getInputStream(), http.getOutputStream());
			}
			// Connect
			long t1 = System.currentTimeMillis();
			http.connect();
			long t2 = System.currentTimeMillis();
			log.info("connect to [" + host + "] run out " + (t2 - t1)+ " ms.");
		} catch (Exception ex) {
			log.warn("connect to remote server error,redirect to original url.reason["+ ex + "]");
			response.sendRedirect(url);
			return;
		}

		InputStream proxy_in = null;
		if (http != null) {
			proxy_in = http.getErrorStream();
			response.setStatus(http.getResponseCode());
		}
		if (proxy_in == null) {
			try {
				proxy_in = http.getInputStream();
			} catch (Exception ex) {
				ex.printStackTrace();
				proxy_in = http.getErrorStream();
			}
		}

		copyResponseHeaders(response, http);
		//测试
		//response.setBufferSize(1024*10);
		response.setHeader("Set-Cookie", "null");//禁用Cookie

		Map<String, String> args = new Hashtable<String, String>();
		args.put("decorate-url", decorateURL);
		args.put("host", host);
		args.put("url", url);
		try {
			processor.handle(request, http, response, args);
		} catch (RequiredIOCopyException ex) {
			log.info(ex.getMessage());
			IO.copy(proxy_in, response.getOutputStream());
		} catch (Exception ex) {
			log.error("", ex);
			// TODO 这里考查一下是否所有错误情况都适合流复制
			IO.copy(proxy_in, response.getOutputStream());
		}
	}

	private void copyResponseHeaders(HttpServletResponse response,
			HttpURLConnection http) {
		// clear response defaults.
		response.setHeader("Date", null);
		response.setHeader("Server", null);

		// set response headers
		int h = 0;
		String hdr = http.getHeaderFieldKey(h);
		String val = http.getHeaderField(h);
		while (hdr != null || val != null) {
			String lhdr = hdr != null ? hdr.toLowerCase() : null;
			if (hdr != null && val != null && !ignoreProxyHeaders.contains(lhdr))
				response.addHeader(hdr, val);

			h++;
			hdr = http.getHeaderFieldKey(h);
			val = http.getHeaderField(h);
		}

		response.addHeader("Via", "1.1 (JPProxy)");
	}
	
	private String getReferer(HttpServletRequest request){
		String ref = request.getHeader("referer");
		if (ref != null) {
			int loc = ref.indexOf("/?");
			if (loc != -1) {
				ref = ref.substring(loc + 2);
				log.info("referer=" + ref);
			}
		}
		return ref;
	}

	private void processReferer(HttpServletRequest request,
			HttpURLConnection http) {
		String ref = request.getHeader("referer");
		if (ref != null) {
			int loc = ref.indexOf("/?");
			if (loc != -1) {
				ref = ref.substring(loc + 2);
				http.addRequestProperty("referer", ref);
				log.info("referer=" + ref);
			}
		}
	}

	private HttpURLConnection createConnection(HttpServletRequest request,
			String address) throws MalformedURLException, IOException,
			ProtocolException {
		URL url = new URL(address);
		URLConnection connection = url.openConnection();
		connection.setReadTimeout(30 * 1000);
		connection.setAllowUserInteraction(false);

		// Set method
		HttpURLConnection http = null;
		if (connection instanceof HttpURLConnection) {
			http = (HttpURLConnection) connection;
			http.setRequestMethod(request.getMethod());
			http.setInstanceFollowRedirects(false);
		}
		return http;
	}

	@SuppressWarnings("unchecked")
	private boolean copyRequestHeaders(HttpServletRequest request,
			URLConnection connection) {
		// check connection header
		String connectionHdr = request.getHeader("Connection");
		if (connectionHdr != null) {
			connectionHdr = connectionHdr.toLowerCase();
			if (connectionHdr.equals("keep-alive")
					|| connectionHdr.equals("close"))
				connectionHdr = null;
		}

		// copy headers
		boolean xForwardedFor = false;
		boolean hasContent = false;
		Enumeration<String> enm = request.getHeaderNames();
		while (enm.hasMoreElements()) {
			// TODO could be better than this!
			String hdr = enm.nextElement();
			String lhdr = hdr.toLowerCase();

			if (ignoreProxyHeaders.contains(lhdr))
				continue;
			if (connectionHdr != null && connectionHdr.indexOf(lhdr) >= 0)
				continue;

			if ("content-type".equals(lhdr))
				hasContent = true;

			Enumeration<String> vals = request.getHeaders(hdr);
			while (vals.hasMoreElements()) {
				String val = (String) vals.nextElement();
				if (val != null) {
					connection.addRequestProperty(hdr, val);
					xForwardedFor |= "X-Forwarded-For".equalsIgnoreCase(hdr);
				}
			}
		}

		// Proxy headers
		connection.setRequestProperty("Via", "1.1 (JPProxy)");
		if (!xForwardedFor) 
			connection.addRequestProperty("X-Forwarded-For", request
					.getRemoteAddr());

		// a little bit of cache control
		String cache_control = request.getHeader("Cache-Control");
		if (cache_control != null
				&& (cache_control.indexOf("no-cache") >= 0 || cache_control
						.indexOf("no-store") >= 0))
			connection.setUseCaches(false);
		return hasContent;
	}

	protected URL proxyHttpURL(String scheme, String serverName,
			int serverPort, String uri) throws MalformedURLException {
		return new URL(scheme, serverName, serverPort, uri);
	}

	public void handleConnect(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String uri = request.getRequestURI();

		String port = "";
		String host = "";

		int c = uri.indexOf(':');
		if (c >= 0) {
			port = uri.substring(c + 1);
			host = uri.substring(0, c);
			if (host.indexOf('/') > 0)
				host = host.substring(host.indexOf('/') + 1);
		}

		InetSocketAddress inetAddress = new InetSocketAddress(host, Integer
				.parseInt(port));

		InputStream in = request.getInputStream();
		OutputStream out = response.getOutputStream();

		Socket socket = new Socket(inetAddress.getAddress(), inetAddress
				.getPort());

		response.setStatus(200);
		response.setHeader("Connection", "close");
		response.flushBuffer();

		IO.copyThread(socket.getInputStream(), out);
		IO.copy(in, socket.getOutputStream());
	}

	public String getServletInfo() {
		return "Proxy Servlet";
	}

	public void destroy() {

	}

}
