package cn.gzjp.wap.proxy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.nodes.TagNode;

import cn.gzjp.wap.proxy.util.StrUtil;

public class CtxSpCleaner implements NodeFilter{
	private static final Log log = LogFactory.getLog(CtxSpCleaner.class);
	static List<CleanIndex> cixs = new ArrayList<CleanIndex>();
	static List<CleanTag> clTags = new ArrayList<CleanTag>();
	static List<String> dels = new ArrayList<String>();
	
	static int cnt = 0;
		
	static{
		try {
			loadCleanIndex("/key/clean");
			loadCleanTag("/key/cleantag");
			loadDels("/key/dels");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 7057927839704297562L;
	private String charset = "UTF-8";
	
	public CtxSpCleaner(String charset){
		
		this.charset  = charset;
	}

	@Override
	public boolean accept(Node node) {
		
		TagNode tagNode = null;
		if(node instanceof TagNode){
			try{
				tagNode = (TagNode)node;				
				return cleanProj1(tagNode);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		return false;
	}
	
	public boolean cleanProj1(TagNode tagNode){
		String tagName = StrUtil.tranEncode(tagNode.getTagName().toLowerCase().trim(), charset);
		String html = StrUtil.tranEncode(tagNode.toHtml().trim(), charset);
		
		//System.out.println("tagName=" + tagName);
		for(CleanTag ct : clTags){
			int ix = html.indexOf(ct.getTxt());
			if(ct.getTagName().equals(tagName)
					&& ((ix >= 0) || "null".equals(ct.getTxt())) ){
				Node parent = tagNode.getParent();
				parent.getChildren().remove(tagNode);
				break;
			}
		}
		
		return false;
	}
	
	public String cleanSp(String html){
		//html = StrUtil.tranEncode(html, charset);
		String nhtml = html;
		html = null;
		
		for(CleanIndex cix : cixs){
			int ixOfTxt = nhtml.indexOf(cix.getStart());
			int enOfTxt = nhtml.lastIndexOf(cix.getEnd());
			
			if((ixOfTxt < 0) || (enOfTxt < 0)){
				continue;
			}
			
			if("1".equals(cix.getType())){
				enOfTxt = enOfTxt + cix.getEnd().length();
				
			}else if("2".equals(cix.getType())){
				ixOfTxt = ixOfTxt + cix.getStart().length();
				
			}else if("3".equals(cix.getType())){
				
			}else if("4".equals(cix.getType())){
				ixOfTxt = ixOfTxt + cix.getStart().length();
				enOfTxt = enOfTxt + cix.getEnd().length();
				
			}		
			
			if( (enOfTxt >= 0) 
				&& (ixOfTxt >= 0) 
				){
				//对以下链接进行处理 http://bjgprs.cn/primary/_-iudqfxgHQyl
				//System.out.println("html=" + nhtml);
				nhtml = StrUtil.delSubStr(nhtml, ixOfTxt, enOfTxt, cix.getNstr());
				//System.out.println("nhtml=" + nhtml);

			}
		}

		for(String d : dels){
			int ixOfTxt = nhtml.indexOf(d);
			while(-1 != ixOfTxt){
				if( ixOfTxt >= 0
					){
					//对以下链接进行处理 http://bjgprs.cn/primary/_-iudqfxgHQyl
					//System.out.println("html=" + nhtml);
					nhtml = StrUtil.delSubStr(nhtml, ixOfTxt, ixOfTxt + d.length(), "");
					//System.out.println("nhtml=" + nhtml);
				}
				ixOfTxt = nhtml.indexOf(d);
			}
		}
		
		//特殊区
		//列表区下边导航删除。
		int deletePos = nhtml.indexOf("<dddt");
		int ptagEndPos = nhtml.lastIndexOf("</p>");
		if((deletePos >= 0) && (ptagEndPos >= 0)){
			nhtml = StrUtil.delSubStr(nhtml, deletePos, ptagEndPos , "");
		}
		
		String delStr = "<br/>\r\n<br/>";
		int inbr = nhtml.indexOf(delStr);
		
		while(inbr >= 0){
			nhtml = StrUtil.delSubStr(nhtml, inbr, inbr + delStr.length() , "<br/>");
			inbr = nhtml.indexOf(delStr);
		}
		
		
		return nhtml;
	}
	
	public static void loadCleanIndex(String resourceStr) throws IOException{
		InputStream inStm = CtxSpCleaner.class.getResourceAsStream(resourceStr);
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStm,"utf-8"));
		
		String line = "";
		while(null != (line = bufReader.readLine())){
//			line = StrUtil.tranEncode(line, "gbk", "utf-8");
//			System.out.println("loadCleanIndex: line=" + line);//debug
			String[] ws = line.split(CleanIndex.TERM_FILE);
			if(ws.length >= 3){
				String pre = ws[0];
				String end = ws[1];
				String type = ws[2];
				
				CleanIndex cix = new CleanIndex();
				cix.setStart(pre);
				cix.setEnd(end);
				cix.setType(type);
				if(ws.length >=4){
					cix.setNstr(ws[3]);
				}
				cixs.add(cix);
			}
		}
		inStm.close();
	}
	public static void loadCleanTag(String resourceStr) throws IOException{
		InputStream inStm = CtxSpCleaner.class.getResourceAsStream(resourceStr);
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStm, "utf-8"));
		
		String line = "";
		while(null != (line = bufReader.readLine())){
//			line = StrUtil.tranEncode(line, "gbk", "utf-8");
//			System.out.println("loadCleanTag: line=" + line);//debug
			String[] ws = line.split(CleanIndex.TERM_FILE);
			if(ws.length >= 2){
				String tagName = ws[0];
				String txt = ws[1];
				CleanTag ct = new CleanTag();
				ct.setTagName(tagName);
				ct.setTxt(txt);
				clTags.add(ct);
			}
		}
		inStm.close();
	}
	public static void loadDels(String resourceStr) throws IOException{
		InputStream inStm = CtxSpCleaner.class.getResourceAsStream(resourceStr);
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStm, "utf-8"));
		
		String line = "";
		while(null != (line = bufReader.readLine())){
//			line = StrUtil.tranEncode(line, "gbk", "utf-8");
//			System.out.println("loadDels: line=" + line);//debug
			if(line.length() > 0){
				dels.add(line);	
			}
		}
		
		inStm.close();
	}
}
