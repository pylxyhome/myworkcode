package cn.gzjp.wap.proxy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.wap.proxy.Configure;

public class SinaTwiterService {
	//是否开启功能
	private static final boolean IS_OPEN = false;
	private static final Log log = LogFactory.getLog(SinaTwiterService.class);
	//表示在电话号码与gsid映射文件中，电话号码与gsid之间的分隔符。
	private static final String WORD_TERIM = "><";
	private static final String TELNUM_OF_HTTP_HEAD = "x-up-calling-line-id";
	//刷新gsid文件的阈值
	private static final int FLUSH_NG_FILE_CNT = Configure.getConfig().getFlushNgFileCnt();
	
	//增加记数器(暂不使用)
	//private static int cntNewGsid = 0;
	
	//新浪微博URL
	private static String sinaTwiterUrl = Configure.getConfig().getSinaTwiterUrl();
	
	private static Map<String, String> numGsidMap = Collections.EMPTY_MAP;
	
	//加载用户号码与gsid之前的对应关系。
	public static void loadNumGsid() throws IOException{
		numGsidMap = new HashMap<String, String>();
		String fileName = Configure.getConfig().getTelnum2SinatwiterGsidFileName();
		File numGsidFile = new File(fileName);
		if(!numGsidFile.exists()){
			numGsidFile.createNewFile();
		}
		FileInputStream numGsidInStm = new FileInputStream(numGsidFile);
		//每行为每个电话号码记录，使用WORD_TERIM对文中的电话号码与gsid分开。第一个电话号，第二个gsid
		Scanner ngScn  = new Scanner(numGsidInStm);
		String line = "";
		while(ngScn.hasNextLine()){
			line = ngScn.nextLine();
			String[] ng = line.split(WORD_TERIM);
			if(ng.length >= 2){
				log.info("load num_gsid:" + line);
				numGsidMap.put(ng[0], ng[1]);
			}			
		}
		ngScn.close();
	}
	
	public static boolean addNumGsid(String num, String gsid){
		if(Collections.EMPTY_MAP == numGsidMap){
			numGsidMap = new HashMap<String, String>();
		}
		String oldGsid = numGsidMap.get(num);
		numGsidMap.put(num, gsid);
		return (null != oldGsid);
	}
	
	public static void addNumGsidAndFlush(String num, String gsid) throws IOException{
		if(Collections.EMPTY_MAP == numGsidMap){
			numGsidMap = new HashMap<String, String>();
		}
		boolean hasOld = addNumGsid(num, gsid);

		if(hasOld){
			flushAllNumGsid();
		}else{
			flushNewNumGsid(num, gsid);
		}
	}
	public static void removeNumGsidAndFlush(String num) throws IOException{
		if(Collections.EMPTY_MAP == numGsidMap){
			numGsidMap = new HashMap<String, String>();
		}
		numGsidMap.remove(num);
		flushAllNumGsid();
	
	}
	
	//将numGsidMap中的数据写到文件里去(增量)
	public static void flushNewNumGsid(String num, String gsid) throws IOException{
		String fileName = Configure.getConfig().getTelnum2SinatwiterGsidFileName();
		
		log.info("flush(appends) SinatwiterGsidFile:" + fileName);
		File numGsidFile = new File(fileName);
		if(!numGsidFile.exists()){
			numGsidFile.createNewFile();
		}
		FileOutputStream ngFileOutStm = null;
		
		ngFileOutStm = new FileOutputStream(numGsidFile, true);
		PrintWriter ngPw = new PrintWriter(ngFileOutStm);
		ngPw.print(num);
		ngPw.print(WORD_TERIM);
		ngPw.println(gsid);
	
		
		ngPw.flush();
		ngPw.close();
	}
	//将numGsidMap中的数据写到文件里去(全量)
	public static void flushAllNumGsid() throws IOException{
		String fileName = Configure.getConfig().getTelnum2SinatwiterGsidFileName();
		
		log.info("flush(All) SinatwiterGsidFile:" + fileName);
		File numGsidFile = new File(fileName);
		if(!numGsidFile.exists()){
			numGsidFile.createNewFile();
		}
		FileOutputStream ngFileOutStm = null;
		
		ngFileOutStm = new FileOutputStream(numGsidFile);
		PrintWriter ngPw = new PrintWriter(ngFileOutStm);
		
		Set<String> nums = numGsidMap.keySet();
		for(String num : nums){
			ngPw.print(num);
			ngPw.print(WORD_TERIM);
			ngPw.println(numGsidMap.get(num));
		}		
		
		ngPw.flush();
		ngPw.close();
	}
	
	public synchronized static String addGsid(String url, HttpServletRequest req) throws IOException{
		//取出电话号码
		String usernum = req.getHeader(TELNUM_OF_HTTP_HEAD);
		
		//新浪微博自动登录处理
		//查看是否退出，如果是退出，则通告下边条件，不加入gsid
		//退出链接http://3g.sina.com.cn/prog/wapsite/sso/loginout.php?backURL=%2Fdpool%2Fttt%2Findex.php%3Fvt%3D&backTitle=%D0%C2%C0%CB%CE%A2%B2%A9&vt=&gsid=3_58a2565d8db6469ae0a8c07d20db19846a8f9880d59965
		String loginoutUrl = Configure.getConfig().getLoginoutUrl();
		if(url.startsWith(loginoutUrl)){//确定是退出
			removeNumGsidAndFlush(usernum);
		}
		
		//TODO检查是否为登录，对登录后
		
		//处在链接(http://3g.sina.com.cn/dpool/ttt)，则加入gsid
		String gsidKey = "gsid=";
		if(url.startsWith(sinaTwiterUrl)){
			log.info("find sina twiter url.");
			if(!url.contains(gsidKey)){//在url中不存在gsid
				log.info("url hasn't gsid.");
				//查看是否有此用户的gsid
				String gsid = numGsidMap.get(usernum);
				
				//｛调试代码开始，方便调试时查看用户
				Map<String, String> ngMap = numGsidMap;
				ngMap.hashCode();
				//｝调试代码结束
				
				if(null != gsid){//如果有这个用户的sid
					log.info("has gsid of usernum=" + usernum + " gsid=" + gsid);
					gsid = gsidKey + gsid;
					url = HttpUtils.addParameterToURL(url, gsid);
				}
				
			}else{ //在url中存在gsid。
				//需要查内存，看看有没有此用户，如果没有则保存。
				
				int startIxInUrl = url.indexOf(gsidKey) + gsidKey.length();
				int endIxInUrl = url.indexOf("&", startIxInUrl);
				String gsid = url.substring(startIxInUrl, endIxInUrl);
				String oldGsid = numGsidMap.get(usernum);
				if((null == oldGsid) || (!oldGsid.equals(gsid))){//情况一:没有此用户的sid,情况二:此用户有新的gsid
					addNumGsidAndFlush(usernum, gsid);
				}
			}
		}
		return url;
	}
	public static void main(String[] args) throws IOException {
		flushAllNumGsid();
	}
}
