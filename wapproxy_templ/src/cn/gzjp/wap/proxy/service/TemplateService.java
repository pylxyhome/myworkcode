package cn.gzjp.wap.proxy.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.wap.proxy.PageDecorator;
import cn.gzjp.wap.proxy.util.FileUtils;
import cn.gzjp.wap.proxy.util.IPParser;
import cn.gzjp.wap.proxy.util.StrUtil;

public class TemplateService {
	private static final Log log = LogFactory.getLog(TemplateService.class);
	public static final boolean IS_ONLY_TEMPLA = true;
	public static final String REMOTE_HOST_HEAD = "Real-Remote-Addr";
	public static final String TEMPL_LINKTAG = "&";
	public static final String TEMPL_IP_TAG = ",";
	public static final String TEMPL_IP_MUH = ",";
	
	private static Map<String, String> ipTempl = new HashMap<String, String>();
	private static Map<String, String> templCtx = new HashMap<String, String>();
	private static Map<String, String> addrNameMap = new HashMap<String, String>();
	private static Set<String> destNeedTempla = new HashSet<String>();
	private static Set<String> unNeedTempla = new HashSet<String>();
	public static Map<String, String> mapUrlCharset = new HashMap<String, String>();
	
	private static IPParser ipparser = new IPParser();
	
	static {
		loadTempls();
		try {
			addAddrs();
			loadAddrName();
			loadDestUrlNeedTempla();
			loadunUrlNeedTempla();
			loadUrlCharset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getTemplByAddr(String addr){
		String templName = "";
		
		templName = ipTempl.get(addr);
		
		return templName;
	}


	public static String getCtx(String ip, String type, String pos){
		String ctx = "";
		String name = ipTempl.get(ip);
		if(null == name){
			name = "default";
		}
		String ctxKey = name + TEMPL_LINKTAG + type + TEMPL_LINKTAG + pos;
		ctx = templCtx.get(ctxKey);
		return ctx;
	}
	//业务使用方法
	public static String getCtxByIpData(String ip, String type, String pos, String dest_url) {
		log.info(" ip=" + ip + " type=" + type + " pos=" + pos + " dest_url=" + dest_url);
		
		boolean needed = true;
		for(String urlPre : unNeedTempla){
			if(dest_url.startsWith(urlPre)){
				needed = false;
				log.info("unneed context.");
			}
		}
		if(!needed){
			return "";
		}
		
		String name = "";
		String ctx = "";
		if((null == name) || "".equals(name)){
			name = "default";
		}
		log.info("templateName=" + name);
		String ctxKey = name + TEMPL_LINKTAG + type + TEMPL_LINKTAG + pos;
		ctx = templCtx.get(ctxKey);
//		System.out.println("getCtxByIpData ctx=" + ctx);//debug
		String charset = PageDecorator.thLocal.get();
		
		Set<String> urlprxs = mapUrlCharset.keySet();
		for(String urlprx : urlprxs){
			if(dest_url.startsWith(urlprx)){
				return ctx;
			}
		}
		System.out.println("charset=" + charset); //debug
		if((charset != null) && (!charset.toLowerCase().startsWith("gb"))){
			ctx = StrUtil.tranEncode(ctx, "utf-8", charset);
		}
//		
		return ctx;
	}
	
	public static void addAddrs() throws IOException{
		BufferedReader reader = 
			new BufferedReader(new InputStreamReader(TemplateService.class.getResourceAsStream("/ipMapTempl")));
		String line = "";
		
		while((line = reader.readLine()) != null){
			String[] lns = line.split(TEMPL_IP_TAG);
			if(lns.length >= 2){
				String ipaddrs = lns[0];
				List<String> ips = StrUtil.trnIpStageToIps(ipaddrs);
				for(String ip : ips){
					ipTempl.put(ip, lns[1]);
				}
			}
		}		
		reader.close();
	}
	
	
	
	public static void loadTempls(){
		File mainDir = new File(TemplateService.class.getResource("/templ").getFile());
		File[] typeDirs = mainDir.listFiles();
		for(int i = 0; i < typeDirs.length; ++i){
			File typeFile = typeDirs[i];
			//System.out.println("name=" + typeFile.getName());
			addTempFile(typeFile);
		}
	}
	
	public static void addTempFile(File dir){
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; ++i){
			File tmpl = files[i];
			String fileName = tmpl.getName();
			String[] names = fileName.split("_");
			String tmplName = "";
			if(names.length >= 2){
				String ctx = "";
				try {
					ctx = FileUtils.streamToString(new FileInputStream(tmpl), "utf-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				StringBuilder key = new StringBuilder();
				key.append(names[0]);
				key.append(TEMPL_LINKTAG);
				key.append(dir.getName());
				key.append(TEMPL_LINKTAG);
				key.append(names[1]);
				templCtx.put(key.toString(), ctx);
			}
		}
	}
	
	private static void loadAddrName() throws IOException{
		String fileStr = FileUtils.streamToString(TemplateService.class.getResourceAsStream("/addrname"), "utf-8");
		BufferedReader reader = 
			new BufferedReader(new StringReader(fileStr));
		String line = "";
		
		while((line = reader.readLine()) != null){
			//line = new String(line.getBytes(), "utf-8");
			String[] lns = line.split(TEMPL_IP_TAG);
			if(lns.length >= 2){
				String name = lns[0];
				String templaName = lns[1];
				addrNameMap.put(name, templaName);
			}
		}		
		reader.close();
	}
	private static void loadDestUrlNeedTempla() throws IOException {
		String fileStr =
			FileUtils.streamToString(
					TemplateService.class.getResourceAsStream("/destNeedTempla"), "utf-8");
		BufferedReader reader = 
			new BufferedReader(new StringReader(fileStr));
		String line = "";
		
		while((line = reader.readLine()) != null){
			destNeedTempla.add(line);			
		}		
		reader.close();
	}
	private static void loadunUrlNeedTempla() throws IOException {
		String fileStr =
			FileUtils.streamToString(
					TemplateService.class.getResourceAsStream("/unNeedTempla"), "utf-8");
		BufferedReader reader = 
			new BufferedReader(new StringReader(fileStr));
		String line = "";
		
		while((line = reader.readLine()) != null){
			unNeedTempla.add(line);			
		}		
		reader.close();
	}
	private static void loadUrlCharset() throws IOException {
		String fileStr =
			FileUtils.streamToString(
					TemplateService.class.getResourceAsStream("/urlCharset"), "utf-8");
		BufferedReader reader = 
			new BufferedReader(new StringReader(fileStr));
		String line = "";
		
		while((line = reader.readLine()) != null){
			String[] lns = line.split(TEMPL_IP_TAG);
			if(lns.length >= 2){
				String url = lns[0];
				String charset = lns[1];
				mapUrlCharset.put(url, charset);
			}		
		}		
		reader.close();
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//		ipparser.seek("220.202.103.116");
//		System.out.println(ipparser.getCountry());
//		ipparser.seek("110.52.11.37");
//		System.out.println(ipparser.getCountry());
//		ipparser.seek("60.13.126.150");
//		System.out.println(ipparser.getCountry());
//		ipparser.seek("114.250.220.220");
//		System.out.println(ipparser.getCountry());
		System.out.println(ipparser.getProv("220.202.103.116"));
		
	}

}
