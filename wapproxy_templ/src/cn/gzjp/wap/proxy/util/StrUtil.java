package cn.gzjp.wap.proxy.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StrUtil {
	
	private static final String DEFAULT_STR_CHARSET = "UTF-8";

	public static String tranEncode(String str, String charset) {
		return tranEncode(str, charset, DEFAULT_STR_CHARSET);
	} 
	
	public static String tranEncode(String str, String charset, String outcharset) {
		String ostr = "";
		try {
			ostr = new String(str.getBytes(charset), outcharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ostr;
	} 
	public static String ignoreNull(String str){
		if(null == str){
			str = "";
		}
		return str;
	}
	public static String delSubStr(String str, int start, int end, String rstr){
		StringBuilder nstr = new StringBuilder();
		
		String pre = str.substring(0, start);
		String su = str.substring(end, str.length());
		
		nstr.append(pre);
		nstr.append(rstr);
		nstr.append(su);
		
		return nstr.toString();
	}
	public static List<String> trnIpStageToIps(String stage){
		List<String> ips = new ArrayList<String>();
		String[] f_b = stage.split("-");
		if(f_b.length >= 2){
			
			String front = f_b[0];
			String end = f_b[1];
			
			int lastDot = front.lastIndexOf(".");
			String start = front.substring(lastDot + 1, front.length());
			front = front.substring(0, lastDot);
			
			int s = Integer.parseInt(start);
			int e = Integer.parseInt(end);
			
			for(int i = s; i <= e; ++i){
				ips.add(front + "." +i);
			}
			
		}else{
			ips.add(stage);
		}
		
		return ips;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(String ip : trnIpStageToIps("192.168.2.1")){
			System.out.println(ip);
		}
		

	}

}
