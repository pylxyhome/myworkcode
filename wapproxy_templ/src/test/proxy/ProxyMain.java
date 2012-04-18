package test.proxy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import cn.gzjp.wap.proxy.NodeFilterBuilder;
import cn.gzjp.wap.proxy.html.handler.BodyTagHandler;
import cn.gzjp.wap.proxy.html.handler.CardTagHandler;
import cn.gzjp.wap.proxy.html.tag.IFrameTag;
import cn.gzjp.wap.proxy.html.tag.WMLCardTag;
import cn.gzjp.wap.proxy.html.tag.WMLGoTag;

public class ProxyMain implements Runnable{

	Socket s = null;
	byte[] buff = new byte[1024];
	PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();
	NodeFilter filter = null;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		go();
	}
	
	public static void go() throws IOException{
		ServerSocket ss = new ServerSocket(8081);
		Socket s = null;
		while((s = ss.accept()) != null){
			ProxyMain pm = new ProxyMain(s);
			Thread td = new Thread(pm);
			td.start();
		}
	}
	
	private ProxyMain(Socket s){
		this.s = s;
		
		
		factory.registerTag(new WMLGoTag());
		factory.registerTag(new WMLCardTag());
		factory.registerTag(new IFrameTag());
//		factory.registerTag(new LinkResourceTag());
//		factory.registerTag(new HeadMetaTag());
		
		String url = "";
		BodyTagHandler bodyHdl = new BodyTagHandler();
		CardTagHandler cardHdl = new CardTagHandler();
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
	public void run() {
		try {
			
			String[] ms = null;
			List<String> heads = new ArrayList<String>();
			HashMap<String, String> headsMap = new HashMap<String, String>();
			
			InputStream clIn = s.getInputStream();
			OutputStream clOut = s.getOutputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(clIn));
			
			int lnCnt = 0;
			String line = "";
			while((line = br.readLine()) != null){
				++lnCnt; 
				if(lnCnt == 1){ //第一行为方法
					ms = handleMethod(line);
				}else if("".equals(line)){//第二行
					break;
				}else{ //处理头
					handleHeads(heads, line, headsMap);
				}
			}
			
			String host = headsMap.get("host");
			String destURL = "http://" + host + ms[1];
			HttpURLConnection httpUrl = (HttpURLConnection) new URL(destURL).openConnection();
			httpUrl.setRequestMethod(ms[0]);//设置请求方法
			copyRequestHead(heads, httpUrl);//复制头
			httpUrl.setDoOutput(true);
			httpUrl.connect();//链接
			
			InputStream destIn = httpUrl.getInputStream();
			int contentLen = httpUrl.getContentLength();
			
			
			//处理文档
			String contentType = StrUtil.tnull(httpUrl.getContentType());
			String contentEncoding = StrUtil.tnull(httpUrl.getContentEncoding());
			
			
			byte[] data = null;
			if(contentType.contains("html")){
				if(contentEncoding.equals("gzip")){//解压缩
					destIn = new GZIPInputStream(destIn);
					clOut = new GZIPOutputStream(clOut);
				}
				data = handleXhtml2(httpUrl, destIn);
				clOut.write(data);
			}else{
				copyDestData2Client(destIn, clOut, contentLen);//复制数据
			}
			
			int responseCode = httpUrl.getResponseCode();
			String responseMessage = httpUrl.getResponseMessage();
			
			
			PrintWriter clPw = new PrintWriter(clOut);
			clPw.println("HTTP/1.1 " + responseCode + " " + responseMessage);//响应信息
			copyResponseHead(httpUrl, clPw);//复制头
			
			//加入头信息
			clPw.println("");
			//加入头信息结束
			
			clPw.println();//头部份完成
			clPw.flush();
			
//			httpUrl.disconnect();//关闭链接
			
//			clPw.close();
			clOut.close();
			br.close();
			clIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	byte[] handleXhtml(HttpURLConnection httpUrl, InputStream destIn) throws IOException, ParserException {
		byte[] data = getData(httpUrl, destIn);
		String ctx = new String(data, "utf-8");
		
		String bodyTag = "<a>";
//		int bodyEndIx = ctx.indexOf(bodyTag) + bodyTag.length();
//		ctx = StrUtil.addSubStr(bodyEndIx, ctx, "欢迎使用代理");
		System.out.println(ctx);
		System.out.println("__________________________");
		
		data = ctx.getBytes("gb2312");
		return data;
	}
	byte[] handleXhtml2(HttpURLConnection httpUrl, InputStream destIn) throws IOException, ParserException {
		byte[] data = getData(httpUrl, destIn);
		String ctx = new String(data, "gb2312");

		Parser parser = new Parser(ctx);
		parser.setNodeFactory(factory);
		NodeList html = parser.parse(null);
		Page page=parser.getLexer().getPage();
		String charset=page.getEncoding();
		html.extractAllNodesThatMatch(new NodeFilter(){
			@Override
			public boolean accept(Node node) {
				TagNode tagNode = null;
				if(node instanceof TagNode){
					try{
						tagNode = (TagNode)node;				
						
						String tagNodeName = tagNode.getTagName().trim();
						System.out.println("tagNodeName=" + tagNodeName);
						if(tagNodeName.equalsIgnoreCase("body")){//处理body
							String headCtx = "欢迎使用代理__头";
//							String tailCtx = "欢迎使用代理__尾";
							tagNode.getChildren().prepend(new TextNode(headCtx));
//							tagNode.getChildren().add(new TextNode(tailCtx));
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				return false;
			}
			},true);
		System.out.println("charset=" + charset);
		ctx = html.toHtml();
		data = ctx.getBytes("gb2312");
		
		return data;
	}

	private byte[] getData(HttpURLConnection httpUrl, InputStream destIn) throws IOException {//文档处理
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int r = 0;
		int cnt = 0;
		while((r = destIn.read(buff)) != -1){
			cnt+=r;
//			System.out.println(new String(buff));
			baos.write(buff, 0, r);
		}
		byte[] data = baos.toByteArray();
		
		return data;
	}

	private void copyDestData2Client(InputStream destIn, OutputStream clOut, int contentLen) throws IOException {
		int r = 0;
		int cnt = 0;
		while((r = destIn.read(buff)) != -1){
			cnt+=r;
//			System.out.println(new String(buff));
			clOut.write(buff, 0, r);
		}
	}

	private void copyResponseHead(HttpURLConnection httpUrl, PrintWriter clOut) {
		Map<String, List<String>> heads = httpUrl.getHeaderFields();		
		Set<String> names = heads.keySet();
		for(String name : names){
			if(name == null){
				continue;
			}
			List<String> valus = heads.get(name);
			for(String value : valus){
				clOut.print(name);
				clOut.print(": ");
				clOut.println(value);
			}
		}
	}

	private void copyRequestHead(List<String> heads,
			HttpURLConnection httpUrl) {
		
		for(String headAndValue :  heads){
			String[] its = headAndValue.split(":");
			httpUrl.setRequestProperty(its[0], its[1]);
		}
	}

	private void handleHeads(List<String> heads,String line, HashMap<String, String> headsMap) {
		heads.add(line);
		String[] it = line.split(": ");
		if(it.length > 1){
			headsMap.put(it[0].trim().toLowerCase(), it[1].trim().toLowerCase());
		}		
	}

	private String[] handleMethod(String line) {
		String[] result = line.split(" ");
		return result;
	}

}
